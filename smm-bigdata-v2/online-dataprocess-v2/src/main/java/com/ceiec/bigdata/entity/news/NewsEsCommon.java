package com.ceiec.bigdata.entity.news;

import com.alibaba.fastjson.JSON;
import com.ceiec.bigdata.entity.AllNewsPost;
import com.ceiec.bigdata.entity.table.Location;
import com.ceiec.bigdata.entity.table.nlp.EntityNlp;
import com.ceiec.bigdata.entity.twitter.es.HotWordJson;
import com.ceiec.bigdata.util.Constants;
import com.ceiec.bigdata.util.InfoIdUtils;
import com.ceiec.bigdata.util.TimeUtils;
import com.ceiec.bigdata.util.eventutil.KeywordClearUtil;
import com.ceiec.bigdata.util.eventutil.KeywordFilter;
import com.ceiec.bigdata.util.locationutil.RelatedRegionInfo;
import com.ceiec.bigdata.util.siteidutil.UrlFilter;

import java.util.*;

/**
 * Created by heyichang on 2017/12/4.
 */
public class NewsEsCommon {


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
    private Location location;
    private String region_id;
    private String language;
    private Integer emotion_id;
    private String creating_date;
    private String gather_date;
    private String parent_region_id;
    private Integer location_type;
    private String location_str;
    private List<Map<String, Object>> tags_nested;
    private Boolean is_rubbish;

    public NewsEsCommon(AllNewsPost news, Map<String, List<Integer>> siteMap, List<String> sensitiveWords) {

    }

    public NewsEsCommon(AllNewsPost news, Map<String, List<Object>> siteMap, String nlpResultString, KeywordFilter keywordFilter, Map<String, List<String>> regionMap) {
        String urlDomain = UrlFilter.filterUrl(news.getSource_url());//获取url主域名
        /*获取站点信息*/
        if (urlDomain != null && siteMap != null) {
            Iterator<Map.Entry<String, List<Object>>> it = siteMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, List<Object>> entry = it.next();
                int endFlag = 0;
                if (entry.getKey().contains(urlDomain) || urlDomain.contains(entry.getKey())) {
                    this.site_id = (int) entry.getValue().get(0);//获取站点信息
                    this.site_type_id = (int) entry.getValue().get(1);//站点类型
                    this.region_id = (String) entry.getValue().get(2);//添加位置信息
                    this.language = (String) entry.getValue().get(3);//添加语言信息
                    this.location_type = 2;
                    try {
                        this.parent_region_id = region_id.substring(0, 3);//获取父位置信息
                    } catch (Exception e) {
                        //TODO：当region_id小于6位时异常
                    }
                    endFlag = 1;
                }
                if (endFlag == 1) {
                    break;
                }
            }
        }

        //去除新闻后面的参数做md5
        if (news.getSource_url() != null) {
            String url = news.getSource_url();
            boolean flag = url.contains("?");
            if (flag) {
                this.source_url = url.split("\\?")[0];
            } else {
                this.source_url = url;//新闻url
            }
            this.info_id = InfoIdUtils.encodeByMD5(this.source_url);
        }

        this.title = news.getTitle();//新闻标题
        /**获得新闻创建时间**/
        if (news.getPublish_time() != null && !news.getPublish_time().contains("T")) {
            this.creating_time = TimeUtils.stampToTime(news.getPublish_time());//得到推文创建时间yy-mm-dd hh:mm:ss
            this.creating_date = TimeUtils.stampToDate(news.getPublish_time());//得到推文创建时间yy-mm-dd
            this.time_slot = TimeUtils.stampToHour(news.getPublish_time());//得到推文创建时间hh
        }
        //获取爬虫location

        if (news.getLang() != null) {
            this.language = news.getLang().toLowerCase();//获取转发推文的语言
        }

        /**获得推文采集时间**/
        this.gather_time = TimeUtils.getTime();//从系统时间获取yy-mm-dd hh:mm:ss
        this.gather_date = TimeUtils.getDate();//从系统时间获取yy-mm-dd

        /**获得推文正文**/
        this.content = news.getContent();
        /**获得作者名**/
        this.account_nick_name = news.getAuthor();
        /*获得摘要*/
        this.summary = news.getSummary();

        /**获取json facebook实体**/
        List<Map<String, Object>> entityOfTwitterJsonList = new LinkedList<Map<String, Object>>();
        Map<String, Object> entityOfTwitterJsonMap = new HashMap<String, Object>();
        /**处理图片信息**/
        if (news.getImg() != null) {
            List<Map<String, Object>> photoList = new LinkedList<Map<String, Object>>();
            Map<String, Object> photoMap = new HashMap<String, Object>();
            int imgLength = news.getImg().size();
            for (int i = 0; i < imgLength; i++) {
                if (i == 0) {
                    photoMap.put(Constants.nlpConstants.SMALL, news.getImg());
                }
                if (i == 1) {
                    photoMap.put(Constants.nlpConstants.MEDIUM, news.getImg());
                }
                if (i == 2) {
                    photoMap.put(Constants.nlpConstants.LARGE, news.getImg());
                }
                if (i == 3) {
                    photoMap.put(Constants.nlpConstants.THUMB, news.getImg());
                }
            }
            photoList.add(photoMap);
            if (photoList.size() > 0) {
                entityOfTwitterJsonMap.put(Constants.nlpConstants.PHOTO, photoList);
            }
        }

        /**处理video信息**/
        if (news.getVideo() != null) {
            List<Map<String, Object>> videoList = new LinkedList<Map<String, Object>>();
            List<String> videos = news.getVideo();
            for (String video : videos) {
                Map<String, Object> videoMap = new HashMap<String, Object>();
                videoMap.put("url", video);
                videoList.add(videoMap);
            }
            if (videoList.size() > 0) {
                entityOfTwitterJsonMap.put(Constants.nlpConstants.VIDEO, videoList);
            }
        }
        if (entityOfTwitterJsonMap.size() > 0) {
            entityOfTwitterJsonList.add(entityOfTwitterJsonMap);
        }
        if (entityOfTwitterJsonList.size() > 0) {
            this.entity_of_twitter_json = entityOfTwitterJsonList;
        }

        List<Map<String, Object>> relatedRegionInfoList = new ArrayList<>();
        if (this.content != null) {
            /**生成地理位置信息***/
            //生成地理位置信息list
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


        /**当转发推文是可以处理语言时进行npl处理**/
        /**调用自然语言处理**/
        if (nlpResultString != null) {
            EntityNlp nlpResult = null;

            try {
                nlpResult = JSON.parseObject(nlpResultString, EntityNlp.class);
            } catch (Exception e) {
                System.err.println("news nlp parse error");
            }
            boolean isjugleInfo = false;
            if(nlpResult.getCategory() != null && nlpResult.getCategory() == 400){
                isjugleInfo = true;
                this.is_rubbish = true;
            }
            //当有自然语言处理结果时
            if (nlpResult != null && !isjugleInfo) {
                if (nlpResult.getLanguage() != null) {
                    this.language = nlpResult.getLanguage();
                } else {
                    this.language = news.getLang();
                }
                List<Map<String, Object>> hotWordList = new ArrayList<>();
                List<Map<String, Object>> entityList = new ArrayList<>();
                /**获取热点词**/
                if (nlpResult.getHotWord() != null && nlpResult.getHotWord().size() > 0) {
                    List<HotWordJson> hotWordLists = nlpResult.getHotWord();
                    for (HotWordJson hotWordJson : hotWordLists) {
                        Map<String, Object> map = new HashMap<>();
                        map.put(Constants.nlpConstants.WORD, hotWordJson.getWord());
                        map.put(Constants.nlpConstants.SCORE, hotWordJson.getScore());
                        hotWordList.add(map);
                    }
                }
                if (hotWordList.size() > 0) {
                    this.hot_word_json = hotWordList;
                }

                /**获取json实体**/
                if (nlpResult.getEntities() != null && nlpResult.getEntities().size() > 0) {
                    List<com.ceiec.bigdata.entity.twitter.es.EntityJson> entitiesLists = nlpResult.getEntities();
                    //生成地理位置信息list
                    List<String> relatedRegionInfos = new ArrayList<>();
                    for (com.ceiec.bigdata.entity.twitter.es.EntityJson entityJson : entitiesLists) {
                        Map<String, Object> map = new HashMap<>();
                        map.put(Constants.nlpConstants.WORD, entityJson.getWord());
                        List<Integer> list2 = new LinkedList<>();
                        list2.add(entityJson.getStart());
                        list2.add(entityJson.getEnd());
                        map.put(Constants.nlpConstants.INDICES, list2);
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
                }
                if (entityList.size() > 0) {
                    this.entity_json = entityList;
                }
                this.first_leval_type_id = nlpResult.getCategory();
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
                if (summary == null) {
                    this.summary = nlpResult.getSummary();
                }
            }
        }


    }

    public List<Map<String, Object>> getRelated_region() {
        return related_region;
    }

    public void setRelated_region(List<Map<String, Object>> related_region) {
        this.related_region = related_region;
    }

    public String getLocation_str() {
        return location_str;
    }

    public void setLocation_str(String location_str) {
        this.location_str = location_str;
    }

    public List<Map<String, Object>> getTags_nested() {
        return tags_nested;
    }

    public void setTags_nested(List<Map<String, Object>> tags_nested) {
        this.tags_nested = tags_nested;
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

    public Boolean getIs_rubbish() {
        return is_rubbish;
    }

    public void setIs_rubbish(Boolean is_rubbish) {
        this.is_rubbish = is_rubbish;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
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

    @Override
    public String toString() {
        return "{" +
                "info_id='" + info_id + '\'' +
                ", creating_time='" + creating_time + '\'' +
                ", gather_time='" + gather_time + '\'' +
                ", sensitive_id=" + sensitive_id +
                ", content='" + content + '\'' +
                ", summary='" + summary + '\'' +
                ", title='" + title + '\'' +
                ", source_content='" + source_content + '\'' +
                ", source_url='" + source_url + '\'' +
                ", hot_word_json=" + hot_word_json +
                ", entity_json=" + entity_json +
                ", entity_of_twitter_json=" + entity_of_twitter_json +
                ", site_id=" + site_id +
                ", site_type_id=" + site_type_id +
                ", remark_number=" + remark_number +
                ", retweet_number=" + retweet_number +
                ", like_number=" + like_number +
                ", account_id='" + account_id + '\'' +
                ", account_nick_name='" + account_nick_name + '\'' +
                ", time_slot=" + time_slot +
                ", first_leval_type_id=" + first_leval_type_id +
                ", first_leval_type_id_manual=" + first_leval_type_id_manual +
                ", second_leval_type_id=" + second_leval_type_id +
                ", is_retweet=" + is_retweet +
                ", retweet_info_id='" + retweet_info_id + '\'' +
                ", location=" + location +
                ", region_id=" + region_id +
                ", language='" + language + '\'' +
                ", emotion_id=" + emotion_id +
                ", creating_date='" + creating_date + '\'' +
                ", gather_date='" + gather_date + '\'' +
                '}';
    }
}
