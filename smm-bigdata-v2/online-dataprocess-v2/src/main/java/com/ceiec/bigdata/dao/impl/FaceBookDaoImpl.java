package com.ceiec.bigdata.dao.impl;

import com.alibaba.fastjson.JSON;
import com.ceiec.bigdata.dao.FaceBookDao;
import com.ceiec.bigdata.entity.AllFacebookPost;
import com.ceiec.bigdata.entity.FacebookUserType;
import com.ceiec.bigdata.entity.facebook.FaceBookRoot;
import com.ceiec.bigdata.entity.facebook.es.FaceBookUserCommon;
import com.ceiec.bigdata.entity.facebook.es.FacebookPostCommon;
import com.ceiec.bigdata.entity.table.keyword.KeywordOutput;
import com.ceiec.bigdata.entity.table.keyword.WarningMapping;
import com.ceiec.bigdata.util.InfoIdUtils;
import com.ceiec.bigdata.util.TimeUtils;
import com.ceiec.bigdata.util.esutil.EsUtils;
import com.ceiec.bigdata.util.eventutil.EventForeUtils;
import com.ceiec.bigdata.util.eventutil.KeywordFilter;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by heyichang on 2018/2/9.
 */
public class FaceBookDaoImpl implements FaceBookDao {
    private static final Logger logger = LoggerFactory.getLogger(FaceBookDaoImpl.class);
    private FaceBookUserCommon faceBookUserCommon;
    private FacebookPostCommon facebookPostCommon;
    //private FaceBookVirtual faceBookVirtual;
    private FaceBookRoot fbRoot;
    private String accountId;

    public FaceBookDaoImpl(FacebookUserType facebookUserType) {
        this.faceBookUserCommon = new FaceBookUserCommon(facebookUserType);
    }

    public FaceBookDaoImpl(AllFacebookPost faceBookPost, String nlpString ,KeywordFilter keywordFilter , Map<String ,List<String>> regionMap) {
        this.facebookPostCommon = new FacebookPostCommon(faceBookPost, nlpString ,keywordFilter , regionMap );
    }

    public FaceBookDaoImpl(AllFacebookPost faceBookPost, String nlpString ) {

    }

    public FaceBookDaoImpl(FaceBookRoot fbRoot, String nlpResult) {
        this.fbRoot = fbRoot;

    }

    @Override
    public IndexRequestBuilder bulkAddPostEsData(Client client, String index, String type) throws Exception {
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
        Class clazz = facebookPostCommon.getClass();
        Field[] fields = clazz.getDeclaredFields();
        xContentBuilder.startObject();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(facebookPostCommon) != null) {
                xContentBuilder.field(field.getName(), field.get(facebookPostCommon));
            }
        }
        xContentBuilder.endObject();
        return EsUtils.bulkCreateData(client, xContentBuilder, index, type, facebookPostCommon.getInfo_id());
    }

    @Override
    public IndexRequestBuilder bulkAddEsData(Client client, String index, String type) throws Exception {
        return null;
    }

    @Override
    public IndexRequestBuilder bulkAddEsVirtualData(Client client, String index, String type) throws Exception {

//        FaceBookVirtual faceBookVirtual;
//        if (fbRoot.getUser() != null) {
//            faceBookVirtual = new FaceBookVirtual(fbRoot, faceBookInfo);
//
//            XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
//            Class clazz = faceBookVirtual.getClass();
//            Field[] fields = clazz.getDeclaredFields();
//            xContentBuilder.startObject();
//            for (Field field : fields) {
//                field.setAccessible(true);
//                if (field.get(faceBookVirtual) != null) {
//                    xContentBuilder.field(field.getName(), field.get(faceBookVirtual));
//                }
//            }
//            xContentBuilder.endObject();
//
//            return EsUtils.bulkCreateData(client, xContentBuilder, index, type, faceBookVirtual.getAccount_id());
//        }
        return null;
    }

    @Override
    public IndexRequestBuilder bulkAddEsUserData(Client client, String index, String type) throws Exception {
        FaceBookUserCommon faceBookUserCommon = this.faceBookUserCommon;
        if (faceBookUserCommon != null && faceBookUserCommon.getAccount_id() != null) {
            XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
            Class clazz = faceBookUserCommon.getClass();
            Field[] fields = clazz.getDeclaredFields();
            xContentBuilder.startObject();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(faceBookUserCommon) != null) {
                    xContentBuilder.field(field.getName(), field.get(faceBookUserCommon));
                }
            }
            xContentBuilder.endObject();
            return EsUtils.bulkCreateData(client, xContentBuilder, index, type, faceBookUserCommon.getAccount_id());
        }
        return null;
    }

    @Override
    public IndexRequestBuilder bulkAddEsVirtualData(Client client, String index, String type, FaceBookUserCommon faceBookUserCommon) throws Exception {
        if (faceBookUserCommon != null) {
            XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
            Class clazz = faceBookUserCommon.getClass();
            Field[] fields = clazz.getDeclaredFields();
            xContentBuilder.startObject();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(faceBookUserCommon) != null) {
                    xContentBuilder.field(field.getName(), field.get(faceBookUserCommon));
                }
            }
            xContentBuilder.endObject();

            return EsUtils.bulkCreateData(client, xContentBuilder, index, type, faceBookUserCommon.getAccount_id());
        }
        return null;
    }

    @Override
    public IndexRequestBuilder updateEsVirtualData(Client client, String index, String type) {
        return null;
    }

    @Override
    public void keyWordWarning(Connection mysqlConn, List<String> keyWordInfoList) {
        if (keyWordInfoList != null && keyWordInfoList.size() > 0) {
            /*关键词预预警*/
            for (String eventForInfoStr : keyWordInfoList) {
                //将string转为对象
                WarningMapping warningMapping = JSON.parseObject(eventForInfoStr, WarningMapping.class);
                /**如果是账号预警则先匹配账号**/
                if (warningMapping.getMonitor_type_id() != null && warningMapping.getMonitor_type_id() == 3) {
                    if (warningMapping.getAccount_id() != null && facebookPostCommon.getAccount_id() != null) {
                        boolean isEqual = facebookPostCommon.getAccount_id().equals(warningMapping.getAccount_id().trim());
                        //如果不是该账号则判断下一个预警信息
                        if (!isEqual) {
                            continue;
                        }
                    }
                }
                /**如果是事件预警则先判断时间**/
                if (warningMapping.getMonitor_type_id() == 2) {
                    String postTimeStr = facebookPostCommon.getCreating_time();
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
                            if (facebookPostCommon.getRelated_region() != null) {
                                List<Map<String, Object>> relatedRegions = facebookPostCommon.getRelated_region();
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




                //得到关键词
                if (warningMapping.getKey_words() != null) {
                    //得到关键字
                    String[] strings = warningMapping.getKey_words().split(",");
                    int wordRalation = warningMapping.getWords_relation();
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


                    if (wordListLen > 1) {
                        for (int i = 0; i < wordListLen; i++) {
                            String keyWord = arrayList.get(i);
                            if (keyWord.contains(" AND ")) {
                                String[] keyWordAnd = keyWord.split(" AND ");
                                List<String> keyWordList = Arrays.asList(keyWordAnd);
                                KeywordFilter filter = new KeywordFilter(keyWordList);
                                Set<String> andSet = filter.getKeyWord(facebookPostCommon.getContent(), 1);
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
                                Set<String> orSet = filter.getKeyWord(facebookPostCommon.getContent(), 1);
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
                        Set<String> set = filter.getKeyWord(facebookPostCommon.getContent(), 1);
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
                            excuteFaceBookWarn(warningMapping, allKeyWordSet, mysqlConn);
                        }
                    } else if (wordRalation == 2) {
                        if (allWarningLegth > 0) {
                            excuteFaceBookWarn(warningMapping, allKeyWordSet, mysqlConn);
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

    public void excuteFaceBookWarn(WarningMapping warningMapping, Set<String> set, Connection mysqlConn) {
        //当所属账号包含关键字预警时写入mysql
        KeywordOutput eventOutput = new KeywordOutput();
        eventOutput.setId(InfoIdUtils.get32UUID());
        eventOutput.setUser_id(warningMapping.getUser_id());
        eventOutput.setInfo_id(facebookPostCommon.getInfo_id());
        eventOutput.setWarning_conf_id(warningMapping.getWarning_conf_id());
        eventOutput.setMonitor_type_id(warningMapping.getMonitor_type_id());
        eventOutput.setMonitor_entity_id(warningMapping.getMonitor_entity_id());
//                        eventOutput.setMonitor_entity_name(warningMapping.getMonitor_entity_id());//事件有
        eventOutput.setWarning_type_id(4);//监控账号有
        eventOutput.setMatched_words(set.toString().replace("[", "").replace("]", ""));
        eventOutput.setAlarm_mode_ids(warningMapping.getAlarm_mode());
        eventOutput.setStatus(2);
        eventOutput.setAuthor(facebookPostCommon.getAccount_nick_name());
        if (facebookPostCommon.getSummary() != null) {
            eventOutput.setSummary(facebookPostCommon.getSummary());
        } else {
            eventOutput.setSummary(facebookPostCommon.getContent());
        }
        //增加事件名
        if (warningMapping.getEvent_name() != null) {
            eventOutput.setMonitor_entity_name(warningMapping.getEvent_name());
        }
        if (warningMapping.getTheme_name() != null) {
            eventOutput.setMonitor_entity_name(warningMapping.getTheme_name());
        }
        if (warningMapping.getNick_name() != null) {
            eventOutput.setMonitor_entity_name(warningMapping.getNick_name());
        }
        eventOutput.setContent(facebookPostCommon.getContent());
        eventOutput.setSite_id(302);
        eventOutput.setSite_type_id(2);
        eventOutput.setCreate_post_time(facebookPostCommon.getCreating_time());
        eventOutput.setWarning_time(TimeUtils.getTime());
//                        eventOutput.setAbnormal_time();
//                        eventOutput.setNormal_place();
//                        eventOutput.setAbnormal_place();
        EventForeUtils eventForeUtils = new EventForeUtils(mysqlConn);
        try {
            boolean flag = eventForeUtils.saveObject(eventOutput);//写入数据库
            eventForeUtils.releasePstmt();
            if (flag) {
                logger.info("insert into event warning succeed from facebook");
            } else {
                logger.info("insert into event warning failure from facebook");
            }

        } catch (Exception e) {
            e.printStackTrace();
            eventForeUtils.releasePstmt();
            logger.error("event input mysql error from facebook:" + e);
        }
    }

    @Override
    public void accoutAndLocationWarning(List<String> list, List<String> forelist, List<String> accountList, List<String> accountTimeList) {

    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
