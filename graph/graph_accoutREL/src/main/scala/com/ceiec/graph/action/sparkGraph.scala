package com.ceiec.graph.action

import java.sql.PreparedStatement
import java.util

import com.alibaba.fastjson.JSON
import com.ceiec.graph.action.test.sparkGraph.log
import com.ceiec.graph.entity.Relation
import com.ceiec.graph.utils.Constants
import com.ceiec.graph.utils.hbaseUtil.HBaseUtils
import com.ceiec.graph.utils.mysqlUtil.{BulkUtil, C3p0Utils}
import com.ceiec.graph.utils.neo4jUtil.{Neo4jDriver, Neo4jUtils}
import kafka.serializer.StringDecoder
import org.apache.hadoop.hbase.client.{Connection, ConnectionFactory, Put, Table}
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.log4j.LogManager
import org.apache.spark.{SparkConf, TaskContext}
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
      val sparkConf = new SparkConf().setAppName("ceiec_graphREL_consumer")
        .setMaster("local[10]")
        .set("spark.streaming.kafka.maxRatePerPartition", "10000")//args(0) 10
        .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        .set("spark.executor.extraJavaOptions", "-XX:+UseConcMarkSweepGC")
        .set("spark.streaming.stopGracefullyOnShutdown", "true")
      val ssc = new StreamingContext(sparkConf, Seconds(5))//args(1).toInt
      //设置checkpoint,保存运行数据
      // Create direct kafka stream with brokers and topics  test_REL  InsightCyber_relation InsightCyber_relation
      val topicsSet = "InsightCyber_relation".split(",").toSet //diplomat_twitter  diplomat_news  diplomat_twitter_streaming   diplomat_twitter_user InsightCyber_relation_1107
      val kafkaParams = scala.collection.immutable.Map[String, String]("bootstrap.servers" -> "10.11.13.4:6667", "auto.offset.reset" -> "smallest", "group.id" ->"ceiec_graph_04") //largest  smallest args(2) test-consumer-group

      val km = new KafkaManager(kafkaParams)
      val kafkaDirectStream = km.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams,topicsSet)
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

//            //获取mysql连接
//            val con = C3p0Utils.getConnection
//            val statement: PreparedStatement = con.prepareStatement("insert IGNORE into recordREl (start,type,end) " + "values (?,?,?)")
//
//            // 关闭事务自动提交 ,这一行必须加上
//            con.setAutoCommit(false)

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
              val parametersOftwitterNode: util.Map[String, AnyRef] = new util.HashMap[String, AnyRef]
              val parametersOffacebookNode: util.Map[String, AnyRef] = new util.HashMap[String, AnyRef]
              val parametersOfedgeTwitter: util.Map[String, AnyRef] = new util.HashMap[String, AnyRef]
              val parametersOfedgeFacebookFriends: util.Map[String, AnyRef] = new util.HashMap[String, AnyRef]
              val parametersOfedgeFacebookFans: util.Map[String, AnyRef] = new util.HashMap[String, AnyRef]

              //获取batches
              val twitterNodeBatches:util.List[util.Map[String,Any]] = new util.ArrayList[util.Map[String,Any]]();
              val facebookNodeBatches:util.List[util.Map[String,Any]] = new util.ArrayList[util.Map[String,Any]]();

              val edgeBatchesOfTwitter:util.List[util.Map[String,Any]] = new util.ArrayList[util.Map[String,Any]]();
              val edgeBatchesOfFacebook:util.List[util.Map[String,Any]] = new util.ArrayList[util.Map[String,Any]]();
              val edgeBatchesOfFacebookOfFans:util.List[util.Map[String,Any]] = new util.ArrayList[util.Map[String,Any]]();

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
                val relationMessage = parseMessage.getString("content")

                sourceType.toString match {
                  case "1" =>
                  {

//                    println("get data is twitter")
                    //解析数据
                    val relation: Relation = JSON.parseObject(relationMessage, classOf[Relation])
                    //存储mysql
//                    BulkUtil.batchOfbuild(statement,relation.getSource,relation.getTarget,relation.getRelation)


                    //获取节点数据
                    val startNode: util.Map[String,Any] = new util.HashMap[String, Any]

                    //accountId 需要生成
                    //节点唯一键
                    startNode.put("node_id",Neo4jUtils.getAcccountId(relation.getSource))
                    startNode.put("name",relation.getSource_name)
                    startNode.put("label","n00030001")
                    startNode.put("home_url",relation.getSource)
                    twitterNodeBatches.add(startNode)

                    //获取节点数据
                    val endNode: util.Map[String,Any] = new util.HashMap[String, Any]

                    //accountId 需要生成
                    //节点唯一键
                    endNode.put("node_id",Neo4jUtils.getAcccountId(relation.getTarget))
                    endNode.put("name",relation.getTarget_name)
                    endNode.put("label","n00030001")
                    endNode.put("home_url",relation.getTarget)
                    twitterNodeBatches.add(endNode)

                    //获取边数据
                    val edge:util.Map[String,Any] = new util.HashMap[String, Any]
                    edge.put("start_node",Neo4jUtils.getAcccountId(relation.getSource))
                    edge.put("end_node",Neo4jUtils.getAcccountId(relation.getTarget))
                    edge.put("label",relation.getRelation)
                    edge.put("source_name",relation.getSource_name)
                    edge.put("target_name",relation.getTarget_name)
                    edge.put("node_type","n00030001")
                    edgeBatchesOfTwitter.add(edge)
                  }
                  case "2" =>
                  {
//                    println("get data is facebook")

                    //解析数据
                    val relation: Relation = JSON.parseObject(relationMessage, classOf[Relation])
                    //存储mysql
//                    BulkUtil.batchOfbuild(statement,relation.getSource,relation.getTarget,relation.getRelation)

                    //获取节点数据
                    val startNode: util.Map[String,Any] = new util.HashMap[String, Any]

                    //accountId 需要生成
                    //节点唯一键
                    startNode.put("node_id",Neo4jUtils.getAcccountId(relation.getSource))
                    startNode.put("name",relation.getSource_name)
                    startNode.put("label","n00030002")
                    startNode.put("home_url",relation.getSource)
                    facebookNodeBatches.add(startNode)

                    //获取节点数据
                    val endNode: util.Map[String,Any] = new util.HashMap[String, Any]

                    //accountId 需要生成
                    //节点唯一键
                    endNode.put("node_id",Neo4jUtils.getAcccountId(relation.getTarget))
                    endNode.put("name",relation.getTarget_name)
                    endNode.put("label","n00030002")
                    endNode.put("home_url",relation.getTarget)
                    facebookNodeBatches.add(endNode)

                    //获取边数据
                    val edge:util.Map[String,Any] = new util.HashMap[String, Any]
                    edge.put("start_node",Neo4jUtils.getAcccountId(relation.getSource))
                    edge.put("end_node",Neo4jUtils.getAcccountId(relation.getTarget))
                    edge.put("label",relation.getRelation)
                    edge.put("source_name",relation.getSource_name)
                    edge.put("target_name",relation.getTarget_name)
                    edge.put("node_type","n00030002")

                    //判断是朋友关系还是粉丝关系
                    if(edge.get("label").equals("r00000008")){
                      println("有粉丝关系数据")
                      edgeBatchesOfFacebookOfFans.add(edge)
                    }else{
                      edgeBatchesOfFacebook.add(edge)
                    }

                  }
                }
              }


              //mysql 提交
//              statement.executeBatch()
//              con.commit()

              //添加参数
              val tc = TaskContext.get()
              println("taskId:"+tc.taskAttemptId)
              println("partitionId:"+TaskContext.getPartitionId())

              val twitterNodeBatchesName = "twitterNodeBatches"+"_"+tc.taskAttemptId+"_"+TaskContext.getPartitionId()
              val facebookNodeBatchesName = "facebookNodeBatches"+"_"+tc.taskAttemptId+"_"+TaskContext.getPartitionId()
              val edgeTwitterBatchesName = "edgeBatchesOfTwitter"+"_"+tc.taskAttemptId+"_"+TaskContext.getPartitionId()
              val edgeFacebookBatchesName = "edgeBatchesOfFacebook"+"_"+tc.taskAttemptId+"_"+TaskContext.getPartitionId()
              val edgeFacebookBatchesOfFansName = "edgeBatchesOfFacebookOfFans"+"_"+tc.taskAttemptId+"_"+TaskContext.getPartitionId()

              parametersOftwitterNode.put(twitterNodeBatchesName,twitterNodeBatches)
              parametersOffacebookNode.put(facebookNodeBatchesName,facebookNodeBatches)
              parametersOfedgeTwitter.put(edgeTwitterBatchesName,edgeBatchesOfTwitter)
              parametersOfedgeFacebookFriends.put(edgeFacebookBatchesName,edgeBatchesOfFacebook)
              parametersOfedgeFacebookFans.put(edgeFacebookBatchesOfFansName,edgeBatchesOfFacebookOfFans)

              log.info("start batch insert twitter node data  into neo4j ")
              try{
                //提交数据
                val tresultNode: StatementResult = session.run("" +
                  "unwind {"+twitterNodeBatchesName+"} as row \n" +
                  "merge (n:n00030001{node_id:row.node_id}) \n" +
                  "set n.name=row.name,n.desc=row.desc,n.home_url = row.home_url"+
                  "", parametersOftwitterNode)
                val trsN = tresultNode.consume()
                println("tresultNode num:"+trsN.counters().nodesCreated())
              }
              catch {
                case ex:Exception =>{
                  ex.printStackTrace()
                  log.error("batch insert twitter note error:"+ex)
                  log.error("batch error twitter data:"+parametersOftwitterNode)
                }
              }


              Thread.sleep(500)

              log.info("start batch insert facebook Node data into neo4j")
              try{
                val fresultNode: StatementResult = session.run("" +
                  "unwind {"+facebookNodeBatchesName+"} as row \n" +
                  "merge (n:n00030002{node_id:row.node_id}) \n" +
                  "set n.name=row.name,n.desc=row.desc,n.home_url = row.home_url"+
                  "", parametersOffacebookNode)
                val frsN = fresultNode.consume()
                println("fresultNode num:"+frsN.counters().nodesCreated())
              }
              catch {
                case ex:Exception=>
                  {
                    ex.printStackTrace()
                    log.error("batch insert facebook note error:"+ex)
                    log.error("batch error facebook data:"+parametersOftwitterNode)
                  }
              }


              Thread.sleep(500)

              log.info("start batch insert twitter edge data into neo4j")
              try{
                val resultEdgeOfTwitter:StatementResult = session.run("" +
                  "unwind {"+edgeTwitterBatchesName+"} as row \n" +
                  "match (n1:n00030001{node_id:row.start_node}),(n2:n00030001{node_id:row.end_node})\n" +
                  " merge (n1)-[r:r00000008]->(n2) \n"+
                  " set r.start_node = row.start_node,r.source_name=row.source_name,r.end_node=row.end_node,r.target_name=row.target_name"+
                  "",parametersOfedgeTwitter)
                val rsEOT = resultEdgeOfTwitter.consume()
                println("resultEdgeOfTwitter num:"+rsEOT.counters().relationshipsCreated())
              }catch{
                case ex:Exception=>
                  ex.printStackTrace()
                  log.error("batch insert twitter edge error:"+ex)
                  log.error("batch error twitter edge data:"+parametersOftwitterNode)
              }

              Thread.sleep(500)

              log.info("start batch insert facebook edge data into neo4j")
              try{
                val resultEdgeOfFacebook:StatementResult = session.run("" +
                  "unwind {"+edgeFacebookBatchesName+"} as row \n" +
                  "match (n1:n00030002{node_id:row.start_node}),(n2:n00030002{node_id:row.end_node})\n" +
                  " merge (n1)-[r1:r00000001]->(n2) \n"+
                  " merge (n1)<-[r2:r00000001]-(n2) \n"+
                  " set r1.start_node = row.start_node,r1.source_name=row.source_name,r1.end_node=row.end_node,r1.target_name=row.target_name,"+
                  "  r2.start_node = row.end_node,r2.source_name=row.target_name,r2.end_node=row.start_node,r2.target_name=row.source_name"+
                  "",parametersOfedgeFacebookFriends)
                resultEdgeOfFacebook.consume()
                println("resultEdgeOfFacebook num:"+resultEdgeOfFacebook.consume().counters().relationshipsCreated())
              }
              catch {
                case ex:Exception=>
                  ex.printStackTrace()
                  log.error("batch insert facebook edge error:"+ex)
                  log.error("batch error facebook edge data:"+parametersOftwitterNode)
              }

              Thread.sleep(500)

              log.info("start batch insert fans facebook edge data into neo4j")
              try{
                val resultEdgeOfFacebookOfFans:StatementResult = session.run("" +
                  "unwind {"+edgeFacebookBatchesOfFansName+"} as row \n" +
                  "match (n1:n00030002{node_id:row.start_node}),(n2:n00030002{node_id:row.end_node})\n" +
                  " merge (n1)-[r1:r00000008]->(n2) \n"+
                  " set r1.start_node = row.start_node,r1.source_name=row.source_name,r1.end_node=row.end_node,r1.target_name=row.target_name"+
                  "",parametersOfedgeFacebookFans)
                resultEdgeOfFacebookOfFans.consume()
                println("resultEdgeOfFacebookOfFans num:"+resultEdgeOfFacebookOfFans.consume().counters().relationshipsCreated())
              }
              catch {
                case ex:Exception=>
                  ex.printStackTrace()
                  log.error("batch insert fans facebook edge error:"+ex)
                  log.error("batch error fans facebook edge data:"+parametersOftwitterNode)
              }
            }
            catch {
              case ex:Exception=>{
                ex.printStackTrace()
//                log.error("can't get session")
                log.error("get a error in top loop:"+ex)
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
