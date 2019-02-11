package com.ceiec.bigdata.entity;

import java.util.List;

public class FacebookUserType {
    private String account_id;
    private String portrait;
    private String account_name;
    private Integer source_type;
    private String nick_name;
    private List<String> other_name;
    private String regist_region;
    private String home_url;
    private Boolean verified;
    private String email;
    private List<String> phone;
    private String introduce;
    private String birthday;
    private String gender;
    private String location;
    private String hometown;
    private List<Family> family;
    private List<SocialMedia> social_account;
    private List<String> websites;
    private List<Language> language;
    private List<Education> education;
    private List<WorkExprience> work;
    private List<Lived> lived;
    private List<String> skills;
    private String createAt;
    private List<EventsEntity> Events;
    private String address;
    private Integer account_type;
    private Long friends_num;
    private Long fans_number;
    private Long likes;

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getFans_number() {
        return fans_number;
    }

    public void setFans_number(Long fans_number) {
        this.fans_number = fans_number;
    }

    public Long getFriends_num() {
        return friends_num;
    }

    public void setFriends_num(Long friends_num) {
        this.friends_num = friends_num;
    }

    public Integer getAccount_type() {
        return account_type;
    }

    public void setAccount_type(Integer account_type) {
        this.account_type = account_type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public Integer getSource_type() {
        return source_type;
    }

    public void setSource_type(Integer source_type) {
        this.source_type = source_type;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public List<String> getOther_name() {
        return other_name;
    }

    public void setOther_name(List<String> other_name) {
        this.other_name = other_name;
    }

    public String getRegist_region() {
        return regist_region;
    }

    public void setRegist_region(String regist_region) {
        this.regist_region = regist_region;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
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

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public List<Family> getFamily() {
        return family;
    }

    public void setFamily(List<Family> family) {
        this.family = family;
    }

    public List<SocialMedia> getSocial_account() {
        return social_account;
    }

    public void setSocial_account(List<SocialMedia> social_account) {
        this.social_account = social_account;
    }

    public List<String> getWebsites() {
        return websites;
    }

    public void setWebsites(List<String> websites) {
        this.websites = websites;
    }

    public List<Language> getLanguage() {
        return language;
    }

    public void setLanguage(List<Language> language) {
        this.language = language;
    }

    public List<Education> getEducation() {
        return education;
    }

    public void setEducation(List<Education> education) {
        this.education = education;
    }

    public List<WorkExprience> getWork() {
        return work;
    }

    public void setWork(List<WorkExprience> work) {
        this.work = work;
    }

    public List<Lived> getLived() {
        return lived;
    }

    public void setLived(List<Lived> lived) {
        this.lived = lived;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public List<EventsEntity> getEvents() {
        return Events;
    }

    public void setEvents(List<EventsEntity> events) {
        Events = events;
    }

    @Override
    public String toString() {
        return "AllUserType{" +
                "account_id='" + account_id + '\'' +
                ", portrait='" + portrait + '\'' +
                ", account_name='" + account_name + '\'' +
                ", source_type=" + source_type +
                ", nick_name='" + nick_name + '\'' +
                ", other_name=" + other_name +
                ", regist_region='" + regist_region + '\'' +
                ", home_url='" + home_url + '\'' +
                ", verified='" + verified + '\'' +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                ", introduce='" + introduce + '\'' +
                ", birthday='" + birthday + '\'' +
                ", gender='" + gender + '\'' +
                ", location='" + location + '\'' +
                ", hometown='" + hometown + '\'' +
                ", family=" + family +
                ", socialmedia_account=" + social_account +
                ", websites=" + websites +
                ", language=" + language +
                ", education=" + education +
                ", work=" + work +
                ", lived=" + lived +
                ", skills=" + skills +
                ", createAt='" + createAt + '\'' +
                ", Events=" + Events +
                '}';
    }
}
