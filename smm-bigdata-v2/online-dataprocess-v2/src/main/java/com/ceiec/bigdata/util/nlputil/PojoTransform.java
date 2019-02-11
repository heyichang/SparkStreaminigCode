package com.ceiec.bigdata.util.nlputil;

import com.alibaba.fastjson.JSON;
import com.ceiec.bigdata.entity.table.nlp.NlpPojo;

/**
 * Created by heyichang on 2018/3/20.
 */
public class PojoTransform {
    public static String pojoToString(NlpPojo nlpPojo){
               String pojoString =  JSON.toJSONString(nlpPojo);
               return pojoString;
    }
}
