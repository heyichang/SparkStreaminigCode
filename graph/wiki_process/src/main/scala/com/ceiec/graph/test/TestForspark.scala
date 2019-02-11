package com.ceiec.graph.test

import java.lang
import java.lang.Thread

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.Test



/**
  * 测试spark相关的代码
  */
@Test
object TestForspark extends {

  def main(args: Array[String]): Unit = {
    //构建sparkconf
    val conf = new SparkConf().setAppName("test").setMaster("local[*]")
    val sc = new SparkContext(conf)

    //读取文件
    //测试文件 tests.txt 20170717.json
    val lines = sc.textFile("D:\\20170717.json")

    //处理文件
    lines.foreachPartition(
      iterators =>
        {
          while (iterators.hasNext){
            //获取单行数据
            var f = iterators.next()

            //去除[ ]第一行和最后一行数据
            if (!(f.startsWith("[")||f.endsWith("]")))
            {
              //判断是否为","结尾的
              if (f.endsWith(",")){

                //截取字符串，获取每个json数据
                var s = f.substring(0,(f.length-1))

                //解析每个json数据
                 var message: JSONObject = JSON.parseObject(s)

                //获取想要的数据
                println(message.getJSONObject("claims"))

                lang.Thread.sleep(5000)
                      }
            }

          }
        }

          )
  }



}
