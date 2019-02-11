package com.ceiec.graph.action

import java.util
import java.util.{HashMap, Map}

import com.alibaba.fastjson.{JSON, JSONException}
import com.ceiec.graph.entity.facebook.FacebookUser
import com.ceiec.graph.entity.neo4jTwitter.VirtualAccount
import com.ceiec.graph.util.{Constants, NormalUtils, TimeUtils}
import com.ceiec.graph.util.esUtil.{EsClient, EsUtils}
import com.ceiec.graph.util.hbaseUtil.HBaseUtils
import com.ceiec.graph.util.locationutil.LocationUtils
import com.ceiec.graph.util.neo4jUtil.{Neo4jDriver, Neo4jUtils}
import kafka.serializer.StringDecoder
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Put, Table}
import org.apache.log4j.LogManager
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.elasticsearch.action.index.IndexRequestBuilder
import org.elasticsearch.common.xcontent.{XContentBuilder, XContentFactory}
import org.neo4j.driver.v1.{Driver, Session, StatementResult}

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * author:heyichang
  * descroption:spark连接kafka处理与图相关数据
  */
object sparkGraph {
  @transient lazy val log = LogManager.getRootLogger

  def main(args: Array[String]) {
    /**
      * 初始化sparkstreamingcontext
      *
      * @return StreamingContext
      */
    def functionToCreateContext(): StreamingContext = {
      val sparkConf = new SparkConf().setAppName("ceiec_graph_consumer")
//        .setMaster("local[*]")
        .set("spark.streaming.kafka.maxRatePerPartition", "100")//args(0) "100"
        .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        .set("spark.executor.extraJavaOptions", "-XX:+UseConcMarkSweepGC")
        .set("spark.streaming.stopGracefullyOnShutdown", "true")
      val ssc = new StreamingContext(sparkConf, Seconds(5))//args(1).toInt
      //设置checkpoint,保存运行数据
      // Create direct kafka stream with brokers and topics
      val topicsSet = "InsightCyber_user".split(",").toSet //diplomat_twitter  diplomat_news  diplomat_twitter_streaming   diplomat_twitter_user
      val kafkaParams = scala.collection.immutable.Map[String, String]("bootstrap.servers" -> "10.11.13.4:6667", "auto.offset.reset" -> "smallest", "group.id" ->"consumer-group") //largest  smallest args(2) test-consumer-group

      val km = new KafkaManager(kafkaParams)
      val kafkaDirectStream = km.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)
      log.warn(s"Initial Done***>>>")

      kafkaDirectStream.cache

      //消费kafka数据
      kafkaDirectStream.foreachRDD(rdd => {

        val begTime = System.currentTimeMillis()


        if (!rdd.isEmpty) {

          /*对实时接受的消息进行处理*/
          rdd.foreachPartition(iterators => {
            /**
              * 创建es连接
              */
            val client = EsClient.getInstance.getTransportClient
            val accountBulkRequestBuilder = client.prepareBulk

            /**备份**/
            //创建hbase的连接,配置
            val conf = HBaseConfiguration.create

            conf.set(Constants.HBASE_ZOOKEEPER_CLIENTPORT, Constants.ZOOKEEPER_PORT)
            conf.set(Constants.HBASE_ZOOKEEPER_QUORUM, Constants.ZOOKEEPER_HOST)
            conf.set(Constants.ZOOKEEPER_ZNODE_PARENT, Constants.PARENT_DIR)
            conf.setInt(Constants.ZOOKEEPER_SESSION_TIMEOUT, Constants.DEFAULT_ZK_SESSION_TIMEOUT)

            //获取连接
            var hConnection:Connection = null

            //创建hbase的批处理list
            val hbasePutArray = new ArrayBuffer[Put]()
            //创建人物关系表hbase连接
            var graphTableName:Table = null


            //neo4j连接
            var neo4jDriver: Neo4jDriver = null
            var driver: Driver = null
            var session:Session = null


            try {
              try {
                //hbase获取连接
                hConnection = ConnectionFactory.createConnection(conf)
                //获取hbase Table
                graphTableName = hConnection.getTable(TableName.valueOf("graph_backup"))
              }
              catch {
                case e:Exception=>{
                  log.error("can not get hbase connection")
                }
              }

              neo4jDriver = Neo4jDriver.getSingleton
              //获取session
              driver = neo4jDriver.driver
              session = driver.session

              //参数变量
              val parameters: util.Map[String, AnyRef] = new util.HashMap[String, AnyRef]

              //获取batches
              val nodeBatches_twitter:util.List[util.Map[String,Any]] = new util.ArrayList[util.Map[String,Any]]();
              //获取batches
              val nodeBatches_facebook:util.List[util.Map[String,Any]] = new util.ArrayList[util.Map[String,Any]]();


              //对分区内所有消息进行处理
              var messageCount = 1
              var messageMap = mutable.Map[String, Any]()
              var newsMap = mutable.Map[String, String]()
              val whilestartTime = System.currentTimeMillis

              while (iterators.hasNext) {

                val message = iterators.next()

                //获取数据
                val messageType = message._1
                val messageValue = message._2


                val parseMessage = JSON.parseObject(messageValue)
                val sourceType = parseMessage.get("source_type")
                val accountMessage = parseMessage.getString("content")

                try {
                  sourceType.toString match {
                    case "1" =>
                    {
//                      println("twitter message:"+accountMessage)
//                      //解析数据
//                      val virtualAccount: VirtualAccount = JSON.parseObject(accountMessage, classOf[VirtualAccount])
//
//                      //获取节点数据
//                      val node: util.Map[String,Any] = new util.HashMap[String, Any]
//
//                      //节点唯一键
//                      node.put("node_id",Neo4jUtils.getAcccountId(virtualAccount.getScreen_name))
//
//                      //节点账户名称
//                      node.put("name",virtualAccount.getScreen_name)
//
//                      //描述
//                      node.put("desc",virtualAccount.getDescription)
//
//                      //添加home_url
////                      node.put("home_url",virtualAccount.get)
//                      nodeBatches_twitter.add(node)
                    }
                    case "2" =>
                    {
//                      println("facebook account:"+accountMessage)
                      try{
                        val facebookVirtualAccount:FacebookUser = JSON.parseObject(accountMessage,classOf[FacebookUser])
                        println("facebookUser:"+facebookVirtualAccount)

                        //获取节点数据
                        val node: util.Map[String,Any] = new util.HashMap[String, Any]

                        //节点唯一键
                        node.put("node_id",Neo4jUtils.getAcccountId(facebookVirtualAccount.getHome_url))

                        //节点账户名称
                        node.put("name",facebookVirtualAccount.getAccount_name)

                        //描述
                        node.put("desc",facebookVirtualAccount.getIntroduce)

                        nodeBatches_facebook.add(node)
                        //获取es数据
                        val builder: XContentBuilder = XContentFactory.jsonBuilder.
                          startObject
                          .field("account_id",Neo4jUtils.getAcccountId(facebookVirtualAccount.getHome_url))
                        if(facebookVirtualAccount.getPortrait != null){
                          builder.field("portrait",facebookVirtualAccount.getPortrait)
                        }
                        builder.field("account_name",facebookVirtualAccount.getAccount_name)
                        builder.field("site_id",302)
                        builder.field("site_type_id",2)
                        if(facebookVirtualAccount.getIntroduce != null){
                          builder.field("introduce",facebookVirtualAccount.getIntroduce)
                        }
                        if(facebookVirtualAccount.getNick_name!=null){
                          builder.field("nick_name",facebookVirtualAccount.getNick_name)
                        }
                        if(facebookVirtualAccount.getOther_name != null){
                          builder.field("other_name",facebookVirtualAccount.getOther_name)
                        }
                        if(facebookVirtualAccount.getLocation != null){
                          var location:String =  null
                          try{
                            location =  LocationUtils.getGisCode(facebookVirtualAccount.getLocation)
                          }
                          catch {
                            case ex:Exception =>
                              log.error("location can't get")
                              ex.printStackTrace()
                          }
                          if( location != null){
                            builder.field("location",location)
                          }
                        }
                        if(facebookVirtualAccount.getLikes != null){
                          builder.field("likes",facebookVirtualAccount.getLikes)
                        }
                        if(facebookVirtualAccount.getHome_url!=null){
                          builder.field("home",facebookVirtualAccount.getHome_url)
                        }
                        if(facebookVirtualAccount.getVerified!=null){
                          builder.field("verified",facebookVirtualAccount.getVerified)
                        }
                        if(facebookVirtualAccount.getFans_number != null){
                          builder.field("fans_number",facebookVirtualAccount.getFans_number)
                        }
                        if(facebookVirtualAccount.getFans_number != null){
                          builder.field("follow_number",facebookVirtualAccount.getFollow_number)
                        }
                        if(facebookVirtualAccount.getBirthday != null){
                          builder.field("birthday",facebookVirtualAccount.getBirthday)
                        }
                        if(facebookVirtualAccount.getGender != null){
                          builder.field("gender",facebookVirtualAccount.getGender)
                        }
                        if(facebookVirtualAccount.getHometown != null){
                          var hometown:String =  null
                          try{
                              hometown = LocationUtils.getGisCode(facebookVirtualAccount.getHometown)
                          }
                          catch {
                            case ex:Exception=>
                              log.error("hometown can't get")
                              ex.printStackTrace()
                          }
                          if(hometown != null){
                            builder.field("hometown",hometown)
                          }
                        }

                        if(facebookVirtualAccount.getHometown != null){
                          var hometown_str:String = null
                          try{
                            hometown_str = LocationUtils.getGisCode(facebookVirtualAccount.getHometown)
                          }
                          catch {
                            case ex:Exception=>
                              log.error("hometown_str can't get")
                              ex.printStackTrace()
                          }
                          if(hometown_str != null){
                            builder.field("hometown_str",hometown_str)
                          }
                        }
                        if(facebookVirtualAccount.getFamily!=null){
                          if(NormalUtils.listObj2listmap(facebookVirtualAccount.getFamily) != null){
                            builder.field("family",NormalUtils.listObj2listmap(facebookVirtualAccount.getFamily))
                          }
                        }
                        if(facebookVirtualAccount.getSocial_account!=null){
                          if(NormalUtils.listObj2listmap(facebookVirtualAccount.getSocial_account)!=null){
                            builder.field("socialmedia_account",NormalUtils.listObj2listmap(facebookVirtualAccount.getSocial_account))
                          }
                        }
                        if(facebookVirtualAccount.getWebsites!=null){
                          builder.field("websites",NormalUtils.listObj2listmap(facebookVirtualAccount.getWebsites))
                        }
                        if(facebookVirtualAccount.getLanguage !=null){
                          builder.field("language",NormalUtils.listObj2listmap(facebookVirtualAccount.getLanguage))
                        }
                        if(facebookVirtualAccount.getEducation!=null){
                          builder.field("education",NormalUtils.listObj2listmap(facebookVirtualAccount.getEducation))
                        }
                        if(facebookVirtualAccount.getWork!=null){
                          builder.field("work",NormalUtils.listObj2listmap(facebookVirtualAccount.getWork))
                        }
                        if(facebookVirtualAccount.getLived != null){
                          builder.field("lived",NormalUtils.listObj2listmap(facebookVirtualAccount.getLived))
                        }
                        if(facebookVirtualAccount.getEvents != null){
                          builder.field("events",NormalUtils.listObj2listmap(facebookVirtualAccount.getEvents))
                        }
                        if(facebookVirtualAccount.getSkills != null){
                          builder.field("skills",facebookVirtualAccount.getSkills)
                        }
                        builder.field("time",TimeUtils.getTime())
                        builder.endObject()

                        val builder_account: IndexRequestBuilder = EsUtils.bulkCreateData(client, builder, "m_cyber_es_account", "account", Neo4jUtils.getAcccountId(facebookVirtualAccount.getHome_url))
                        accountBulkRequestBuilder.add(builder_account)

                      }
                     catch {
                       case ex:JSONException =>
                         println("facebook account:"+accountMessage)
                         ex.printStackTrace()
                     }

                    }
                  }
                }
                catch {
                  case ex:Exception=>
                    log.error("出现了问题")
                    ex.printStackTrace()
                }
              }

//              //添加参数
//              parameters.put("nodeBatches_twitter",nodeBatches_twitter)
//              parameters.put("nodeBatches_facebook",nodeBatches_facebook)




//              //提交数据
//              val resultNode: StatementResult = session.run("" +
//                "unwind {nodeBatches_twitter} as row \n" +
//                "merge (n:n00030001{node_id:row.node_id}) \n" +
//                "set n.name=row.name,n.desc=row.desc" +
//                "", parameters)
//              val rs = resultNode.consume()
//              println("result:"+rs.counters().nodesCreated())
//
//              //提交数据
//              val resultNode2: StatementResult = session.run("" +
//                "unwind {nodeBatches_facebook} as row \n" +
//                "merge (n:n00030002{node_id:row.node_id}) \n" +
//                "set n.name=row.name,n.desc=row.desc" +
//                "", parameters)
//              val rs2 = resultNode2.consume()
//              println("result:"+rs2.counters().nodesCreated())

              //提交es数据
              if (accountBulkRequestBuilder.numberOfActions > 0) {
                val bulkResponse = accountBulkRequestBuilder.execute.actionGet
                if (bulkResponse.hasFailures) {
                  log.error("error in doing index request: " + bulkResponse.buildFailureMessage)
                }
              }
            }
            finally {
              if (session != null){
                session.close()
              }
              graphTableName.close()
              hConnection.close()
            }

            val EndTime = System.currentTimeMillis

            println("use Time:"+(EndTime-begTime))
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
    val ssc = functionToCreateContext
    ssc.start()
    ssc.awaitTermination()
  }
}
