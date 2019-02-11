package com.ceiec.bigdata.action

import java.io.{ObjectInputStream, ObjectOutputStream}

import com.ceiec.bigdata.util.hbaseutil.HBaseUtils
import com.ceiec.bigdata.util.jdbcutil.JdbcUtils
import org.apache.log4j.LogManager
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.StreamingContext
import org.slf4j.{Logger, LoggerFactory}

import scala.reflect.ClassTag

/**
  * Created by heyichang on 2017/12/1.
  */
case class BroadcastWrapper[T: ClassTag] (@transient private val ssc: StreamingContext,
                                         @transient private val _v: T) extends Serializable {
  @transient lazy val log = LogManager.getLogger("BroadcastWrapper")
    @transient private var v = ssc.sparkContext.broadcast(_v)

    /**
    * 更新广播变量
    * @param newValue
    * @param blocking
    */
    def update(newValue: T, blocking: Boolean = false): Unit = {
      // 删除broadcast时RDD是否需要锁定
      v.unpersist(blocking)
      log.info("clear broadcast succeed")
      v = ssc.sparkContext.broadcast(newValue)
      log.info("update broadcast succeed")
    }

    def value: T = v.value

    /**
    * 广播变量的输出
    * @param out
    */
    private def writeObject(out: ObjectOutputStream): Unit = {
      out.writeObject(v)
    }

    /**
    * 广播变量输入
    * @param in
    */
    private def readObject(in: ObjectInputStream): Unit = {
      v = in.readObject().asInstanceOf[Broadcast[T]]
    }

}
