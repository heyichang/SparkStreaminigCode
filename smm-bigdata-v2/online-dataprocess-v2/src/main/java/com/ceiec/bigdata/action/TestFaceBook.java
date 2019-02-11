package com.ceiec.bigdata.action;

import com.alibaba.fastjson.JSON;
import com.ceiec.bigdata.dao.FaceBookDao;
import com.ceiec.bigdata.dao.impl.FaceBookDaoImpl;
import com.ceiec.bigdata.entity.FacebookUserType;
import com.ceiec.bigdata.entity.UserEntityCommon;
import com.ceiec.bigdata.util.esutil.EsClient;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;


/**
 * Created by heyichang on 2018/2/9.
 */
public class TestFaceBook {
    public static void main(String[] args) throws Exception {
        String str = "{\n" +
                "    \"content\": {\n" +
                "        \"account_id\": \"100024866880616\",\n" +
                "        \"portrait\": \"https://scontent-lax3-1.xx.fbcdn.net/v/t1.0-1/c9.0.160.160/p160x160/38482663_241776469994581_560548048795598848_n.jpg?_nc_cat=0&oh=93d44e0f059cd6d7ba3c1bf3a322f58b&oe=5C22D579\",\n" +
                "        \"account_name\": \"shoorakrest\",\n" +
                "        \"account_type\": 2,\n" +
                "        \"nick_name\": \"Александр Абрамов (sasha+)\",\n" +
                "        \"home_url\": \"https://www.facebook.com/shoorakrest\",\n" +
                "        \"introduce\": \".\",\n" +
                "        \"friends_num\": \"2934\",\n" +
                "        \"work\": [\n" +
                "            {\n" +
                "                \"name\": \"Facebook\",\n" +
                "                \"desc\": \"Killer\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"Self-Employed\",\n" +
                "                \"desc\": \"ночной сторож\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"education\": [\n" +
                "            {\n" +
                "                \"name\": \"State University of Management\",\n" +
                "                \"desc\": \"Управление качеством · Moscow, Russia\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"name\": \"Школа № 1199\",\n" +
                "                \"desc\": \"Moscow, Russia\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"location\": \"Nowhere, Oklahoma\",\n" +
                "        \"hometown\": \"Moscow, Russia\",\n" +
                "        \"social_account\": [\n" +
                "            {\n" +
                "                \"account_name\": \"shoorakrest\",\n" +
                "                \"web\": \"Instagram\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"birthday\": \"August 12, 1979\",\n" +
                "        \"gender\": \"male\",\n" +
                "        \"other_name\": [\n" +
                "            \"sasha+\"\n" +
                "        ],\n" +
                "        \"events\": [\n" +
                "            {\n" +
                "                \"content\": \"In a Relationship\",\n" +
                "                \"time\": \"2018-01-01\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"content\": \"Got Engaged\",\n" +
                "                \"time\": \"2018-01-01\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"content\": \"Born on August 12, 1979\",\n" +
                "                \"time\": \"1979-01-01\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"fans_number\": 87\n" +
                "    },\n" +
                "    \"source_type\": 2\n" +
                "}";
        UserEntityCommon userEntityCommon = JSON.parseObject(str, UserEntityCommon.class);
        String fbContent = userEntityCommon.getContent();
        FacebookUserType facebookUserType = JSON.parseObject(fbContent, FacebookUserType.class);
        TransportClient client = EsClient.getInstance().getTransportClient();
        try {
            BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
            FaceBookDao faceBookDao = new FaceBookDaoImpl(facebookUserType);
//        IndexRequestBuilder indexRequestBuilder = faceBookDao.bulkAddEsData(client,"zmc_test8","test");
            IndexRequestBuilder indexRequestBuilder2 = faceBookDao.bulkAddEsUserData(client, "m_cyber_es_account_test", "account");

            bulkRequestBuilder.add(indexRequestBuilder2);
            BulkResponse bulkResponse = bulkRequestBuilder.execute().actionGet();
            if (bulkResponse.hasFailures()) {
                System.out.println("error in doing index request: " + bulkResponse.buildFailureMessage());
            }
        } catch (Exception e) {

        } finally {
            client.close();
        }

    }
}
