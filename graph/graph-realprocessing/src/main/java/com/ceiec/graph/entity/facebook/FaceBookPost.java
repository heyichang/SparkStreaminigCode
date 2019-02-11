package com.ceiec.graph.entity.facebook;

import java.util.List;

public class FaceBookPost {
    private String fb_id;
    private String post_id;
    private Long post_ts;
    private long updated_ts;
    private String parent_fb_id;
    private String parent_post_id;
    private String message;
    private String description;
    private String picture;
    private String full_picture;
    private FaceBookPlace place;
    private String story;
    private String video;
    private List<StoryTags> story_tags;
    private int likes;
    private int comments;
    private int shares;

    public FaceBookPlace getPlace() {
        return place;
    }

    public void setPlace(FaceBookPlace place) {
        this.place = place;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getFb_id() {
        return fb_id;
    }

    public void setFb_id(String fb_id) {
        this.fb_id = fb_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public Long getPost_ts() {
        return post_ts;
    }

    public void setPost_ts(Long post_ts) {
        this.post_ts = post_ts;
    }

    public long getUpdated_ts() {
        return updated_ts;
    }

    public void setUpdated_ts(long updated_ts) {
        this.updated_ts = updated_ts;
    }

    public String getParent_fb_id() {
        return parent_fb_id;
    }

    public void setParent_fb_id(String parent_fb_id) {
        this.parent_fb_id = parent_fb_id;
    }

    public String getParent_post_id() {
        return parent_post_id;
    }

    public void setParent_post_id(String parent_post_id) {
        this.parent_post_id = parent_post_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getFull_picture() {
        return full_picture;
    }

    public void setFull_picture(String full_picture) {
        this.full_picture = full_picture;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public List<StoryTags> getStory_tags() {
        return story_tags;
    }

    public void setStory_tags(List<StoryTags> story_tags) {
        this.story_tags = story_tags;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }
}
