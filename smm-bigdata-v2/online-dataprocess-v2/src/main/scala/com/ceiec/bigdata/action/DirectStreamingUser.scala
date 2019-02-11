package com.ceiec.bigdata.action

import java.io._
import java.util

import com.alibaba.fastjson.JSON
import com.ceiec.bigdata.dao.FaceBookDao
import com.ceiec.bigdata.dao.impl.{FaceBookDaoImpl, TwitterRootDaoImpl}
import com.ceiec.bigdata.entity.table.nlp.NlpPojo
import com.ceiec.bigdata.entity.twitter.{TwitterRoot, User}
import com.ceiec.bigdata.entity.{AllFacebookPost, AllNewsPost, FacebookUserType, UserEntityCommon}
import com.ceiec.bigdata.util.Constants
import com.ceiec.bigdata.util.esutil.EsClient
import com.ceiec.bigdata.util.nlputil.PojoTransform
import kafka.serializer.StringDecoder
import org.apache.log4j.LogManager
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.elasticsearch.action.index.IndexRequestBuilder
import org.elasticsearch.action.update.UpdateRequest

/**
  * Created by heyichang on 2017/10/25.
  */
object DirectStreamingUser extends Serializable {

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

          try {
            //创建es连接
            val client = EsClient.getInstance.getTransportClient
            //创建es的批处理的容器
            val twitterBulkRequestBuilder = client.prepareBulk
            while (iterators.hasNext) {
              val line = iterators.next()
              val linetype = line._1
              val linevalue = line._2
              val userEntityCommon = JSON.parseObject(linevalue, classOf[UserEntityCommon])
              val userType = userEntityCommon.getSource_type
              //              println("out:" + linevalue)
              //处理用户信息
              userType match {
                case "1" => {
                  val twitterUserStr = userEntityCommon.getContent
                  val twitterUser: User = JSON.parseObject(twitterUserStr, classOf[User])
                  val twitterDao = new TwitterRootDaoImpl(twitterUser)
                  val twitterUpdateRequest: UpdateRequest = twitterDao.updateUserData(Constants.VIRTUAL_INDEX, Constants.ACCOUNT)
                  twitterBulkRequestBuilder.add(twitterUpdateRequest)
                }
                case "2" => {
                  val facebookUserStr = userEntityCommon.getContent
                  val facebookUser: FacebookUserType = JSON.parseObject(facebookUserStr, classOf[FacebookUserType])
                  val faceBookDao: FaceBookDao = new FaceBookDaoImpl(facebookUser)
                  val fbUserIndexRequestBuilder: IndexRequestBuilder = faceBookDao.bulkAddEsUserData(client, Constants.VIRTUAL_INDEX, Constants.ACCOUNT)
                  if (fbUserIndexRequestBuilder != null) {
                    twitterBulkRequestBuilder.add(fbUserIndexRequestBuilder)
                  }
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

  def transNlpPojoToString(amount: String, lang: String, content: String): String = {
    val nlpPojo = new NlpPojo(amount, lang, content)
    val pojoString = PojoTransform.pojoToString(nlpPojo)
    return pojoString
  }
}
