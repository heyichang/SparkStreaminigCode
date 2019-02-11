package com.ceiec.bigdata.entity.facebook.es;

import com.alibaba.fastjson.JSON;
import com.ceiec.bigdata.entity.AllFacebookPost;
import com.ceiec.bigdata.entity.table.location.LocationRequst;
import com.ceiec.bigdata.entity.table.location.LocationResponse;
import com.ceiec.bigdata.entity.table.location.ResponseValue;
import com.ceiec.bigdata.entity.table.nlp.EntityNlp;
import com.ceiec.bigdata.entity.twitter.es.EntityJson;
import com.ceiec.bigdata.entity.twitter.es.HotWordJson;
import com.ceiec.bigdata.util.Constants;
import com.ceiec.bigdata.util.InfoIdUtils;
import com.ceiec.bigdata.util.TimeUtils;
import com.ceiec.bigdata.util.eventutil.KeywordClearUtil;
import com.ceiec.bigdata.util.eventutil.KeywordFilter;
import com.ceiec.bigdata.util.locationutil.LocationHttpUtils;
import com.ceiec.bigdata.util.locationutil.RegistLocationHelper;
import com.ceiec.bigdata.util.locationutil.RelatedRegionInfo;

import java.util.*;

public class FacebookPostCommon {
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
    private List<Map<String, Object>> related_region;
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
    private String region_id;
    private String language;
    private Integer emotion_id;
    private String creating_date;
    private String gather_date;
    private Integer location_type;
    private String location_str;
    private String retweet_account_id;
    private String update_time;
    private Integer post_type;
    private String hometown;
    private String address;
    private String parent_region_id;
    private boolean is_rubbish;


    public FacebookPostCommon(AllFacebookPost allFacebookPost, String nlpResultString, KeywordFilter keywordFilter, Map<String, List<String>> regionMap) {
        this.site_id = 302;
        this.site_type_id = 2;
        this.content = allFacebookPost.getContent();
        this.remark_number = allFacebookPost.getRemark_number();
        this.like_number = allFacebookPost.getLike_number();
        this.retweet_number = allFacebookPost.getRetweet_number();
        this.post_type = allFacebookPost.getType();
        this.hometown = allFacebookPost.getHometown();
        this.address = allFacebookPost.getAddress();
//        this.title = allFacebookPost;
        if (allFacebookPost != null) {
            this.account_nick_name = allFacebookPost.getNick_name();
        }

        //生成infoID和信息url
        if (allFacebookPost.getSource_url() != null) {
            this.source_url = allFacebookPost.getSource_url();
            this.info_id = InfoIdUtils.generate32MD5ID(source_url);
        }
        //生成account_id
        if (allFacebookPost.getAccount_id() != null) {
            StringBuilder homeUrl = new StringBuilder();
            homeUrl.append("http://www.facebook.com/").append(allFacebookPost.getAccount_id());
            this.account_id = InfoIdUtils.generate32MD5ID(homeUrl.toString());
        }
        //得到转推ID
        if (allFacebookPost.getParent_source_url() != null) {
            String refbSourceUrl = allFacebookPost.getParent_source_url();
            this.retweet_info_id = InfoIdUtils.generate32MD5ID(refbSourceUrl.toString());
//            this.retweet_account_id = //TODO
        }


        if (allFacebookPost.getCreating_time() != null) {
            this.creating_time = TimeUtils.stampToTime(allFacebookPost.getCreating_time());//得到推文创建时间yy-mm-dd hh:mm:ss
            this.creating_date = TimeUtils.stampToDate(allFacebookPost.getCreating_time());//得到推文创建时间yy-mm-dd
            this.time_slot = TimeUtils.stampToHour(allFacebookPost.getCreating_time());//得到推文创建时间hh
        }
        if (allFacebookPost.getCrawled_time() != null) {
            this.gather_time = TimeUtils.stampToTime(allFacebookPost.getCrawled_time());//爬虫获取到的时间
            this.gather_date = TimeUtils.stampToDate(allFacebookPost.getCrawled_time());//爬虫获取到的时间
        } else {
            this.gather_time = TimeUtils.getTime();
            this.gather_date = TimeUtils.getDate();
        }
        this.update_time = TimeUtils.getTime();

        /**获取json facebook实体**/
        List<Map<String, Object>> entityOfTwitterJsonList = new LinkedList<Map<String, Object>>();
        Map<String, Object> entityOfTwitterJsonMap = new HashMap<String, Object>();
        /**处理图片信息**/
        if (allFacebookPost.getImg() != null) {
            List<Map<String, Object>> photoList = new LinkedList<Map<String, Object>>();
            Map<String, Object> photoMap = new HashMap<String, Object>();
            int imgLength = allFacebookPost.getImg().size();
            for (int i = 0; i < imgLength; i++) {
                if (i == 0) {
                    photoMap.put(Constants.nlpConstants.SMALL, allFacebookPost.getImg());
                }
                if (i == 1) {
                    photoMap.put(Constants.nlpConstants.MEDIUM, allFacebookPost.getImg());
                }
                if (i == 2) {
                    photoMap.put(Constants.nlpConstants.LARGE, allFacebookPost.getImg());
                }
                if (i == 3) {
                    photoMap.put(Constants.nlpConstants.THUMB, allFacebookPost.getImg());
                }
            }
            photoList.add(photoMap);
            if (photoList.size() > 0) {
                entityOfTwitterJsonMap.put(Constants.nlpConstants.PHOTO, photoList);
            }
        }

        /**处理video信息**/
        if (allFacebookPost.getVideo() != null) {
            List<Map<String, Object>> videoList = new LinkedList<Map<String, Object>>();
            List<String> videos = allFacebookPost.getVideo();
            for (String video : videos) {
                Map<String, Object> videoMap = new HashMap<String, Object>();
                videoMap.put("url", video);
                videoList.add(videoMap);
            }
            if (videoList.size() > 0) {
                entityOfTwitterJsonMap.put(Constants.nlpConstants.VIDEO, videoList);
            }
        }
        /**处理外部连接信息**/
        if (allFacebookPost.getLinks() != null) {
            List<Map<String, Object>> linksList = new LinkedList<Map<String, Object>>();
            List<String> links = allFacebookPost.getLinks();
            Map<String, Object> linkMap = new HashMap<String, Object>();
            linkMap.put("url", links);
            linksList.add(linkMap);
            if (linksList.size() > 0) {
                entityOfTwitterJsonMap.put(Constants.nlpConstants.URLS, linksList);
            }
        }
        /**得到位置信息*/
        if (allFacebookPost.getLocation() != null) {
            String faceBookLocationStr = allFacebookPost.getLocation();
            this.location_str = faceBookLocationStr;

            /*获取info的region信息*/
            if (location.size() > 2) {
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
                        this.parent_region_id = responseValue.getCode().substring(0, 3);//得到父行政区域位置id
                        this.region_id = responseValue.getCode();//得到该行政区域位置id
                    }
                }
            } else {
                if (location_str != null) {
                    String locationString = location_str;
                    ResponseValue responseValue = RegistLocationHelper.getRegistResponse(locationString);
                    if (responseValue != null && responseValue.getCode() != null && !responseValue.getCode().equals("N")) {
                        this.parent_region_id = responseValue.getCode().substring(0, 3);//得到父行政区域位置id
                        this.region_id = responseValue.getCode();//得到该行政区域位置id
                    }
                }
            }
        }
        if (entityOfTwitterJsonMap.size() > 0) {
            entityOfTwitterJsonList.add(entityOfTwitterJsonMap);
        }
        if (entityOfTwitterJsonList.size() > 0) {
            this.entity_of_twitter_json = entityOfTwitterJsonList;
        }

        /**生成地理位置信息***/
        //生成地理位置信息list
        List<Map<String, Object>> relatedRegionInfoList = new ArrayList<>();
        if (this.content != null) {

            //匹配地理位置信息
            Set<String> matchedRegionSet = keywordFilter.getKeyWord(this.content.toLowerCase(), 2);
            if (matchedRegionSet.size() > 0) {
                Set<String> clearedMatchedRegionSet = KeywordClearUtil.getCompletedSet(this.content.toLowerCase(), matchedRegionSet);
                if (clearedMatchedRegionSet != null && clearedMatchedRegionSet.size() > 0 && regionMap != null) {
                    for (String regionInfo : clearedMatchedRegionSet) {
                        List<String> regionIDInfoList = regionMap.get(regionInfo);
                        if (regionIDInfoList != null && regionIDInfoList.size() > 1) {
                            Map<String, Object> relatedMap = new HashMap<String, Object>();
                            relatedMap.put("name", regionInfo);
                            relatedMap.put("region_id", regionIDInfoList.get(1));
                            relatedMap.put("parent_region_id", regionIDInfoList.get(0));
                            relatedRegionInfoList.add(relatedMap);
                        }
                    }
                }
                if (relatedRegionInfoList.size() > 0) {
                    this.related_region = relatedRegionInfoList;
                }
            }
        }


        /*处理nlp信息*/
        if (nlpResultString != null && !nlpResultString.trim().equals("")) {
            EntityNlp nlpResult;
            //当解析错误时不进行后面的操作
            try {
                nlpResult = JSON.parseObject(nlpResultString, EntityNlp.class);
                /**获取热点词**/
                //判断是否垃圾信息
                boolean isjugleInfo = false;
                if(nlpResult.getCategory() != null && nlpResult.getCategory() == 400){
                    isjugleInfo = true;
                    this.is_rubbish = true;
                }
                if (nlpResult != null && !isjugleInfo) {
                    if (nlpResult.getHotWord() != null && nlpResult.getHotWord().size() > 0) {
                        List<Map<String, Object>> hotWordList = new ArrayList<Map<String, Object>>();
                        List<HotWordJson> hotWordLists = nlpResult.getHotWord();
                        for (HotWordJson hotWordJson : hotWordLists) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put(Constants.nlpConstants.WORD, hotWordJson.getWord());
                            map.put(Constants.nlpConstants.SCORE, hotWordJson.getScore());
                            hotWordList.add(map);
                        }
                        if (hotWordList.size() > 0) {
                            this.hot_word_json = hotWordList;
                        }
                    }
                    //获取语言信息
                    this.language = nlpResult.getLanguage();
                    /**获取json实体**/
                    if (nlpResult.getEntities() != null && nlpResult.getEntities().size() > 0) {
                        List<Map<String, Object>> entityList = new ArrayList<>();
                        //生成地理位置信息list
                        List<String> relatedRegionInfos = new ArrayList<>();
                        //生成实体信息list
                        List<EntityJson> entitiesLists = nlpResult.getEntities();
                        for (EntityJson entityJson : entitiesLists) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put(Constants.nlpConstants.WORD, entityJson.getWord());
                            List<Integer> indicesList = new LinkedList<>();
                            indicesList.add(entityJson.getStart());
                            indicesList.add(entityJson.getEnd());
                            map.put(Constants.nlpConstants.INDICES, indicesList);
                            map.put(Constants.nlpConstants.TYPE, entityJson.getType());
                            //将包含地理位置信息加入relatedRegionInfos type = 6代表地理信息类型 (entityJson.getType().equals("6") || entityJson.getType().equals("5"))
                            if (entityJson.getType() != null && (entityJson.getType().equals("6") || entityJson.getType().equals("5"))) {
                                if (regionMap != null && entityJson.getWord() != null && regionMap.get(entityJson.getWord().toLowerCase()) == null) {
                                    relatedRegionInfos.add(entityJson.getWord());
                                }
                            }
                            entityList.add(map);
                        }
                        //处理地理位置信息
                        if (relatedRegionInfos != null && relatedRegionInfos.size() > 0) {
                            List<Map<String, Object>> relatedRegionInfoListNLP = RelatedRegionInfo.getRelatedRegionInfoList(relatedRegionInfos);
                            if (relatedRegionInfoListNLP != null && relatedRegionInfoListNLP.size() > 0) {
                                relatedRegionInfoList.addAll(relatedRegionInfoListNLP);
                                this.related_region = relatedRegionInfoList;
                            }
                        }
                        if (entityList.size() > 0) {
                            this.entity_json = entityList;
                        }
                    }
                    //文本分类
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
            } catch (Exception e) {
                System.out.println("facebook nlp parse error");
            }
        }
    }

    public boolean isIs_rubbish() {
        return is_rubbish;
    }

    public void setIs_rubbish(boolean is_rubbish) {
        this.is_rubbish = is_rubbish;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getParent_region_id() {
        return parent_region_id;
    }

    public void setParent_region_id(String parent_region_id) {
        this.parent_region_id = parent_region_id;
    }

    public List<Map<String, Object>> getRelated_region() {
        return related_region;
    }

    public void setRelated_region(List<Map<String, Object>> related_region) {
        this.related_region = related_region;
    }

    public Integer getPost_type() {
        return post_type;
    }

    public void setPost_type(Integer post_type) {
        this.post_type = post_type;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
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

    public String getRetweet_account_id() {
        return retweet_account_id;
    }

    public void setRetweet_account_id(String retweet_account_id) {
        this.retweet_account_id = retweet_account_id;
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

    public Integer getLocation_type() {
        return location_type;
    }

    public void setLocation_type(Integer location_type) {
        this.location_type = location_type;
    }


    public String getLocation_str() {
        return location_str;
    }

    public void setLocation_str(String location_str) {
        this.location_str = location_str;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
