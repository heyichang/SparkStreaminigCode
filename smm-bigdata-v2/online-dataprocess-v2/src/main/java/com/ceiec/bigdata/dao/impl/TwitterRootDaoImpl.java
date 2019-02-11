package com.ceiec.bigdata.dao.impl;


import com.alibaba.fastjson.JSON;
import com.ceiec.bigdata.dao.TwitterRootDao;
import com.ceiec.bigdata.entity.table.AccountLastTime;
import com.ceiec.bigdata.entity.table.EsInfo;
import com.ceiec.bigdata.entity.table.EsVirtual;
import com.ceiec.bigdata.entity.table.keyword.KeywordOutput;
import com.ceiec.bigdata.entity.table.keyword.WarningMapping;
import com.ceiec.bigdata.entity.table.updatetwitter.UpdateTwitterUser;
import com.ceiec.bigdata.entity.twitter.TwitterRoot;
import com.ceiec.bigdata.entity.twitter.User;
import com.ceiec.bigdata.util.Constants;
import com.ceiec.bigdata.util.InfoIdUtils;
import com.ceiec.bigdata.util.TimeUtils;
import com.ceiec.bigdata.util.accountutil.AccountCheck;
import com.ceiec.bigdata.util.esutil.EsUtils;
import com.ceiec.bigdata.util.eventutil.EventForeUtils;
import com.ceiec.bigdata.util.eventutil.KeywordFilter;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by heyichang on 2017/10/17.
 */
public class TwitterRootDaoImpl implements TwitterRootDao {
    private static final Logger logger = LoggerFactory.getLogger(TwitterRootDaoImpl.class);
    private TwitterRoot examplesRoot;
    private EsInfo esInfo;
    private EsVirtual esVirtual;

    public EsInfo getEsInfo() {
        return esInfo;
    }

    public void setEsInfo(EsInfo esInfo) {
        this.esInfo = esInfo;
    }

    private String accountId;

    //private RetweetedEs retweetedEs;

    public TwitterRootDaoImpl(UpdateTwitterUser updateTwitterUser) {
        this.esVirtual = new EsVirtual(updateTwitterUser);
    }


    public TwitterRootDaoImpl(User twitterUser) {
        this.esVirtual = new EsVirtual(twitterUser);
    }

    public TwitterRootDaoImpl(TwitterRoot twitterRoot, String nlpResult ) {

        this.examplesRoot = twitterRoot;

    }

    public TwitterRootDaoImpl(TwitterRoot twitterRoot, String nlpResult ,KeywordFilter keywordFilter , Map<String ,List<String>> regionMap) {

        this.examplesRoot = twitterRoot;
        this.esInfo = new EsInfo(twitterRoot, nlpResult , keywordFilter ,regionMap);
        this.accountId = esInfo.getAccountId();

    }


    @Override
    public IndexRequestBuilder bulkAddEsData(Client client, String index, String type) throws IOException, NoSuchAlgorithmException {

        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
        xContentBuilder.startObject()
                .field(Constants.esTableInfo.C1, esInfo.getInfoId());
        if (esInfo.getHotWordJson() != null && esInfo.getHotWordJson().size() > 0) {
            xContentBuilder.field(Constants.examplesTableInfo.HOTWORD_JSON, esInfo.getHotWordJson());
        }
        if (esInfo.getEntityJson() != null && esInfo.getEntityJson().size() > 0) {
            xContentBuilder.field(Constants.examplesTableInfo.ENTITY_JSON, esInfo.getEntityJson());
        }
        if (esInfo.getEntityOfTwitterJson() != null && esInfo.getEntityOfTwitterJson().size() > 0) {
            xContentBuilder.field(Constants.examplesTableInfo.ENTITY_OF_TWITTER_JSON, esInfo.getEntityOfTwitterJson());
        }
        if (esInfo.getFirstLevalTypeId() != null && esInfo.getFirstLevalTypeId() != 0) {
            xContentBuilder.field(Constants.examplesTableInfo.FIRST_LEVAL_TYPE_ID, esInfo.getFirstLevalTypeId());
        }
        if (esInfo.getFirst_leval_type_id_manual() != null && esInfo.getFirst_leval_type_id_manual() != 0) {
            xContentBuilder.field(Constants.examplesTableInfo.FIRST_LEVAL_TYPE_ID_MANUAL, esInfo.getFirst_leval_type_id_manual());//TODO
        }
        if (esInfo.getSecondLevalTypeId() != null && esInfo.getSecondLevalTypeId() != 0) {
            xContentBuilder.field(Constants.examplesTableInfo.SECOND_LEVAL_TYPE_ID, esInfo.getSecondLevalTypeId());
        }
        if (esInfo.getRetweetInfoId() != null) {
            xContentBuilder.field(Constants.examplesTableInfo.RETWEET_INFO_ID, esInfo.getRetweetInfoId());
        }
        if (esInfo.getEmotionId() != null) {
            xContentBuilder.field(Constants.examplesTableInfo.EMOTION_ID, esInfo.getEmotionId());
        }
        if (esInfo.getLocation() != null) {
            xContentBuilder.startObject(Constants.esTableInfo.LOCATION).field(Constants.esTableInfo.LOCATION_LAT, esInfo.getLocation().getLat()).field(Constants.esTableInfo.LOCATION_LON, esInfo.getLocation().getLon()).endObject();
            if (esInfo.getRegion_id_str() != null) {
                xContentBuilder.field(Constants.examplesTableInfo.REGION_ID_STR, esInfo.getRegion_id_str())
                        .field(Constants.examplesTableInfo.PARENT_REGION_ID_STR, esInfo.getParent_region_id_str());
            }
        }
        if (esInfo.getLocation_type() != null) {
            xContentBuilder.field(Constants.examplesTableInfo.LOCATION_TYPE, esInfo.getLocation_type());
        }
        if (esInfo.getFrom_streaming() != null) {
            xContentBuilder.field(Constants.examplesTableInfo.FROM_STREAMING, esInfo.getFrom_streaming());
        }
        if (esInfo.getRemarkNumber() != null) {
            xContentBuilder.field(Constants.examplesTableInfo.REMARK_NUMBER, esInfo.getRemarkNumber());
        }
        if (esInfo.getTwitterId() != null) {
            xContentBuilder.field(Constants.examplesTableInfo.TWITTER_STATUS_ID, esInfo.getTwitterId());
        }
        if (esInfo.getRelated_region() != null) {
            xContentBuilder.field("related_region", esInfo.getRelated_region());
        }
        if (esInfo.getRetweet_account_id() != null) {
            xContentBuilder.field("retweet_account_id", esInfo.getRetweet_account_id());
        }
        if (esInfo.getIs_quote_status() != null && esInfo.getIs_quote_status()) {
            xContentBuilder.field("is_quote_status", esInfo.getIs_quote_status());
        }
        if (esInfo.getIs_rubbish() != null && esInfo.getIs_rubbish()) {
            xContentBuilder.field("is_rubbish", esInfo.getIs_rubbish());
        }


        xContentBuilder.field(Constants.examplesTableInfo.IS_RETWEET, esInfo.getRetweet())
                .field(Constants.examplesTableInfo.CREATING_TIME, esInfo.getCreatingTIme())
                .field(Constants.examplesTableInfo.GATHER_TIME, esInfo.getGatherTime())
                .field(Constants.examplesTableInfo.UPDATE_TIME, esInfo.getUpdateTime())
                //.field(Constants.examplesTableInfo.SENSITIVE_ID, esInfo.getSensitiveId())
                .field(Constants.examplesTableInfo.CONTENT, esInfo.getContent())
                .field(Constants.examplesTableInfo.SOURCE_CONTENT, esInfo.getSourceContent())
                .field(Constants.examplesTableInfo.SOURCE_URL, esInfo.getSourceUrl())
                .field(Constants.examplesTableInfo.SITE_ID, esInfo.getSiteId())
                .field(Constants.examplesTableInfo.SITE_TYPE_ID, esInfo.getSiteTypeId())
                .field(Constants.examplesTableInfo.RETWEET_NUMBER, esInfo.getRetweetNumber())
                .field(Constants.examplesTableInfo.LIKE_NUMBER, esInfo.getLikeNumber())
                .field(Constants.examplesTableInfo.ACCOUNT_ID, esInfo.getAccountId())
                .field(Constants.examplesTableInfo.ACCOUNT_NICK_NAME, esInfo.getAccountNickName())
                .field(Constants.examplesTableInfo.TIME_SLOT, esInfo.getTimeSlot())
                .field(Constants.examplesTableInfo.LANGUAGE, esInfo.getLanguage())
                .field(Constants.examplesTableInfo.CREATING_DATE, esInfo.getCreatingDate())
                .field(Constants.examplesTableInfo.GATHER_DATE, esInfo.getGatherDate())
                .field(Constants.examplesTableInfo.VERIFIED, esInfo.getVerified())
                .endObject();

        return EsUtils.bulkCreateData(client, xContentBuilder, index, type, esInfo.getInfoId());

    }

    @Override
    public IndexRequestBuilder bulkAddEsVirtualData(Client client, String index, String type) throws Exception {

        this.esVirtual = new EsVirtual(examplesRoot, esInfo);
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
        Class clazz = esVirtual.getClass();
        Field[] fields = clazz.getDeclaredFields();
        xContentBuilder.startObject();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(esVirtual) != null) {
                xContentBuilder.field(field.getName(), field.get(esVirtual));
            }
        }
        xContentBuilder.endObject();

        return EsUtils.bulkCreateData(client, xContentBuilder, index, type, esInfo.getAccountId());
    }

    @Override
    public IndexRequestBuilder bulkAddTwitterUserData(Client client, String index, String type) throws Exception {
        return null;
    }

    @Override
    public IndexRequestBuilder updateEsVirtualData(Client client, String index, String type) {

        if (esVirtual.getAccount_id() != null) {
            try {
                XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
                Class clazz = esVirtual.getClass();
                Field[] fields = clazz.getDeclaredFields();
                xContentBuilder.startObject();
                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        if (field.get(esVirtual) != null) {
                            xContentBuilder.field(field.getName(), field.get(esVirtual));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                xContentBuilder.endObject();
                return EsUtils.bulkCreateData(client, xContentBuilder, index, type, esVirtual.getAccount_id());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public UpdateRequest updateUserData(String index, String type) {

        if (esVirtual.getAccount_id() != null) {
            try {
                XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
                Class clazz = esVirtual.getClass();
                Field[] fields = clazz.getDeclaredFields();
                xContentBuilder.startObject();
                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        if (field.get(esVirtual) != null) {
                            xContentBuilder.field(field.getName(), field.get(esVirtual));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                xContentBuilder.endObject();
                UpdateRequest updateRequest = new UpdateRequest(index, type, esVirtual.getAccount_id())
                        .doc(xContentBuilder);
                return updateRequest;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    @Override
    public void keyWordWarning(Connection mysqlConn, List<String> keyWordInfoList) {

        if (keyWordInfoList != null && keyWordInfoList.size() > 0) {
            /*关键词预预警*/
            for (String eventForInfoStr : keyWordInfoList) {
                //将string转为对象
                WarningMapping warningMapping = JSON.parseObject(eventForInfoStr, WarningMapping.class);

                int wordRalation = warningMapping.getWords_relation();
                /**如果是账号预警则先匹配账号**/
                if (warningMapping.getMonitor_type_id() != null && warningMapping.getMonitor_type_id() == 3) {
                    if (warningMapping.getAccount_id() != null && esInfo.getAccountId() != null) {
                        boolean isEqual = esInfo.getAccountId().equals(warningMapping.getAccount_id().trim());
                        //如果不是该账号则判断下一个预警信息
                        if (!isEqual) {
                            continue;
                        }
                    }
                }
                /**如果是事件预警则先判断时间**/
                if (warningMapping.getMonitor_type_id() != null && warningMapping.getMonitor_type_id() == 2) {
                    String postTimeStr = esInfo.getCreatingTIme();
                    Timestamp postTimeStamp = TimeUtils.transStrToTimeStamp(postTimeStr);
                    //先判断起始时间
                    if (warningMapping.getStart_time() != null) {
                        Timestamp startTimestamp = warningMapping.getStart_time();
                        //判断发帖时间是否早于起始时间，如果早于则进行报警，否则判断下一个
                        boolean isBeforeStartTime = TimeUtils.compareTime(startTimestamp, postTimeStamp);
                        if (!isBeforeStartTime) {
                            continue;
                        }
                    }
                    //判断结束时间
                    if (warningMapping.getEnd_time() != null) {
                        Timestamp endTime = warningMapping.getEnd_time();
                        //判断发帖时间是否晚于结束时间，如果晚于则进行报警，否则判断下一个
                        boolean isLaterEndTime = TimeUtils.compareTime(postTimeStamp, endTime);
                        if (!isLaterEndTime) {
                            continue;
                        }
                    }
                }

                /**如果是主题预警则先判断地理位置信息是否匹配**/
                if (warningMapping.getMonitor_type_id() != null && warningMapping.getMonitor_type_id() == 1) {
                    if (warningMapping.getCountry() != null) {
                        String parentIDMapping = warningMapping.getCountry();
                        if (!parentIDMapping.equals("0")) {
                            int isExists = 0;
                            if (esInfo.getParent_region_id_str() != null) {
                                if (esInfo.getParent_region_id_str().equals(parentIDMapping)) {
                                    isExists++;
                                }
                            }
                            if (esInfo.getRelated_region() != null) {
                                List<Map<String, Object>> relatedRegions = esInfo.getRelated_region();
                                for (Map<String, Object> map : relatedRegions) {
                                    if (map.get("parent_region_id") != null) {
                                        String relatedParentRegionID = map.get("parent_region_id").toString();
                                        if (relatedParentRegionID.equals(parentIDMapping)) {
                                            isExists++;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (isExists == 0) {
                                continue;
                            }
                        }
                    }
                }

                //都符合要求则开始预警流程先得到关键词
                if (warningMapping.getKey_words() != null) {
                    //得到关键字
                    String[] strings = warningMapping.getKey_words().split(",");
                    //将数组转list
                    List<String> wordList = Arrays.asList(strings);
                    //生成原始关键字list
                    ArrayList<String> arrayList = new ArrayList<String>();
                    arrayList.addAll(wordList);
                    //原始关键字长度
                    int keyWordLegth = arrayList.size();
                    //生成queryString的set
                    Set<String> queryStringSet = new HashSet<String>();
                    //当原始list不为空时
                    int wordListLen = arrayList.size();
                    //当关键字关系是3时
                    if (wordListLen >= 1) {
                        for (int i = 0; i < wordListLen; i++) {
                            String keyWord = arrayList.get(i);
                            if (keyWord.contains(" AND ")) {
                                String[] keyWordAnd = keyWord.split(" AND ");
                                List<String> keyWordList = Arrays.asList(keyWordAnd);
                                KeywordFilter filter = new KeywordFilter(keyWordList);
                                Set<String> andSet = filter.getKeyWord(esInfo.getContent(), 1);
                                if (andSet.size() == keyWordAnd.length) {
                                    //返回全部匹配的集合
                                    queryStringSet.addAll(andSet);
                                }
                                arrayList.remove(i);
                                wordListLen--;
                            } else if (keyWord.contains(" OR ")) {
                                String[] keyWordOR = keyWord.split(" OR ");
                                List<String> keyWordList = Arrays.asList(keyWordOR);
                                KeywordFilter filter = new KeywordFilter(keyWordList);
                                Set<String> orSet = filter.getKeyWord(esInfo.getContent(), 1);
                                if (orSet.size() > 0) {
                                    //返回匹配的集合
                                    queryStringSet.addAll(orSet);
                                }
                                arrayList.remove(i);
                                wordListLen--;
                            }
                        }
                    }

                    //生成总set集
                    Set<String> allKeyWordSet = new HashSet<String>();

                    //关键词预警
                    if (arrayList.size() > 0) {
                        KeywordFilter filter = new KeywordFilter(arrayList);
                        Set<String> set = filter.getKeyWord(esInfo.getContent(), 1);
                        allKeyWordSet.addAll(set);
                    }
                    int allWarningLegth = allKeyWordSet.size();
                    if (queryStringSet.size() > 0) {
                        allWarningLegth++;
                    }
                    //判断关键字预警的关系
                    if (wordRalation == 1) {
                        allKeyWordSet.addAll(queryStringSet);
                        if (allWarningLegth == keyWordLegth) {
                            excuteTwitterWarn(warningMapping, allKeyWordSet, mysqlConn);
                        }
                    } else if (wordRalation == 2) {
                        if (allWarningLegth > 0) {
                            excuteTwitterWarn(warningMapping, allKeyWordSet, mysqlConn);
                        }
                    } else if (wordRalation == 3) {
//                        if (allWarningLegth > 0) {
//                            excuteTwitterWarn(warningMapping, allKeyWordSet, mysqlConn);
//                        }
                    }

                }

            }

        }
    }

    public void excuteTwitterWarn(WarningMapping warningMapping, Set<String> set, Connection mysqlConn) {
        //当所属账号包含关键字预警时写入mysql
        KeywordOutput eventOutput = new KeywordOutput();
        eventOutput.setId(InfoIdUtils.get32UUID());
        eventOutput.setUser_id(warningMapping.getUser_id());
        eventOutput.setInfo_id(esInfo.getInfoId());
        eventOutput.setWarning_conf_id(warningMapping.getWarning_conf_id());
        eventOutput.setMonitor_type_id(warningMapping.getMonitor_type_id());
        eventOutput.setMonitor_entity_id(warningMapping.getMonitor_entity_id());
        eventOutput.setWarning_type_id(4);
        eventOutput.setMatched_words(set.toString().replace("[", "").replace("]", ""));
        eventOutput.setAlarm_mode_ids(warningMapping.getAlarm_mode());
        eventOutput.setStatus(2);
        eventOutput.setAuthor(esInfo.getAccountNickName());
        if (esInfo.getSummary() != null) {
            eventOutput.setSummary(esInfo.getSummary());
        } else {
            eventOutput.setSummary(esInfo.getContent());
        }
        eventOutput.setContent(esInfo.getContent());
        eventOutput.setSite_id(301);
        eventOutput.setSite_type_id(1);
        eventOutput.setCreate_post_time(esInfo.getCreatingTIme());
        eventOutput.setWarning_time(TimeUtils.getTime());
        //增加事件名
        if (warningMapping.getEvent_name() != null) {
            eventOutput.setMonitor_entity_name(warningMapping.getEvent_name());//事件有
        }
        if (warningMapping.getTheme_name() != null) {
            eventOutput.setMonitor_entity_name(warningMapping.getTheme_name());//事件有
        }
        if (warningMapping.getNick_name() != null) {
            eventOutput.setMonitor_entity_name(warningMapping.getNick_name());//事件有
        }
//                        eventOutput.setAbnormal_time();
//                        eventOutput.setNormal_place();
//                        eventOutput.setAbnormal_place();
        EventForeUtils eventForeUtils = new EventForeUtils(mysqlConn);
        try {
            boolean flag = eventForeUtils.saveObject(eventOutput);//写入数据库
            eventForeUtils.releasePstmt();
            if (flag) {
                logger.info("insert into event warning succeed from twitter");
            } else {
                logger.info("insert into event warning failure from twitter");
            }

        } catch (Exception e) {
            e.printStackTrace();
            eventForeUtils.releasePstmt();
            logger.error("event input mysql error from twitter:" + e);
        }

    }


    /**
     * 时间和账号异常监测
     * @param list 账号时间和位置统计表list

     * @param eventForeList 预警配置表list
     * @param accountList 账号list
     * @param accountTimeList 账号最新更新时间list
     */
    @Override
    public void accoutAndLocationWarning(List<String> list, List<String> eventForeList, List<String> accountList, List<String> accountTimeList) {

        if (accountList != null) {

            if (accountList.contains(esInfo.getAccountId())) {
                boolean flag = false;
                if (accountTimeList != null) {
                    System.out.println("进入到了");
                    for (String timeStr : accountTimeList) {
                        try {
                            AccountLastTime accountLastTime = JSON.parseObject(timeStr, AccountLastTime.class);
                            if (accountLastTime.getAccount_id() != null && accountLastTime.getAccount_id().trim().equals(esInfo.getAccountId().trim())) {
                                String crateTime = esInfo.getCreatingTIme();
                                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                try {
                                    Date date = sf.parse(crateTime);
                                    if (accountLastTime.getLatest_time() != null) {
                                        flag = date.after(accountLastTime.getLatest_time());
                                    } else {
                                        flag = true;
                                    }
                                    break;

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error("account lastTime get error");
                        }
                    }
                }
                else {
                    flag = true;
                }

                System.out.println("flag:"+flag);
                if (flag) {
                    AccountCheck accountCheck = new AccountCheck();
                    try {
                        //获取帖子能够提供的信息
                        String infoId = esInfo.getInfoId();
                        String  monitorEntityId = esInfo.getAccountId();
                        String author = esInfo.getAccountNickName();
                        String content = esInfo.getContent();
                        String summary = esInfo.getContent();
                        int siteId = esInfo.getSiteId();
                        int siteTypeId = esInfo.getSiteTypeId();
                        String createPostTime = esInfo.getCreatingTIme();

                        //匹配信息
                        String timeSlot = String.valueOf(esInfo.getTimeSlot());
                        String regionId = esInfo.getRegion_id_str();

                        accountCheck.accountCheckForTimeAndPlace( infoId,monitorEntityId,author,content,summary,siteId,siteTypeId,createPostTime,timeSlot,regionId,list, eventForeList);

                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("account waring error");
                    }
                }

            }
        }

    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

}


