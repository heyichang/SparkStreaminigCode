package com.ceiec.graph.entity;

import java.util.List;

/**
 * Created by zoumengcheng on 2017/11/10.
 */
public class Entities2 {
    private List<String> hashtags;

    private List<String> symbols;

    private List<String> user_mentions;

    private List<String> urls;

    private List<Media> media;

    public void setHashtags(List<String> hashtags){
        this.hashtags = hashtags;
    }
    public List<String> getHashtags(){
        return this.hashtags;
    }
    public void setSymbols(List<String> symbols){
        this.symbols = symbols;
    }
    public List<String> getSymbols(){
        return this.symbols;
    }
    public void setUser_mentions(List<String> user_mentions){
        this.user_mentions = user_mentions;
    }
    public List<String> getUser_mentions(){
        return this.user_mentions;
    }
    public void setUrls(List<String> urls){
        this.urls = urls;
    }
    public List<String> getUrls(){
        return this.urls;
    }
    public void setMedia(List<Media> media){
        this.media = media;
    }
    public List<Media> getMedia(){
        return this.media;
    }
}
