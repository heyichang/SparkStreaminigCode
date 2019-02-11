package com.ceiec.bigdata.util.nlputil;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by heyichang on 2017/11/24.
 */
public class NlpTwitterHelper {
    private static Map<String, String> createMap ;
    static {
        Map<String, String> parmsMap = new HashMap<String, String>();
//        parmsMap.put("summary", "1");
//        parmsMap.put("sentiment", "1");
//        parmsMap.put("hotword", "1");
//        parmsMap.put("category", "1");
//        parmsMap.put("entity", "1");
        parmsMap.put("type", "1");
        createMap = parmsMap ;
    }

    public static Map<String,String> getParms(){

        return createMap;
    }



    public static  String transformWord(String str){

        return StringTransform.transformWord(str);
    }

    public static String getResultJson(String str){

        return  StringTransform.getResultJson(str);
    }


}
