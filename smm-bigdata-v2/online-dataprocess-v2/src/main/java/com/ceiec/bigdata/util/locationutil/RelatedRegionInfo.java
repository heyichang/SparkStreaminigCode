package com.ceiec.bigdata.util.locationutil;

import com.alibaba.fastjson.JSON;
import com.ceiec.bigdata.entity.table.location.LocationResponse;
import com.ceiec.bigdata.entity.table.location.RegistLocationRequst;
import com.ceiec.bigdata.entity.table.location.ResponseValue;

import java.util.*;

public class RelatedRegionInfo {
    public static List<Map<String, Object>> getRelatedRegionInfoList(List<String> regions) {
        List<Map<String, Object>> mapList = new ArrayList<>();

        if (regions != null && regions.size() > 0) {
            Set<String> set = new HashSet<>(regions);
            List<String> regionListDistinct = new ArrayList<>(set);
            List<RegistLocationRequst> list = new ArrayList();
            int regionLen = regionListDistinct.size();
            for (int i = 0; i < regionLen; i++) {
                String regionInfo = regionListDistinct.get(i);
                RegistLocationRequst registLocationRequst = new RegistLocationRequst();
                registLocationRequst.setId(String.valueOf(i + 1));
                registLocationRequst.setName(regionInfo);
                list.add(registLocationRequst);
            }
            if (list.size() > 0) {
                String requestStr = JSON.toJSONString(list);
                if (requestStr != null) {
                    String respondStr;
                    try {
                        respondStr = RegistLocationHttpUtils.sendPost(requestStr);
                        LocationResponse locationResponse = JSON.parseObject(respondStr, LocationResponse.class);
                        List<ResponseValue> responseValueList = locationResponse.getData();
                        List<Map<String, Object>> relatedRegionList = new LinkedList<Map<String, Object>>();
                        for (ResponseValue responseValue : responseValueList) {
                            if (responseValue.getCode() != null && !responseValue.getCode().trim().equals("")) {
                                Map<String, Object> regionMap = new HashMap<String, Object>();
                                RegistLocationRequst registLocationRequst = list.get(Integer.valueOf(responseValue.getId()) - 1);
                                regionMap.put("name", registLocationRequst.getName());
                                regionMap.put("region_id", responseValue.getCode());
                                try {
                                    regionMap.put("parent_region_id", responseValue.getCode().substring(0, 3));
                                } catch (Exception e) {
                                    System.err.println("region_id has not 6 bit");
                                }
                                if (regionMap.size() > 0) {
                                    relatedRegionList.add(regionMap);
                                }
                            }
                        }
                        return relatedRegionList;
                    } catch (Exception e) {
                        System.err.println("region id parse error");
                    }
                }
            }
        }
        return mapList;
    }
}
