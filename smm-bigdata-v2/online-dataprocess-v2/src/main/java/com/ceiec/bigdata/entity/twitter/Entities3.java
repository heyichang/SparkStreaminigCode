package com.ceiec.bigdata.entity.twitter;

import java.util.List;

/**
 * Created by heyichang on 2017/11/10.
 */
public class Entities3 {
    private List<String> hashtags;

    private List<String> symbols;

    private List<String> user_mentions;

    private List<Urls> urls;

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
    public void setUrls(List<Urls> urls){
        this.urls = urls;
    }
    public List<Urls> getUrls(){
        return this.urls;
    }

    @Override
    public String toString() {
        return "{" +
                "hashtags=" + hashtags +
                ", symbols=" + symbols +
                ", user_mentions=" + user_mentions +
                ", urls=" + urls +
                '}';
    }
}
