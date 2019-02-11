package com.ceiec.bigdata.util.siteidutil;

import com.ceiec.bigdata.entity.AllNewsPost;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SideInfoJudgement {
    public static boolean siteIdIsExists(String source_url, Map<String, List<Object>> siteMap) {
        if (source_url != null) {
            String urlDomain = UrlFilter.filterUrl(source_url);//获取url主域名
            /*获取站点信息*/
            if (urlDomain != null && siteMap != null) {
                Iterator<Map.Entry<String, List<Object>>> it = siteMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, List<Object>> entry = it.next();
                    if (entry.getKey().contains(urlDomain) || urlDomain.contains(entry.getKey())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
