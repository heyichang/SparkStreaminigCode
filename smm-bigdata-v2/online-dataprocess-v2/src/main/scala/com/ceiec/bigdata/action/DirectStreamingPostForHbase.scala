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
object DirectStreamingPostForHbase extends Serializable {


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

    val km = new KafkaManager(kafkaParams)
    val kafkaDirectStream = km.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)
    log.warn(s"Initial Done***>>>")

    kafkaDirectStream.cache


    //更新zk中的offset
    kafkaDirectStream.foreachRDD(rdd => {

      if (!rdd.isEmpty) {

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

          //创建jedis的连接
          val jedis = RedisUtil.getJedis
          //创建redis的info批处理list
          //val redisStrArray = new ArrayBuffer[String]()
          //创建redis的account批处理list
          val redisAccountStrArray = new ArrayBuffer[String]()

          //生成region判断的类
          try {
            while (iterators.hasNext) {
              val line = iterators.next()
              val linetype = line._1
              val linevalue = line._2
              val userEntityCommon = JSON.parseObject(linevalue, classOf[UserEntityCommon])
              val userType = userEntityCommon.getSource_type
              //创建文本处理容器
              var messageArray = new ArrayBuffer[String]()
              var fbMessageArray = new ArrayBuffer[String]()
              var newsMessageArray = new ArrayBuffer[String]()
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
                        val languageFilterList = new ArrayBuffer[String]()
                        languageFilterList += "es"
                        languageFilterList += "en"
                        languageFilterList += "ar"
                        languageFilterList += "fr"
                        languageFilterList += "pt"
                        languageFilterList += "zh"
                        languageFilterList += "ru"
                        languageFilterList += "kz"
                        //拿到twitter粉丝数
                        var twitterFansNub = twitterRoot.getUser.getFollowers_count
                        if (twitterFansNub != null) {} else {
                          twitterFansNub = 0
                        }
                        val isLanguageRight = languageFilterList.contains(language)
                        if (isLanguageRight || (twitterRoot.getStreaming != null && twitterRoot.getStreaming == 0)) {
                          /** 将twitter历史数组提存入hbase **/
                          if (twitterRoot != null) {
                            //生成infoId
                            val infoIdStr = "https://twitter.com/" + twitterRoot.getUser.getScreen_name + "/status/" + twitterRoot.getId_str
                            val infoId = InfoIdUtils.generate32MD5ID(infoIdStr)
                            val isAccountIdExists = RedisUtil.isIdExists(jedis, Constants.redisKEY.INFO_CYBER_KEY, infoId)
                            //转化为hbase的put
                            if (isAccountIdExists != null && isAccountIdExists == false) {
                              redisAccountStrArray += infoId
                              val twitterPut = HBaseUtils.addRow(infoId, Constants.examplesTableInfo.FAMILY, Constants.examplesTableInfo.COLUMN, twitterMetaInfo)
                              //将put放入批量提交容器
                              hbasePutArray += twitterPut
                            }
                          }

                        }

                      }
                    }
                    //处理facebook帖子信息
                    case "2" => {
                      val facebookPostStr = userEntityCommon.getContent

                      val facebookPost: AllFacebookPost = JSON.parseObject(facebookPostStr, classOf[AllFacebookPost])

                      if (facebookPost != null && facebookPost.getSource_url != null) {
                        //生成facebookinfoid
                        val sourceUrl = facebookPost.getSource_url
                        val infoId = InfoIdUtils.generate32MD5ID(sourceUrl)
                        val isAccountIdExists = RedisUtil.isIdExists(jedis, Constants.redisKEY.INFO_CYBER_KEY, infoId)
                        //将facebook 原始数据存入hbase ，转化为hbase的put
                        if (isAccountIdExists != null && isAccountIdExists == false) {
                          redisAccountStrArray += infoId
                          val fbPut = HBaseUtils.addRow(infoId, Constants.examplesTableInfo.FAMILY, Constants.examplesTableInfo.COLUMN, facebookPostStr)
                          //将put放入批量提交容器
                          hbasePutArray += fbPut
                        }

                      }

                    }
                    //处理新闻博客信息
                    case _ => {
                      val newsContent = userEntityCommon.getContent
                      //                      println(linevalue)
                      val allNewsPost: AllNewsPost = JSON.parseObject(newsContent, classOf[AllNewsPost])

                      //判断是否符合写入es的条件
                      if (allNewsPost != null && allNewsPost.getContent != null && (!allNewsPost.getContent.trim.equals("")) && allNewsPost.getTitle != null) {
                        val infoId = InfoIdUtils.generate32MD5ID(allNewsPost.getSource_url)
                        //转化为hbase的put
                        val isAccountIdExists = RedisUtil.isIdExists(jedis, Constants.redisKEY.INFO_CYBER_KEY, infoId)
                        if (isAccountIdExists != null && isAccountIdExists == false) {
                          redisAccountStrArray += infoId
                          val newsPut = HBaseUtils.addRow(infoId, Constants.examplesTableInfo.FAMILY, Constants.examplesTableInfo.COLUMN, newsContent)
                          //将put放入批量提交容器
                          hbasePutArray += newsPut
                        }
                      } else {
                        //messageMap += (String.valueOf(messageCount) -> None)
                        log.warn("crawled content or title is none : " + newsContent)
                      }


                    }
                  }
                }
              }
            }

            import scala.collection.JavaConverters._
            val redisAccountStringList = redisAccountStrArray.toList.asJava
            val hbasePutList = hbasePutArray.toList.asJava
            //批量提交{hbase}的批量数据
            if (hbasePutList.size() > 0) {
              HBaseUtils.batchAddPut(newsRootTableName, hbasePutList)
            }
            //将id号加入hbase重复就不再写入
            if (redisAccountStringList.size() > 0) {
              RedisUtil.sAdd(jedis, Constants.redisKEY.ACCOUNT_KEY, redisAccountStringList)
            }

          }
          catch {
            case ex: Exception => {
              println("error :", ex)
              log.error("itrator iterms error :", ex)
            }
          }
          finally {

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

}

