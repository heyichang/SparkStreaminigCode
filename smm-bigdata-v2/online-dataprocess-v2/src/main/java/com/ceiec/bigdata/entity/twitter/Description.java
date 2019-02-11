package com.ceiec.bigdata.entity.twitter;

import java.util.List;

/**
 * Created by heyichang on 2017/11/10.
 */
public class Description {
    private List<Urls> urls;

    public void setUrls(List<Urls> urls){
        this.urls = urls;
    }
    public List<Urls> getUrls(){
        return this.urls;
    }

    @Override
    public String toString() {
        return "{" +
                "urls=" + urls +
                '}';
    }
}
