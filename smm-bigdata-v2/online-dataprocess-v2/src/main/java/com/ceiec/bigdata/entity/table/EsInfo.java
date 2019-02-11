package com.ceiec.bigdata.entity.table;

import com.alibaba.fastjson.JSON;
import com.ceiec.bigdata.entity.table.location.*;
import com.ceiec.bigdata.entity.table.nlp.EntityNlp;
import com.ceiec.bigdata.entity.table.nlp.NlpResult;
import com.ceiec.bigdata.entity.twitter.*;
import com.ceiec.bigdata.entity.twitter.es.EntityJson;
import com.ceiec.bigdata.entity.twitter.es.HotWordJson;
import com.ceiec.bigdata.util.Constants;
import com.ceiec.bigdata.util.InfoIdUtils;
import com.ceiec.bigdata.util.TestUtils;
import com.ceiec.bigdata.util.TimeUtils;
import com.ceiec.bigdata.util.eventutil.KeywordClearUtil;
import com.ceiec.bigdata.util.eventutil.KeywordFilter;
import com.ceiec.bigdata.util.locationutil.LocationHttpUtils;
import com.ceiec.bigdata.util.locationutil.LocationUtils;
import com.ceiec.bigdata.util.locationutil.RelatedRegionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by heyichang on 2017/11/1.
 */
public class EsInfo {
    private static final Logger logger = LoggerFactory.getLogger(EsInfo.class);

    private TwitterRoot twitterRoot;
    private String InfoId;
    private String task_id;
    private String CreatingTIme;
    private String GatherTime;
    private String UpdateTime;


    private Short SensitiveId;
    private String Content;
    private String Summary;
    private String Title;

    private String SourceContent;
    private String SourceUrl;
    private List<Map<String, Object>> HotWordJson;
    private List<Map<String, Object>> EntityJson;
    private List<Map<String, Object>> EntityOfTwitterJson;
    private Integer SiteId;
    private Integer SiteTypeId;

    private Integer RemarkNumber;
    private Integer RetweetNumber;
    private Integer LikeNumber;
    private String AccountId;
    private String AccountNickName;
    private Short TimeSlot;
    private Integer FirstLevalTypeId;
    private Integer first_leval_type_id_manual;
    private Integer SecondLevalTypeId;
    private Boolean IsRetweet;
    private String region_id_str;
    private String parent_region_id_str;
    private Integer location_type;
    private Boolean verified;
    private Integer from_streaming;

    private String RetweetInfoId;
    private String retweet_account_id;
    private Location location;
    private Integer RegionId;
    private List<Map<String, Object>> related_region;

    private String Language;
    private Integer EmotionId;
    private String CreatingDate;
    private String GatherDate;
    private String twitterId;
    private Boolean is_quote_status;
    private Boolean is_rubbish;

    private User user;
    private NlpResult nlpResult;

    public EsInfo(TwitterRoot twitterRoot) {
        this.LikeNumber = twitterRoot.getFavorite_count();//获取喜欢数
        this.RemarkNumber = twitterRoot.getReply_count();//TODO:未确定 获取重评论数
        this.IsRetweet = twitterRoot.getRetweeted();//获取是否转推
        /**获得推文采集时间**/
        if (twitterRoot.getCrawled_time() != null) {
            this.GatherTime = TimeUtils.stampToTime(twitterRoot.getCrawled_time());//从爬虫获取yy-mm-dd hh:mm:ss
            this.GatherDate = TimeUtils.stampToDate(twitterRoot.getCrawled_time());//从爬虫时间获取yy-mm-dd
        } else {
            this.GatherTime = twitterRoot.GatherTime;//从系统时间获取yy-mm-dd hh:mm:ss
            this.GatherDate = twitterRoot.GatherData;//从系统时间获取yy-mm-dd
        }

    }

    public EsInfo(TwitterRoot twitterRoot, String nlpResultString, KeywordFilter keywordFilter, Map<String, List<String>> regionMap) {
        //this.task_id = twitterRoot.getTask_id();//获取爬虫任务id
        this.twitterRoot = twitterRoot;//得到推文解析实体
        this.SiteId = 301;
        this.SiteTypeId = 1;
        this.twitterId = twitterRoot.getId_str();


        if (twitterRoot.getStreaming() != null && twitterRoot.getStreaming()  == 1) {
            this.from_streaming = 1;//此标识时来源是twitter streaming api
        } else if (twitterRoot.getStreaming()  != null && twitterRoot.getStreaming()  != 1) {
            this.from_streaming = twitterRoot.getStreaming() ;
        } else {
            this.from_streaming = 0;//当有此标识时判断信息来源是主动爬取
        }


        //this.Summary = TestUtils.get10To100Radom();//twitter无
        //this.Title = TestUtils.get10To100Radom();//twitter无

        /*获取推文地理位置信息**/
        if (twitterRoot.getPlace() != null) {
            this.location = LocationUtils.getTwitterLoction(twitterRoot.getPlace());//当有place字段时，根据place地址信息获取location
            //logger.info(location.toString());
            this.location_type = 0;//如果是自身来源则取值为0
            LocationRequst locationRequst = new LocationRequst("1", location.getLat(), location.getLon());
            //String requstString = JSON.toJSONString(locationRequst);
            List<LocationRequst> list = new ArrayList<>();
            list.add(locationRequst);
            long s = System.currentTimeMillis();
            String response = LocationHttpUtils.sendPost(JSON.toJSONString(list));
            long e = System.currentTimeMillis();
            if ((e - s) > 1000) {
                logger.warn("twitter info location used time : " + (e - s));
            }
            LocationResponse locationResponse = JSON.parseObject(response, LocationResponse.class);
            if (locationResponse.getData() != null) {
                ResponseValue responseValue = locationResponse.getData().get(0);
                if (responseValue.getCode() != null && !responseValue.getCode().equals("N")) {
                    this.parent_region_id_str = responseValue.getCode().substring(0, 3);//得到父行政区域位置id
                    this.region_id_str = responseValue.getCode();//得到该行政区域位置id
                }
            }

        } else if (twitterRoot.getUser().getPlace() != null) {
            this.location = LocationUtils.getTwitterLoction(twitterRoot.getUser().getPlace());
            //logger.info(location.toString());
            this.location_type = 1;
            LocationRequst locationRequst = new LocationRequst("1", location.getLat(), location.getLon());
            List<LocationRequst> list = new ArrayList<>();
            list.add(locationRequst);
            String response = LocationHttpUtils.sendPost(JSON.toJSONString(list));
            LocationResponse locationResponse = JSON.parseObject(response, LocationResponse.class);
            if (locationResponse.getData() != null) {
                ResponseValue responseValue = locationResponse.getData().get(0);
                if (responseValue.getCode() != null && !responseValue.getCode().equals("N")) {
                    this.parent_region_id_str = responseValue.getCode().substring(0, 3);//得到父行政区域位置id
                    this.region_id_str = responseValue.getCode().substring(3, 6);//得到该行政区域位置id
                }
            }
        }

        //this.first_leval_type_id_manual = twitterRoot.getFirst_leval_type_id_manual();//TODO:直接从解析字段获取
        this.RemarkNumber = twitterRoot.getReply_count();//TODO:未确定 获取重评论数
        this.Language = twitterRoot.getLang();//获取转发推文的语言
        this.RetweetNumber = twitterRoot.getRetweet_count();//获取重推数
        this.LikeNumber = twitterRoot.getFavorite_count();//获取喜欢数
        this.IsRetweet = twitterRoot.getRetweeted();//获取是否转推
        this.is_quote_status = twitterRoot.getIs_quote_status();
        /**获得推文创建时间**/
        if (twitterRoot.getCreated_at() != null) {
            this.CreatingTIme = TimeUtils.getCreatingTime(twitterRoot.getCreated_at());//得到推文创建时间yy-mm-dd hh:mm:ss
            this.CreatingDate = TimeUtils.getCreatingDate(twitterRoot.getCreated_at());//得到推文创建时间yy-mm-dd
            this.TimeSlot = TimeUtils.getCreatingTimeHour(twitterRoot.getCreated_at());//得到推文创建时间hh
        }
        /**获得推文采集时间**/
        this.GatherTime = twitterRoot.GatherTime;//从系统时间获取yy-mm-dd hh:mm:ss
        this.UpdateTime = twitterRoot.GatherTime;//从系统时间获取yy-mm-dd hh:mm:ss
        this.GatherDate = twitterRoot.GatherData;//从系统时间获取yy-mm-dd
        /**获得推文正文**/
        //当有全信息时

        if (twitterRoot.getFull_text() != null) {
            if (twitterRoot.getRetweeted_status() == null) {
                this.Content = twitterRoot.getFull_text();
            } else {
                String textPre = null;
                try {
                    textPre = twitterRoot.getFull_text().split(":")[0] + ": ";
                } catch (Exception e) {
                    logger.error("split text error");
                }
                if (twitterRoot.getRetweeted_status().getFull_text() != null) {
                    this.Content = textPre + twitterRoot.getRetweeted_status().getFull_text();
                } else {
                    if (twitterRoot.getRetweeted_status().getExtended_tweet() != null && twitterRoot.getRetweeted_status().getExtended_tweet().getFull_text() != null) {
                        this.Content = textPre + twitterRoot.getRetweeted_status().getExtended_tweet().getFull_text();
                    } else {
                        this.Content = textPre + twitterRoot.getRetweeted_status().getText();
                    }

                }
            }
        }//当为省略信息时
        else {
            if (twitterRoot.getRetweeted_status() == null) {
                if (twitterRoot.getExtended_tweet() != null && twitterRoot.getExtended_tweet().getFull_text() != null) {
                    this.Content = twitterRoot.getExtended_tweet().getFull_text();
                } else {
                    this.Content = twitterRoot.getText();
                }
            } else {
                String textPre = null;
                try {
                    textPre = twitterRoot.getText().split(":")[0] + ": ";
                } catch (Exception e) {
                    logger.error("split text error");
                }
                if (twitterRoot.getRetweeted_status().getFull_text() != null) {
                    this.Content = textPre + twitterRoot.getRetweeted_status().getFull_text();
                } else {
                    if (twitterRoot.getRetweeted_status().getExtended_tweet() != null && twitterRoot.getRetweeted_status().getExtended_tweet().getFull_text() != null) {
                        this.Content = textPre + twitterRoot.getRetweeted_status().getExtended_tweet().getFull_text();
                    } else {
                        this.Content = textPre + twitterRoot.getRetweeted_status().getText();
                    }
                }
            }
        }

        //this.SensitiveId = 2;//默认为不敏感
        /**当消息为转推时获取转推文id的md5**/
        if (twitterRoot.getRetweeted_status() != null && twitterRoot.getRetweeted_status().getUser() != null) {
            TwitterRoot retweetedStatus = twitterRoot.getRetweeted_status();
            User user2 = retweetedStatus.getUser();
            StringBuffer retweetedId = new StringBuffer();
            retweetedId.append("https://twitter.com/").append(user2.getScreen_name())
                    .append("/status/").append(retweetedStatus.getId_str());
            this.RetweetInfoId = InfoIdUtils.generate32MD5ID(retweetedId.toString());//从转推url和转推id加md5生成
            String retweetedAccountStr = "https://twitter.com/" + user2.getScreen_name();
            this.retweet_account_id = InfoIdUtils.generate32MD5ID(retweetedAccountStr);
        }


        if (null != twitterRoot.getUser()) {
            User user = twitterRoot.getUser();
            StringBuffer infoId = new StringBuffer();
            infoId.append("https://twitter.com/").append(user.getScreen_name())
                    .append("/status/").append(twitterRoot.getId_str());
            this.InfoId = InfoIdUtils.generate32MD5ID(infoId.toString());//得到InfoId的值

            if (user.getScreen_name() != null) {
                StringBuffer accountId = new StringBuffer();
                accountId.append("https://twitter.com/").append(user.getScreen_name());
                this.AccountId = InfoIdUtils.generate32MD5ID(accountId.toString());//获取虚拟账号id
                //仅针对cnn账号随机生成地址信息
                if (AccountId.equals("D7447C9CE9EC68E4E900D0ABAF91D8A8")) {
                    int a = TestUtils.get1To4IntRadom();
                    switch (a) {
                        case 1:
                            this.location = new Location(CnnLocation.CNN_LOCATION1.getLon(), CnnLocation.CNN_LOCATION1.getLat());
                            this.parent_region_id_str = CnnLocation.CNN_LOCATION1.getP_region_id();
                            this.region_id_str = CnnLocation.CNN_LOCATION1.getRegion_id();
                            break;
                        case 2:
                            this.location = new Location(CnnLocation.CNN_LOCATION2.getLon(), CnnLocation.CNN_LOCATION2.getLat());
                            this.parent_region_id_str = CnnLocation.CNN_LOCATION2.getP_region_id();
                            this.region_id_str = CnnLocation.CNN_LOCATION2.getRegion_id();
                            break;
                        case 3:
                            this.location = new Location(CnnLocation.CNN_LOCATION3.getLon(), CnnLocation.CNN_LOCATION3.getLat());
                            this.parent_region_id_str = CnnLocation.CNN_LOCATION3.getP_region_id();
                            this.region_id_str = CnnLocation.CNN_LOCATION3.getRegion_id();
                            break;
                        case 4:
                            this.location = new Location(CnnLocation.CNN_LOCATION4.getLon(), CnnLocation.CNN_LOCATION4.getLat());
                            this.parent_region_id_str = CnnLocation.CNN_LOCATION4.getP_region_id();
                            this.region_id_str = CnnLocation.CNN_LOCATION4.getRegion_id();
                            break;
                        default:
                            this.location = new Location(CnnLocation.CNN_LOCATION1.getLon(), CnnLocation.CNN_LOCATION1.getLat());
                            this.parent_region_id_str = CnnLocation.CNN_LOCATION1.getP_region_id();
                            this.region_id_str = CnnLocation.CNN_LOCATION1.getRegion_id();
                            break;
                    }
                }
            }
            this.AccountNickName = user.getName();//获取虚拟昵称
            this.verified = user.getVerified();//获取用户认证信息

            /**获得文本url**/
            if (twitterRoot.getSource() != null) {
                this.SourceContent = twitterRoot.getSource();
            }
            /**拼接原文url**/
            StringBuffer urlSource = new StringBuffer();
            urlSource.append("https://twitter.com/").append(user.getScreen_name())
                    .append("/status/").append(twitterRoot.getId_str());
            this.SourceUrl = urlSource.toString();

            /**生成地理位置信息***/
            //生成地理位置信息list
            List<Map<String, Object>> relatedRegionInfoList = new ArrayList<>();
            if (this.Content != null) {
                //匹配地理位置信息
                Set<String> matchedRegionSet = keywordFilter.getKeyWord(this.Content.toLowerCase(), 2);
                if (matchedRegionSet.size() > 0) {
                    Set<String> clearedMatchedRegionSet = KeywordClearUtil.getCompletedSet(this.Content.toLowerCase(), matchedRegionSet);
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


            /**处理自然语言信息**/
            if (nlpResultString != null && !nlpResultString.trim().equals("")) {

                EntityNlp nlpResult = null;

                try {
                    nlpResult = JSON.parseObject(nlpResultString, EntityNlp.class);
                } catch (Exception e) {
                    System.err.println("twitter nlp parse error");
                }
                /**调用自然语言处理**/
                //当消息不是垃圾信息时进行处理
                boolean isjugleInfo = false;
                if(nlpResult.getCategory() != null && nlpResult.getCategory() == 400){
                    isjugleInfo = true;
                    this.is_rubbish = true;
                }
                if (nlpResult != null && ! isjugleInfo) {
                    /**获取热点词**/
                    /**当转发推文是英文时进行npl处理**/
                    List<Map<String, Object>> hotWordList = new ArrayList<>();
                    if (nlpResult.getHotWord() != null && nlpResult.getHotWord().size() > 0) {
                        List<HotWordJson> hotWordLists = nlpResult.getHotWord();
                        for (HotWordJson hotWordJson : hotWordLists) {
                            Map<String, Object> map = new HashMap<>();
                            map.put(Constants.nlpConstants.WORD, hotWordJson.getWord());
                            map.put(Constants.nlpConstants.SCORE, hotWordJson.getScore());
                            hotWordList.add(map);
                        }
                    }
                    this.HotWordJson = hotWordList;
                    List<Map<String, Object>> entityList = new ArrayList<>();
                    /**获取json实体**/
                    if (nlpResult.getEntities() != null && nlpResult.getEntities().size() > 0) {
                        List<com.ceiec.bigdata.entity.twitter.es.EntityJson> entitiesLists = nlpResult.getEntities();
                        //生成地理位置信息list
                        List<String> relatedRegionInfos = new ArrayList<>();
                        for (EntityJson entityJson : entitiesLists) {
                            Map<String, Object> map = new HashMap<>();
                            map.put(Constants.nlpConstants.WORD, entityJson.getWord());
                            List<Integer> list2 = new LinkedList<>();
                            list2.add(entityJson.getStart());
                            list2.add(entityJson.getEnd());
                            map.put(Constants.nlpConstants.INDICES, list2);
                            map.put(Constants.nlpConstants.TYPE, entityJson.getType());
                            entityList.add(map);
                            //将包含地理位置信息加入relatedRegionInfos type = 6代表地理信息类型 (entityJson.getType().equals("6") || entityJson.getType().equals("5"))
                            if (entityJson.getType() != null && (entityJson.getType().equals("6") || entityJson.getType().equals("5"))) {
                                if (regionMap != null && entityJson.getWord() != null && regionMap.get(entityJson.getWord().toLowerCase()) == null) {
                                    relatedRegionInfos.add(entityJson.getWord());
                                }
                            }
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

                    this.EntityJson = entityList;
                    this.FirstLevalTypeId = nlpResult.getCategory();
                    //处理情感属性
                    if (nlpResult.getSentiment() != null) {
                        int a = nlpResult.getSentiment();
                        if (a == 0) {
                            this.EmotionId = 2;//负面2
                        } else if (a == 1) {
                            this.EmotionId = 1;//正面1
                        } else if (a == 2) {
                            this.EmotionId = 3;// 无关3
                        } else {
                            this.EmotionId = null;
                        }
                    }
                }
            }

            /**获取json推特实体**/
            List<Map<String, Object>> entityOfTwitterJsonList = new LinkedList<>();
            Map<String, Object> entityOfTwitterJsonMap = new HashMap<>();

            if (twitterRoot.getEntities() != null) {
                Entities1 entities1 = twitterRoot.getEntities();
                /**处理图片和视频信息**/

                //添加video
                List<Map<String, Object>> videoList = new LinkedList<>();


                /**处理视频信息**/
                if (twitterRoot.getExtended_entities() != null && twitterRoot.getExtended_entities().getMedia() != null) {
                    List<MediaExtend> medias = twitterRoot.getExtended_entities().getMedia();
                    if (medias.size() > 0) {
                        MediaExtend mediaExtend = medias.get(0);
                        Map<String, Object> videoMap = new HashMap<>();
                        if (mediaExtend.getType().trim().equals("video")) {
                            VideoInfo videoInfo = mediaExtend.getVideo_info();
                            List<Variants> variantsList = videoInfo.getVariants();
                            for (Variants variants : variantsList) {
                                if (variants.getContent_type().contains("mp4")) {
                                    videoMap.put("url", variantsList.get(1).getUrl());
                                    if (mediaExtend.getMedia_url_https() != null) {
                                        videoMap.put("img", mediaExtend.getMedia_url_https());
                                    }
                                    videoMap.put(Constants.nlpConstants.INDICES, mediaExtend.getIndices());
                                    videoList.add(videoMap);
                                    break;
                                }
                            }
                        }
                    }
                    if (videoList.size() > 0) {
                        entityOfTwitterJsonMap.put(Constants.nlpConstants.VIDEO, videoList);
                    }
                }
                //添加图片
                List<Map<String, Object>> photoList = new LinkedList<>();
                /**处理图片信息**/
                if (videoList.size() < 1) {
                    /**处理图片信息1**/
                    if (twitterRoot.getExtended_entities() != null && twitterRoot.getExtended_entities().getMedia() != null) {
                        List<MediaExtend> mediaList = twitterRoot.getExtended_entities().getMedia();
                        for (MediaExtend media : mediaList) {
                            if (media.getType().equals("photo") && media.getMedia_url_https() != null) {
                                Map<String, Object> photoMap = new HashMap<>();
                                photoMap.put(Constants.nlpConstants.THUMB, media.getMedia_url_https() + ":thumb");
                                photoMap.put(Constants.nlpConstants.LARGE, media.getMedia_url_https() + ":large");
                                photoMap.put(Constants.nlpConstants.MEDIUM, media.getMedia_url_https() + ":medium");
                                photoMap.put(Constants.nlpConstants.SMALL, media.getMedia_url_https() + ":small");
                                photoMap.put(Constants.nlpConstants.INDICES, media.getIndices());
                                photoList.add(photoMap);
                            }
                        }
                    }
                    /**处理图片信息2**/
                    if (photoList.size() < 1 && entities1.getMedia() != null && entities1.getMedia().size() > 0) {
                        List<Media> mediaList = entities1.getMedia();
                        for (Media media : mediaList) {
                            if (media.getType().equals("photo") && media.getMedia_url_https() != null) {
                                Map<String, Object> photoMap = new HashMap<>();
                                photoMap.put(Constants.nlpConstants.THUMB, media.getMedia_url_https() + ":thumb");
                                photoMap.put(Constants.nlpConstants.LARGE, media.getMedia_url_https() + ":large");
                                photoMap.put(Constants.nlpConstants.MEDIUM, media.getMedia_url_https() + ":medium");
                                photoMap.put(Constants.nlpConstants.SMALL, media.getMedia_url_https() + ":small");
                                photoMap.put(Constants.nlpConstants.INDICES, media.getIndices());
                                photoList.add(photoMap);
                            }
                        }
                    }

                    if (photoList.size() > 0) {
                        entityOfTwitterJsonMap.put(Constants.nlpConstants.PHOTO, photoList);
                    }
                }


                /**处理hashtages信息**/
                List<Map<String, Object>> hashtagesList = new LinkedList<>();
                if (entities1.getHashtags() != null && entities1.getHashtags().size() > 0) {
                    List<HashtagsInside> hashtagsLists = entities1.getHashtags();
                    for (HashtagsInside hashtagsInside : hashtagsLists) {
                        Map<String, Object> hashtagesMap = new HashMap<>();
                        hashtagesMap.put(Constants.nlpConstants.TEXT, hashtagsInside.getText());
                        hashtagesMap.put(Constants.nlpConstants.INDICES, hashtagsInside.getIndices());
                        hashtagesList.add(hashtagesMap);
                    }
                }
                if (hashtagesList.size() > 0) {
                    entityOfTwitterJsonMap.put(Constants.nlpConstants.HASHTAGS, hashtagesList);
                }
                /**处理url信息**/
                List<Map<String, Object>> urlsList = new LinkedList<>();


                if (entities1.getUrls() != null && entities1.getUrls().size() > 0) {
                    List<Urls> urlsLists = entities1.getUrls();
                    for (Urls urls : urlsLists) {
                        Map<String, Object> urlMap = new HashMap<>();
                        urlMap.put(Constants.nlpConstants.DISPLAY_URL, urls.getDisplay_url());
                        urlMap.put(Constants.nlpConstants.EXPANDED_URL, urls.getExpanded_url());
                        urlMap.put(Constants.nlpConstants.URL, urls.getUrl());
                        urlMap.put(Constants.nlpConstants.INDICES, urls.getIndices());
                        urlsList.add(urlMap);
                    }
                }
                if (urlsList.size() > 0) {
                    entityOfTwitterJsonMap.put(Constants.nlpConstants.URLS, urlsList);
                }
                /**处理userMention信息**/
                List<Map<String, Object>> userMentionList = new LinkedList<>();

                if (entities1.getUser_mentions() != null && entities1.getUser_mentions().size() > 0) {
                    List<UserMentions> userMentionsLists = entities1.getUser_mentions();
                    for (UserMentions userMentions : userMentionsLists) {
                        Map<String, Object> userMentionMap = new HashMap<>();
                        userMentionMap.put(Constants.nlpConstants.NAME, userMentions.getName());
                        userMentionMap.put(Constants.nlpConstants.ID_STR, userMentions.getId_str());
                        userMentionMap.put(Constants.nlpConstants.SCREEN_NAME, userMentions.getScreen_name());
                        userMentionMap.put(Constants.nlpConstants.INDICES, userMentions.getIndices());
                        userMentionMap.put(Constants.nlpConstants.ID, userMentions.getId());
                        userMentionList.add(userMentionMap);
                    }

                }
                if (userMentionList.size() > 0) {
                    entityOfTwitterJsonMap.put(Constants.nlpConstants.USER_MENTIONS, userMentionList);
                }
            }
            if (entityOfTwitterJsonMap.size() > 0) {
                entityOfTwitterJsonList.add(entityOfTwitterJsonMap);
            }
            this.EntityOfTwitterJson = entityOfTwitterJsonList;

        }

    }

    public EsInfo(TwitterRoot twitterRoot, List<String> sensitiveWords, String nlpResult) {


    }

    public Boolean getIs_quote_status() {
        return is_quote_status;
    }

    public void setIs_quote_status(Boolean is_quote_status) {
        this.is_quote_status = is_quote_status;
    }

    public String getRetweet_account_id() {
        return retweet_account_id;
    }

    public void setRetweet_account_id(String retweet_account_id) {
        this.retweet_account_id = retweet_account_id;
    }

    public List<Map<String, Object>> getRelated_region() {
        return related_region;
    }

    public void setRelated_region(List<Map<String, Object>> related_region) {
        this.related_region = related_region;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public String getInfoId() {
        return InfoId;
    }

    public void setInfoId(String infoId) {
        InfoId = infoId;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getCreatingTIme() {
        return CreatingTIme;
    }

    public void setCreatingTIme(String creatingTIme) {
        CreatingTIme = creatingTIme;
    }

    public String getGatherTime() {
        return GatherTime;
    }

    public void setGatherTime(String gatherTime) {
        GatherTime = gatherTime;
    }

    public Short getSensitiveId() {
        return SensitiveId;
    }

    public void setSensitiveId(Short sensitiveId) {
        SensitiveId = sensitiveId;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getSummary() {
        return Summary;
    }

    public void setSummary(String summary) {
        Summary = summary;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSourceContent() {
        return SourceContent;
    }

    public void setSourceContent(String sourceContent) {
        SourceContent = sourceContent;
    }

    public String getSourceUrl() {
        return SourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        SourceUrl = sourceUrl;
    }

    public List<Map<String, Object>> getHotWordJson() {
        return HotWordJson;
    }

    public void setHotWordJson(List<Map<String, Object>> hotWordJson) {
        HotWordJson = hotWordJson;
    }

    public List<Map<String, Object>> getEntityJson() {
        return EntityJson;
    }

    public void setEntityJson(List<Map<String, Object>> entityJson) {
        EntityJson = entityJson;
    }

    public List<Map<String, Object>> getEntityOfTwitterJson() {
        return EntityOfTwitterJson;
    }

    public void setEntityOfTwitterJson(List<Map<String, Object>> entityOfTwitterJson) {
        EntityOfTwitterJson = entityOfTwitterJson;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    public Integer getSiteId() {
        return SiteId;
    }

    public void setSiteId(Integer siteId) {
        SiteId = siteId;
    }

    public Integer getSiteTypeId() {
        return SiteTypeId;
    }

    public void setSiteTypeId(Integer siteTypeId) {
        SiteTypeId = siteTypeId;
    }

    public Integer getRemarkNumber() {
        return RemarkNumber;
    }

    public void setRemarkNumber(Integer remarkNumber) {
        RemarkNumber = remarkNumber;
    }

    public Integer getRetweetNumber() {
        return RetweetNumber;
    }

    public void setRetweetNumber(Integer retweetNumber) {
        RetweetNumber = retweetNumber;
    }

    public Integer getLikeNumber() {
        return LikeNumber;
    }

    public void setLikeNumber(Integer likeNumber) {
        LikeNumber = likeNumber;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getAccountNickName() {
        return AccountNickName;
    }

    public void setAccountNickName(String accountNickName) {
        AccountNickName = accountNickName;
    }

    public Short getTimeSlot() {
        return TimeSlot;
    }

    public void setTimeSlot(Short timeSlot) {
        TimeSlot = timeSlot;
    }

    public Integer getFirstLevalTypeId() {
        return FirstLevalTypeId;
    }

    public void setFirstLevalTypeId(Integer firstLevalTypeId) {
        FirstLevalTypeId = firstLevalTypeId;
    }

    public Integer getFirst_leval_type_id_manual() {
        return first_leval_type_id_manual;
    }

    public void setFirst_leval_type_id_manual(Integer first_leval_type_id_manual) {
        this.first_leval_type_id_manual = first_leval_type_id_manual;
    }

    public Integer getSecondLevalTypeId() {
        return SecondLevalTypeId;
    }

    public void setSecondLevalTypeId(Integer secondLevalTypeId) {
        SecondLevalTypeId = secondLevalTypeId;
    }

    public Boolean getRetweet() {
        return IsRetweet;
    }

    public void setRetweet(Boolean retweet) {
        IsRetweet = retweet;
    }

    public String getRetweetInfoId() {
        return RetweetInfoId;
    }

    public void setRetweetInfoId(String retweetInfoId) {
        RetweetInfoId = retweetInfoId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Integer getRegionId() {
        return RegionId;
    }

    public void setRegionId(Integer regionId) {
        RegionId = regionId;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public Integer getEmotionId() {
        return EmotionId;
    }

    public void setEmotionId(Integer emotionId) {
        EmotionId = emotionId;
    }

    public String getCreatingDate() {
        return CreatingDate;
    }

    public void setCreatingDate(String creatingDate) {
        CreatingDate = creatingDate;
    }

    public String getGatherDate() {
        return GatherDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Integer getLocation_type() {
        return location_type;
    }

    public void setLocation_type(Integer location_type) {
        this.location_type = location_type;
    }


    public Integer getFrom_streaming() {
        return from_streaming;
    }

    public void setFrom_streaming(Integer from_streaming) {
        this.from_streaming = from_streaming;
    }

    public void setGatherDate(String gatherDate) {
        GatherDate = gatherDate;
    }

    public Boolean getIs_rubbish() {
        return is_rubbish;
    }

    public void setIs_rubbish(Boolean is_rubbish) {
        this.is_rubbish = is_rubbish;
    }

    @Override
    public String toString() {
        return "{" +
                "twitterRoot=" + twitterRoot +
                ", InfoId='" + InfoId + '\'' +
                ", task_id='" + task_id + '\'' +
                ", CreatingTIme='" + CreatingTIme + '\'' +
                ", GatherTime='" + GatherTime + '\'' +
                ", SensitiveId=" + SensitiveId +
                ", Content='" + Content + '\'' +
                ", Summary='" + Summary + '\'' +
                ", Title='" + Title + '\'' +
                ", SourceContent='" + SourceContent + '\'' +
                ", SourceUrl='" + SourceUrl + '\'' +
                ", HotWordJson=" + HotWordJson +
                ", EntityJson=" + EntityJson +
                ", EntityOfTwitterJson=" + EntityOfTwitterJson +
                ", SiteId=" + SiteId +
                ", SiteTypeId=" + SiteTypeId +
                ", RemarkNumber=" + RemarkNumber +
                ", RetweetNumber=" + RetweetNumber +
                ", LikeNumber=" + LikeNumber +
                ", AccountId='" + AccountId + '\'' +
                ", AccountNickName='" + AccountNickName + '\'' +
                ", TimeSlot=" + TimeSlot +
                ", FirstLevalTypeId=" + FirstLevalTypeId +
                ", first_leval_type_id_manual=" + first_leval_type_id_manual +
                ", SecondLevalTypeId=" + SecondLevalTypeId +
                ", IsRetweet=" + IsRetweet +
                ", RetweetInfoId='" + RetweetInfoId + '\'' +
                ", location=" + location +
                ", RegionId=" + RegionId +
                ", Language='" + Language + '\'' +
                ", EmotionId=" + EmotionId +
                ", CreatingDate='" + CreatingDate + '\'' +
                ", GatherDate='" + GatherDate + '\'' +
                ", user=" + user +
                ", twitterNlpResult=" + nlpResult +
                '}';
    }
}
