package com.ceiec.bigdata.entity;

public class SocialMedia {
    private String account_name;
    private String web;

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    @Override
    public String toString() {
        return "SocialMedia{" +
                "account_name='" + account_name + '\'' +
                ", web='" + web + '\'' +
                '}';
    }
}
