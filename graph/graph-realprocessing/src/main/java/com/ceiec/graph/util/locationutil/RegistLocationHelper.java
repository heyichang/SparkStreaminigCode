package com.ceiec.graph.util.locationutil;

import com.alibaba.fastjson.JSON;

import com.ceiec.graph.entity.table.location.LocationResponse;
import com.ceiec.graph.entity.table.location.RegistLocationRequst;
import com.ceiec.graph.entity.table.location.ResponseValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoumengcheng on 2017/12/28.
 */
public class RegistLocationHelper {
    private static final Logger logger = LoggerFactory.getLogger(RegistLocationHelper.class);

    public static ResponseValue getRegistResponse(String location){
        RegistLocationRequst registLocationRequst = new RegistLocationRequst("1",location);
        List list = new ArrayList();
        list.add(registLocationRequst);
        long s = System.currentTimeMillis();
        String response = RegistLocationHttpUtils.sendPost(JSON.toJSONString(list));
        long e = System.currentTimeMillis();
        try {
            LocationResponse locationResponse = JSON.parseObject(response, LocationResponse.class);
            if (locationResponse != null && locationResponse.getData() != null) {
                ResponseValue responseValue = locationResponse.getData().get(0);
                return responseValue;
            }
        }catch (Exception e1){
            e1.printStackTrace();
            logger.error("location format is not right");
        }
        return null;
    }
}
