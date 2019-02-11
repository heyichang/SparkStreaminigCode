package com.ceiec.bigdata.entity.facebook.es;

import com.alibaba.fastjson.JSON;
import com.ceiec.bigdata.entity.facebook.*;
import com.ceiec.bigdata.entity.table.location.LocationRequst;
import com.ceiec.bigdata.entity.table.location.LocationResponse;
import com.ceiec.bigdata.entity.table.location.ResponseValue;
import com.ceiec.bigdata.entity.table.nlp.EntityNlp;
import com.ceiec.bigdata.entity.table.nlp.NlpResult;
import com.ceiec.bigdata.entity.twitter.es.EntityJson;
import com.ceiec.bigdata.entity.twitter.es.HotWordJson;
import com.ceiec.bigdata.util.Constants;
import com.ceiec.bigdata.util.InfoIdUtils;
import com.ceiec.bigdata.util.TimeUtils;
import com.ceiec.bigdata.util.locationutil.LocationHttpUtils;
import com.ceiec.bigdata.util.locationutil.RegistLocationHelper;

import java.util.*;

public class FaceBookInfo {
    private String info_id;
    private String creating_time;
    private String gather_time;
    private Short sensitive_id;
    private String content;
    private String summary;
    private String title;

    private String source_content;
    private String source_url;
    private List<Map<String, Object>> hot_word_json;
    private List<Map<String, Object>> entity_json;
    private List<Map<String, Object>> entity_of_twitter_json;
    private Integer site_id;
    private Integer site_type_id;

    private Integer remark_number;
    private Integer retweet_number;
    private Integer like_number;
    private String account_id;
    private String account_nick_name;
    private Short time_slot;
    private Integer first_leval_type_id;
    private Integer first_leval_type_id_manual;
    private Integer second_leval_type_id;
    private Boolean is_retweet;

    private String retweet_info_id;
    private List<Double> location;
    private Integer region_id;
    private String language;
    private Integer emotion_id;
    private String creating_date;
    private String gather_date;
    private Integer location_type;
    private String parent_region_id_str;
    private String region_id_str;

    private NlpResult nlpResult;

    public FaceBookInfo() {

    }

    public FaceBookInfo(FaceBookRoot faceBookRoot, String nlpResultString) {
        FaceBookPost faceBookPost = faceBookRoot.getPost();
        FaceBookUser faceBookUserCommon = faceBookRoot.getUser();
        this.site_id = 302;
        this.site_type_id = 2;
        this.content = faceBookPost.getMessage();
        this.title = faceBookPost.getDescription();
        if (faceBookUserCommon != null) {
            this.account_nick_name = faceBookUserCommon.getNick_name();
        }

        //生成infoID和信息url
        if (faceBookPost.getFb_id() != null && faceBookPost.getPost_id() != null) {
            StringBuilder sourceUrl = new StringBuilder();
            sourceUrl.append("http://www.facebook.com/").append(faceBookPost.getFb_id())
                    .append("_").append(faceBookPost.getPost_id());
            this.source_url = sourceUrl.toString();
            this.info_id = InfoIdUtils.generate32MD5ID(source_url);
        }
        //生成account_id
        if (faceBookPost.getFb_id() != null) {
            StringBuilder homeUrl = new StringBuilder();
            homeUrl.append("http://www.facebook.com/").append(faceBookPost.getFb_id());
            this.account_id = InfoIdUtils.generate32MD5ID(homeUrl.toString());
        }
        //得到转推ID
        if (faceBookPost.getParent_fb_id() != null && faceBookPost.getParent_post_id() != null) {
            StringBuilder refbSourceUrl = new StringBuilder();
            refbSourceUrl.append("http://www.facebook.com/").append(faceBookPost.getParent_fb_id())
                    .append("_").append(faceBookPost.getParent_post_id());
            this.retweet_info_id = InfoIdUtils.generate32MD5ID(refbSourceUrl.toString());
        }

        if (faceBookPost.getPost_ts() != null) {
            long fbTime = faceBookPost.getPost_ts();
            String fbTimeStr = String.valueOf(fbTime);
            this.creating_time = TimeUtils.stampToTime(fbTimeStr);//得到推文创建时间yy-mm-dd hh:mm:ss
            this.creating_date = TimeUtils.stampToDate(fbTimeStr);//得到推文创建时间yy-mm-dd
            this.time_slot = TimeUtils.stampToHour(fbTimeStr);//得到推文创建时间hh
        }
        if(faceBookRoot.getCrawled_time() != null){
            this.gather_time = TimeUtils.stampToTime(faceBookRoot.getCrawled_time());//爬虫获取到的时间
            this.gather_date = TimeUtils.stampToDate(faceBookRoot.getCrawled_time());//爬虫获取到的时间
        }else {
            this.gather_time = TimeUtils.getTime();
            this.gather_date = TimeUtils.getDate();
        }


        /**获取json facebook实体**/
        List<Map<String, Object>> entityOfTwitterJsonList = new LinkedList<Map<String, Object>>();
        Map<String, Object> entityOfTwitterJsonMap = new HashMap<String, Object>();
        /**处理图片信息**/
        List<Map<String, Object>> photoList = new LinkedList<Map<String, Object>>();
        Map<String, Object> photoMap = new HashMap<String, Object>();

        /**处理视频信息**/
        if (faceBookPost.getPicture() != null) {
            photoMap.put(Constants.nlpConstants.SMALL, faceBookPost.getPicture());
        }
        if (faceBookPost.getFull_picture() != null) {
            photoMap.put(Constants.nlpConstants.LARGE, faceBookPost.getFull_picture());
        }
        if (photoMap.size() > 0) {
            photoList.add(photoMap);
        }
        if (photoList.size() > 0) {
            entityOfTwitterJsonMap.put(Constants.nlpConstants.PHOTO, photoList);
        }
        /**处理video信息**/
        List<Map<String, Object>> videoList = new LinkedList<Map<String, Object>>();
        if (faceBookPost.getVideo() != null) {
            Map<String, Object> videoMap = new HashMap<String, Object>();
            videoMap.put("url", faceBookPost.getVideo());
            videoList.add(videoMap);
            entityOfTwitterJsonMap.put(Constants.nlpConstants.VIDEO, videoList);
        }
        /**得到story信息*/
        if (faceBookPost.getStory() != null) {
            entityOfTwitterJsonMap.put("story", faceBookPost.getStory());
        }
        /**得到story_tags信息*/
        List<Map<String, Object>> storyTagsList = new LinkedList<Map<String, Object>>();
        if (faceBookPost.getStory_tags() != null) {
            List<StoryTags> stLists = faceBookPost.getStory_tags();
            for (StoryTags storyTags : stLists) {
                Map<String, Object> storyTagsMap = new HashMap<String, Object>();
                storyTagsMap.put("id", storyTags.getId());
                storyTagsMap.put("text", storyTags.getName());
                storyTagsMap.put("type", storyTags.getType());
                List<Integer> list = new LinkedList<Integer>();
                list.add(storyTags.getOffset());
                list.add(storyTags.getLength());
                storyTagsMap.put(Constants.nlpConstants.INDICES, list);
                storyTagsList.add(storyTagsMap);
            }
            if (storyTagsList.size() > 0) {
                entityOfTwitterJsonMap.put("story_tags", storyTagsList);
            }
        }
        /**得到位置信息*/
        if (faceBookPost.getPlace() != null) {
            FaceBookPlace faceBookPlace = faceBookPost.getPlace();
            Map<String, Object> placeMap = new HashMap<String, Object>();
            placeMap.put("name", faceBookPlace.getName());
            placeMap.put("id", faceBookPlace.getId());
            if (faceBookPlace.getLocation() != null) {
                FaceBookLocation faceBookLocation = faceBookPlace.getLocation();
                String locationStr = JSON.toJSONString(faceBookLocation);
                placeMap.put("location", locationStr);
                List<Double> fbLocation = new LinkedList<Double>();
                fbLocation.add(faceBookLocation.getLatitude());
                fbLocation.add(faceBookLocation.getLongitude());
                this.location = fbLocation;

                /*获取info的region信息*/
                if ( location.size() > 2) {
                    //logger.info(location.toString());
                    this.location_type = 0;//如果是自身来源则取值为0
                    LocationRequst locationRequst = new LocationRequst("1", location.get(0), location.get(1));
                    //String requstString = JSON.toJSONString(locationRequst);
                    List<LocationRequst> list = new ArrayList<>();
                    list.add(locationRequst);

                    String response = LocationHttpUtils.sendPost(JSON.toJSONString(list));

                    LocationResponse locationResponse = JSON.parseObject(response, LocationResponse.class);
                    if (locationResponse.getData() != null) {
                        ResponseValue responseValue = locationResponse.getData().get(0);
                        if (responseValue.getCode() != null && !responseValue.getCode().equals("N")) {
                            this.parent_region_id_str = responseValue.getCode().substring(0, 3);//得到父行政区域位置id
                            this.region_id_str = responseValue.getCode();//得到该行政区域位置id
                        }
                    }
                } else {
                    if (faceBookLocation.getCity() != null) {

                        String locationString = faceBookLocation.getCity();
                        ResponseValue responseValue = RegistLocationHelper.getRegistResponse(locationString);
                        if (responseValue != null && responseValue.getCode() != null && !responseValue.getCode().equals("N")) {
                            this.parent_region_id_str = responseValue.getCode().substring(0, 3);//得到父行政区域位置id
                            this.region_id_str = responseValue.getCode();//得到该行政区域位置id
                        }
                    }
                }

            }
            entityOfTwitterJsonMap.put("place", placeMap);

        }


        if (entityOfTwitterJsonMap.size() > 0) {
            entityOfTwitterJsonList.add(entityOfTwitterJsonMap);
        }
        this.entity_of_twitter_json = entityOfTwitterJsonList;

        if (nlpResultString != null && !nlpResultString.trim().equals("")) {
            /**当转发推文是英文时进行npl处理**/
            List<Map<String, Object>> hotWordList = new ArrayList<>();

            List<Map<String, Object>> entityList = new ArrayList<>();
            EntityNlp nlpResult = null;
            try {
                nlpResult = JSON.parseObject(nlpResultString, EntityNlp.class);
            } catch (Exception e) {
                System.out.println("facebook nlp parse error");
            }

            /**调用自然语言处理**/
            /**获取热点词**/
            if (nlpResult != null) {
                if (nlpResult.getHotWord() != null && nlpResult.getHotWord().size() > 0) {
                    List<HotWordJson> hotWordLists = nlpResult.getHotWord();
                    for (HotWordJson hotWordJson : hotWordLists) {
                        Map<String, Object> map = new HashMap<>();
                        map.put(Constants.nlpConstants.WORD, hotWordJson.getWord());
                        map.put(Constants.nlpConstants.SCORE, hotWordJson.getScore());
                        hotWordList.add(map);
                    }
                }
                this.language = nlpResult.getLanguage();
                this.hot_word_json = hotWordList;
                /**获取json实体**/
                if (nlpResult.getEntities() != null && nlpResult.getEntities().size() > 0) {
                    List<EntityJson> entitiesLists = nlpResult.getEntities();
                    for (EntityJson entityJson : entitiesLists) {
                        Map<String, Object> map = new HashMap<>();
                        map.put(Constants.nlpConstants.WORD, entityJson.getWord());
                        List<Integer> list2 = new LinkedList<>();
                        list2.add(entityJson.getStart());
                        list2.add(entityJson.getEnd());
                        map.put(Constants.nlpConstants.INDICES, list2);
                        map.put(Constants.nlpConstants.TYPE, entityJson.getType());
                        entityList.add(map);
                    }
                }
                if (entityList.size() > 0) {
                    this.entity_json = entityList;
                }
                this.first_leval_type_id = nlpResult.getCategory();
                //处理情感属性
                if (nlpResult.getSentiment() != null) {
                    int a = nlpResult.getSentiment();
                    if (a == 0) {
                        this.emotion_id = 2;//负面2
                    } else if (a == 1) {
                        this.emotion_id = 1;//正面1
                    } else if (a == 2) {
                        this.emotion_id = 3;// 无关3
                    } else {
                        this.emotion_id = null;
                    }
                }
            }


        }


    }

    public String getParent_region_id_str() {
        return parent_region_id_str;
    }

    public void setParent_region_id_str(String parent_region_id_str) {
        this.parent_region_id_str = parent_region_id_str;
    }

    public String getRegion_id_str() {
        return region_id_str;
    }

    public void setRegion_id_str(String region_id_str) {
        this.region_id_str = region_id_str;
    }

    public Integer getLocation_type() {
        return location_type;
    }

    public void setLocation_type(Integer location_type) {
        this.location_type = location_type;
    }

    public String getInfo_id() {
        return info_id;
    }

    public void setInfo_id(String info_id) {
        this.info_id = info_id;
    }

    public String getCreating_time() {
        return creating_time;
    }

    public void setCreating_time(String creating_time) {
        this.creating_time = creating_time;
    }

    public String getGather_time() {
        return gather_time;
    }

    public void setGather_time(String gather_time) {
        this.gather_time = gather_time;
    }

    public Short getSensitive_id() {
        return sensitive_id;
    }

    public void setSensitive_id(Short sensitive_id) {
        this.sensitive_id = sensitive_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSource_content() {
        return source_content;
    }

    public void setSource_content(String source_content) {
        this.source_content = source_content;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public List<Map<String, Object>> getHot_word_json() {
        return hot_word_json;
    }

    public void setHot_word_json(List<Map<String, Object>> hot_word_json) {
        this.hot_word_json = hot_word_json;
    }

    public List<Map<String, Object>> getEntity_json() {
        return entity_json;
    }

    public void setEntity_json(List<Map<String, Object>> entity_json) {
        this.entity_json = entity_json;
    }

    public List<Map<String, Object>> getEntity_of_twitter_json() {
        return entity_of_twitter_json;
    }

    public void setEntity_of_twitter_json(List<Map<String, Object>> entity_of_twitter_json) {
        this.entity_of_twitter_json = entity_of_twitter_json;
    }

    public Integer getSite_id() {
        return site_id;
    }

    public void setSite_id(Integer site_id) {
        this.site_id = site_id;
    }

    public Integer getSite_type_id() {
        return site_type_id;
    }

    public void setSite_type_id(Integer site_type_id) {
        this.site_type_id = site_type_id;
    }

    public Integer getRemark_number() {
        return remark_number;
    }

    public void setRemark_number(Integer remark_number) {
        this.remark_number = remark_number;
    }

    public Integer getRetweet_number() {
        return retweet_number;
    }

    public void setRetweet_number(Integer retweet_number) {
        this.retweet_number = retweet_number;
    }

    public Integer getLike_number() {
        return like_number;
    }

    public void setLike_number(Integer like_number) {
        this.like_number = like_number;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getAccount_nick_name() {
        return account_nick_name;
    }

    public void setAccount_nick_name(String account_nick_name) {
        this.account_nick_name = account_nick_name;
    }

    public Short getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(Short time_slot) {
        this.time_slot = time_slot;
    }

    public Integer getFirst_leval_type_id() {
        return first_leval_type_id;
    }

    public void setFirst_leval_type_id(Integer first_leval_type_id) {
        this.first_leval_type_id = first_leval_type_id;
    }

    public Integer getFirst_leval_type_id_manual() {
        return first_leval_type_id_manual;
    }

    public void setFirst_leval_type_id_manual(Integer first_leval_type_id_manual) {
        this.first_leval_type_id_manual = first_leval_type_id_manual;
    }

    public Integer getSecond_leval_type_id() {
        return second_leval_type_id;
    }

    public void setSecond_leval_type_id(Integer second_leval_type_id) {
        this.second_leval_type_id = second_leval_type_id;
    }

    public Boolean getIs_retweet() {
        return is_retweet;
    }

    public void setIs_retweet(Boolean is_retweet) {
        this.is_retweet = is_retweet;
    }

    public String getRetweet_info_id() {
        return retweet_info_id;
    }

    public void setRetweet_info_id(String retweet_info_id) {
        this.retweet_info_id = retweet_info_id;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public Integer getRegion_id() {
        return region_id;
    }

    public void setRegion_id(Integer region_id) {
        this.region_id = region_id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getEmotion_id() {
        return emotion_id;
    }

    public void setEmotion_id(Integer emotion_id) {
        this.emotion_id = emotion_id;
    }

    public String getCreating_date() {
        return creating_date;
    }

    public void setCreating_date(String creating_date) {
        this.creating_date = creating_date;
    }

    public String getGather_date() {
        return gather_date;
    }

    public void setGather_date(String gather_date) {
        this.gather_date = gather_date;
    }
}
