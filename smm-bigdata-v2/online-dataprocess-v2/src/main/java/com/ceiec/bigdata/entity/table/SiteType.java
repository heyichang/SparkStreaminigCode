package com.ceiec.bigdata.entity.table;


/**
 * Created by heyichang on 2017/12/4.
 */
public class SiteType  {



    private int site_id;
    private String name;
    private int site_type_id;
    private String url;

    public int getSite_id() {
        return site_id;
    }

    public void setSite_id(int site_id) {
        this.site_id = site_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSite_type_id() {
        return site_type_id;
    }

    public void setSite_type_id(int site_type_id) {
        this.site_type_id = site_type_id;
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
                "site_id=" + site_id +
                ", name='" + name + '\'' +
                ", site_type_id=" + site_type_id +
                ", url='" + url + '\'' +
                '}';
    }
}
