package com.ceiec.bigdata.entity.news;

import com.alibaba.fastjson.JSON;
import com.ceiec.bigdata.entity.table.Location;
import com.ceiec.bigdata.entity.table.nlp.EntityNlp;
import com.ceiec.bigdata.entity.table.nlp.NlpResult;
import com.ceiec.bigdata.entity.twitter.es.HotWordJson;
import com.ceiec.bigdata.util.Constants;
import com.ceiec.bigdata.util.InfoIdUtils;
import com.ceiec.bigdata.util.TimeUtils;
import com.ceiec.bigdata.util.siteidutil.UrlFilter;

import java.util.*;

/**
 * Created by heyichang on 2017/12/4.
 */
public class NewsEs {


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
    private Location location;
    private Integer region_id;
    private String language;
    private Integer emotion_id;
    private String creating_date;
    private String gather_date;
    private String region_id_str;
    private String parent_region_id_str;
    private Integer location_type;
    private String location_str;
    private List<Map<String, Object>> tags_nested;
    private NlpResult nlpResult;

    public NewsEs(News news, Map<String, List<Integer>> siteMap, List<String> sensitiveWords) {

    }

    public NewsEs(News news, Map<String, List<Object>> siteMap, String nlpResultString) {
        String urlDomain = UrlFilter.filterUrl(news.getUrl());//获取url主域名
        /*获取站点信息*/
        if (urlDomain != null && siteMap != null ) {
            Iterator<Map.Entry<String, List<Object>>> it = siteMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, List<Object>> entry = it.next();
                int endFlag = 0;
                if (entry.getKey().contains(urlDomain) || urlDomain.contains(entry.getKey())) {
                    this.site_id = (int) entry.getValue().get(0);//获取站点信息
                    this.site_type_id = (int) entry.getValue().get(1);//站点类型
                    this.region_id_str = (String) entry.getValue().get(2);//添加位置信息
                    this.language = (String) entry.getValue().get(3);//添加语言信息
                    this.location_type = 2;
                    try {
                        this.parent_region_id_str = region_id_str.substring(0, 3);//获取父位置信息
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
        this.info_id = InfoIdUtils.encodeByMD5(news.getUrl());
        this.first_leval_type_id_manual = news.getPost_type_id();//一级分类id
        this.source_url = news.getUrl();//新闻url
        if (news.getData() != null) {
            Data data = news.getData();
            this.title = data.getTitle();//新闻标题
            /**获得推文创建时间**/
            if( data.getCreate_time() != null && !data.getCreate_time().contains("T")){
                this.creating_time = TimeUtils.stampToTime(data.getCreate_time());//得到推文创建时间yy-mm-dd hh:mm:ss
                this.creating_date = TimeUtils.stampToDate(data.getCreate_time());//得到推文创建时间yy-mm-dd
                this.time_slot = TimeUtils.stampToHour(data.getCreate_time());//得到推文创建时间hh
            }
            //获取爬虫location
            if(data.getLocation() != null){
                this.location_str = data.getLocation();
            }
            //获取爬虫tags
            if(data.getTags() != null){
                /**处理标签**/
                List<Map<String, Object>> tagsNestedList = new ArrayList<>();
               //当标签是数组类型时
                try {
                    List<String> tagsList = JSON.parseArray(data.getTags(), String.class);
                    Map<String, Object> map = new HashMap<>();

                    for (String tags:tagsList) {
                        map.put(Constants.nlpConstants.WORD, tags);
                    }
                    tagsNestedList.add(map);
                }catch (Exception e){
                    Map<String, Object> map = new HashMap<>();
                    map.put(Constants.nlpConstants.WORD, data.getTags());
                    tagsNestedList.add(map);
                }
                if (tagsNestedList.size() > 0) {
                    this.tags_nested = tagsNestedList;
                }
            }

        }


        if (news.getLang() != null) {
            this.language = news.getLang().toLowerCase();//获取转发推文的语言
        }

        /**获得推文采集时间**/
        if(news.getCreate_time() != null){
            this.gather_time = TimeUtils.stampToTime(news.getCreate_time());//从系统时间获取yy-mm-dd hh:mm:ss
            this.gather_date = TimeUtils.stampToDate(news.getCreate_time());//从系统时间获取yy-mm-dd
        }else {
            this.gather_time = news.GatherTime;
            this.gather_time = news.GatherData;
        }

        /**获得推文正文**/
        this.content = news.getData().getContent();
        /*敏感词判断
        SensitivewordFilter filter = new SensitivewordFilter(sensitiveWords);
        Set<String> set = filter.getSensitiveWord(this.content, 1);
        if (set.size() > 0) {
            //当有敏感词时值1
            this.sensitive_id = 1;
        } else {
            //当无敏感词时值2
            this.sensitive_id = 2;
        }
        */

        /**当转发推文是英文时进行npl处理**/
        List<Map<String, Object>> hotWordList = new ArrayList<>();

        List<Map<String, Object>> entityList = new ArrayList<>();

        // if (this.language.equals(Constants.nlpConstants.EN)) {
        /**调用自然语言处理**/
        if (nlpResultString != null) {
            EntityNlp nlpResult = null;
            try {
                nlpResult = JSON.parseObject(nlpResultString, EntityNlp.class);
            } catch (Exception e) {
                System.err.println("news nlp parse error");
            }

            //当有自然语言处理结果时
            if (nlpResult != null) {

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
                    for (com.ceiec.bigdata.entity.twitter.es.EntityJson entityJson : entitiesLists) {
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
                this.summary = nlpResult.getSummary();
            }
        }


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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
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
