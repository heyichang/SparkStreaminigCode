package com.ceiec.graph.entity.facebook;

public class FaceBookRoot {
    private FaceBookPost post;
    private FaceBookUser user;
    private String lang;

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
