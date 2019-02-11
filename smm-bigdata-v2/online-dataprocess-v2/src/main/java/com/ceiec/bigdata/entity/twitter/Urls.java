package com.ceiec.bigdata.entity.twitter;

import java.util.List;

/**
 * Created by heyichang on 2017/11/10.
 */
public class Urls {

    private String url;

    private String expanded_url;

    private String display_url;

    private List<Long> indices;


    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
    public void setExpanded_url(String expanded_url){
        this.expanded_url = expanded_url;
    }
    public String getExpanded_url(){
        return this.expanded_url;
    }
    public void setDisplay_url(String display_url){
        this.display_url = display_url;
    }
    public String getDisplay_url(){
        return this.display_url;
    }
    public void setIndices(List<Long> indices){
        this.indices = indices;
    }
    public List<Long> getIndices(){
        return this.indices;
    }

    @Override
    public String toString() {
        return "{" +
                "url='" + url + '\'' +
                ", expanded_url='" + expanded_url + '\'' +
                ", display_url='" + display_url + '\'' +
                ", indices=" + indices +
                '}';
    }
}
