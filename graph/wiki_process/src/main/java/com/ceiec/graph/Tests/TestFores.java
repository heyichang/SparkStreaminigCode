package com.ceiec.graph.Tests;

import com.ceiec.graph.utils.esUtil.EsClient;
import com.ceiec.graph.utils.esUtil.EsUtils;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.*;
import static org.elasticsearch.common.xcontent.XContentFactory.*;
/**
 * @author :heyichang
 * description:测试javaAPI
 */
public class TestFores {

    @Test
    public void sertindex() throws IOException {

        //document的结构
//        String json = "{" +
//                "\"user\":\"kimchy\"," +
//                "\"postDate\":\"2013-01-30\"," +
//                "\"message\":\"trying out Elasticsearch\"" +
//                "}";
//        //使用map表示json数据结构
//        Map<String, Object> json = new HashMap<String, Object>();
//        json.put("user","kimchy");
//        json.put("postDate",new Date());
//        json.put("message","trying out Elasticsearch");

//        // instance a json mapper
//        ObjectMapper mapper = new ObjectMapper(); // create once, reuse
//        // generate json
//        byte[] json = mapper.writeValueAsBytes(yourbeaninstance);
        EsClient esclient = EsClient.getInstance();
        TransportClient client = esclient.getTransportClient();
        XContentBuilder builder = jsonBuilder()
                .startObject()
                .field("user", "kimchy02")
                .field("postDate", new Date())
                .field("message", "trying out Elasticsearch02")
                .endObject();
        IndexResponse response = client.prepareIndex("twitter", "tweet", "2")
                .setSource(builder
                )
                .get();
        System.out.println(response.toString()
        );
    }

    /**
     * 测试单条数据插入
     */
    @Test
    public void testsingle() throws IOException {
        EsClient esClient = EsClient.getInstance();
        TransportClient client = esClient.getTransportClient();
        XContentBuilder builder = jsonBuilder()
                .startObject()
                .field("user", "kimchy03")
                .field("postDate", new Date())
                .field("message", "trying out Elasticsearch03")
                .endObject();
        EsUtils.saveData(client,builder,"twitter","tweet","3");
    }

    /**
     * 测试单条数据插入（mapObject）
     */
    @Test
    public void testSigleOfMap(){
        //使用map表示json数据结构
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("user","kimchy05");
        json.put("postDate",new Date());
        json.put("message","trying out Elasticsearch05");

        EsClient esclient = EsClient.getInstance();
        TransportClient client = esclient.getTransportClient();

        EsUtils.saveData(client,json,"twitter","tweet","5");
    }

    @Test
    public void testBulk() throws IOException {
        EsClient esclient = EsClient.getInstance();
        TransportClient client = esclient.getTransportClient();

        BulkRequestBuilder bulkRequest = client.prepareBulk();

        // either use client#prepare, or use Requests# to directly build index/delete requests
        bulkRequest.add(client.prepareIndex("twitter", "tweet", "6")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "kimchy06")
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch06")
                        .endObject()
                )
        );

        bulkRequest.add(client.prepareIndex("twitter", "tweet", "7")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "kimchy07")
                        .field("postDate", new Date())
                        .field("message", "another post07")
                        .endObject()
                )
        );

        BulkResponse bulkResponse = bulkRequest.execute().actionGet();

        if (bulkResponse.hasFailures()) {
            // process failures by iterating through each bulk response item
            System.out.println(bulkResponse.buildFailureMessage());
        }
    }

    @Test
    public void testBulkBuilder() throws IOException {
        EsClient esclient = EsClient.getInstance();
        TransportClient client = esclient.getTransportClient();

        //builder
        XContentBuilder builder01 = jsonBuilder()
                .startObject()
                .field("user", "kimchy08")
                .field("postDate", new Date())
                .field("message", "trying out Elasticsearch08")
                .endObject();
        IndexRequestBuilder builder_01 = EsUtils.bulkCreateData(client,builder01,"twitter", "tweet", "8");
        //builder
        XContentBuilder builder02 = jsonBuilder()
                .startObject()
                .field("user", "kimchy09")
                .field("postDate", new Date())
                .field("message", "trying out Elasticsearch09")
                .endObject();

        IndexRequestBuilder builder_02 = EsUtils.bulkCreateData(client,builder01,"twitter", "tweet", "9");

        BulkRequestBuilder bulkRequest = client.prepareBulk();

        bulkRequest.add(builder_01);
        bulkRequest.add(builder_02);

        BulkResponse bulkResponse = bulkRequest.execute().actionGet();

        if (bulkResponse.hasFailures()) {
            // process failures by iterating through each bulk response item
            System.out.println(bulkResponse.buildFailureMessage());
        }
    }
}
