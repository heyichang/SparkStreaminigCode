package com.ceiec.bigdata.entity.table.updatetwitter;

import com.ceiec.bigdata.entity.twitter.User;

/**
 * Created by heyichang on 2018/1/19.
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
