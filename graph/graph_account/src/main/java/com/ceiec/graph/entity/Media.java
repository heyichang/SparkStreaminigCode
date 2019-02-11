package com.ceiec.graph.entity;

import java.util.List;

/**
 * Created by thinkpad on 2017/11/28.
 */
public class Media {
    private Long id;

    private List<Integer> indices;

    private String type;

    private String expanded_url;

    private String id_str;

    private String media_url;

    private String display_url;

    private Sizes sizes;

    private String media_url_https;

    private String url;

    public void setId(Long id){
        this.id = id;
    }
    public Long getId(){
        return this.id;
    }
    public void setIndices(List<Integer> indices){
        this.indices = indices;
    }
    public List<Integer> getIndices(){
        return this.indices;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setExpanded_url(String expanded_url){
        this.expanded_url = expanded_url;
    }
    public String getExpanded_url(){
        return this.expanded_url;
    }
    public void setId_str(String id_str){
        this.id_str = id_str;
    }
    public String getId_str(){
        return this.id_str;
    }
    public void setMedia_url(String media_url){
        this.media_url = media_url;
    }
    public String getMedia_url(){
        return this.media_url;
    }
    public void setDisplay_url(String display_url){
        this.display_url = display_url;
    }
    public String getDisplay_url(){
        return this.display_url;
    }
    public void setSizes(Sizes sizes){
        this.sizes = sizes;
    }
    public Sizes getSizes(){
        return this.sizes;
    }
    public void setMedia_url_https(String media_url_https){
        this.media_url_https = media_url_https;
    }
    public String getMedia_url_https(){
        return this.media_url_https;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", indices=" + indices +
                ", type='" + type + '\'' +
                ", expanded_url='" + expanded_url + '\'' +
                ", id_str='" + id_str + '\'' +
                ", media_url='" + media_url + '\'' +
                ", display_url='" + display_url + '\'' +
                ", sizes=" + sizes +
                ", media_url_https='" + media_url_https + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
