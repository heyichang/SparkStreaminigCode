package com.ceiec.bigdata.action

import java.io._
import java.util
import java.util.Calendar

import com.alibaba.fastjson.JSON
import com.ceiec.bigdata.dao.FaceBookDao
import com.ceiec.bigdata.dao.impl.{FaceBookDaoImpl, NewsDaoImpl}
import com.ceiec.bigdata.entity.table.nlp.NlpPojo
import com.ceiec.bigdata.entity.twitter.TwitterRoot
import com.ceiec.bigdata.entity.{AllFacebookPost, AllNewsPost, FacebookUserType, UserEntityCommon}
import com.ceiec.bigdata.util.Constants
import com.ceiec.bigdata.util.esutil.EsClient
import com.ceiec.bigdata.util.nlputil.{HttpUtils, NlpNewsHelper, NlpTwitterHelper, PojoTransform}
import com.ceiec.bigdata.util.siteidutil.SiteInfoUtils
import kafka.serializer.StringDecoder
import org.apache.log4j.LogManager
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.elasticsearch.action.index.IndexRequestBuilder

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * Created by heyichang on 2017/10/25.
  */
object DirectStreamingMain3 extends Serializable {

  @transient lazy val log = LogManager.getRootLogger


  /**
    * sparkcontext的初始化
    *
    * @return
    */
  def functionToCreateContext(args: Array[String]): StreamingContext = {
    val sparkConf = new SparkConf().setAppName("ceiec_smm_twitter_consumer_1117").setMaster("local[*]")
      .set("spark.streaming.kafka.maxRatePerPartition", "1")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.executor.extraJavaOptions", "-XX:+UseConcMarkSweepGC")
    //.set("spark.streaming.blockInterval","200")
    val ssc = new StreamingContext(sparkConf, Seconds(10))
    //设置checkpoint,保存运行数据
    //ssc.checkpoint("hdfs://n1:8020/test-sparkstreaming/kafka_17")

    //初始化广播变量
    val myBoradcast = BroadcastWrapper[Map[String, util.List[String]]](ssc, DataFromMysql.updateAllParams)
    // val siteInfoStrs = myBroadcast.getOrElse("siteid",null)//得到字典表站点信息
    //val sensitiveStrs = myBroadcast.getOrElse("sensitive",null)//获取敏感词表信息
    // Create direct kafka stream with brokers and topics
    val topicsSet = "InsightCyber_post_3".split(",").toSet //diplomat_twitter,diplomat_news
    val kafkaParams = scala.collection.immutable.Map[String, String]("bootstrap.servers" -> "172.16.3.31:6667,172.16.3.32:6667,172.16.3.33:6667", "auto.offset.reset" -> "largest", "group.id" -> "test_video")
    //console-consumer-68474
    //ceiec_smm_twitter_consumer1712_01
    val km = new KafkaManager(kafkaParams)
    val kafkaDirectStream = km.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)
    log.warn(s"Initial Done***>>>")

    kafkaDirectStream.cache


    //更新zk中的offset
    kafkaDirectStream.foreachRDD(rdd => {
      if (rdd != null) {

        //worker工作内容
        rdd.foreachPartition(iterators => {

          try {
            while (iterators.hasNext) {
              val line = iterators.next()
              val linetype = line._1
              val linevalue = line._2
              val userEntityCommon = JSON.parseObject(linevalue, classOf[UserEntityCommon])
              val userType = userEntityCommon.getSource_type
              println("out:" + linevalue)
              linetype match {
                //处理用户信息
                case "user" => {
                  userType match {
                    case "1" => {
                      val twitterUserStr = userEntityCommon.getContent
                      println("twitterprase:" + twitterUserStr)
                      val twitterRoot: TwitterRoot = JSON.parseObject(twitterUserStr, classOf[TwitterRoot])
                      println("twitter:" + twitterRoot)
                    }

                    case "2" => {
                      val facebookUserStr = userEntityCommon.getContent
                      val facebookUser: FacebookUserType = JSON.parseObject(facebookUserStr, classOf[FacebookUserType])
                      println("facebook:" + facebookUser)
                    }
                  }
                }
                //处理帖子信息
                case _ => {
                  userType match {
                    //处理twitter帖子信息
                    case "1"
                    => {
                      println("twitter:" + userEntityCommon.getContent)
                      val twitterContent = userEntityCommon.getContent
                      val twitterRoot: TwitterRoot = JSON.parseObject(twitterContent, classOf[TwitterRoot])
                      println("twitter:" + twitterRoot)
                    }
                    //处理facebook帖子信息
                    case "2"
                    => {
                      val facebookPostStr = userEntityCommon.getContent

                      println("facebookPost : " + facebookPostStr)
                      val facebookPost: AllFacebookPost = JSON.parseObject(facebookPostStr, classOf[AllFacebookPost])
                      //判断是否符合写入es的条件
                    }
                    //处理新闻博客信息
                    case _ => {
                      val newsContent = userEntityCommon.getContent
                      println(linevalue)
                      val allNewsPost: AllNewsPost = JSON.parseObject(newsContent, classOf[AllNewsPost])

                      println("news:" + allNewsPost)
                    }
                  }
                }
              }
            }

          }
          catch {
            case ex: Exception => {
              println("error :", ex)
              log.error("error :", ex)
            }
          }
          finally {

          }
        })
        //处理完消息后将kafka偏移量更新到zk
        km.updateZKOffsets(rdd)
      } else {
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

  def transNlpPojoToString(amount: String, lang: String, content: String): String = {
    val nlpPojo = new NlpPojo(amount, lang, content)
    val pojoString = PojoTransform.pojoToString(nlpPojo)
    return pojoString
  }
}
