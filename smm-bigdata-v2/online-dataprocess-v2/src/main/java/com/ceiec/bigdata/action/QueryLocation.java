package com.ceiec.bigdata.action;

import com.alibaba.fastjson.JSON;
import com.ceiec.bigdata.entity.table.location.RegistLocationRequst;
import com.ceiec.bigdata.util.locationutil.RegistLocationHttpUtils;
import com.ceiec.bigdata.util.locationutil.RelatedRegionInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by heyichang on 2017/12/19.
 */
public class QueryLocation {

    public static void main(String[] args) {


//        LocationRequst locationRequst = new LocationRequst();
//
//
//        locationRequst.setId("1");
//        locationRequst.setLatitude(33.7586);
//        locationRequst.setLongitude(-84.3944);
//
//       // LocationRequst locationRequst = new LocationRequst("1", location.getLat(), location.getLon());
//        //String requstString = JSON.toJSONString(locationRequst);
//        List list = new ArrayList();
//        list.add(locationRequst);
//        long s = System.currentTimeMillis();
//        String response = LocationHttpUtils.sendPost(JSON.toJSONString(list));
//        long e = System.currentTimeMillis();
//        if((e-s) > 1000){
//          //  logger.warn("twitter info location used time : "+(e-s));
//        }
//        LocationResponse locationResponse = JSON.parseObject(response, LocationResponse.class);
//        System.out.println(locationResponse);
//
//        if (locationResponse.getData() != null) {
//            ResponseValue responseValue = locationResponse.getData().get(0);
//            if (responseValue.getCode() != null && !responseValue.getCode().equals("N")) {
//                this.parent_region_id_str = responseValue.getCode().substring(0, 3);//得到父行政区域位置id
//                this.region_id_str = responseValue.getCode();//得到该行政区域位置id
//            }
//        }
        RegistLocationRequst registLocationRequst = new RegistLocationRequst();
        registLocationRequst.setId("1");
        registLocationRequst.setName("الجزائر");
        List list = new ArrayList();
        list.add(registLocationRequst);
        String str = JSON.toJSONString(list);
        for (int i = 0 ; i<5;i++){
            long startTime = System.currentTimeMillis();
            // LocationHttpUtils locationHttpUtils = new LocationHttpUtils();
            String re = RegistLocationHttpUtils.sendPost(str);
            long endTime = System.currentTimeMillis();
            System.out.println("used time : "+(endTime-startTime));
            System.out.println(re);
        }







//        String str = "149016";
//        System.out.println(str.substring(3,6));
    }
}
