package com.ceiec.bigdata.entity.twitter;

import java.util.List;

/**
 * Created by heyichang on 2017/11/10.
 */
public class Entities1 {
    private List<HashtagsInside> hashtags;

    private List<String> symbols;

    private List<UserMentions> user_mentions;

    private List<Urls> urls;

    private List<Media> media;

    public void setHashtags(List<HashtagsInside> hashtags) {
        this.hashtags = hashtags;
    }

    public List<HashtagsInside> getHashtags() {
        return this.hashtags;
    }

    public void setSymbols(List<String> symbols) {
        this.symbols = symbols;
    }

    public List<String> getSymbols() {
        return this.symbols;
    }

    public void setUser_mentions(List<UserMentions> user_mentions) {
        this.user_mentions = user_mentions;
    }

    public List<UserMentions> getUser_mentions() {
        return this.user_mentions;
    }

    public void setUrls(List<Urls> urls) {
        this.urls = urls;
    }

    public List<Urls> getUrls() {
        return this.urls;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    @Override
    public String toString() {
        return "Entities1{" +
                "hashtags=" + hashtags +
                ", symbols=" + symbols +
                ", user_mentions=" + user_mentions +
                ", urls=" + urls +
                ", media=" + media +
                '}';
    }
}
