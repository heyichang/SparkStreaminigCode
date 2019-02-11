package com.ceiec.bigdata.entity.twitter;

/**
 * Created by heyichang on 2017/12/22.
 */
public class Variants {
    private String url;

    private String content_type;

    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
    public void setContent_type(String content_type){
        this.content_type = content_type;
    }
    public String getContent_type(){
        return this.content_type;
    }

    @Override
    public String toString() {
        return "Variants{" +
                "url='" + url + '\'' +
                ", content_type='" + content_type + '\'' +
                '}';
    }
}
