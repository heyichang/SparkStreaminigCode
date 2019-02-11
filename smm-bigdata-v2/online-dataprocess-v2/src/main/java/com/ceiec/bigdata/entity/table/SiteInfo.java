package com.ceiec.bigdata.entity.table;

/**
 * Created by heyichang on 2017/12/1.
 */
public class SiteInfo {
    private Integer site_id;
    private Integer site_type_id;
    private String name;
    private String url;
    private String region_id;
    private String lang;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public Integer getSite_id() {
        return site_id;
    }

    public void setSite_id(int site_id) {
        this.site_id = site_id;
    }

    public Integer getSite_type_id() {
        return site_type_id;
    }

    public void setSite_type_id(int site_type_id) {
        this.site_type_id = site_type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "{" +
                "\"site_id\"" +":"+ site_id +
                ",\"site_type_id\""+":" + site_type_id +
                ",\"name\""+":" +"\""+ name+"\"" +
                ", \"url\""+":" + "\""+url+"\""  +
                ", \"region_id\""+":" + "\""+region_id+"\""  +
                ", \"lang\""+":" + "\""+lang+"\""  +
                "}";
    }


}
