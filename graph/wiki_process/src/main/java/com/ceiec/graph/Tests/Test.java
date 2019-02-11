package com.ceiec.graph.Tests;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Test {
    public static void main(String[] args) {
        Message message = new Message();
        message.setMessage();
//        System.out.println(message.message);
        JSONObject object = JSON.parseObject(message.message);
//        System.out.println(object.getJSONObject("labels").getJSONObject("en").getString("value"));
//        System.out.println(object.getJSONObject("descriptions").getJSONObject("en").getString("value"));
//        System.out.println(object.getJSONObject("aliases").getJSONObject("en").getString("value"));
        JSONArray array = object.getJSONObject("claims").getJSONArray("P31");
        for (int i =0;i<array.size();i++){
            System.out.println(array.getJSONObject(i).getJSONObject("mainsnak").getJSONObject("datavalue").getJSONObject("value"));
        }
    }
    public static void st(JSONObject object){
        System.out.println(object.getString("mainsnak"));
    }

    /**
     * 数据处理逻辑
     *  关系属性，实体属性
     *  1、es存储实体属性
     *  2、neo4j存储关系属性（id,名称）
     */
    @org.junit.Test
    public void testForprocess(){
        //获取数据
        Message message = new Message();
        message.setMessage();
        //json解析
        JSONObject object = JSON.parseObject(message.message);
        //获取想要的数据
        //处理数据
        //输出数据
        JSONArray array = object.getJSONObject("claims").getJSONArray("P1036");
        for (int i =0;i<array.size();i++){
            if (array.getJSONObject(i).getJSONObject("mainsnak").getString("datatype").trim().equals("string")){
                System.out.println("string value : "+array.getJSONObject(i).getJSONObject("mainsnak").getJSONObject("datavalue").getString("value"));
            }
            if (array.getJSONObject(i).getJSONObject("mainsnak").getString("datatype").trim().equals("wikibase-item")){
                System.out.println("wikibase-item value : "+array.getJSONObject(i).getJSONObject("mainsnak").getJSONObject("datavalue").getJSONObject("value").getString("id"));
            }
        }
    }


}
