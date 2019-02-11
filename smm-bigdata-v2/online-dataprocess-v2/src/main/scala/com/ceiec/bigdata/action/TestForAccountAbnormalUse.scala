package com.ceiec.bigdata.action

import java.util

import com.ceiec.bigdata.dao.impl.TwitterRootDaoImpl
import com.ceiec.bigdata.entity.table.EsInfo
import com.ceiec.bigdata.entity.twitter.TwitterRoot

/**
  * @author heyichang
  * description:测试账号的发帖时间和位置异常预警
  */
object TestForAccountAbnormalUse {
  def main(args: Array[String]): Unit = {

    //读取数据库
    val list :util.List[String] = DataFromMysql.updateAllParams.getOrElse("accountMsgFromTable", null)
    val mappingList :util.List[String] = DataFromMysql.updateAllParams.getOrElse("ForeofAccount", null)
    val accountList :util.List[String] = DataFromMysql.updateAllParams.getOrElse("accountId", null)
    val accountTimeList :util.List[String] = DataFromMysql.updateAllParams.getOrElse("accountLastTime", null)

    /**
      * 简单记录一下数据库需要读取的字段，由最终需要写入的字段推导
      * 需要写入的字段
      */
    //解析类
    val twitterRoot = new TwitterRoot()

    val nlpResultString = ""
    var esInfo = new EsInfo(twitterRoot)

    esInfo = getNewEsInfo(esInfo)

    val twitterRootDao = new TwitterRootDaoImpl(twitterRoot, nlpResultString)

    twitterRootDao.setEsInfo(esInfo)

    twitterRootDao.accoutAndLocationWarning(list,mappingList,accountList,accountTimeList)

  }

  def getNewEsInfo(esInfo: EsInfo):EsInfo={
    //发帖的account_id
    esInfo.setAccountId("4A028F871F9CF115963D62193D5E6CB8")
//    esInfo.setAccountNickName("")
    esInfo.setContent("只是测试而已")
    esInfo.setCreatingDate("2018-10-29 11:01:01")
    esInfo.setCreatingTIme("2018-10-29 11:01:01")
    esInfo.setEmotionId(123)
    esInfo.setGatherDate("2018-10-29")
    esInfo.setGatherTime("2018-10-29 11:01:02")
    esInfo.setInfoId("9929929299")
    //发帖的地理位置
    esInfo.setRegion_id_str("244003")
    esInfo.setSummary("只是测试而已")
    //发帖的时间段
    esInfo.setTimeSlot(java.lang.Short.valueOf("10"))
    esInfo.setSiteId(1000)
    esInfo.setSiteTypeId(100000)
    esInfo
  }
}
