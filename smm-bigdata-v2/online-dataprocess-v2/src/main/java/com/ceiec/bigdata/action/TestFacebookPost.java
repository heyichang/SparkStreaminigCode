package com.ceiec.bigdata.action;

import com.alibaba.fastjson.JSON;
import com.ceiec.bigdata.dao.FaceBookDao;
import com.ceiec.bigdata.dao.impl.FaceBookDaoImpl;
import com.ceiec.bigdata.entity.AllFacebookPost;
import com.ceiec.bigdata.entity.UserEntityCommon;
import com.ceiec.bigdata.util.esutil.EsClient;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;

import java.sql.SQLException;

public class TestFacebookPost {
    public static void main(String[] args) throws Exception {
        String str = "{\n" +
                "    \"content\": {\n" +
                "        \"img\": [\n" +
                "            \"https://scontent-lax3-1.xx.fbcdn.net/v/t15.0-10/35754576_10155974194010787_4570568861983703040_n.jpg?_nc_cat=1&oh=ab9c0c22e9621a9fe737c7e091c36d7e&oe=5C2E50FD\"\n" +
                "        ],\n" +
                "        \"like_number\": 127,\n" +
                "        \"creating_time\": \"1536179520\",\n" +
                "        \"retweet_number\": 44,\n" +
                "        \"source_type\": 2,\n" +
                "        \"remark_number\": 23,\n" +
                "        \"portrait\": \"https://scontent-lax3-1.xx.fbcdn.net/v/t1.0-1/p200x200/10357439_1150581611623703_6780872256748631392_n.jpg?_nc_cat=0&oh=1684b32d1678edf0d7d55d133614c2d9&oe=5C324899\",\n" +
                "        \"type\": 1,\n" +
                "        \"source_url\": \"https://www.facebook.com/bbc/videos/10156064085970787/\",\n" +
                "        \"content\": \"Taylor Lautner's pronunciation of British place names could use some work...  \uD83D\uDE02\uD83C\uDDEC\uD83C\uDDE7 (Via BBC Three)\",\n" +
                "        \"account_id\": \"1143803202301544\",\n" +
                "        \"account_name\": \"bbc\",\n" +
                "        \"nick_name\": \"BBC\",\n" +
                "        \"links\": [\n" +
                "            \"https://www.facebook.com/TaylorLautner/?fref=mentions&__xts__%5B0%5D=68.ARBhjFH0YkThK8aOzWdq2sv9t1qMmSQqm5wPMQ5uK3MXZ48mn_ubn7P8iiUzsirXPzaZWZPLYF-JSPBBlc-rFWsaINWUbvjXhSY1kFN6yCFUob-XOZ6JiMbDEdyqf4s3wjTG-7opu8tQH3FG-XsV8S4NEjJdlp_IRxjXEdbPVB6nMgVYT7b_B5g&__tn__=K-R\",\n" +
                "            \"https://www.facebook.com/bbcthree/?fref=mentions&__xts__%5B0%5D=68.ARBhjFH0YkThK8aOzWdq2sv9t1qMmSQqm5wPMQ5uK3MXZ48mn_ubn7P8iiUzsirXPzaZWZPLYF-JSPBBlc-rFWsaINWUbvjXhSY1kFN6yCFUob-XOZ6JiMbDEdyqf4s3wjTG-7opu8tQH3FG-XsV8S4NEjJdlp_IRxjXEdbPVB6nMgVYT7b_B5g&__tn__=K-R\"\n" +
                "        ]\n" +
                "    },\n" +
                "    \"source_type\": 2\n" +
                "}";
        UserEntityCommon userEntityCommon = JSON.parseObject(str, UserEntityCommon.class);
        String fbContent = userEntityCommon.getContent();
        AllFacebookPost facebookUserType = JSON.parseObject(fbContent, AllFacebookPost.class);
        TransportClient client = EsClient.getInstance().getTransportClient();
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        try {
            FaceBookDao faceBookDao = new FaceBookDaoImpl(facebookUserType,"{\n" +
                    "        \"language\": \"en\",\n" +
                    "        \"entities\": [\n" +
                    "            {\n" +
                    "                \"word\": \"Duncan Hunter\",\n" +
                    "                \"type\": 1,\n" +
                    "                \"start\": 5,\n" +
                    "                \"end\": 18\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"word\": \"Margaret\",\n" +
                    "                \"type\": 1,\n" +
                    "                \"start\": 296,\n" +
                    "                \"end\": 304\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"word\": \"Hunter\",\n" +
                    "                \"type\": 1,\n" +
                    "                \"start\": 670,\n" +
                    "                \"end\": 676\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"word\": \"Hawaii\",\n" +
                    "                \"type\": 5,\n" +
                    "                \"start\": 720,\n" +
                    "                \"end\": 726\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"word\": \"the Wounded Warriors Project\",\n" +
                    "                \"type\": 4,\n" +
                    "                \"start\": 846,\n" +
                    "                \"end\": 874\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"word\": \"Hunter\",\n" +
                    "                \"type\": 1,\n" +
                    "                \"start\": 896,\n" +
                    "                \"end\": 902\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"word\": \"Fox News\",\n" +
                    "                \"type\": 4,\n" +
                    "                \"start\": 951,\n" +
                    "                \"end\": 959\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"word\": \"Martha MacCallum\",\n" +
                    "                \"type\": 1,\n" +
                    "                \"start\": 965,\n" +
                    "                \"end\": 981\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"word\": \"Hunter\",\n" +
                    "                \"type\": 1,\n" +
                    "                \"start\": 988,\n" +
                    "                \"end\": 994\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"word\": \"Republican\",\n" +
                    "                \"type\": 2,\n" +
                    "                \"start\": 1063,\n" +
                    "                \"end\": 1073\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"word\": \"San Diego\",\n" +
                    "                \"type\": 5,\n" +
                    "                \"start\": 1079,\n" +
                    "                \"end\": 1088\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"word\": \"Marines\",\n" +
                    "                \"type\": 2,\n" +
                    "                \"start\": 1107,\n" +
                    "                \"end\": 1114\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"word\": \"Iraq\",\n" +
                    "                \"type\": 5,\n" +
                    "                \"start\": 1152,\n" +
                    "                \"end\": 1156\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"word\": \"Congress\",\n" +
                    "                \"type\": 4,\n" +
                    "                \"start\": 1314,\n" +
                    "                \"end\": 1322\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"word\": \"Wounded Warriors\",\n" +
                    "                \"type\": 4,\n" +
                    "                \"start\": 1590,\n" +
                    "                \"end\": 1606\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"category\": 103,\n" +
                    "        \"hotword\": [\n" +
                    "            {\n" +
                    "                \"word\": \"hunter\",\n" +
                    "                \"score\": 0.2759418431608328\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"word\": \"money\",\n" +
                    "                \"score\": 0.23373603034582371\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"word\": \"campaign\",\n" +
                    "                \"score\": 0.23058161173900552\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"word\": \"charges\",\n" +
                    "                \"score\": 0.2014755907181702\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"word\": \"thursday\",\n" +
                    "                \"score\": 0.1776227069346645\n" +
                    "            }\n" +
                    "        ],\n" +
                    "        \"sentiment\": 0\n" +
                    "    }");
//        IndexRequestBuilder indexRequestBuilder = faceBookDao.bulkAddEsData(client,"zmc_test8","test");
            IndexRequestBuilder indexRequestBuilder2 = faceBookDao.bulkAddPostEsData(client, "m_cyber_es_info_test", "info");
            bulkRequestBuilder.add(indexRequestBuilder2);
            BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
            if (bulkResponse.hasFailures()) {
                System.out.println("error in doing index request: " + bulkResponse.buildFailureMessage());
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            client.close();
        }

    }
}
