package com.ceiec.bigdata.entity.news;

import com.ceiec.bigdata.util.TimeUtils;


/**
 * Created by heyichang on 2017/11/6.
 */
public class News {
    private String url;

    private String crawlid;

    private String raw;

    private String site_name;

    private String create_time;

    private int post_type_id;

    private String lang;

    private Data data;

    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
    public void setCrawlid(String crawlid){
        this.crawlid = crawlid;
    }
    public String getCrawlid(){
        return this.crawlid;
    }
    public void setRaw(String raw){
        this.raw = raw;
    }
    public String getRaw(){
        return this.raw;
    }
    public void setSite_name(String site_name){
        this.site_name = site_name;
    }
    public String getSite_name(){
        return this.site_name;
    }
    public void setCreate_time(String create_time){
        this.create_time = create_time;
    }
    public String getCreate_time(){
        return this.create_time;
    }
    public void setPost_type_id(int post_type_id){
        this.post_type_id = post_type_id;
    }
    public int getPost_type_id(){
        return this.post_type_id;
    }
    public void setLang(String lang){
        this.lang = lang;
    }
    public String getLang(){
        return this.lang;
    }
    public void setData(Data data){
        this.data = data;
    }
    public Data getData(){
        return this.data;
    }


    public String GatherTime = TimeUtils.getTime();
    public String GatherData = TimeUtils.getDate();

    @Override
    public String toString() {
        return "{" +
                "url='" + url + '\'' +
                ", crawlid='" + crawlid + '\'' +
                ", raw='" + raw + '\'' +
                ", site_name='" + site_name + '\'' +
                ", create_time='" + create_time + '\'' +
                ", post_type_id=" + post_type_id +
                ", lang='" + lang + '\'' +
                ", data=" + data +
                '}';
    }
}
