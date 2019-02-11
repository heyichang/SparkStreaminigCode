package com.ceiec.bigdata.entity.facebook.es;

import com.ceiec.bigdata.entity.*;
import com.ceiec.bigdata.entity.table.location.RegistLocationRequst;
import com.ceiec.bigdata.entity.table.location.ResponseValue;
import com.ceiec.bigdata.util.EntityFieldUtils;
import com.ceiec.bigdata.util.InfoIdUtils;
import com.ceiec.bigdata.util.ObjectUtils;
import com.ceiec.bigdata.util.TimeUtils;
import com.ceiec.bigdata.util.locationutil.RegistLocationHelper;

import java.util.*;

public class FaceBookUserCommon {
    private String account_id;
    private String portrait;
    private String account_name;
    private Integer site_id;
    private Integer site_type_id;
    private String desc;
    private String nick_name;
    private String other_name;
    private String regist_region_id;
    private String parent_regist_region_id;
    private String location;
    private Long likes;
    private String home;
    private Boolean verified;
    private Long fans_number;
    private Long follow_number;
    private String email;
    private String phone;
    private Long friends_num;
    private String introduce;
    private String birthday;
    private String gender;
    private String hometown;
    private String timezone_str;
    private List<Map<String, Object>> interest;
    private List<String> ability;
    private List<Map<String, Object>> family;
    private List<Map<String, Object>> socialmedia_account;
    private List<Map<String, Object>> websites;
    private List<Map<String, Object>> language;
    private List<Map<String, Object>> education;
    private List<Map<String, Object>> work;
    private List<Map<String, Object>> lived;
    private List<Map<String, Object>> events;
    private List<Map<String, Object>> skills;
    private String time;
    private String create_at;
    private String timezone;
    private List<Map<String, Object>> fans;
    private String address;
    private Integer account_type;

    public FaceBookUserCommon() {
    }

    public FaceBookUserCommon(FacebookUserType facebookUserType) {
        if (facebookUserType != null) {
            //生成account_id
            if (facebookUserType.getAccount_id() != null) {
                StringBuilder homeUrl = new StringBuilder();
                homeUrl.append("http://www.facebook.com/").append(facebookUserType.getAccount_id());
                this.account_id = InfoIdUtils.generate32MD5ID(homeUrl.toString());
            }
            this.portrait = facebookUserType.getPortrait();
            this.hometown = facebookUserType.getHometown();
            //this.sensitive_number = esInfo.getSensitiveId();
            this.site_id = 302;
            this.site_type_id = 2;
            // this.regist_region_id = esInfo.getRegionId();
            this.account_name = facebookUserType.getAccount_name();
            this.nick_name = facebookUserType.getNick_name();
            if(facebookUserType.getOther_name() != null && facebookUserType.getOther_name().size() > 0){
                this.other_name = facebookUserType.getOther_name().toString();
            }
            this.create_at = facebookUserType.getCreateAt();
            this.address = facebookUserType.getAddress();
            this.account_type = facebookUserType.getAccount_type();
            this.friends_num = facebookUserType.getFriends_num();
            this.fans_number = facebookUserType.getFans_number();
            this.likes = facebookUserType.getLikes();
            /**获得新闻创建时间**/
            if (ObjectUtils.isNotNull(facebookUserType.getCreateAt())) {
                this.create_at = TimeUtils.stampToTime(facebookUserType.getCreateAt());//得到推文创建时间yy-mm-dd hh:mm:ss
            }
            //获取注册地理信息
            if (facebookUserType.getLocation() != null) {
                ResponseValue responseValue = RegistLocationHelper.getRegistResponse(facebookUserType.getLocation());
                if (responseValue != null && responseValue.getCode() != null && !responseValue.getCode().equals("N")) {
                    this.parent_regist_region_id = responseValue.getCode().substring(0, 3);//得到父行政区域位置id
                    this.regist_region_id = responseValue.getCode();//得到该行政区域位置id
                    if(hometown == null){
                        this.hometown = regist_region_id;
                    }

                }
            }
//            this.location 账号信息时写入
//            this.likes 账号信息时写入
            this.home = facebookUserType.getHome_url();
            this.verified = facebookUserType.getVerified();
//            this.fans_number 账号信息时写入
//            this.follow_number 账号信息时写入
            this.email = facebookUserType.getEmail();
            if (facebookUserType.getPhone() != null && facebookUserType.getPhone().size() > 0) {
                this.phone = facebookUserType.getPhone().get(0);
            }
//            this.friends_num 账号信息时写入
            this.introduce = facebookUserType.getIntroduce();
            this.birthday = facebookUserType.getBirthday();
            this.gender = facebookUserType.getGender();
            this.location = facebookUserType.getLocation();
            this.hometown = facebookUserType.getHometown();

//            this.interest 待录入
//            if(facebookUserType.getSkills() != null && facebookUserType.getSkills().size()>0){
//                this.ability = facebookUserType.getSkills();
//            }
            //家庭信息
            if (facebookUserType.getFamily() != null && facebookUserType.getFamily().size() > 0) {
                List<Map<String, Object>> familyList = new ArrayList<Map<String, Object>>();
                List<Family> families = facebookUserType.getFamily();
                for (Family family : families) {
                    Map<String, Object> familyJsonMap = new HashMap<String, Object>();
                    try {
                        EntityFieldUtils.getEntityMap(family, familyJsonMap);
                        if (ObjectUtils.isNotEmpty(familyJsonMap) && familyJsonMap.size() > 0) {
                            familyList.add(familyJsonMap);
                        }
                    } catch (Exception e) {
                        System.err.println("family data form is error");
                    }
                }
                if (familyList.size() > 0) {
                    this.family = familyList;
                }
            }
            //社交媒体
            if (facebookUserType.getSocial_account() != null && facebookUserType.getSocial_account().size() > 0) {
                List<Map<String, Object>> socialList = new ArrayList<Map<String, Object>>();
                List<SocialMedia> socialMedias = facebookUserType.getSocial_account();
                for (SocialMedia socialMedia : socialMedias) {
                    Map<String, Object> socialMediaJsonMap = new HashMap<String, Object>();
                    try {
                        EntityFieldUtils.getEntityMap(socialMedia, socialMediaJsonMap);
                        if (ObjectUtils.isNotEmpty(socialMediaJsonMap) && socialMediaJsonMap.size() > 0) {
                            socialList.add(socialMediaJsonMap);
                        }
                    } catch (Exception e) {
                        System.err.println("socialMedia data form is error");
                    }
                }
                if (socialList.size() > 0) {
                    this.socialmedia_account = socialList;
                }
            }
            //网站信息
            if (facebookUserType.getWebsites() != null && facebookUserType.getWebsites().size() > 0) {
                List<Map<String, Object>> webList = new ArrayList<Map<String, Object>>();
                List<String> websites = facebookUserType.getWebsites();
                for (String website : websites) {
                    Map<String, Object> webJsonMap = new HashMap<String, Object>();
                    webJsonMap.put("src", website);
                    webList.add(webJsonMap);
                }
                if (webList.size() > 0) {
                    this.websites = webList;
                }
            }
            //语言信息
            if (facebookUserType.getLanguage() != null && facebookUserType.getLanguage().size() > 0) {
                List<Map<String, Object>> langList = new ArrayList<Map<String, Object>>();
                List<Language> languages = facebookUserType.getLanguage();
                for (Language language : languages) {
                    Map<String, Object> langJsonMap = new HashMap<String, Object>();
                    langJsonMap.put("lang", language.getLang());
                    langList.add(langJsonMap);
                }
                if (langList.size() > 0) {
                    this.language = langList;
                }
            }
            //教育信息
            if (facebookUserType.getEducation() != null && facebookUserType.getEducation().size() > 0) {
                List<Map<String, Object>> educationList = new ArrayList<Map<String, Object>>();
                List<Education> educations = facebookUserType.getEducation();
                for (Education eDucation : educations) {
                    Map<String, Object> educationJsonMap = new HashMap<String, Object>();
                    try {
                        EntityFieldUtils.getEntityMap(eDucation, educationJsonMap);
                        if (ObjectUtils.isNotEmpty(educationJsonMap) && educationJsonMap.size() > 0) {
                            educationList.add(educationJsonMap);
                        }
                    } catch (Exception e) {
                        System.err.println("education data form is error");
                    }
                }
                if (educationList.size() > 0) {
                    this.education = educationList;
                }
            }

            //工作信息
            if (facebookUserType.getWork() != null && facebookUserType.getWork().size() > 0) {
                List<Map<String, Object>> workList = new ArrayList<Map<String, Object>>();
                List<WorkExprience> workExpriences = facebookUserType.getWork();
                for (WorkExprience workExprience : workExpriences) {
                    Map<String, Object> workListJsonMap = new HashMap<String, Object>();
                    try {
                        EntityFieldUtils.getEntityMap(workExprience, workListJsonMap);
                        if (ObjectUtils.isNotEmpty(workListJsonMap) && workListJsonMap.size() > 0) {
                            workList.add(workListJsonMap);
                        }
                    } catch (Exception e) {
                        System.err.println("workExprience data form is error");
                    }
                }
                if (workList.size() > 0) {
                    this.work = workList;
                }
            }

            //居住信息
            if (facebookUserType.getLived() != null && facebookUserType.getLived().size() > 0) {
                List<Map<String, Object>> livedList = new ArrayList<Map<String, Object>>();
                List<Lived> liveds = facebookUserType.getLived();
                for (Lived lived : liveds) {
                    Map<String, Object> livedListJsonMap = new HashMap<String, Object>();
                    try {
                        EntityFieldUtils.getEntityMap(lived, livedListJsonMap);
                        if (ObjectUtils.isNotEmpty(livedListJsonMap) && livedListJsonMap.size() > 0) {
                            livedList.add(livedListJsonMap);
                        }
                    } catch (Exception e) {
                        System.err.println("lived data form is error");
                    }
                }
                if (livedList.size() > 0) {
                    this.lived = livedList;
                }
            }

            //事件信息
            if (facebookUserType.getEvents() != null && facebookUserType.getEvents().size() > 0) {
                List<Map<String, Object>> eventsList = new ArrayList<Map<String, Object>>();
                List<EventsEntity> events = facebookUserType.getEvents();
                for (EventsEntity event : events) {
                    Map<String, Object> eventJsonMap = new HashMap<String, Object>();
                    if (ObjectUtils.isNotNull(event.getContent())) {
                        eventJsonMap.put("content", event.getContent());
                    }
                    if (ObjectUtils.isNotNull(event.getTime())) {
                        eventJsonMap.put("time", event.getTime());
                    }
                    eventsList.add(eventJsonMap);
                }
                if (eventsList.size() > 0) {
                    this.events = eventsList;
                }
            }

            //技能信息
            if (facebookUserType.getSkills() != null && facebookUserType.getSkills().size() > 0) {
                Set<String> set = new HashSet<>(facebookUserType.getSkills());
                List<String> skills = new ArrayList<>(set);
                List<Map<String, Object>> skillsList = new ArrayList<Map<String, Object>>();
                for (String skill : skills) {
                    Map<String, Object> skillsJsonMap = new HashMap<String, Object>();
                    skillsJsonMap.put("name", skill);
//                    eventJsonMap.put("score",);
                    skillsList.add(skillsJsonMap);
                }
                if (skillsList.size() > 0) {
                    this.skills = skillsList;
                }
            }
            this.time = TimeUtils.getTime();

        }

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

    public Integer getSite_id() {
        return site_id;
    }

    public void setSite_id(Integer site_id) {
        this.site_id = site_id;
    }

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

    public String getOther_name() {
        return other_name;
    }

    public void setOther_name(String other_name) {
        this.other_name = other_name;
    }

    public String getRegist_region_id() {
        return regist_region_id;
    }

    public void setRegist_region_id(String regist_region_id) {
        this.regist_region_id = regist_region_id;
    }

    public String getParent_regist_region_id() {
        return parent_regist_region_id;
    }

    public void setParent_regist_region_id(String parent_regist_region_id) {
        this.parent_regist_region_id = parent_regist_region_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Long getFans_number() {
        return fans_number;
    }

    public void setFans_number(Long fans_number) {
        this.fans_number = fans_number;
    }

    public Long getFollow_number() {
        return follow_number;
    }

    public void setFollow_number(Long follow_number) {
        this.follow_number = follow_number;
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

    public Long getFriends_num() {
        return friends_num;
    }

    public void setFriends_num(Long friends_num) {
        this.friends_num = friends_num;
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

    public String getGendar() {
        return gender;
    }

    public void setGendar(String gender) {
        this.gender = gender;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getTimezone_str() {
        return timezone_str;
    }

    public void setTimezone_str(String timezone_str) {
        this.timezone_str = timezone_str;
    }

    public List<Map<String, Object>> getInterest() {
        return interest;
    }

    public void setInterest(List<Map<String, Object>> interest) {
        this.interest = interest;
    }

    public List<String> getAbility() {
        return ability;
    }

    public void setAbility(List<String> ability) {
        this.ability = ability;
    }

    public List<Map<String, Object>> getFamily() {
        return family;
    }

    public void setFamily(List<Map<String, Object>> family) {
        this.family = family;
    }

    public List<Map<String, Object>> getSocialmedia_account() {
        return socialmedia_account;
    }

    public void setSocialmedia_account(List<Map<String, Object>> socialmedia_account) {
        this.socialmedia_account = socialmedia_account;
    }

    public List<Map<String, Object>> getWebsites() {
        return websites;
    }

    public void setWebsites(List<Map<String, Object>> websites) {
        this.websites = websites;
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

    public List<Map<String, Object>> getLived() {
        return lived;
    }

    public void setLived(List<Map<String, Object>> lived) {
        this.lived = lived;
    }

    public List<Map<String, Object>> getEvents() {
        return events;
    }

    public void setEvents(List<Map<String, Object>> events) {
        this.events = events;
    }

    public List<Map<String, Object>> getSkills() {
        return skills;
    }

    public void setSkills(List<Map<String, Object>> skills) {
        this.skills = skills;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public Integer getSite_type_id() {
        return site_type_id;
    }

    public void setSite_type_id(Integer site_type_id) {
        this.site_type_id = site_type_id;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public List<Map<String, Object>> getFans() {
        return fans;
    }

    public void setFans(List<Map<String, Object>> fans) {
        this.fans = fans;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
