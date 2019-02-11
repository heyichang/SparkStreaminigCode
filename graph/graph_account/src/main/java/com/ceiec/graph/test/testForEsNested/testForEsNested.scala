package com.ceiec.graph.test.testForEsNested

import java.util

import com.alibaba.fastjson.JSONArray
import com.ceiec.graph.util.NormalUtils
import com.ceiec.graph.util.esUtil.{EsClient, EsUtils}
import com.ceiec.graph.util.neo4jUtil.Neo4jUtils
import org.elasticsearch.action.index.IndexRequestBuilder
import org.elasticsearch.common.xcontent.{XContentBuilder, XContentFactory}

object testForEsNested {
  def main(args: Array[String]): Unit = {
    /**
      * 创建es连接
      */
    val client = EsClient.getInstance.getTransportClient
    val accountBulkRequestBuilder = client.prepareBulk

    //构建我所需要的数据
    val comment1 = new Comment()
    val comment2 = new Comment()

    comment1.setName("hyc01")
    comment1.setComment("no comment for this operation")
    comment1.setAge("21")
    comment1.setDate("2017-07-24")

    comment2.setName("hyc02")
    comment2.setComment("no comment for this operation 02")
    comment2.setAge("22")
    comment2.setDate("2017-07-26")

    val comments = new util.ArrayList[Comment]()
    comments.add(comment1)
    comments.add(comment2)

    val myNested = new MyNested()
    myNested.setTitle("hello world for first time to get nested")
    myNested.setComments(comments)
//
//    val listMap = new util.ArrayList[util.HashMap]()
//    val hashMap = new util.HashMap[Object,Object]()
//
//    JSONArray
//    comments
    val builder: XContentBuilder = XContentFactory.jsonBuilder.
      startObject
      .field("title",myNested.getTitle)
      .field("comments",NormalUtils.listObj2listmap(myNested.getComments))

    builder.endObject()
    val builder_account: IndexRequestBuilder = EsUtils.bulkCreateData(client, builder, "my_index_nested", "blogpost", "1")
    accountBulkRequestBuilder.add(builder_account)

    //提交es数据
    if (accountBulkRequestBuilder.numberOfActions > 0) {
      val bulkResponse = accountBulkRequestBuilder.execute.actionGet
      if (bulkResponse.hasFailures) {
        println("写入存在错误")
//        log.error("error in doing index request: " + bulkResponse.buildFailureMessage)
      }
    }
  }
}
