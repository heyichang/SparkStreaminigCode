package com.ceiec.graph.action.test

import java.sql.{DriverManager, PreparedStatement}
import java.util

import com.alibaba.fastjson.JSON
import com.ceiec.graph.action.KafkaManager
import com.ceiec.graph.entity.Relation
import com.ceiec.graph.utils.Constants
import com.ceiec.graph.utils.mysqlUtil.{BulkUtil, C3p0Utils}
import com.ceiec.graph.utils.neo4jUtil.Neo4jUtils
import kafka.serializer.StringDecoder
import org.apache.log4j.LogManager
import org.apache.spark.{SparkConf, TaskContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.neo4j.jdbc.Connection
import org.neo4j.jdbc.PreparedStatement

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * @author heyichang
  * description:测试jdbc的方式写入neo4j
  */
object testForJdbc {
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
        .set("spark.streaming.kafka.maxRatePerPartition", "100")//args(0) 10
        .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        .set("spark.executor.extraJavaOptions", "-XX:+UseConcMarkSweepGC")
        .set("spark.streaming.stopGracefullyOnShutdown", "true")
      val ssc = new StreamingContext(sparkConf, Seconds(5))//args(1).toInt
      //设置checkpoint,保存运行数据
      // Create direct kafka stream with brokers and topics  test_REL  InsightCyber_relation
      val topicsSet = "test_REL".split(",").toSet //diplomat_twitter  diplomat_news  diplomat_twitter_streaming   diplomat_twitter_user InsightCyber_relation_1107
      val kafkaParams = scala.collection.immutable.Map[String, String]("bootstrap.servers" -> "172.16.3.31:6667,172.16.3.32:6667,172.16.3.33:6667", "auto.offset.reset" -> "smallest", "group.id" ->"smm-guide-crwl") //largest  smallest args(2) test-consumer-group

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



            //neo4j连接
            val neoConn: Connection = DriverManager.getConnection("jdbc:neo4j:bolt://172.16.3.31:7687").asInstanceOf[Connection]

            //获取mysql连接
            val con = C3p0Utils.getConnection
            val statement: java.sql.PreparedStatement = con.prepareStatement("insert IGNORE into recordREl (start,type,end) " + "values (?,?,?)")

            // 关闭事务自动提交 ,这一行必须加上
            con.setAutoCommit(false)

            try {
//              try {
//                //hbase获取连接
//                hConnection = ConnectionFactory.createConnection(conf)
//                //获取hbase Table
//                graphTableName = hConnection.getTable(TableName.valueOf("graph_backup"))
//              }
//              catch {
//                case e:Exception=>{
//                  log.error("can not get hbase connection")
//                }
//              }

//              neo4jDriver = Neo4jDriver.getSingleton
//              //获取session
//              driver = neo4jDriver.driver
//              session = driver.session

              //参数变量
              val parameters: util.Map[String, AnyRef] = new util.HashMap[String, AnyRef]

              //获取batches
              val nodeBatches:util.List[util.Map[String,Any]] = new util.ArrayList[util.Map[String,Any]]();
              val edgeBatchesOfTwitter:util.List[util.Map[String,Any]] = new util.ArrayList[util.Map[String,Any]]();
              val edgeBatchesOfFacebook:util.List[util.Map[String,Any]] = new util.ArrayList[util.Map[String,Any]]();

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

//                println("messageKey:"+messageType)
//                println("messageValue:"+messageValue)

                val parseMessage = JSON.parseObject(messageValue)
                val sourceType = parseMessage.get("source_type")
                val relationMessage = parseMessage.getString("content")

                sourceType.toString match {
                  case "1" =>
                  {

                    println("get data is twitter")
                    //解析数据
                    val relation: Relation = JSON.parseObject(relationMessage, classOf[Relation])
                    //存储mysql
                    BulkUtil.batchOfbuild(statement,relation.getSource,relation.getTarget,relation.getRelation)


                    //获取节点数据
                    val startNode: util.Map[String,Any] = new util.HashMap[String, Any]

                    //accountId 需要生成
                    //节点唯一键
                    startNode.put("node_id",Neo4jUtils.getAcccountId(relation.getSource))
                    startNode.put("name",relation.getSource_name)
                    startNode.put("label","n00030001")
                    startNode.put("home_url",relation.getSource)
                    nodeBatches.add(startNode)

                    //获取节点数据
                    val endNode: util.Map[String,Any] = new util.HashMap[String, Any]

                    //accountId 需要生成
                    //节点唯一键
                    endNode.put("node_id",Neo4jUtils.getAcccountId(relation.getTarget))
                    endNode.put("name",relation.getTarget_name)
                    endNode.put("label","n00030001")
                    endNode.put("home_url",relation.getTarget)
                    nodeBatches.add(endNode)

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
                    println("get data is facebook")

                    //解析数据
                    val relation: Relation = JSON.parseObject(relationMessage, classOf[Relation])
                    //存储mysql
                    BulkUtil.batchOfbuild(statement,relation.getSource,relation.getTarget,relation.getRelation)

                    //获取节点数据
                    val startNode: util.Map[String,Any] = new util.HashMap[String, Any]

                    //accountId 需要生成
                    //节点唯一键
                    startNode.put("node_id",Neo4jUtils.getAcccountId(relation.getSource))
                    startNode.put("name",relation.getSource_name)
                    startNode.put("label","n00030002")
                    startNode.put("home_url",relation.getSource)
                    nodeBatches.add(startNode)

                    //获取节点数据
                    val endNode: util.Map[String,Any] = new util.HashMap[String, Any]

                    //accountId 需要生成
                    //节点唯一键
                    endNode.put("node_id",Neo4jUtils.getAcccountId(relation.getTarget))
                    endNode.put("name",relation.getTarget_name)
                    endNode.put("label","n00030002")
                    endNode.put("home_url",relation.getTarget)
                    nodeBatches.add(endNode)

                    //获取边数据
                    val edge:util.Map[String,Any] = new util.HashMap[String, Any]
                    edge.put("start_node",Neo4jUtils.getAcccountId(relation.getSource))
                    edge.put("end_node",Neo4jUtils.getAcccountId(relation.getTarget))
                    edge.put("label",relation.getRelation)
                    edge.put("source_name",relation.getSource_name)
                    edge.put("target_name",relation.getTarget_name)
                    edge.put("node_type","n00030002")
                    edgeBatchesOfFacebook.add(edge)
                  }
                }
              }


              //mysql 提交
              statement.executeBatch()
              con.commit()

              //添加参数
              val tc = TaskContext.get()
              println("taskId:"+tc.taskAttemptId)
              println("partitionId:"+TaskContext.getPartitionId())


              val nodeBatchesName = "nodeBatches"+"_"+tc.taskAttemptId+"_"+TaskContext.getPartitionId()
              val edgeTwitterBatchesName = "edgeBatchesOfTwitter"+"_"+tc.taskAttemptId+"_"+TaskContext.getPartitionId()

              parameters.put(nodeBatchesName,nodeBatches)
              parameters.put(edgeTwitterBatchesName,edgeBatchesOfTwitter)

              //提交数据
              val nodeBatchCypher = "unwind {"+nodeBatchesName+"} as row \n" +
                "call apoc.merge.node([row.label],{id:row.node_id},{node_id:row.node_id,name:row.name,home_url:row.home_url}) " +
                "yield node " +
                "return count(*)"

              val edgeBatchCypher = "unwind {"+edgeTwitterBatchesName+"} as row \n" +
                "match (n1:n00030001{node_id:row.start_node}),(n2:n00030001{node_id:row.end_node})\n" +
                "call apoc.merge.relationship(n1,row.label,{start_node:row.start_node,source_name:row.source_name},{end_node:row.end_node,target_name:row.target_name},n2)\n" +
                "yield rel\n" +
                "return count(*)"

              neoConn.setAutoCommit(false)
              val nodePst = neoConn.prepareStatement(nodeBatchCypher)
              val edgePst = neoConn.prepareStatement(edgeBatchCypher)

              val flagOfnode = nodePst.execute()
//              val flagOfedge = edgePst.execute()

              neoConn.commit()

              println("flagOfnode:"+flagOfnode)
//              println("flageOfedge:"+flagOfedge)

            }
            catch {
              case ex:Exception=>{
                log.error("can't get session")
                ex.printStackTrace()
              }
            }
            finally {

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
