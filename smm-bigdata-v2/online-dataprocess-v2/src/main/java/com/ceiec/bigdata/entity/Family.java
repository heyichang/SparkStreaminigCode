package com.ceiec.bigdata.entity;

public class Family {
    private String nick_name;
    private String account_name;
    private String relation;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public String toString() {
        return "Family{" +
                "nick_name='" + nick_name + '\'' +
                ", account_name='" + account_name + '\'' +
                ", relation='" + relation + '\'' +
                '}';
    }
}
