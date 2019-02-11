package com.ceiec.graph.util.locationutil;//package com.ceiec.graph.util.locationutil;

import com.alibaba.fastjson.JSON;
import com.ceiec.graph.entity.table.location.RegistLocationRequst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zoumengcheng on 2017/12/4.
 */
public class LocationUtils {
    private static final Logger logger = LoggerFactory.getLogger(LocationUtils.class);

    /**
     * 通过地理位置名称，获取GIS code
     * @param name
     * @return code
     */
    public static String getGisCode(String name){
        RegistLocationRequst registLocationRequst = new RegistLocationRequst();
        registLocationRequst.setId("1");
        registLocationRequst.setName(name);
        List list = new ArrayList();
        list.add(registLocationRequst);
        String str = JSON.toJSONString(list);
        String re = RegistLocationHttpUtils.sendPost(str);
        return JSON.parseObject(re).getJSONArray("data").getJSONObject(0).getString("code");
    }

    public static String getParentCode(String name){
        RegistLocationRequst registLocationRequst = new RegistLocationRequst();
        registLocationRequst.setId("1");
        registLocationRequst.setName(name);
        List list = new ArrayList();
        list.add(registLocationRequst);
        String str = JSON.toJSONString(list);
        String re = RegistLocationHttpUtils.sendPost(str);
        String rigCode =  JSON.parseObject(re).getJSONArray("data").getJSONObject(0).getString("code");
        String parentCode = rigCode.substring(0,3);
        return parentCode;
    }

    public static void main(String[] args) {
        System.out.println(LocationUtils.getGisCode("Beijing, China"));
        System.out.println(LocationUtils.getParentCode("Beijing, China"));
    }
}
