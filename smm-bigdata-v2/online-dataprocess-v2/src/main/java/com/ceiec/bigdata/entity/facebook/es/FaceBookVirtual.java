package com.ceiec.bigdata.entity.facebook.es;

import com.alibaba.fastjson.JSON;
import com.ceiec.bigdata.entity.facebook.FaceBookLocation;
import com.ceiec.bigdata.entity.facebook.FaceBookRoot;
import com.ceiec.bigdata.entity.facebook.FaceBookUser;
import com.ceiec.bigdata.entity.table.location.ResponseValue;
import com.ceiec.bigdata.util.TimeUtils;
import com.ceiec.bigdata.util.locationutil.RegistLocationHelper;

import java.util.List;
import java.util.Map;

public class FaceBookVirtual {
    private String account_id;
    private String portrait;
    private String account_name;
    private int site_id;
    private String nick_name;
    private Integer regist_region_id;
    private String home;
    private Integer post_number;
    private Long fans_number;
    private Integer follow_number;
    private Integer twitted_number;
    private Integer negative_number;
    private Integer sensitive_number;
    private Integer is_monitor;
    private String email;
    private String phone;
    private String follow_list;
    private String friends_list;
    private Integer friends_num;
    private String introduce;
    private String region_id_str;
    private String parent_region_id_str;
    private String regist_region_id_str;
    private String parent_regist_region_id_str;
    private Boolean verified;
    private Integer site_type_id;

    private String birthday;
    private String gendar;
    private String hometown;
    private String hometown_str;
    private String timezone;
    private List<Map<String, Object>> language;
    private List<Map<String, Object>> education;
    private List<Map<String, Object>> work;
    private String time;
    private List<Map<String, Object>> fans;
    private String website;
    private String createAt;
    private String location;
    private String timezone_str;
    private String likes;

    public FaceBookVirtual() {
    }

    public FaceBookVirtual(FaceBookRoot faceBookRoot, FaceBookInfo faceBookInfo) {
        FaceBookUser user = faceBookRoot.getUser();
        if (user != null) {
            this.account_id = faceBookInfo.getAccount_id();

            //this.sensitive_number = esInfo.getSensitiveId();
            this.site_id = 302;
            this.site_type_id = 2;
            // this.regist_region_id = esInfo.getRegionId();
            this.account_name = user.getUser_name();
            this.nick_name = user.getNick_name();
            //this.post_number = faceBookRoot.getPost().getShares();
            this.fans_number = user.getFans();
            this.portrait = user.getPortrait();
            this.time = TimeUtils.getTime();
            if (user.getEmails() != null && user.getEmails().size() > 0)
            {
                this.email = user.getEmails().get(0);
            }
            if(user.getVerified() != null && user.getVerified().booleanValue()){
                this.verified = user.getVerified();
            }else {
                this.verified = true;
            }


            this.website = user.getWebsite();

            //  this.timezone = user.getTime_zone();

            this.time = faceBookInfo.getGather_time();

            if (user.getHome_page() != null) {
                this.home = user.getHome_page();
            } else {
                this.home = "http://www.facebook.com/" + faceBookRoot.getPost().getFb_id();
            }


            this.introduce = user.getAbout();
            if (user.getLocation() != null) {
                FaceBookLocation fbLocation = user.getLocation();
                String userLocation = JSON.toJSONString(user.getLocation());
                this.location = userLocation;
                if (fbLocation.getCity() != null) {
                    ResponseValue responseValue = RegistLocationHelper.getRegistResponse(fbLocation.getCity());
                    if (responseValue != null && responseValue.getCode() != null && !responseValue.getCode().equals("N")) {
                        this.parent_regist_region_id_str = responseValue.getCode().substring(0, 3);//得到父行政区域位置id
                        this.regist_region_id_str = responseValue.getCode();//得到该行政区域位置id
                        this.hometown_str = regist_region_id_str;
                    }
                }

            }

        }

    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
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

    public int getSite_id() {
        return site_id;
    }

    public void setSite_id(int site_id) {
        this.site_id = site_id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public Integer getRegist_region_id() {
        return regist_region_id;
    }

    public void setRegist_region_id(Integer regist_region_id) {
        this.regist_region_id = regist_region_id;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public Integer getPost_number() {
        return post_number;
    }

    public void setPost_number(Integer post_number) {
        this.post_number = post_number;
    }

    public Long getFans_number() {
        return fans_number;
    }

    public void setFans_number(Long fans_number) {
        this.fans_number = fans_number;
    }

    public Integer getFollow_number() {
        return follow_number;
    }

    public void setFollow_number(Integer follow_number) {
        this.follow_number = follow_number;
    }

    public Integer getTwitted_number() {
        return twitted_number;
    }

    public void setTwitted_number(Integer twitted_number) {
        this.twitted_number = twitted_number;
    }

    public Integer getNegative_number() {
        return negative_number;
    }

    public void setNegative_number(Integer negative_number) {
        this.negative_number = negative_number;
    }

    public Integer getSensitive_number() {
        return sensitive_number;
    }

    public void setSensitive_number(Integer sensitive_number) {
        this.sensitive_number = sensitive_number;
    }

    public Integer getIs_monitor() {
        return is_monitor;
    }

    public void setIs_monitor(Integer is_monitor) {
        this.is_monitor = is_monitor;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFollow_list() {
        return follow_list;
    }

    public void setFollow_list(String follow_list) {
        this.follow_list = follow_list;
    }

    public String getFriends_list() {
        return friends_list;
    }

    public void setFriends_list(String friends_list) {
        this.friends_list = friends_list;
    }

    public Integer getFriends_num() {
        return friends_num;
    }

    public void setFriends_num(Integer friends_num) {
        this.friends_num = friends_num;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getRegion_id_str() {
        return region_id_str;
    }

    public void setRegion_id_str(String region_id_str) {
        this.region_id_str = region_id_str;
    }

    public String getParent_region_id_str() {
        return parent_region_id_str;
    }

    public void setParent_region_id_str(String parent_region_id_str) {
        this.parent_region_id_str = parent_region_id_str;
    }

    public String getRegist_region_id_str() {
        return regist_region_id_str;
    }

    public void setRegist_region_id_str(String regist_region_id_str) {
        this.regist_region_id_str = regist_region_id_str;
    }

    public String getParent_regist_region_id_str() {
        return parent_regist_region_id_str;
    }

    public void setParent_regist_region_id_str(String parent_regist_region_id_str) {
        this.parent_regist_region_id_str = parent_regist_region_id_str;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Integer getSite_type_id() {
        return site_type_id;
    }

    public void setSite_type_id(Integer site_type_id) {
        this.site_type_id = site_type_id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGendar() {
        return gendar;
    }

    public void setGendar(String gendar) {
        this.gendar = gendar;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getHometown_str() {
        return hometown_str;
    }

    public void setHometown_str(String hometown_str) {
        this.hometown_str = hometown_str;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public List<Map<String, Object>> getLanguage() {
        return language;
    }

    public void setLanguage(List<Map<String, Object>> language) {
        this.language = language;
    }

    public List<Map<String, Object>> getEducation() {
        return education;
    }

    public void setEducation(List<Map<String, Object>> education) {
        this.education = education;
    }

    public List<Map<String, Object>> getWork() {
        return work;
    }

    public void setWork(List<Map<String, Object>> work) {
        this.work = work;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Map<String, Object>> getFans() {
        return fans;
    }

    public void setFans(List<Map<String, Object>> fans) {
        this.fans = fans;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimezone_str() {
        return timezone_str;
    }

    public void setTimezone_str(String timezone_str) {
        this.timezone_str = timezone_str;
    }
}
