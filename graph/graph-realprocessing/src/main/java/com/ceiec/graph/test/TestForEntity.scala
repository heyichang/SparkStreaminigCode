package com.ceiec.graph.test

import com.alibaba.fastjson.JSON
import com.ceiec.graph.entity.neo4jTwitter.VirtualAccount
import com.ceiec.graph.util.neo4jUtil.Neo4jUtils

object TestForEntity {
  def main(args: Array[String]): Unit = {
    val messageValue = "{\n    \"id\": 973360606496608300,\n    \"id_str\": \"973360606496608256\",\n    \"name\": \"Adom\",\n    \"screen_name\": \"Adom30499256\",\n    \"location\": \"\",\n    \"url\": null,\n    \"description\": \"\",\n    \"protected\": false,\n    \"followers_count\": 2,\n    \"friends_count\": 45,\n    \"listed_count\": 0,\n    \"created_at\": \"Tue Mar 13 00:50:27 +0000 2018\",\n    \"favourites_count\": 0,\n    \"utc_offset\": null,\n    \"time_zone\": null,\n    \"geo_enabled\": false,\n    \"verified\": false,\n    \"statuses_count\": 0,\n    \"lang\": \"en\",\n    \"contributors_enabled\": false,\n    \"is_translator\": false,\n    \"is_translation_enabled\": false,\n    \"profile_background_color\": \"F5F8FA\",\n    \"profile_background_image_url\": null,\n    \"profile_background_image_url_https\": null,\n    \"profile_background_tile\": false,\n    \"profile_image_url\": \"http://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png\",\n    \"profile_image_url_https\": \"https://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png\",\n    \"profile_link_color\": \"1DA1F2\",\n    \"profile_sidebar_border_color\": \"C0DEED\",\n    \"profile_sidebar_fill_color\": \"DDEEF6\",\n    \"profile_text_color\": \"333333\",\n    \"profile_use_background_image\": true,\n    \"has_extended_profile\": false,\n    \"default_profile\": true,\n    \"default_profile_image\": true,\n    \"following\": false,\n    \"live_following\": false,\n    \"follow_request_sent\": false,\n    \"notifications\": false,\n    \"muting\": false,\n    \"blocking\": false,\n    \"blocked_by\": false,\n    \"translator_type\": \"none\",\n    \"related_user_info\": {\n        \"type\": \"follow\",\n        \"user_screen_name\": \"abc\"\n    }\n}"
    val virtualAccount: VirtualAccount = JSON.parseObject(messageValue, classOf[VirtualAccount])
    println(virtualAccount.getRelated_user_info.getType)
    println(Neo4jUtils.getAcccountId(virtualAccount.getScreen_name))
  }
}
