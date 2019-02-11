package com.ceiec.graph.entity.facebook;

import java.util.List;

public class FacebookUser {
    private String account_id;
    private String portrait;
    private String account_name;
    private String account_type;
    private String nick_name;
    private String other_name;
    private String regist_region;
    private String home_url;
    private Boolean verified;
    private List<String> email;
    private List<String> phone;
    private String introduce;
    private String birthday;
    private String gender;
    private String location;
    private String address;
    private String hometown;
    private List<FacebookFamilies> family;
    private List<FacebookSocialAccount> social_account;
    private List<String> websites;
    private List<FacebookLanguage> language;
    private List<FacebookEducation> education;
    private List<FacebookWork> work;
    private List<FacebookLived> lived;
    private List<String> skills;
    private String createAt;
    private List<FacebookEvents> Events;

    //补齐几个字段
    private int fans_number;
    private int follow_number;
    private int likes;

    @Override
    public String toString() {
        return "FacebookUser{" +
                "account_id='" + account_id + '\'' +
                ", portrait='" + portrait + '\'' +
                ", account_name='" + account_name + '\'' +
                ", account_type='" + account_type + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", other_name='" + other_name + '\'' +
                ", home_url='" + home_url + '\'' +
                ", verified=" + verified +
                ", email=" + email +
                ", phone=" + phone +
                ", introduce='" + introduce + '\'' +
                ", birthday='" + birthday + '\'' +
                ", gender='" + gender + '\'' +
                ", location='" + location + '\'' +
                ", address='" + address + '\'' +
                ", hometown='" + hometown + '\'' +
                ", family=" + family +
                ", social_account=" + social_account +
                ", websites=" + websites +
                ", language=" + language +
                ", education=" + education +
                ", work=" + work +
                ", lived=" + lived +
                ", skills=" + skills +
                ", events=" + events +
                '}';
    }

    public int getFans_number() {
        return fans_number;
    }

    public void setFans_number(int fans_number) {
        this.fans_number = fans_number;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getFollow_number() {
        return follow_number;
    }

    public void setFollow_number(int follow_number) {
        this.follow_number = follow_number;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
    public String getRegist_region() {
        return regist_region;
    }

    public void setRegist_region(String regist_region) {
        this.regist_region = regist_region;
    }
    public List<FacebookWork> getWork() {
        return work;
    }

    public void setWork(List<FacebookWork> work) {
        this.work = work;
    }

    private List<FacebookEvents> events;

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_type() {
        return account_type;
    }

    public void setAccount_type(String account_type) {
        this.account_type = account_type;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getOther_name() {
        return other_name;
    }

    public void setOther_name(String other_name) {
        this.other_name = other_name;
    }

    public String getHome_url() {
        return home_url;
    }

    public void setHome_url(String home_url) {
        this.home_url = home_url;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public List<FacebookFamilies> getFamily() {
        return family;
    }

    public void setFamily(List<FacebookFamilies> family) {
        this.family = family;
    }

    public List<FacebookSocialAccount> getSocial_account() {
        return social_account;
    }

    public void setSocial_account(List<FacebookSocialAccount> social_account) {
        this.social_account = social_account;
    }

    public List<String> getWebsites() {
        return websites;
    }

    public void setWebsites(List<String> websites) {
        this.websites = websites;
    }

    public List<FacebookLanguage> getLanguage() {
        return language;
    }

    public void setLanguage(List<FacebookLanguage> language) {
        this.language = language;
    }

    public List<FacebookEducation> getEducation() {
        return education;
    }

    public void setEducation(List<FacebookEducation> education) {
        this.education = education;
    }

    public List<FacebookLived> getLived() {
        return lived;
    }

    public void setLived(List<FacebookLived> lived) {
        this.lived = lived;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<FacebookEvents> getEvents() {
        return events;
    }

    public void setEvents(List<FacebookEvents> events) {
        this.events = events;
    }
}
