package com.ceiec.graph.entity.table.updatetwitter;


import com.ceiec.graph.entity.User;

/**
 * Created by zoumengcheng on 2018/1/19.
 */
public class UpdateTwitterUser {

    private int id ;
    private User content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getContent() {
        return content;
    }

    public void setContent(User content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "UpdateTwitterUser{" +
                "id=" + id +
                ", content=" + content +
                '}';
    }
}
