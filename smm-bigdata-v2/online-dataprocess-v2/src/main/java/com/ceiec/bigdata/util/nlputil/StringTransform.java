package com.ceiec.bigdata.util.nlputil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


/**
 * Created by heyichang on 2017/12/4.
 */
public class StringTransform {

    public static  String transformWord(String str){
        String oldStr = str.replaceAll("\"","'");
        StringBuffer sb = new StringBuffer();
        sb.append("{\"1\":").append("\"").append(oldStr).append("\"").append("}");
        String str2 = sb.toString();
        JSONObject json = JSON.parseObject(str2);

        return json.toString();
    }


    public static  String transformWordIter(String str){
        if(str!= null){
            String oldStr = str.replaceAll("\"","'");
            return oldStr;
        }
        return null;
    }

    public static String getResultJson(String str){
        if(str != null){
            String newStr = "{\"result\"" + str.substring(4,str.length());
            return  newStr;
        }
       return null;
    }
}
