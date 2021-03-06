package com.ceiec.bigdata.action;

import com.alibaba.fastjson.JSON;
import com.ceiec.bigdata.dao.NewsDao;
import com.ceiec.bigdata.dao.impl.NewsDaoImpl;
import com.ceiec.bigdata.entity.AllNewsPost;
import com.ceiec.bigdata.entity.UserEntityCommon;
import com.ceiec.bigdata.util.esutil.EsClient;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;

import java.sql.SQLException;

public class TestNewsPost {
    public static void main(String[] args) throws Exception {
        String str = "{\n" +
                "    \"content\": {\n" +
                "        \"source_url\": \"https://www.huffingtonpost.com/entry/rep-duncan-hunter-blames-wife-for-campaign-spending_us_5b7f7dabe4b07295151179f2\",\n" +
                "        \"publish_time\": 1535070267,\n" +
                "        \"title\": \"Rep. Duncan Hunter Blames Wife For Campaign Spending Under Inquiry\",\n" +
                "        \"summary\": \"Hunter and his wife are under indictment for the alleged misuse of  $250,000 in campaign money.\",\n" +
                "        \"content\": \"Rep. Duncan Hunter (R-Calif.), who has been indicted on corruption charges related to the use of his campaign money, appeared to blame his wife for the predicament when he said in an interview Thursday night that she handled the finances for their family and his campaign.\\n\\nHunter and his wife Margaret, who was also indicted, entered pleas of not guilty in federal court Thursday morning. They face 60 charges related to the alleged misuse of $250,000 in campaign money. Prosecutors say the couple used the money to illegally pay for personal expenses, including lavish vacations, dental fees and a plane ticket for their pet rabbit.\\n\\nAmong the allegations is that Hunter called his wife when he wanted to buy some Hawaii shorts and that she told him to purchase them at the pro shop of a golf club so they could be listed as golf balls for the Wounded Warriors Project, a veterans charity. Hunter denied that accusation.\\n\\nOn Thursday evening, Fox News host Martha MacCallum asked Hunter, “Are you saying it’s more her fault than your fault?”\\n\\nHunter, a Republican from San Diego who served in the Marines, replied: “I’m saying when I went to Iraq in 2003, the first time, I gave her power of attorney and she handled my finances throughout my entire military career and that continued on when I got into Congress. Because I’m gone five days a week, I’m home for two.”\\n\\n“And she was also the campaign manager,” he added. “Whatever she did, that will be looked at too, I’m sure. But I didn’t do it. I didn’t spend any money illegally. I did not use campaign money, especially for Wounded Warriors stuff, there’s no way.”\\n\\nHunter, who is running for re-election in November, said the charges were politically motivated.\\n\\n“This is pure politics and the prosecutors can make an indictment read like a scandalous novel if they want to,” he said. “They’ve had a year and a half to do this. There’s no way for me now to go out and be able to talk to my people or get this done in court before my election. They’ve had this for a long time. This is a late hit.”\",\n" +
                "        \"author\": \"Carla Baranauckas\",\n" +
                "        \"img\": [\n" +
                "            \"https://img.huffingtonpost.com/asset/5b7f7f252000007a0337a6ec.jpeg?cache=n5mr4wg3ox&ops=1910_1000\"\n" +
                "        ],\n" +
                "        \"lang\": \"en\"\n" +
                "    },\n" +
                "    \"source_type\": 5\n" +
                "}";
        UserEntityCommon userEntityCommon = JSON.parseObject(str, UserEntityCommon.class);
        String fbContent = userEntityCommon.getContent();
        AllNewsPost newsPost = JSON.parseObject(fbContent, AllNewsPost.class);
        TransportClient client = EsClient.getInstance().getTransportClient();
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        try {
            NewsDao newsDao = new NewsDaoImpl(newsPost,null ,"{\n" +
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
            IndexRequestBuilder indexRequestBuilder2 = newsDao.bulkAddEsData(client, "m_cyber_es_info_test", "info");
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
