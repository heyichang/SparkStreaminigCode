package com.ceiec.bigdata.action

import java.io._
import java.util
import java.util.{Arrays, Calendar, Properties}

import com.alibaba.fastjson.JSON
import com.ceiec.bigdata.action.DirectStreamingMain.log
import com.ceiec.bigdata.action.DirectStreamingTest.{log, transNlpPojoToString}
import com.ceiec.bigdata.dao.FaceBookDao
import com.ceiec.bigdata.dao.impl.{FaceBookDaoImpl, NewsDaoImpl, TwitterRootDaoImpl}
import com.ceiec.bigdata.entity.{AllFacebookPost, AllNewsPost, FacebookUserType, UserEntityCommon}
import com.ceiec.bigdata.entity.facebook.FaceBookRoot
import com.ceiec.bigdata.entity.facebook.es.FaceBookInfo
import com.ceiec.bigdata.entity.news.{Data, News}
import com.ceiec.bigdata.entity.table.nlp.{EntityNlp, NlpPojo}
import com.ceiec.bigdata.entity.twitter.TwitterRoot
import com.ceiec.bigdata.util.{Constants, InfoIdUtils}
import com.ceiec.bigdata.util.esutil.{EsClient, EsUtils}
import com.ceiec.bigdata.util.eventutil.KeywordFilter
import com.ceiec.bigdata.util.hbaseutil.{HBaseConnectionFactory, HBaseUtils}
import com.ceiec.bigdata.util.nlputil.{HttpUtils, NlpNewsHelper, NlpTwitterHelper, PojoTransform}
import com.ceiec.bigdata.util.redisutil.RedisUtil
import com.ceiec.bigdata.util.siteidutil.{SideInfoJudgement, SiteInfoUtils}
import kafka.serializer.StringDecoder
import org.apache.hadoop.hbase.client.{ConnectionFactory, Put}
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.kafka.clients.consumer.{ConsumerRecords, KafkaConsumer}
import org.elasticsearch.action.index.IndexRequestBuilder
import org.apache.log4j.LogManager
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.elasticsearch.action.index.IndexRequestBuilder
import org.elasticsearch.action.update.UpdateRequest

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by heyichang on 2017/10/25.
  */
object DirectStreamingPost extends Serializable {

  @transient lazy val log = LogManager.getRootLogger


  /**
    * sparkcontext的初始化
    *
    * @return
    */
  def functionToCreateContext(args: Array[String]): StreamingContext = {
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

    val myBoradcast = BroadcastWrapper[Map[String, util.List[String]]](ssc, DataFromMysql.updateAllParams)

    val km = new KafkaManager(kafkaParams)
    val kafkaDirectStream = km.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)
    log.warn(s"Initial Done***>>>")

    kafkaDirectStream.cache


    //更新zk中的offset
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
        if(myBoradcast != null){

          //得到广播变量的值
          val myBroadcastValue = myBoradcast.value
          //得到站点信息表信息
          val siteInfoStrs = myBroadcastValue.getOrElse("siteidMapping", null)
          //转化站点信息
          val siteInfoMap = SiteInfoUtils.getSiteTypeId(siteInfoStrs)
          //得到站点信息表信息
          val regionInfoStrs = myBroadcastValue.getOrElse("regionInfoMapping", null)
          //转化站点信息
          val regionInfoMap = SiteInfoUtils.getRegionInfoMap(regionInfoStrs)
          //得到关键字预警配置表信息
          val keywordInfoList = myBroadcastValue.getOrElse("keywordMapping", null)
          //得到需要过滤的语言
          val languageFilterList = myBroadcastValue.getOrElse("languageFilter", null)
          //worker工作内容
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
            //创建news的hbase表的连接
            val newsRootTableName = hConnection.getTable(TableName.valueOf(Constants.examplesTableInfo.NEWS_TABLE_NAME_v2))
            //创建es连接
            val client = EsClient.getInstance.getTransportClient
            //创建es的批处理的容器
            val twitterBulkRequestBuilder = client.prepareBulk
            //对分区内所有消息进行处理
            var messageCount = 1
            //对新闻类内所有消息进行处理
            var newsCount = 1
            //对facebook类内所有消息进行处理
            var facebookCount = 1
            //创建mysql的连接
            val mysqlConn = DataFromMysql.getConn()
            //创建jedis的连接
            val jedis = RedisUtil.getJedis
            //创建redis的info批处理list
            //val redisStrArray = new ArrayBuffer[String]()
            //创建redis的account批处理list
            val redisAccountStrArray = new ArrayBuffer[String]()
            //创建文本处理容器
            var messageArray = new ArrayBuffer[String]()
            var fbMessageArray = new ArrayBuffer[String]()
            var newsMessageArray = new ArrayBuffer[String]()
            var newsMap = mutable.Map[String, String]()
            var facebookMap = mutable.Map[String, AllFacebookPost]()
            var twitterMap = mutable.Map[String, TwitterRoot]()
            //从广播中拿到regionId信息的set集合
            val regionInfoSet = regionInfoMap.keySet()
            //生成region判断的类
            try {
              val filter = new KeywordFilter(regionInfoSet);
              while (iterators.hasNext) {
                val line = iterators.next()
                val linetype = line._1
                val linevalue = line._2
                val userEntityCommon = JSON.parseObject(linevalue, classOf[UserEntityCommon])
                val userType = userEntityCommon.getSource_type

                linetype match {
                  //处理帖子信息
                  case _ => {
                    userType match {
                      //处理twitter帖子信息
                      case "1" => {
                        //处理本条推文消息
                        val twitterMetaInfo = userEntityCommon.getContent
                        val twitterRoot: TwitterRoot = JSON.parseObject(userEntityCommon.getContent, classOf[TwitterRoot])
                        if (twitterRoot.getLang != null) {
                          //过滤推文语言
                          val language = twitterRoot.getLang.trim
                          //拿到来源
                          var fromWhere = twitterRoot.getStreaming
                          if (fromWhere != null && (fromWhere == 1 || fromWhere == 0)) {
                          } else {
                            fromWhere = 0
                          }

                          //拿到twitter粉丝数
                          var twitterFansNub = twitterRoot.getUser.getFollowers_count
                          if (twitterFansNub != null) {} else {
                            twitterFansNub = 0
                          }
                          val isLanguageRight = languageFilterList.contains(language)
                          if (isLanguageRight ||(twitterRoot.getStreaming != null && twitterRoot.getStreaming == 0)) {
                            /** 将twitter历史数组提存入hbase **/
                            if (twitterRoot != null) {
                              //生成infoId
                              val infoIdStr = "https://twitter.com/" + twitterRoot.getUser.getScreen_name + "/status/" + twitterRoot.getId_str
                              val infoId = InfoIdUtils.generate32MD5ID(infoIdStr)
                              //转化为hbase的put
                              val twitterPut = HBaseUtils.addRow(infoId, Constants.examplesTableInfo.FAMILY, Constants.examplesTableInfo.COLUMN, twitterMetaInfo)
                              //将put放入批量提交容器
                              hbasePutArray += twitterPut
                            }


                            //当推特为完全信息时
                            if (twitterRoot.getFull_text != null) {
                              //当不是转推消息时
                              if (twitterRoot.getRetweeted_status == null) {
                                try {
                                  val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), language, twitterRoot.getFull_text, twitterFansNub, fromWhere)
                                  messageArray += pojoString
                                } catch {
                                  case ex: Exception => {
                                    println("error :", ex)
                                    log.error("twitter nlp pojoString error :", ex)
                                  }
                                }

                              }
                              //当推特是转发消息时取转推和全文消息
                              else {
                                try {
                                  //获取转推的人的信息
                                  val textPre = twitterRoot.getFull_text.split(":")(0)
                                  //当转推消息是全文时
                                  if (twitterRoot.getRetweeted_status.getFull_text != null) {
                                    val text = textPre + ": " + twitterRoot.getRetweeted_status.getFull_text
                                    val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), language, text, twitterFansNub, fromWhere)
                                    messageArray += pojoString
                                  }
                                  //当转推消不息是全文时
                                  else {
                                    try {
                                      val text = textPre + ": " + twitterRoot.getRetweeted_status.getText
                                      val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), language, text, twitterFansNub, fromWhere)
                                      messageArray += pojoString
                                    } catch {
                                      case ex: Exception => {
                                        println("error :", ex)
                                        log.error("twitter nlp pojoString error :", ex)
                                      }
                                    }

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
                              try {
                                val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), language, twitterRoot.getText, twitterFansNub, fromWhere)
                                messageArray += pojoString
                              } catch {
                                case ex: Exception => {
                                  println("error :", ex)
                                  log.error("twitter nlp pojoString error :", ex)
                                }
                              }

                            }

                            //当此消息不是英语时文本不处理
                            //消息来源streaming
                            twitterMap += (String.valueOf(messageCount) -> twitterRoot)

                            /** 当推文是转推时 **/
                            if (twitterRoot.getRetweeted_status != null) {

                              val retweetedTwitter = twitterRoot.getRetweeted_status
                              val reLanguage = retweetedTwitter.getLang
                              //拿到twitter粉丝数
                              var reTwitterFansNub = retweetedTwitter.getUser.getFollowers_count
                              if (reTwitterFansNub != null) {} else {
                                reTwitterFansNub = 0
                              }
                              val reLanguageIsRight = languageFilterList.contains(reLanguage)
                              if (reLanguageIsRight) {
                                messageCount = messageCount + 1
                                //消息来源streaming
                                twitterMap += (String.valueOf(messageCount) -> retweetedTwitter)
                                //将content放入nlp容器
                                //当转推特为完全信息时
                                if (retweetedTwitter.getFull_text != null) {
                                  val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), reLanguage, retweetedTwitter.getFull_text, reTwitterFansNub, 0)
                                  messageArray += pojoString
                                } //当转不是推特为完全信息时
                                else {
                                  val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), reLanguage, retweetedTwitter.getText, reTwitterFansNub, 0)
                                  messageArray += pojoString
                                }
                              }
                            }

                            /** 当推文是引用时 **/
                            if (twitterRoot.getQuoted_status != null) {

                              val quotedTwitter = twitterRoot.getQuoted_status
                              val quoLanguage = quotedTwitter.getLang
                              //拿到twitter粉丝数
                              var quoTwitterFansNub = quotedTwitter.getUser.getFollowers_count
                              if (quoTwitterFansNub != null) {} else {
                                quoTwitterFansNub = 0
                              }
                              val quoLanguageIsRight = languageFilterList.contains(quoLanguage)
                              if (quoLanguageIsRight) {
                                messageCount = messageCount + 1
                                //消息来源streaming
                                twitterMap += (String.valueOf(messageCount) -> quotedTwitter)
                                //当转推特为完全信息时

                                if (quotedTwitter.getFull_text != null) {
                                  val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), quoLanguage, quotedTwitter.getFull_text, quoTwitterFansNub, 0)
                                  messageArray += pojoString
                                } //当转不是推特为完全信息时
                                else {
                                  val pojoString: String = transNlpPojoToString(String.valueOf(messageCount), quoLanguage, quotedTwitter.getText, quoTwitterFansNub, 0)
                                  messageArray += pojoString
                                }
                              }
                            }
                            messageCount = messageCount + 1
                          }
                        }
                      }
                      //处理facebook帖子信息
                      case "2" => {
                        val facebookPostStr = userEntityCommon.getContent

                        //                      println(facebookPostStr)
                        val facebookPost: AllFacebookPost = JSON.parseObject(facebookPostStr, classOf[AllFacebookPost])
                        //判断是否符合写入es的条件
                        val pojoString: String = transFBNlpPojoToString(String.valueOf(facebookCount), "auto", facebookPost.getContent)
                        fbMessageArray += pojoString

                        if (facebookPost.getSource_url != null) {
                          //生成facebookinfoid
                          val sourceUrl = facebookPost.getSource_url
                          val infoId = InfoIdUtils.generate32MD5ID(sourceUrl)
                          //将facebook 原始数据存入hbase ，转化为hbase的put
                          val newsPut = HBaseUtils.addRow(infoId, Constants.examplesTableInfo.FAMILY, Constants.examplesTableInfo.COLUMN, facebookPostStr)
                          //将put放入批量提交容器
                          hbasePutArray += newsPut
                        }


                        facebookMap += (String.valueOf(facebookCount) -> facebookPost)
                        facebookCount = facebookCount + 1
                      }
                      //处理新闻博客信息
                      case _ => {
                        val newsContent = userEntityCommon.getContent
                        //                      println(linevalue)
                        val allNewsPost: AllNewsPost = JSON.parseObject(newsContent, classOf[AllNewsPost])

                        val isHaveSiteInfo = SideInfoJudgement.siteIdIsExists(allNewsPost.getSource_url, siteInfoMap)
                        if (isHaveSiteInfo) {
                          //判断是否符合写入es的条件
                          if (allNewsPost.getContent != null && (!allNewsPost.getContent.trim.equals("")) && allNewsPost.getTitle != null) {
                            var pojoString: String = null
                            //当有语言时提交文本处理
                            if (allNewsPost.getLang != null) {
                              pojoString = transNewsNlpPojoToString(String.valueOf(newsCount), allNewsPost.getLang, allNewsPost.getContent)
                            }
                            //当没有语言时提交文本处理
                            else {
                              pojoString = transNewsNlpPojoToString(String.valueOf(newsCount), "auto", allNewsPost.getContent)
                            }
                            newsMessageArray += pojoString
                            val infoId = InfoIdUtils.generate32MD5ID(allNewsPost.getSource_url)
                            //转化为hbase的put
                            val newsPut = HBaseUtils.addRow(infoId, Constants.examplesTableInfo.FAMILY, Constants.examplesTableInfo.COLUMN, newsContent)
                            //将put放入批量提交容器
                            hbasePutArray += newsPut
                          } else {
                            //messageMap += (String.valueOf(messageCount) -> None)
                            log.warn("crawled content or title is none : " + newsContent)
                          }
                          newsMap += (String.valueOf(newsCount) -> newsContent)
                          newsCount = newsCount + 1

                        }

                      }
                    }
                  }
                }
              }

              /*处理facebook数据**/
              if (facebookMap.size > 0) {
                val createParams = NlpTwitterHelper.getParms
                import scala.collection.JavaConverters._
                val messageList = fbMessageArray.toList.asJava
                val str = messageList.toString

                var result: String = null

                if (str != null) {

                  try {
                    result = HttpUtils.getResultByPost(createParams, str)
                    println("facebook  :  " + result)
                  } catch {
                    case ex: Exception => {
                      log.error("facebook nlp http request  data error :", ex)
                      log.error(result)
                    }
                  }
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

                    val facebookDao = new FaceBookDaoImpl(y, nlpResultString, filter, regionInfoMap)
                    //账号和地点异常账号和地点预警
                    //facebookDao.accoutAndLocationWarning(accountAndLocationEventInfoList, accountEventMappingList, accountIdList, accountTimeList)

                    //处理关键词预警
                    facebookDao.keyWordWarning(mysqlConn, keywordInfoList)

                    //单条处理es
                    val infoIndexRequestBuilder = facebookDao.bulkAddPostEsData(client, Constants.INFO_INDEX, Constants.INFO_TYPE)
                    twitterBulkRequestBuilder.add(infoIndexRequestBuilder)
                  }

                } catch {
                  case ex: Exception => {
                    println("error :", ex)
                    log.error("facebook data process error :", ex)
                    log.error(result)
                  }
                }

              }

              /*处理news数据**/
              else if (newsMap.size > 0) {
                val createParams = NlpNewsHelper.getParms

                import scala.collection.JavaConverters._
                val messageList = newsMessageArray.toList.asJava
                val str = messageList.toString
                var result: String = null

                if (str != null) {
                  try {
                    result = HttpUtils.getResultByPost(createParams, str)
                    println("news  :  " + result)
                  } catch {
                    case ex: Exception => {
                      log.error("news nlp http request  data error :", ex)
                      log.error(result)
                    }
                  }
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
                  val allNewsPost: AllNewsPost = JSON.parseObject(y, classOf[AllNewsPost])
                  val newsRootDao = new NewsDaoImpl(allNewsPost, siteInfoMap, nlpResultString, filter, regionInfoMap)

                  if (allNewsPost.getContent != null && !allNewsPost.getContent.trim.equals("") && allNewsPost.getTitle != null) {
                    try {

                      /*对新闻消息进行关键字预警和单条信息处理*/
                      val newsInfoIndexRequestBuilder = newsRootDao.bulkAddEsData(client, Constants.INFO_INDEX, Constants.INFO_TYPE)
                      twitterBulkRequestBuilder.add(newsInfoIndexRequestBuilder)
                      if (newsInfoIndexRequestBuilder != null) {
                        //关键词预警
                        newsRootDao.keyWordWarning(mysqlConn, keywordInfoList)
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
              /*处理twitter数据**/
              else if (twitterMap.size > 0) {
                val createParams = NlpTwitterHelper.getParms
                import scala.collection.JavaConverters._
                val messageList = messageArray.toList.asJava
                val str = messageList.toString
                var result: String = null
                if (str != null) {
                  try {
                    result = HttpUtils.getResultByPost(createParams, str)
                    println("twitter  :  " + result)
                  } catch {
                    case ex: Exception => {
                      log.error("twitter nlp http request  data error :", ex)
                      log.error(result)
                    }
                  }

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
                    var isrub = false
                    if (nlpResultString != null) {
                      try {
                        val nlpResult = JSON.parseObject(nlpResultString, classOf[EntityNlp])
                        if (nlpResult != null && nlpResult.getCategory != null && nlpResult.getCategory == 400) {
                          isrub = true
                        }
                      }
                      catch {
                        case ex: Exception => {
                          log.error("twitter nlpinfo  process error :", ex)
                        }
                      }

                    }
                    //如果不是垃圾信息则处理
                    if (!isrub || (y.getStreaming != null && y.getStreaming == 0)) {
                      val twitterRootDao = new TwitterRootDaoImpl(y, nlpResultString, filter, regionInfoMap)
                      //账号和地点异常检测预警
                      // twitterRootDao.accoutAndLocationWarning(accountAndLocationEventInfoList, accountEventMappingList, accountIdList, accountTimeList)

                      if (y.getLang.trim.equals("en") || y.getLang.trim.equals("zh") || y.getLang.trim.equals("es")) {
                        //处理关键词预警
                        twitterRootDao.keyWordWarning(mysqlConn, keywordInfoList)
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
                        //                    if (y.getFrom_where == null) {
                        redisAccountStrArray += accountId
                        //                    }
                        if (y.getUser != null) {
                          //写入虚拟账号表
                          val virtualIndexRequestBuilder = twitterRootDao.bulkAddEsVirtualData(client, Constants.VIRTUAL_INDEX, Constants.ACCOUNT)
                          twitterBulkRequestBuilder.add(virtualIndexRequestBuilder)
                        }
                      }
                    }


                  }
                } catch {
                  case ex: Exception => {
                    log.error("twitter data  process error :", ex)
                  }
                }

              }
              import scala.collection.JavaConverters._
              val redisAccountStringList = redisAccountStrArray.toList.asJava
              val hbasePutList = hbasePutArray.toList.asJava
              //批量提交{es、hbase}的批量数据
              if (twitterBulkRequestBuilder.numberOfActions > 0) {
                val bulkResponse = twitterBulkRequestBuilder.execute.actionGet
                if (bulkResponse.hasFailures) {
                  System.out.println("error in doing index request: " + bulkResponse.buildFailureMessage)
                }
              }
              if (redisAccountStringList.size() > 0) {
                RedisUtil.sAdd(jedis, Constants.redisKEY.ACCOUNT_KEY, redisAccountStringList)
              }
              if (hbasePutList.size() > 0) {
                HBaseUtils.batchAddPut(newsRootTableName, hbasePutList)
              }

            }
            catch {
              case ex: Exception => {
                println("error :", ex)
                log.error("error :", ex)
              }
            }
            finally {
              if (mysqlConn != null) {
                mysqlConn.close()
              }
              if (newsRootTableName != null) {
                newsRootTableName.close()
              }
              if (hConnection != null) {
                hConnection.close()
              }

              RedisUtil.close(jedis)
            }
          }
          )
          //处理完消息后将kafka偏移量更新到zk
          km.updateZKOffsets(rdd)
        }

      }

      else {
        log.warn("rdd is empty")
      }
    }

    )
    ssc
  }


  def main(args: Array[String]) {

    val ssc = functionToCreateContext(args: Array[String])
    // val ssc = StreamingContext.getOrCreate("hdfs://n1:8020/test-sparkstreaming/kafka2",functionToCreateContext _)
    // Start the computation
    ssc.start()
    ssc.awaitTermination()
  }

  def transNlpPojoToString(amount: String, lang: String, content: String, fans_num: Int, streaming: Int): String = {

    val nlpPojo = new NlpPojo(amount, lang, content, fans_num, streaming)
    val pojoString = PojoTransform.pojoToString(nlpPojo)
    return pojoString

  }

  def transFBNlpPojoToString(amount: String, lang: String, content: String): String = {
    val nlpPojo = new NlpPojo(amount, lang, content, 1100, 0)
    val pojoString = PojoTransform.pojoToString(nlpPojo)
    return pojoString
  }

  def transNewsNlpPojoToString(amount: String, lang: String, content: String): String = {
    val nlpPojo = new NlpPojo(amount, lang, content)
    val pojoString = PojoTransform.pojoToString(nlpPojo)
    return pojoString
  }
}
