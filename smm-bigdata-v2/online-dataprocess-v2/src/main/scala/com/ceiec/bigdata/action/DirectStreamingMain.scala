package com.ceiec.bigdata.action

import java.util
import java.util.Calendar

import com.alibaba.fastjson.JSON
import com.ceiec.bigdata.dao.impl.{FaceBookDaoImpl, NewsDaoImpl, TwitterRootDaoImpl}
import com.ceiec.bigdata.entity.facebook.FaceBookRoot
import com.ceiec.bigdata.entity.news.News
import com.ceiec.bigdata.entity.table.nlp.NlpPojo
import com.ceiec.bigdata.entity.table.updatetwitter.UpdateTwitterUser
import com.ceiec.bigdata.entity.twitter.TwitterRoot
import com.ceiec.bigdata.util.{Constants, InfoIdUtils}
import com.ceiec.bigdata.util.esutil.EsClient
import com.ceiec.bigdata.util.hbaseutil.HBaseUtils
import com.ceiec.bigdata.util.nlputil.{HttpUtils, NlpNewsHelper, NlpTwitterHelper, PojoTransform}
import com.ceiec.bigdata.util.redisutil.RedisUtil
import com.ceiec.bigdata.util.siteidutil.SiteInfoUtils
import com.ceiec.bigdata.util.upuserutil.UpdateUserUtil
import kafka.serializer.StringDecoder
import org.apache.hadoop.hbase.client.{ConnectionFactory, Increment, Put}
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.log4j.LogManager
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by heyichang on 2017/10/25.
  */
object DirectStreamingMain extends Serializable {

  @transient lazy val log = LogManager.getRootLogger


  def main(args: Array[String]) {
    if (args.length < 5) {
      println("please input args like: 1:maxRatePerPartition 2:seconds  3:groupId 4:topicName 5:appName 6:offsetReset")
      System.exit(1)
    }

    /**
      * 初始化sparkstreamingcontext
      *
      * @return StreamingContext
      */
    def functionToCreateContext(): StreamingContext = {
      val sparkConf = new SparkConf().setAppName(args(4))
        .set("spark.streaming.kafka.maxRatePerPartition", args(0))
        .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        .set("spark.executor.extraJavaOptions", "-XX:+UseConcMarkSweepGC")
        .set("spark.streaming.stopGracefullyOnShutdown", "true")
      val ssc = new StreamingContext(sparkConf, Seconds(args(1).toInt))
      //设置checkpoint,保存运行数据
      //ssc.checkpoint(args(2).trim)

      // Create direct kafka stream with brokers and topics
      val topicsSet = args(3).split(",").toSet //diplomat_twitter,diplomat_news,diplomat_twitter_streaming,diplomat_twitter_user,diplomat_facebook
      val kafkaParams = scala.collection.immutable.Map[String, String]("bootstrap.servers" -> "172.16.3.31:6667,172.16.3.32:6667,172.16.3.33:6667", "auto.offset.reset" -> args(5), "group.id" -> args(2)) //largest  smallest

      val km = new KafkaManager(kafkaParams)
      val kafkaDirectStream = km.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)
      log.warn(s"Initial Done***>>>")
      //初始化广播变量
      val myBoradcast = BroadcastWrapper[Map[String, util.List[String]]](ssc, DataFromMysql.updateAllParams)

      kafkaDirectStream.cache

      //消费kafka数据
      kafkaDirectStream.foreachRDD(rdd => {
        if (!rdd.isEmpty) {

          /*更新广播变量每2分钟更新一次*/
          try {
            val cal = Calendar.getInstance()
            val minute = cal.get(Calendar.MINUTE)
            val second = cal.get(Calendar.SECOND)
            if (minute % 2 == 0 && (second > 8 && second < 12)) {
              log.warn("updating broadcast ......")
              val slong = System.currentTimeMillis()
              myBoradcast.update(DataFromMysql.updateAllParams, true)
              val elong = System.currentTimeMillis()
              log.warn("update broadcast used time : " + (elong - slong))
            }
          } catch {
            case ex: Exception => {
              log.error("update broadcast error :", ex)
            }
          }
          //得到广播变量的值
          val myBroadcastValue = myBoradcast.value
          //得到站点信息表信息
          val siteInfoStrs = myBroadcastValue.getOrElse("siteid", null)
          //转化站点信息
          val siteInfoMap = SiteInfoUtils.getSiteTypeId(siteInfoStrs)
          //得到推特预警配置表信息
          val twitterEventInfoList = myBroadcastValue.getOrElse("twitterEvent", null)
          //得到新闻预警配置表信息
          val newsEventInfoList = myBroadcastValue.getOrElse("newsEvent", null)
          //得到facebook关键字预警信息
          val facebookEventInfoList = myBroadcastValue.getOrElse("facebookEvent", null)
          //得到新闻预警配置表信息
          val accountEventMappingList = myBroadcastValue.getOrElse("ForeofAccount", null)
          //得到新闻预警配置表信息
          val accountAndLocationEventInfoList = myBroadcastValue.getOrElse("accountMsgFromTable", null)
          //得到异常表账号
          val accountIdList = myBroadcastValue.getOrElse("accountId", null)
          //得到需要过滤的语言
          val languageFilterList = myBroadcastValue.getOrElse("languageFilter", null)
          //得到账号异常的时间
          val accountTimeList = myBroadcastValue.getOrElse("accountLastTime", null)
          /*对实时接受的消息进行处理*/
          rdd.foreachPartition(iterators => {
            //创建hbase的连接
            val conf = HBaseConfiguration.create
            val beg = System.currentTimeMillis()
            conf.set(Constants.HBASE_ZOOKEEPER_CLIENTPORT, Constants.ZOOKEEPER_PORT)
            conf.set(Constants.HBASE_ZOOKEEPER_QUORUM, Constants.ZOOKEEPER_HOST)
            conf.set(Constants.ZOOKEEPER_ZNODE_PARENT, Constants.PARENT_DIR)
            conf.setInt(Constants.ZOOKEEPER_SESSION_TIMEOUT, Constants.DEFAULT_ZK_SESSION_TIMEOUT)
            val hConnection = ConnectionFactory.createConnection(conf)
            //创建hbase的批处理list
            val hbasePutArray = new ArrayBuffer[Put]()
            //创建twitter hbase的批处理list
            val twitterHbasePutArray = new ArrayBuffer[Put]()
            //创建hbase的统计表list
            val hbaseIncrementArray = new ArrayBuffer[Increment]()
            //创建redis的info批处理list
            val redisStrArray = new ArrayBuffer[String]()
            //创建redis的account批处理list
            val redisAccountStrArray = new ArrayBuffer[String]()
            //创建news的hbase表的连接
            val newsRootTableName = hConnection.getTable(TableName.valueOf(Constants.examplesTableInfo.NEWS_TABLE_NAME))
            //创建统计hbase表的连接
            val statsTableName = hConnection.getTable(TableName.valueOf(Constants.statsHbaseTable.TABLE_NAME))
            //创建推特历史数据hbase表的连接
            val twitterTableName = hConnection.getTable(TableName.valueOf(Constants.twitterHbaseTable.TWITTER_TABLE_NAME))
            //创建es连接
            val client = EsClient.getInstance.getTransportClient
            //创建es的批处理的容器
            val twitterBulkRequestBuilder = client.prepareBulk
            //创建mysql的连接
            val mysqlConn = DataFromMysql.getConn()
            //创建jedis的连接
            val jedis = RedisUtil.getJedis
            //对分区内所有消息进行处理
            var messageCount = 1
            //创建文本处理容器
            var messageArray = new ArrayBuffer[String]()
            var newsMap = mutable.Map[String, String]()
            var facebookMap = mutable.Map[String, FaceBookRoot]()
            var twitterMap = mutable.Map[String, TwitterRoot]()
            //val langFilter = Set("en", "es", "ar", "fr", "pt", "zh", "ru", "kz")
            try {
              while (iterators.hasNext) {
                val line = iterators.next()
                val messageType = line._1
                val messageValue = line._2
                messageType match {
                  case "diplomat" => {
                    try {
                      //解析新闻内容
                      val newsRoot: News = JSON.parseObject(messageValue, classOf[News])
                      //news向统计表添加内容
                      val newsIncrement = HBaseUtils.addIncrement(newsRoot.getCrawlid, Constants.statsHbaseTable.CF)
                      hbaseIncrementArray += newsIncrement
                      //判断是否符合写入es的条件
                      if (newsRoot.getData.getContent != null && (!newsRoot.getData.getContent.trim.equals("")) && newsRoot.getData.getTitle != null) {
                        var pojoString: String = null
                        //当有语言时提交文本处理
                        if (newsRoot.getLang != null) {
                          pojoString = transNlpPojoToString(String.valueOf(messageCount), newsRoot.getLang, newsRoot.getData.getContent)
                        }
                        //当没有语言时提交文本处理
                        else {
                          pojoString = transNlpPojoToString(String.valueOf(messageCount), "auto", newsRoot.getData.getContent)
                        }
                        messageArray += pojoString
                      } else {
                        //messageMap += (String.valueOf(messageCount) -> None)
                        log.warn("crawled content or title is none : " + messageValue)
                      }
                      newsMap += (String.valueOf(messageCount) -> messageValue)
                      messageCount = messageCount + 1
                    } catch {
                      case ex: Exception => {
                        log.error("news parse error : " + ex)
                        log.error("news parse error : " + messageValue)
                      }
                    }
                  }

                  case "streaming" => {
                    //处理本条推文消息
                    val twitterRoot: TwitterRoot = JSON.parseObject(messageValue, classOf[TwitterRoot])
                    if (twitterRoot.getLang != null) {
                      //过滤推文语言
                      val language = twitterRoot.getLang.trim
                      val isLanguageRight = languageFilterList.contains(language)
                      if (isLanguageRight) {

                        /** 将历史数组提存入hbase **/
                        //生成infoId
                        val infoIdStr = "https://twitter.com/" + twitterRoot.getUser.getScreen_name + "/status/" + twitterRoot.getId_str
                        val infoId = InfoIdUtils.generate32MD5ID(infoIdStr)
                        //转化为hbase的put
                        val twitterPut = HBaseUtils.addRow(infoId, Constants.twitterHbaseTable.TEITTER_CF, Constants.twitterHbaseTable.TEITTER_COLUMN, messageValue)
                        //将put放入批量提交容器
                        twitterHbasePutArray += twitterPut

                        //当推特为完全信息时
                        if (twitterRoot.getFull_text != null) {

                          //当不是转推消息时
                          if (twitterRoot.getRetweeted_status == null) {
                            val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), language, twitterRoot.getFull_text)
                            messageArray += pojoString
                          }
                          //当推特是转发消息时取转推和全文消息
                          else {
                            try {
                              //获取转推的人的信息
                              val textPre = twitterRoot.getFull_text.split(":")(0)
                              //当转推消息是全文时
                              if (twitterRoot.getRetweeted_status.getFull_text != null) {
                                val text = textPre + ": " + twitterRoot.getRetweeted_status.getFull_text
                                val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), language, text)
                                messageArray += pojoString
                              }
                              //当转推消不息是全文时
                              else {
                                val text = textPre + ": " + twitterRoot.getRetweeted_status.getText
                                val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), language, text)
                                messageArray += pojoString
                              }
                            } catch {
                              case ex: Exception => {
                                println("error :", ex)
                                log.error("twitter split error :", ex)
                              }
                            }
                          }
                        }
                        //当不是推特为完全信息时
                        else {
                          val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), language, twitterRoot.getText)
                          messageArray += pojoString
                        }

                        //当此消息不是英语时文本不处理
                        //消息来源streaming
                        twitterMap += (String.valueOf(messageCount) -> twitterRoot)

                        /** 当推文是转推时 **/
                        if (twitterRoot.getRetweeted_status != null) {
                          val retweetedTwitter = twitterRoot.getRetweeted_status
                          val reLanguage = retweetedTwitter.getLang
                          val reLanguageIsRight = languageFilterList.contains(reLanguage)
                          if (reLanguageIsRight) {
                            messageCount = messageCount + 1
                            //消息来源streaming
                            twitterMap += (String.valueOf(messageCount) -> retweetedTwitter)
                            //将content放入nlp容器
                            //当转推特为完全信息时
                            if (retweetedTwitter.getFull_text != null) {
                              val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), reLanguage, retweetedTwitter.getFull_text)
                              messageArray += pojoString
                            } //当转不是推特为完全信息时
                            else {
                              val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), reLanguage, retweetedTwitter.getText)
                              messageArray += pojoString
                            }
                          }
                        }

                        /** 当推文是引用时 **/
                        if (twitterRoot.getQuoted_status != null) {

                          val quotedTwitter = twitterRoot.getQuoted_status
                          val quoLanguage = quotedTwitter.getLang
                          val quoLanguageIsRight = languageFilterList.contains(quoLanguage)
                          if (quoLanguageIsRight) {
                            messageCount = messageCount + 1
                            //消息来源streaming
                            twitterMap += (String.valueOf(messageCount) -> quotedTwitter)
                            //当转推特为完全信息时

                            if (quotedTwitter.getFull_text != null) {
                              val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), quoLanguage, quotedTwitter.getFull_text)
                              messageArray += pojoString
                            } //当转不是推特为完全信息时
                            else {
                              val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), quoLanguage, quotedTwitter.getText)
                              messageArray += pojoString
                            }
                          }
                        }
                        messageCount = messageCount + 1
                      }
                    }
                  }

                  case "facebook" => {
                    try {
                      //解析facebook内容

                      val faceBookRoot = JSON.parseObject(messageValue, classOf[FaceBookRoot])
                      val sourceUrl = "http://www.facebook.com/" + faceBookRoot.getPost.getFb_id + "_" + faceBookRoot.getPost.getPost_id
                      val infoId = InfoIdUtils.generate32MD5ID(sourceUrl)
                      val isExists = RedisUtil.isIdExists(jedis, Constants.redisKEY.INFO_KEY, infoId)
                      if (isExists != null && isExists == false) {
                        redisStrArray += infoId
                        val content = faceBookRoot.getPost.getMessage

                        //判断是否符合写入es的条件

                        val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), "auto", content)
                        messageArray += pojoString

                        log.warn("facebook crawled content or title is none : " + messageValue)


                        facebookMap += (String.valueOf(messageCount) -> faceBookRoot)
                        messageCount = messageCount + 1
                      }
                    } catch {
                      case ex: Exception => {
                        log.error("facebook parse error : " + ex)
                        log.error("facebook parse error : " + messageValue)
                      }
                    }

                  }

                  case "user" => {
                    //解析更新用户信息数据
                    val twitterUser: UpdateTwitterUser = JSON.parseObject(messageValue, classOf[UpdateTwitterUser])
                    if (twitterUser.getContent != null && twitterUser.getContent.getErrors == null) {
                      val twitterRootDao = new TwitterRootDaoImpl(twitterUser)
                      val virtualUpdateRequestBuilder = twitterRootDao.updateEsVirtualData(client, Constants.VIRTUAL_INDEX, Constants.ACCOUNT)


                      if (virtualUpdateRequestBuilder != null) {
                        twitterBulkRequestBuilder.add(virtualUpdateRequestBuilder)
                        UpdateUserUtil.updateEsVirtualOverDue(twitterUser.getId)
                      }
                    }
                    else {
                      val twitterRootDao = new TwitterRootDaoImpl(twitterUser)
                      UpdateUserUtil.updateOverDue(twitterUser.getId)
                      log.error("input User has been suspended.   " + messageValue)
                    }

                  }

                  case _ => {
                    //当推文不是转发时
                    val twitterRoot: TwitterRoot = JSON.parseObject(messageValue, classOf[TwitterRoot])
                    //生成infoId
                    val infoIdStr = "https://twitter.com/" + twitterRoot.getUser.getScreen_name + "/status/" + twitterRoot.getId_str
                    val infoId = InfoIdUtils.generate32MD5ID(infoIdStr)
                    val isExists = RedisUtil.isIdExists(jedis, Constants.redisKEY.INFO_KEY, infoId)
                    if (isExists != null && isExists == false) {
                      redisStrArray += infoId
                      //twitter向统计表添加内容
                      val twitterIncrement = HBaseUtils.addIncrement(twitterRoot.getCrawlid, Constants.statsHbaseTable.CF)
                      hbaseIncrementArray += twitterIncrement
                      //推特语言
                      val language = twitterRoot.getLang
                      /** 将历史数组提存入hbase **/
                      val twitterPut = HBaseUtils.addRow(infoId, Constants.twitterHbaseTable.TEITTER_CF, Constants.twitterHbaseTable.TEITTER_COLUMN, messageValue)
                      twitterHbasePutArray += twitterPut
                      //当此消息是指定语言时提交给文本处理
                      val isLanguageRight = languageFilterList.contains(language)
                      //当推特为完全信息时
                      if (isLanguageRight) {
                        if (twitterRoot.getFull_text != null) {
                          //当不是转推消息时
                          if (twitterRoot.getRetweeted_status == null) {
                            val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), language, twitterRoot.getFull_text)
                            messageArray += pojoString
                          }
                          //当推特是转发消息时取转推和全文消息
                          else {
                            try {
                              //获取转推的人的信息
                              val textPre = twitterRoot.getFull_text.split(":")(0)
                              //当转推消息是全文时
                              if (twitterRoot.getRetweeted_status.getFull_text != null) {
                                val text = textPre + ": " + twitterRoot.getRetweeted_status.getFull_text
                                val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), language, text)
                                messageArray += pojoString
                              }
                              //当转推消不息是全文时
                              else {
                                val text = textPre + ": " + twitterRoot.getRetweeted_status.getText
                                val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), language, text)
                                messageArray += pojoString
                              }
                            } catch {
                              case ex: Exception => {
                                println("error :", ex)
                                log.error("twitter split error :", ex)
                              }
                            }
                          }
                        }
                        //当不是推特为完全信息时
                        else {
                          val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), language, twitterRoot.getText)
                          messageArray += pojoString
                        }
                      }

                      //消息来源crlw
                      twitterMap += (String.valueOf(messageCount) -> twitterRoot)

                      /** 当推文是转推时 **/
                      if (twitterRoot.getRetweeted_status != null) {
                        val retweetedTwitter = twitterRoot.getRetweeted_status
                        val reLanguage = retweetedTwitter.getLang
                        val reLanguageIsRight = languageFilterList.contains(reLanguage)
                        //是指定语言提交给文本
                        messageCount = messageCount + 1
                        if (reLanguageIsRight) {
                          //将content放入nlp容器
                          //当转推特为完全信息时
                          if (twitterRoot.getRetweeted_status.getFull_text != null) {
                            val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), reLanguage, retweetedTwitter.getFull_text)
                            messageArray += pojoString
                          } //当转不是推特为完全信息时
                          else {
                            val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), reLanguage, retweetedTwitter.getText)
                            messageArray += pojoString
                          }
                        }

                        twitterMap += (String.valueOf(messageCount) -> retweetedTwitter)
                      }

                      /** 当推文是引用时 **/
                      if (twitterRoot.getQuoted_status != null) {

                        val quotedTwitter = twitterRoot.getQuoted_status
                        val quoLanguage = quotedTwitter.getLang
                        val quoLanguageIsRight = languageFilterList.contains(quoLanguage)
                        messageCount = messageCount + 1
                        if (quoLanguageIsRight) {
                          //当转推特为完全信息时
                          if (quotedTwitter.getFull_text != null) {
                            val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), quoLanguage, quotedTwitter.getFull_text)
                            messageArray += pojoString
                          }
                          //当转不是推特为完全信息时
                          else {
                            val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), quoLanguage, quotedTwitter.getText)
                            messageArray += pojoString
                          }
                        }
                        twitterMap += (String.valueOf(messageCount) -> quotedTwitter)
                      }

                      messageCount = messageCount + 1
                    }
                  }

                }
              }

              /*处理twitter数据**/
              if (twitterMap.size > 0) {
                val createParams = NlpTwitterHelper.getParms
                import scala.collection.JavaConverters._
                val messageList = messageArray.toList.asJava
                val str = messageList.toString
                var result: String = null
                if (str != null) {
                  result = HttpUtils.getResultByPost(createParams, str);
                }
                //处理一批量返回数据的异常
                try {
                  var nlpResultMap: util.HashMap[String, AnyRef] = null
                  //处理nlp返回的格式错误异常
                  try {
                    if (result != null) {
                      nlpResultMap = JSON.parseObject(result, classOf[util.HashMap[String, AnyRef]])
                    }
                  } catch {
                    case ex: Exception => {
                      log.error("twitter nlp  data parse error :", ex)
                      log.error(result)
                    }
                  }

                  for ((x, y) <- twitterMap) {
                    var nlpResultString: String = null
                    if (nlpResultMap != null && nlpResultMap.get(x) != null) {
                      nlpResultString = nlpResultMap.get(x).toString
                    }

                    val twitterRootDao = new TwitterRootDaoImpl(y, nlpResultString)
                    //账号和地点异常检测预警
                    twitterRootDao.accoutAndLocationWarning(accountAndLocationEventInfoList, accountEventMappingList, accountIdList, accountTimeList)

                    if (y.getLang.trim.equals("en") || y.getLang.trim.equals("zh") || y.getLang.trim.equals("es")) {
                      //处理关键词预警
                      twitterRootDao.keyWordWarning(mysqlConn, twitterEventInfoList)
                    }

                    //单条处理es
                    val infoIndexRequestBuilder = twitterRootDao.bulkAddEsData(client, Constants.INFO_INDEX, Constants.INFO_TYPE)
                    twitterBulkRequestBuilder.add(infoIndexRequestBuilder)
                    //当有用户信息时则添加用户信息虚拟表
                    val accountId = twitterRootDao.getAccountId
                    val isAccountIdExists = RedisUtil.isIdExists(jedis, Constants.redisKEY.ACCOUNT_KEY, accountId)
                    //判断账号是否存在
                    if (isAccountIdExists != null && isAccountIdExists == false) {
                      //如果不是streaming则添加去重
                      if (y.getStreaming != null && y.getStreaming == 0) {
                        redisAccountStrArray += accountId
                      }
                      if (y.getUser != null) {
                        //写入虚拟账号表
                        val virtualIndexRequestBuilder = twitterRootDao.bulkAddEsVirtualData(client, Constants.VIRTUAL_INDEX, Constants.ACCOUNT)
                        twitterBulkRequestBuilder.add(virtualIndexRequestBuilder)
                      }
                    }

                  }

                } catch {
                  case ex: Exception => {
                    log.error("twitter data  process error :", ex)
                  }
                }

              }

              /*处理news数据**/
              else if (newsMap.size > 0) {
                val createParams = NlpNewsHelper.getParms

                import scala.collection.JavaConverters._
                val messageList = messageArray.toList.asJava
                val str = messageList.toString
                var result: String = null

                if (str != null) {
                  result = HttpUtils.getResultByPost(createParams, str);
                }

                var nlpResultMap: util.HashMap[String, AnyRef] = null
                //处理新闻nlp返回结果解析异常
                try {
                  if (result != null) {
                    nlpResultMap = JSON.parseObject(result, classOf[util.HashMap[String, AnyRef]])
                  }
                } catch {
                  case ex: Exception => {
                    log.error("news nlp data parse error :", ex)
                    log.error(result)
                  }
                }

                for ((x, y) <- newsMap) {
                  var nlpResultString: String = null
                  if (nlpResultMap != null && nlpResultMap.get(x) != null) {
                    nlpResultString = nlpResultMap.get(x).toString
                  }
                  val newsRoot: News = JSON.parseObject(y, classOf[News])
                  val newsRootDao = new NewsDaoImpl(newsRoot, siteInfoMap, y, nlpResultString)
                  hbasePutArray += newsRootDao.getHbaseRowPut
                  if (newsRoot.getData != null) {
                    try {

                      val data = newsRoot.getData
                      /*对新闻消息进行关键字预警和单条信息处理*/
                      if (data.getContent != null && !data.getContent.trim.equals("") && data.getTitle != null) {

                        val infoIndexRequestBuilder = newsRootDao.bulkAddEsData(client, Constants.INFO_INDEX, Constants.INFO_TYPE)
                        twitterBulkRequestBuilder.add(infoIndexRequestBuilder)
                        if (infoIndexRequestBuilder != null) {
                          //关键词预警
                          newsRootDao.keyWordWarning(mysqlConn, newsEventInfoList)
                          if (data.getCreate_time != null && data.getCreate_time.contains("T") && data.getCreate_time.contains("Z")) {
                            log.warn("news creatdata is incorrect " + data)
                          }
                        }
                      }
                    } catch {
                      case ex: Exception => {
                        log.error("process new data error :", ex)
                      }
                    }
                  }
                }
                val newEndTime = System.currentTimeMillis //TODO
                //  println("new process time one message : " + (newEndTime - newStartTime))
              }

              /*处理facebook数据**/
              if (facebookMap.size > 0) {
                val createParams = NlpTwitterHelper.getParms
                import scala.collection.JavaConverters._
                val messageList = messageArray.toList.asJava
                val str = messageList.toString

                var result: String = null


                if (str != null) {
                  result = HttpUtils.getResultByPost(createParams, str);
                }
                //处理一批量fb数据的异常
                try {
                  var nlpResultMap: util.HashMap[String, AnyRef] = null
                  //处理facebook返回nlp解析异常
                  try {
                    if (result != null) {
                      nlpResultMap = JSON.parseObject(result, classOf[util.HashMap[String, AnyRef]])
                    }
                  } catch {
                    case ex: Exception => {
                      log.error("facebook nlp  data parse error :", ex)
                      log.error(result)
                    }
                  }

                  for ((x, y) <- facebookMap) {
                    var nlpResultString: String = null
                    if (nlpResultMap != null && nlpResultMap.get(x) != null) {
                      nlpResultString = nlpResultMap.get(x).toString
                    }

                    val facebookDao = new FaceBookDaoImpl(y, nlpResultString)
                    //账号和地点异常账号和地点预警
                    //facebookDao.accoutAndLocationWarning(accountAndLocationEventInfoList, accountEventMappingList, accountIdList, accountTimeList)

                    //处理关键词预警
                    facebookDao.keyWordWarning(mysqlConn, facebookEventInfoList)

                    //单条处理es
                    val infoIndexRequestBuilder = facebookDao.bulkAddPostEsData(client, Constants.INFO_INDEX, Constants.INFO_TYPE)
                    twitterBulkRequestBuilder.add(infoIndexRequestBuilder)
                    //当有用户信息时则添加用户信息虚拟表
                    //当有用户信息时则添加用户信息虚拟表
                    val accountId = facebookDao.getAccountId
                    val isAccountIdExists = RedisUtil.isIdExists(jedis, "es_account_id", accountId)
                    if (isAccountIdExists != null && isAccountIdExists == false) {
                      if (y.getUser != null) {
                        //将此账号加入redis缓存
                        redisAccountStrArray += accountId
                        //写入虚拟账号表
                        val virtualIndexRequestBuilder = facebookDao.bulkAddEsVirtualData(client, Constants.VIRTUAL_INDEX, Constants.ACCOUNT)
                        twitterBulkRequestBuilder.add(virtualIndexRequestBuilder)
                      }
                    }

                  }

                } catch {
                  case ex: Exception => {
                    println("error :", ex)
                    log.error("facebook data process error :", ex)
                    log.error(result)
                  }
                }

              }

              //批量提交{es、hbase}的批量数据
              if (twitterBulkRequestBuilder.numberOfActions > 0) {
                val bulkResponse = twitterBulkRequestBuilder.execute.actionGet
                if (bulkResponse.hasFailures) {
                  System.out.println("error in doing index request: " + bulkResponse.buildFailureMessage)
                }
              }
              import scala.collection.JavaConverters._
              val hbasePutList = hbasePutArray.toList.asJava
              val hbaseIncrementList = hbaseIncrementArray.toList.asJava
              val twitterHbasePutList = twitterHbasePutArray.toList.asJava
              val redisInfoStringList = redisStrArray.toList.asJava
              val redisAccountStringList = redisAccountStrArray.toList.asJava
              if (hbasePutList.size() > 0) {
                HBaseUtils.batchAddPut(newsRootTableName, hbasePutList)
              }
              if (twitterHbasePutList.size() > 0) {
                HBaseUtils.batchAddPut(twitterTableName, twitterHbasePutList)
              }
              if (hbaseIncrementList.size() > 0) {
                HBaseUtils.batchAddIncrement(statsTableName, hbaseIncrementList)
              }
              if (redisInfoStringList.size() > 0) {
                RedisUtil.sAdd(jedis, Constants.redisKEY.INFO_KEY, redisInfoStringList)
              }
              if (redisAccountStringList.size() > 0) {
                RedisUtil.sAdd(jedis, Constants.redisKEY.ACCOUNT_KEY, redisAccountStringList)
              }

            } catch {
              case ex: Exception => {
                println("error :", ex)
                log.error("error :", ex)
              }
            } finally {
              if(newsRootTableName != null){
                newsRootTableName.close()
              }
              if(statsTableName != null){
                statsTableName.close()
              }
              if(twitterTableName != null){
                twitterTableName.close()
              }
              if(hConnection != null ){
                hConnection.close()
              }
              if(mysqlConn != null){
                mysqlConn.close()
              }
              RedisUtil.close(jedis)
            }
          })

          /*更新kafka的offset*/
          km.updateZKOffsets(rdd)
        }
        else {
          log.info("rdd is empty")

        }
      }
      )
      ssc
    }

    //  val ssc = StreamingContext.getOrCreate(args(2).trim, functionToCreateContext _) 暂时取消切点功能
    val ssc = functionToCreateContext
    ssc.start()
    ssc.awaitTermination()
  }

  def transNlpPojoToString(amount: String, lang: String, content: String): String = {
    val nlpPojo = new NlpPojo(amount, lang, content)
    val pojoString = PojoTransform.pojoToString(nlpPojo)
    return pojoString
  }


}
