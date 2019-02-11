package com.ceiec.bigdata.entity;

import java.util.List;

public class AllFacebookPost {
    private String portrait;
    private String account_name;
    private Integer source_type;
    private String nick_name;
    private String source_url;
    private String creating_time;
    private Integer remark_number;
    private Integer retweet_number;
    private Integer like_number;
    private String location;
    private String content;
    private List<String> img;
    private List<String> video;
    private List<String> Links;
    private Integer type;
    private String  parent_source_url;
    private String crawled_time;
    private String account_id;
    private String hometown;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getCrawled_time() {
        return crawled_time;
    }

    public void setCrawled_time(String crawled_time) {
        this.crawled_time = crawled_time;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public Integer getSource_type() {
        return source_type;
    }

    public void setSource_type(Integer source_type) {
        this.source_type = source_type;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public String getCreating_time() {
        return creating_time;
    }

    public void setCreating_time(String creating_time) {
        this.creating_time = creating_time;
    }

    public Integer getRemark_number() {
        return remark_number;
    }

    public void setRemark_number(Integer remark_number) {
        this.remark_number = remark_number;
    }

    public Integer getRetweet_number() {
        return retweet_number;
    }

    public void setRetweet_number(Integer retweet_number) {
        this.retweet_number = retweet_number;
    }

    public Integer getLike_number() {
        return like_number;
    }

    public void setLike_number(Integer like_number) {
        this.like_number = like_number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImg() {
        return img;
    }

    public void setImg(List<String> img) {
        this.img = img;
    }

    public List<String> getVideo() {
        return video;
    }

    public void setVideo(List<String> video) {
        this.video = video;
    }

    public List<String> getLinks() {
        return Links;
    }

    public void setLinks(List<String> links) {
        Links = links;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getParent_source_url() {
        return parent_source_url;
    }

    public void setParent_source_url(String parent_source_url) {
        this.parent_source_url = parent_source_url;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    @Override
    public String toString() {
        return "AllFacebookPost{" +
                "portrait='" + portrait + '\'' +
                ", account_name='" + account_name + '\'' +
                ", source_type=" + source_type +
                ", nick_name='" + nick_name + '\'' +
                ", source_url='" + source_url + '\'' +
                ", creating_time='" + creating_time + '\'' +
                ", remark_number=" + remark_number +
                ", retweet_number=" + retweet_number +
                ", like_number=" + like_number +
                ", location='" + location + '\'' +
                ", content='" + content + '\'' +
                ", img=" + img +
                ", video=" + video +
                ", Links=" + Links +
                ", type=" + type +
                ", parent_source_url='" + parent_source_url + '\'' +
                ", crawled_time='" + crawled_time + '\'' +
                ", account_id='" + account_id + '\'' +
                ", hometown='" + hometown + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
