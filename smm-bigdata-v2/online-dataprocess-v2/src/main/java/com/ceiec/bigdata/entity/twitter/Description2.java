package com.ceiec.bigdata.entity.twitter;

import java.util.List;

/**
 * Created by heyichang on 2017/11/10.
 */
public class Description2 {
    private List<String> urls;

    public void setUrls(List<String> urls){
        this.urls = urls;
    }
    public List<String> getUrls(){
        return this.urls;
    }

    @Override
    public String toString() {
        return "{" +
                "urls=" + urls +
                '}';
    }
}
