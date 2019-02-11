package ceiec.com.process
import java.util

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import com.ceiec.process.util.jdbcutil.C3p0Utils
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @author heyichang
  * description:主要处理维基上的数据
  */
object processData {

  def main(args: Array[String]): Unit = {
      //构建spark context 配置spark 并读取文件
      //构建sparkconf
      val conf = new SparkConf().setAppName("wiki_process")
        .setMaster("local[*]")
      val sc = new SparkContext(conf)

    //读取文件
    //测试文件 tests.txt D:\20170717.json /spark_graph/wikidata/wiki_data/20170717.json
      val lines = sc.textFile("D:\\20170717.json")

    //匹配和过滤获取需要的数据
      lines.foreachPartition(
        iterations => {
          //写入mysql或者是es hbase
          val connectionOfMysql = C3p0Utils.getConnection

          //创建连接
          //过滤数据 P17国家
          //Q148 中国

          /************************************************
            * 遍历处理整个partition数据
            * *********************************************
            */
          while (iterations.hasNext){
            //获取单行数据
            var f = iterations.next()

            //去除[ ]第一行和最后一行数据
            if (!(f.startsWith("[")||f.endsWith("]"))) {
              //判断是否为","结尾的
              if (f.endsWith(",")) {
                //截取字符串，获取每个json数据
                var s = f.substring(0, (f.length - 1))

                //解析每个json数据
                var message: JSONObject = JSON.parseObject(s)
                val country:JSONArray = message.getJSONObject("claims").getJSONArray("P17")

                val countryID = new util.ArrayList[String]()

                if( country != null  && country.size() != 0){
                  for(i <- 0 to (country.size()-1)){
                    val countryInstance:JSONObject = country.getJSONObject(i)
                    try {
                      countryID.add(countryInstance.getJSONObject("mainsnak").getJSONObject("datavalue").getJSONObject("value").getString("id"))
                    }
                    catch {
                      case ex:Exception =>
                        println("get error because it don't have this value ")
                    }
                  }
                }

                /**
                  * id(自增)，目标实体名,相关实体名，目标实体语言，相关实体语言
                  */
                if (countryID.size()!= 0){
                  if(countryID.contains("Q148")){
//                    println("shuju:"+countryID)
                    //每种语言 - 名称 转换为一个类
                    //根据传入的目标实体和目标实体语言 关系 为上一步的类添加属性

                  }
                }

              }
            }
            }
          }
      )


  }
}
