package com.ceiec.bigdata.action;

import com.alibaba.fastjson.JSON;
import com.ceiec.bigdata.entity.table.nlp.EntityNlp;
import com.ceiec.bigdata.entity.table.nlp.NlpPojo;
import com.ceiec.bigdata.entity.twitter.es.EntityJson;
import com.ceiec.bigdata.util.Constants;
import com.ceiec.bigdata.util.locationutil.RelatedRegionInfo;
import com.ceiec.bigdata.util.nlputil.HttpUtils;
import com.ceiec.bigdata.util.nlputil.NlpNewsHelper;
import com.ceiec.bigdata.util.nlputil.NlpTwitterHelper;

import java.util.*;

/**
 * Created by heyichang on 2017/11/8.
 */
public class TestDao {


    public static void main(String[] args) throws Exception {


        Map<String, String> parmsMap = NlpTwitterHelper.getParms();

        Map<String, String> parmsMap2 = NlpNewsHelper.getParms();
//        String str = StringTransform.transformWordIter("Taylor Lautner's pronunciation of British place names could use some work...  \uD83D\uDE02\uD83C\uDDEC\uD83C\uDDE7 (Via BBC Three)");
//        int a = Integer.valueOf(2);
//        Map<String, String> map = new HashMap<>();
//        for (int i = 1; i < a; i++) {
//            if(i%2==0){
//                map.put(String.valueOf(i), null);
//            }else
//                map.put(String.valueOf(i), str);
//        }

//        String str2 = JSON.toJSONString(map);
//        System.out.println(str2);
        List<String> list = new ArrayList();
        for (int i = 1; i < 2; i++) {
            long nlpstartTime = System.currentTimeMillis();//TODO
            NlpPojo nlpPojo = new NlpPojo(String.valueOf(i), "auto", "hi,i'm wwwwwlllljjjj123,i'm in ganggang,now is 20181025 16:15 https://t.co/CdDnSFoNc4 #FoxNews",1100,0 );
            String strjson = JSON.toJSONString(nlpPojo);
            list.add(strjson);
            System.out.println(list.toString());

//            HashMap hashMap = JSON.parseObject(str3,HashMap.class);
//            System.out.println(hashMap.get("message"));
//            long nlpendTime = System.currentTimeMillis();//TODO
//            System.out.println("time : " + (nlpendTime - nlpstartTime));

//            System.out.println(str3.contains("\"summary\": \"Undefined\""));
        }
        String str3 = null;
//        while ( str3 != null){
//            try{
                str3 = HttpUtils.getResultByPost(parmsMap, list.toString());//NlpNewsHelper.transformWord()TODO:dasmdasm
//            }catch (Exception ex){
//
//            }
//        }


        System.out.println(str3);
        HashMap<String, Object> nlpResultMap = JSON.parseObject(str3, HashMap.class);
        EntityNlp nlpResult = JSON.parseObject(nlpResultMap.get("1").toString(), EntityNlp.class);
        /**获取json实体**/
        if (nlpResult.getEntities() != null && nlpResult.getEntities().size() > 0) {
            List<com.ceiec.bigdata.entity.twitter.es.EntityJson> entitiesLists = nlpResult.getEntities();
            //生成地理位置信息list
            List<String> relatedRegionInfos = new ArrayList<>();
            for (EntityJson entityJson : entitiesLists) {

                //将包含地理位置信息加入relatedRegionInfos type = 6代表地理信息类型
                if (entityJson.getType() != null && (entityJson.getType().equals("6") || entityJson.getType().equals("5"))) {
                    relatedRegionInfos.add(entityJson.getWord());
                }
            }
            //获得帖子当中相关地理位置信息
            if (relatedRegionInfos.size() > 0) {
                List<Map<String, Object>> relatedRegionInfoList = RelatedRegionInfo.getRelatedRegionInfoList(relatedRegionInfos);
                System.out.println(relatedRegionInfoList.size() + "  :  " + relatedRegionInfoList.toString());
            }
        }
//        HashMap<String, Object> map1 = JSON.parseObject(str3, HashMap.class);
//        Iterator<String> it = map1.keySet().iterator();
//        while (it.hasNext()) {
//            String key = it.next();
//            System.out.println(key + " :" + map1.get(key));
//        }
    }

}
