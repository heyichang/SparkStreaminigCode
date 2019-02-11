package com.ceiec.graph.action

import java.util
import java.util.{HashMap, Map}

import com.alibaba.fastjson.JSON
import com.ceiec.graph.entity.neo4jTwitter.VirtualAccount
import com.ceiec.graph.util.Constants
import com.ceiec.graph.util.esUtil.EsClient
import com.ceiec.graph.util.hbaseUtil.HBaseUtils
import com.ceiec.graph.util.neo4jUtil.{Neo4jDriver, Neo4jUtils}
import kafka.serializer.StringDecoder
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Put, Table}
import org.apache.log4j.LogManager
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
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
        .setMaster("local[*]")
        .set("spark.streaming.kafka.maxRatePerPartition", args(0))//args(0)
        .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        .set("spark.executor.extraJavaOptions", "-XX:+UseConcMarkSweepGC")
        .set("spark.streaming.stopGracefullyOnShutdown", "true")
      val ssc = new StreamingContext(sparkConf, Seconds(args(1).toInt))//args(1).toInt
      //设置checkpoint,保存运行数据
      // Create direct kafka stream with brokers and topics
      val topicsSet = "diplomat_relations_test".split(",").toSet //diplomat_twitter  diplomat_news  diplomat_twitter_streaming   diplomat_twitter_user
      val kafkaParams = scala.collection.immutable.Map[String, String]("bootstrap.servers" -> "172.16.3.31:6667,172.16.3.32:6667,172.16.3.33:6667", "auto.offset.reset" -> "smallest", "group.id" ->args(2)) //largest  smallest args(2) test-consumer-group

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
              val nodeBatches:util.List[util.Map[String,Any]] = new util.ArrayList[util.Map[String,Any]]();
              val edgeBatches:util.List[util.Map[String,Any]] = new util.ArrayList[util.Map[String,Any]]();

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

                messageType match {
                  case "twitter" =>
                  {
                    //解析数据
                    val virtualAccount: VirtualAccount = JSON.parseObject(messageValue, classOf[VirtualAccount])

                    //获取节点数据
                    val node: util.Map[String,Any] = new util.HashMap[String, Any]

                    //accountId 需要生成
                    //节点唯一键
                    node.put("nodeID",Neo4jUtils.getAcccountId(virtualAccount.getScreen_name))
                    //补充
                    node.put("account_id",Neo4jUtils.getAcccountId(virtualAccount.getScreen_name))
                    //节点账户名称
                    node.put("account_name",virtualAccount.getScreen_name)
                    //节点昵称
                    node.put("nick_name",virtualAccount.getName)
                    //描述
                    node.put("introduce",virtualAccount.getDescription)
                    //站点来源，1-twitter，2-facebook
                    node.put("site_type_id",1)
                    //账号人物头像
                    node.put("portrait",virtualAccount.getProfile_image_url)
                    //账号是否为认证用户
                    node.put("verified",virtualAccount.getVerified)
                    //发文数据
                    node.put("post_number",virtualAccount.getStatuses_count)
                    //粉丝数据
                    node.put("fans_number",virtualAccount.getFollowers_count)
                    //关注数据
                    node.put("follow_number",virtualAccount.getFriends_count)
                    //账号主页,例如:https://twitter.com/JustonWaite
                    node.put("home",Neo4jUtils.getHomeUrl(virtualAccount.getScreen_name))
                    nodeBatches.add(node)

                    //获取边数据
                    val edge:util.Map[String,Any] = new util.HashMap[String, Any]
                    edge.put("startNode",Neo4jUtils.getAcccountId(virtualAccount.getScreen_name))
                    edge.put("endNode",Neo4jUtils.getAcccountId(virtualAccount.getRelated_user_info.getUser_screen_name))
                    edgeBatches.add(edge)

                    //hbase备份数据
                    //获取rowkey
                    val rowKey = HBaseUtils.getRowkeyOfBackUp(virtualAccount.getScreen_name,virtualAccount.getRelated_user_info.getUser_screen_name
                                  ,virtualAccount.getRelated_user_info.getType)
                    //将要存储的数据转化为put
                    val graphPut = HBaseUtils.addRow(rowKey,"graph","twitter",messageValue)
                    //将put放入批量提交容器
                    hbasePutArray += graphPut

                  }
                }
              }


              //添加参数
              parameters.put("nodeBatches",nodeBatches)
              parameters.put("edgeBatches",edgeBatches)

              //提交数据
              val resultNode: StatementResult = session.run("" +
                "unwind {nodeBatches} as row \n" +
                "merge (n:Account{nodeID:row.nodeID}) \n" +
                "set n.account_id=row.account_id,n.account_name=row.account_name,n.nick_name=row.nick_name,n.introduce=row.introduce" +
                ",n.site_type_id=row.site_type_id,n.portrait=row.portrait,n.verified=row.verified,n.post_number=row.post_number" +
                ",n.fans_number=row.fans_number,n.home=row.home" +
                "", parameters)
              resultNode.consume()

              val resultEdge:StatementResult = session.run("" +
                "unwind {edgeBatches} as edgerow \n" +
                "match (n1:Account{nodeID:edgerow.startNode}),(n2:Account{nodeID:edgerow.endNode})" +
                " merge (n1)<-[r:follow]-(n2) \n" +
                " set r.start_node = edgerow.startNode,r.name = \"follow\",r.end_node = edgerow.endNode",parameters)
              resultEdge.consume()

              //批量提交hbase备份数据
              import scala.collection.JavaConverters._

              val graphHbasePutList = hbasePutArray.toList.asJava

              if(graphHbasePutList.size() >0){
                HBaseUtils.batchAddPut(graphTableName,graphHbasePutList)
              }
            }
            catch {
              case ex:Exception=>{
                log.error("can't get session")
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
