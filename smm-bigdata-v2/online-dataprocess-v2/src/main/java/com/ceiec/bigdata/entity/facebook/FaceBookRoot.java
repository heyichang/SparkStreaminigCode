package com.ceiec.bigdata.entity.facebook;

public class FaceBookRoot {
    private FaceBookPost post;
    private FaceBookUser user;
    private String lang;
    private String crawled_time;

    public String getCrawled_time() {
        return crawled_time;
    }

    public void setCrawled_time(String crawled_time) {
        this.crawled_time = crawled_time;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public FaceBookPost getPost() {
        return post;
    }

    public void setPost(FaceBookPost post) {
        this.post = post;
    }

    public FaceBookUser getUser() {
        return user;
    }

    public void setUser(FaceBookUser user) {
        this.user = user;
    }
}
