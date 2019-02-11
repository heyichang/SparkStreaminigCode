package com.ceiec.bigdata.util.eventutil;

import com.alibaba.fastjson.JSON;

/**
 * Created by heyichang on 2017/12/26.
 */
public class EventTrans {
    public static String transEventToJson(Object obj){
        return JSON.toJSONString(obj);
    }
}
