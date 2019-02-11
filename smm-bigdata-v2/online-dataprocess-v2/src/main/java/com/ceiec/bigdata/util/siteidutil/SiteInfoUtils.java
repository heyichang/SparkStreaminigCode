package com.ceiec.bigdata.util.siteidutil;

import com.alibaba.fastjson.JSON;
import com.ceiec.bigdata.entity.table.RegionInfo;
import com.ceiec.bigdata.entity.table.SiteInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heyichang on 2017/12/4.
 */
public class SiteInfoUtils {

    public static Map<String ,List<Object>> getSiteTypeId(List<String> stringLists){
        Map<String ,List<Object>> map = new HashMap<>();
        for (String siteInfoString :stringLists) {
            try {
                SiteInfo siteInfo = JSON.parseObject(siteInfoString,SiteInfo.class);
                List<Object> integerList = new ArrayList<>();
                //添加siteid
                integerList.add(siteInfo.getSite_id());
                //添加sitetypeid
                integerList.add(siteInfo.getSite_type_id());
                //添加region_id
                integerList.add(siteInfo.getRegion_id());
                //添加lang
                integerList.add(siteInfo.getLang());
                  map.put(siteInfo.getUrl(),integerList);
            }catch (Exception e){
                System.err.println("SiteInfo parse error : "+siteInfoString);
            }
        }
        return map;
    }

    public static Map<String ,List<String>> getRegionInfoMap(List<String> stringLists){
        Map<String ,List<String>> map = new HashMap<>();
        for (String siteInfoString :stringLists) {
            try {
                RegionInfo regionInfo = JSON.parseObject(siteInfoString,RegionInfo.class);
                List<String> integerList = new ArrayList<>();
                //添加Parent_region_id
                integerList.add(regionInfo.getParent_region_id());
                //添加Region_id
                integerList.add(regionInfo.getRegion_id());
                map.put(regionInfo.getFull_name(),integerList);
            }catch (Exception e){
                System.err.println("SiteInfo parse error : "+siteInfoString);
            }
        }

        return map;
    }





}
