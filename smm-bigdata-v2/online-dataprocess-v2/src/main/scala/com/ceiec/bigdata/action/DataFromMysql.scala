package com.ceiec.bigdata.action

import java.sql.{Connection, PreparedStatement, ResultSet, Timestamp}
import java.util
import java.util.List

import com.alibaba.fastjson.JSON
import com.ceiec.bigdata.dao.common.AccountCheckDao
import com.ceiec.bigdata.entity.table.event.EventForeMapping
import com.ceiec.bigdata.entity.table.keyword.WarningMapping
import com.ceiec.bigdata.entity.table.{AccountLastTime, RegionInfo, SiteInfo}
import com.ceiec.bigdata.util.TimeUtils
import com.ceiec.bigdata.util.eventutil.{EventTrans, KeywordClearUtil, KeywordFilter}
import com.ceiec.bigdata.util.jdbcutil.JdbcUtils
import com.ceiec.bigdata.util.siteidutil.SiteInfoUtils
import org.apache.log4j.LogManager

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


/**
  * Created by heyichang on 2017/12/4.
  */
object DataFromMysql {

  private val map = mutable.LinkedHashMap[String, util.List[String]]()
  @transient lazy val log = LogManager.getRootLogger


  /**
    * 获取mysql字典表数据,更新敏感词广播变量
    *
    * @return
    */
  def getConn(): Connection = {
    //1.获取mysql连接池的一个连接
    import java.sql.{DriverManager, SQLException}
    val driver = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://172.16.3.35:3306/smm_v1.1"
    var conn: Connection = null
    try {
      Class.forName(driver)
      conn = DriverManager.getConnection(url, "root", "1qaz@WSX")
    } catch {
      case ex: SQLException => {
        log.error("jdbc get connection error")
      }
    }
    conn

  }

  /**
    * 获取mysql站点表数据,更新站点变量
    *
    * @return
    */
  def updateSiteInfo(conn: Connection): Map[String, util.List[String]] = {
    //1.获取mysql连接池的一个连接

    val jdbcUtils = new JdbcUtils
    //val conn = jdbcUtils.getConnection
    //2.查询新的数据
    val sql = "select * from m_di_site"
    //val ps = conn.prepareStatement(sql)
    //val rs = ps.executeQuery()
    var jsonList = new ArrayBuffer[String]
    // val list = new
    val aid = "siteidMapping" //sensitive_word_id
    val list: util.List[SiteInfo] = jdbcUtils.findResultWithConn(conn, sql, null.asInstanceOf[util.List[AnyRef]], classOf[SiteInfo])
    import scala.collection.JavaConversions._
    for (siteInfo1 <- list) {
      //      import com.alibaba.fastjson.JSON
      // val jsonString = JSON.toJSONString(siteInfo1) //JSON.toJSONString()

      jsonList += siteInfo1.toString
    }

    import scala.collection.JavaConverters._
    val jsonLists = jsonList.toList.asJava
    map += (aid -> jsonLists)

    //jdbcUtils.findMoreRefResult(sql,null，SiteInfo)
    //3.连接池回收连接

    map.toMap
  }


  /**
    * 获取mysql站点表数据,更新站点变量
    *
    * @return
    */
  def updateRegionInfo(conn: Connection): Map[String, util.List[String]] = {
    //1.获取mysql连接池的一个连接

    val jdbcUtils = new JdbcUtils
    //val conn = jdbcUtils.getConnection
    //2.查询新的数据
    val sql = "select * from m_di_region"
    //val ps = conn.prepareStatement(sql)
    //val rs = ps.executeQuery()
    var jsonList = new ArrayBuffer[String]
    // val list = new
    val aid = "regionInfoMapping" //sensitive_word_id
    val list: util.List[RegionInfo] = jdbcUtils.findRegionInfiToLowResultWithConn(conn, sql, null.asInstanceOf[util.List[AnyRef]], classOf[RegionInfo])
    import scala.collection.JavaConversions._
    for (regionInfo <- list) {
      //      import com.alibaba.fastjson.JSON
      // val jsonString = JSON.toJSONString(siteInfo1) //JSON.toJSONString()

      jsonList += regionInfo.toString
    }

    import scala.collection.JavaConverters._
    val jsonLists = jsonList.toList.asJava
    map += (aid -> jsonLists)

    //jdbcUtils.findMoreRefResult(sql,null，SiteInfo)
    //3.连接池回收连接

    map.toMap
  }




  /**
    * 获取mysql预警信息表数据,更新站点变量
    *
    * @return
    */
  def updateKeyWordMapping(conn: Connection): Map[String, util.List[String]] = {
    //1.获取mysql连接池的一个连接
    val jdbcUtils = new JdbcUtils
    val aid = "keywordMapping"
    //2.查询新的数据
    //账号预警配置
    val sql = "SELECT a.warning_conf_id,a.monitor_type_id,a.monitor_entity_id,a.user_id,a.switch_status,a.key_words,a.words_relation,a.warning_threshold,a.warning_type,a.alarm_mode,b.nick_name ,b.account_id FROM \nm_bs_warning_mapping a LEFT JOIN `m_bs_account_monitor_base_info` b ON a.monitor_entity_id = b.id  \nWHERE a.switch_status = 2 AND a.monitor_type_id = 3"
    var jsonList = new ArrayBuffer[String]
    val list: util.List[WarningMapping] = jdbcUtils.findResultWithConn(conn, sql, null.asInstanceOf[util.List[AnyRef]], classOf[WarningMapping])
    //事件预警配置
    val sqlEvent = "SELECT a.warning_conf_id,a.monitor_type_id,a.monitor_entity_id,a.user_id,a.switch_status,a.key_words,a.words_relation,a.warning_threshold,a.warning_type,a.alarm_mode,b.event_name ,b.start_time,b.end_time  FROM\nm_bs_warning_mapping a LEFT JOIN m_bs_event_base_info b ON a.monitor_entity_id = b.event_id  \nWHERE a.switch_status = 2 AND a.monitor_type_id=2"
    val listEvent: util.List[WarningMapping] = jdbcUtils.findResultWithConn(conn, sqlEvent, null.asInstanceOf[util.List[AnyRef]], classOf[WarningMapping])
    //主题预警配置
    val sqlTheme = "SELECT a.warning_conf_id,a.monitor_type_id,a.monitor_entity_id,a.user_id,a.switch_status,a.key_words,a.words_relation,a.warning_threshold,a.warning_type,a.alarm_mode,b.theme_name ,b.country  \nFROM m_bs_warning_mapping a LEFT JOIN `m_bs_theme_base_info` b ON a.monitor_entity_id = b.theme_id  \nWHERE a.switch_status = 2  AND a.monitor_type_id= 1"
    val listTheme: util.List[WarningMapping] = jdbcUtils.findResultWithConn(conn, sqlTheme, null.asInstanceOf[util.List[AnyRef]], classOf[WarningMapping])

    list.addAll(listEvent)
    list.addAll(listTheme)
    import scala.collection.JavaConversions._
    for (eventForeInfo <- list) {
      //      import com.alibaba.fastjson.JSON
      //val jsonString = JSON.toJSONString(eventForeInfo) //JSON.toJSONString()
      //val str = scala.util.parsing.json.JSONFormat
      jsonList += EventTrans.transEventToJson(eventForeInfo)
    }

    import scala.collection.JavaConverters._
    val jsonLists = jsonList.toList.asJava
    map += (aid -> jsonLists)

    //jdbcUtils.findMoreRefResult(sql,null，SiteInfo)
    //3.连接池回收连接

    map.toMap
  }


  /**
    * 获取mysql预警信息表数据,更新站点变量
    *
    * @return
    */
  def testWordMapping(conn: Connection): Unit = {
    //1.获取mysql连接池的一个连接
    val jdbcUtils = new JdbcUtils

    //事件预警配置
    val sqlEvent = "SELECT a.warning_conf_id,a.monitor_type_id,a.monitor_entity_id,a.user_id,a.switch_status,a.key_words,a.words_relation,a.warning_threshold,a.warning_type,a.alarm_mode,b.event_name ,b.start_time,b.end_time  FROM\nm_bs_warning_mapping a LEFT JOIN m_bs_event_base_info b ON a.monitor_entity_id = b.event_id  \nWHERE a.switch_status = 2 AND a.monitor_type_id=2"
    val listEvent: util.List[WarningMapping] = jdbcUtils.findResultWithConn(conn, sqlEvent, null.asInstanceOf[util.List[AnyRef]], classOf[WarningMapping])
    import scala.collection.JavaConverters._
    val buffer: scala.collection.mutable.Buffer[WarningMapping] = listEvent.asScala

    val list = buffer.toList

    list.foreach(em => {

      val startTime = em.getStart_time
      val time = "2018-09-19 02:03:23"
      val timestamp = TimeUtils.transStrToTimeStamp(time)
      val isbeforeTime = TimeUtils.compareTime(timestamp, startTime)
      println(isbeforeTime)

    })

  }

  /**
    * 获取mysql预警信息表数据,更新站点变量
    *
    * @return
    */
  def updateNewsEventForeInfo(conn: Connection): Map[String, util.List[String]] = {
    //1.获取mysql连接池的一个连接
    val jdbcUtils = new JdbcUtils
    //val conn = jdbcUtils.getConnection
    //2.查询新的数据
    val sql = "SELECT * FROM `m_bs_event_fore_warning_mapping` WHERE FIND_IN_SET('4', warning_type)  AND FIND_IN_SET('5', source) AND STATUS =0 "
    //val ps = conn.prepareStatement(sql)
    //val rs = ps.executeQuery()
    var jsonList = new ArrayBuffer[String]

    // val list = new
    val aid = "newsEvent" //sensitive_word_id
    val list: util.List[EventForeMapping] = jdbcUtils.findResultWithConn(conn, sql, null.asInstanceOf[util.List[AnyRef]], classOf[EventForeMapping])
    import scala.collection.JavaConversions._
    for (eventForeInfo <- list) {
      //      import com.alibaba.fastjson.JSON
      //val jsonString = JSON.toJSONString(eventForeInfo) //JSON.toJSONString()
      //val str = scala.util.parsing.json.JSONFormat
      jsonList += EventTrans.transEventToJson(eventForeInfo)
    }

    import scala.collection.JavaConverters._
    val jsonLists = jsonList.toList.asJava
    map += (aid -> jsonLists)

    //jdbcUtils.findMoreRefResult(sql,null，SiteInfo)
    //3.连接池回收连接
    map.toMap
  }

  /**
    * 获取mysql预警信息表数据,更新站点变量
    *
    * @return
    */
  def updateFaceBookEventForeInfo(conn: Connection): Map[String, util.List[String]] = {
    //1.获取mysql连接池的一个连接
    val jdbcUtils = new JdbcUtils
    //val conn = jdbcUtils.getConnection
    //2.查询新的数据

    val sql = "SELECT * FROM `m_bs_event_fore_warning_mapping` WHERE FIND_IN_SET('4', warning_type)  AND FIND_IN_SET('2', source) AND STATUS = 0 "
    //val ps = conn.prepareStatement(sql)
    //val rs = ps.executeQuery()
    var jsonList = new ArrayBuffer[String]

    // val list = new
    val aid = "facebookEvent" //sensitive_word_id
    val list: util.List[EventForeMapping] = jdbcUtils.findResultWithConn(conn, sql, null.asInstanceOf[util.List[AnyRef]], classOf[EventForeMapping])
    import scala.collection.JavaConversions._
    for (eventForeInfo <- list) {
      //      import com.alibaba.fastjson.JSON
      //val jsonString = JSON.toJSONString(eventForeInfo) //JSON.toJSONString()
      //val str = scala.util.parsing.json.JSONFormat
      jsonList += EventTrans.transEventToJson(eventForeInfo)
    }

    import scala.collection.JavaConverters._
    val jsonLists = jsonList.toList.asJava
    map += (aid -> jsonLists)

    //jdbcUtils.findMoreRefResult(sql,null，SiteInfo)
    //3.连接池回收连接

    map.toMap
  }

  /**
    * 获取mysql预警信息表数据,更新站点变量
    *
    * @return
    */
  def updateAccoutMapping(conn: Connection): Map[String, util.List[String]] = {
    //1.获取mysql连接池的一个连接
    val jdbcUtils = new JdbcUtils
    //val conn = jdbcUtils.getConnection
    //2.查询新的数据
    val sql = "SELECT a.warning_conf_id,a.monitor_type_id,a.monitor_entity_id,a.user_id,a.switch_status,a.key_words,a.words_relation,a.warning_threshold,a.warning_type,a.alarm_mode,b.nick_name ,b.account_id FROM \nm_bs_warning_mapping a LEFT JOIN `m_bs_account_monitor_base_info` b ON a.monitor_entity_id = b.id  \nWHERE a.switch_status = 2 AND a.monitor_type_id = 3 "
    //val ps = conn.prepareStatement(sql)
    //val rs = ps.executeQuery()
    var jsonList = new ArrayBuffer[String]
    // try{
    // val list = new
    val aid = "ForeofAccount" //sensitive_word_id
    val list: util.List[WarningMapping] = jdbcUtils.findResultWithConn(conn, sql, null.asInstanceOf[util.List[AnyRef]], classOf[WarningMapping])
    import scala.collection.JavaConversions._
    for (eventForeInfo <- list) {
      jsonList += EventTrans.transEventToJson(eventForeInfo)
    }

    import scala.collection.JavaConverters._
    val jsonLists = jsonList.toList.asJava
    map += (aid -> jsonLists)

    //jdbcUtils.findMoreRefResult(sql,null，SiteInfo)
    //3.连接池回收连接
    //    } catch {
    //      case ex: Exception => {
    //        println("error :", ex)
    //      }
    //    }finally {
    //      jdbcUtils.close(conn,ps,rs)
    //    }
    map.toMap
  }

  /**
    * 获取监控账号使用数据
    *
    * @return
    */
  def updateAccountMonitor(conn: Connection): Map[String, List[String]] = {
    //1.获取mysql连接池的一个连接
    val jdbcUtils = new JdbcUtils
    var sql: String = null
    var ps: PreparedStatement = null
    var rs: ResultSet = null
    try {
      //2.查询新的数据
      sql = "select * from m_rp_account_normaluse_info "
      ps = conn.prepareStatement(sql)
      rs = ps.executeQuery()
      //获取AccountDao处理数据

      val accountCheckDao: AccountCheckDao = new AccountCheckDao()
      val aid = "accountMsgFromTable" //
      val list: util.List[String] = accountCheckDao.convertList(rs)
      map += (aid -> list)
      //3.连接池回收连接
    } catch {
      case ex: Exception => {
        println("error :", ex)
      }
    } finally {
      jdbcUtils.closePst(ps)
      jdbcUtils.closeRes(rs)
    }
    map.toMap
  }


  /**
    * 获取监控账号使用数据
    *
    * @return
    */
  def updateAccountId(conn: Connection): Map[String, List[String]] = {
    //1.获取mysql连接池的一个连接
    val jdbcUtils = new JdbcUtils
    //2.查询新的数据
    var sql: String = null
    var ps: PreparedStatement = null
    var rs: ResultSet = null
    try {
      sql = "select account_id from m_rp_account_normaluse_info "
      ps = conn.prepareStatement(sql)
      rs = ps.executeQuery()
      //获取AccountDao处理数据

      val accountCheckDao: AccountCheckDao = new AccountCheckDao()
      val aid = "accountId" //
      val list: util.List[String] = accountCheckDao.resultSetToList(rs)
      map += (aid -> list)
      //3.连接池回收连接
    } catch {
      case ex: Exception => {
        println("error :", ex)
      }
    } finally {
      jdbcUtils.closePst(ps)
      jdbcUtils.closeRes(rs)
    }
    map.toMap
  }


  /**
    * 获取监控账号使用数据
    *
    * @return
    */
  def updateLanguageFilter(): Map[String, List[String]] = {

    val aid = "languageFilter"
    val list: util.List[String] = new util.ArrayList[String]()
    //en", "es", "ar", "fr", "pt", "zh", "ru", "kz"
    list.add("es")
    list.add("en")
    list.add("ar")
    list.add("fr")
    list.add("pt")
    list.add("zh")
    list.add("ru")
    list.add("kz")

    map += (aid -> list)


    map.toMap
  }


  /**
    * 获取mysql预警信息表数据,更新站点变量
    *
    * @return
    */
  def updateAccountTime(conn: Connection): Map[String, util.List[String]] = {
    //1.获取mysql连接池的一个连接
    val jdbcUtils = new JdbcUtils
    //val conn = jdbcUtils.getConnection
    //2.查询新的数据
    val sql = "select account_id,latest_time from m_rp_account_normaluse_info  "
    //val ps = conn.prepareStatement(sql)
    //val rs = ps.executeQuery()
    var jsonList = new ArrayBuffer[String]

    // val list = new
    val aid = "accountLastTime" //sensitive_word_id
    val list: util.List[AccountLastTime] = jdbcUtils.findResultWithConn(conn, sql, null.asInstanceOf[util.List[AnyRef]], classOf[AccountLastTime])
    import scala.collection.JavaConversions._
    for (eventForeInfo <- list) {
      //      import com.alibaba.fastjson.JSON
      //val jsonString = JSON.toJSONString(eventForeInfo) //JSON.toJSONString()
      //val str = scala.util.parsing.json.JSONFormat
      jsonList += EventTrans.transEventToJson(eventForeInfo)
    }

    import scala.collection.JavaConverters._
    val jsonLists = jsonList.toList.asJava
    map += (aid -> jsonLists)

    //jdbcUtils.findMoreRefResult(sql,null，SiteInfo)
    //3.连接池回收连接
    map.toMap
  }

  def updateAllParams(): Map[String, util.List[String]] = {
    //updateSensitiveWord
    // val jdbcUtils = new JdbcUtils

    val conn = getConn
    var mapInfo: Map[String, util.List[String]] = null
    try {
      updateSiteInfo(conn)
      updateRegionInfo(conn)
      updateKeyWordMapping(conn)
            /**账号和帖子配置表**/
            updateAccoutMapping(conn)
            updateAccountMonitor(conn)
            updateAccountId(conn)
      updateLanguageFilter
      //      mapInfo = updateAccountTime(conn)
      mapInfo = updateLanguageFilter
    } catch {
      case ex: Exception => {
        println("error :", ex)
      }
    } finally {
      if (conn != null) {
        conn.close()
      }
    }
    mapInfo

  }


  def main(args: Array[String]): Unit = {
    //      val cal= Calendar.getInstance()
    //      val minute = cal.get(Calendar.MINUTE)
    //      val second = cal.get(Calendar.SECOND)
    //    println(minute + ":" +second)

    //     for (i <- 1 to 50000 ){


    //    val conn = getConn
    //    try {
    //      testWordMapping(conn)
    //    } catch {
    //      case ex: Exception => {
    //        println("error :", ex)
    //      }
    //    } finally {
    //      if (conn != null) {
    //        conn.close()
    //      }
    //    }

          val start = System.currentTimeMillis()
          val siteInfoStrs  = DataFromMysql.updateAllParams
          for ( (k,v)<- siteInfoStrs){
            println(v)
          }

//    import scala.collection.JavaConverters._
//    val buffer: scala.collection.mutable.Buffer[String] = siteInfoStrs.asScala
//
//    val list = buffer.toList
//
////    list.foreach(em => {
////      val warningMapping = JSON.parseObject(em, classOf[WarningMapping])
////      val startTime = warningMapping.getStart_time
////
////      if(startTime != null){
////        println("=======================================" +startTime.isInstanceOf[Timestamp])
////        val time = "2018-09-19 01:55:23"
////        val timestamp = TimeUtils.transStrToTimeStamp(time)
////        val isbeforeTime = TimeUtils.compareTime(timestamp, startTime)
////        println(isbeforeTime)
////      }
////
////
////    })
////          println(siteInfoStrs)
//          //    val flag = siteInfoStrs.equals("")
//          //    println(flag)
//          val regionStrTest = "chin's take ewas massively fun!! love you all,chisnax cahin!a thanks cshinas so much!! \uD83E\uDD29take take taketakecare folks!xchin".toLowerCase
//          val end = System.currentTimeMillis()
////          for(i <- 1 to 1000){
////            val end = System.currentTimeMillis()
//            val siteInfoMap = SiteInfoUtils.getRegionInfoMap(siteInfoStrs)//将字典表信息转化为映射关系
////            println(siteInfoMap)
//            val keySet = siteInfoMap.keySet()
//            println(keySet.size())
//            val boo =  keySet.contains("China")
//            println(boo)
//            val  filter = new KeywordFilter(keySet);
//            val  regionSet =  filter.getKeyWord(regionStrTest, 2)
//            val regionSet2 = KeywordClearUtil.getCompletedSet(regionStrTest , regionSet)
//            println("original : " +regionSet + "len1 : "+ regionSet.size() )
//            println("=====================================================")
//            println("modified : "+regionSet2+ "len2 : "+ regionSet2.size() )
//
//            val end2 = System.currentTimeMillis()
//            println("region time :"+(end2 - end)  )
////          }
//
//
//
//    //          println(siteInfoMap)
//          //    println(siteInfoMap)
//          println(end - start)


    //     }
    //    val list :util.List[String]= siteInfoStrs.getOrElse("accountLastTime", null)
    //    TestUtils.timeTest(list)

  }


}
