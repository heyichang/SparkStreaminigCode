package com.ceiec.graph.entity.neo4jTwitter;

/**
 * @author:heyichang
 * @description:关系信息
 * @date:Created in 11:32 2018-03-14
 */
public class RelatedInfo {
    private String type;
    private String user_screen_name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_screen_name() {
        return user_screen_name;
    }

    public void setUser_screen_name(String user_screen_name) {
        this.user_screen_name = user_screen_name;
    }
}
