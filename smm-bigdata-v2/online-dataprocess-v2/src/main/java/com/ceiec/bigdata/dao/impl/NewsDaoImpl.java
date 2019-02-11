package com.ceiec.bigdata.dao.impl;

import com.alibaba.fastjson.JSON;
import com.ceiec.bigdata.dao.NewsDao;
import com.ceiec.bigdata.entity.AllNewsPost;
import com.ceiec.bigdata.entity.news.*;
import com.ceiec.bigdata.entity.table.keyword.KeywordOutput;
import com.ceiec.bigdata.entity.table.keyword.WarningMapping;
import com.ceiec.bigdata.util.Constants;
import com.ceiec.bigdata.util.InfoIdUtils;
import com.ceiec.bigdata.util.TimeUtils;
import com.ceiec.bigdata.util.esutil.EsUtils;
import com.ceiec.bigdata.util.eventutil.EventForeUtils;
import com.ceiec.bigdata.util.eventutil.KeywordFilter;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by heyichang on 2017/11/7.
 */
public class NewsDaoImpl implements NewsDao {

    private NewsHbase newsHbase;
    private NewsEs newsEs;
    private News news;
    private NewsEsCommon newsEsCommon;
    private static final Logger logger = LoggerFactory.getLogger(NewsDaoImpl.class);

    public NewsDaoImpl() {
    }

    public NewsDaoImpl(AllNewsPost allNewsPost, Map<String, List<Object>> siteMap, String nlpResultString ) {
        // this.newsHbase = new NewsHbase(news);

    }

    public NewsDaoImpl(AllNewsPost allNewsPost, Map<String, List<Object>> siteMap, String nlpResultString ,KeywordFilter keywordFilter, Map<String, List<String>> regionMap) {
        // this.newsHbase = new NewsHbase(news);
        this.newsEsCommon = new NewsEsCommon(allNewsPost, siteMap, nlpResultString ,keywordFilter , regionMap);


    }

    public NewsDaoImpl(News news, Map<String, List<Integer>> siteMap, List<String> sensitiveWords) {
        this.news = news;
        // this.newsHbase = new NewsHbase(news);
        this.newsEs = new NewsEs(news, siteMap, sensitiveWords);

    }

    public NewsDaoImpl(News news, Map<String, List<Object>> siteMap, String newsJson, String nlpResultString) {
        this.news = news;
        this.newsHbase = new NewsHbase(newsJson);
        this.newsEs = new NewsEs(news, siteMap, nlpResultString);

    }


    @Override
    public Put getHbaseRowPut() {
        Put put = new Put(Bytes.toBytes(newsHbase.getRowkey()));
        Class clazz = newsHbase.getClass();
        // 参数分别:列族、列、值
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(newsHbase) != null) {
                    put.addColumn(Bytes.toBytes(Constants.examplesTableInfo.FAMILY), Bytes.toBytes(field.getName()), Bytes.toBytes((String) field.get(newsHbase)));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return put;
    }

    public void batchAddHbaseRow(List<Put> putList, Table table) {

        try {
            table.put(putList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IndexRequestBuilder bulkAddEsData(Client client, String index, String type) throws Exception {
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();
        Class clazz = newsEsCommon.getClass();
        Field[] fields = clazz.getDeclaredFields();
        xContentBuilder.startObject();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.get(newsEsCommon) != null) {
                xContentBuilder.field(field.getName(), field.get(newsEsCommon));
            }
        }
        xContentBuilder.endObject();
        return EsUtils.bulkCreateData(client, xContentBuilder, index, type, InfoIdUtils.encodeByMD5(newsEsCommon.getSource_url()));
    }

    @Override
    public IndexRequestBuilder bulkAddEsVirtualData(Client client, String index, String type) throws Exception {
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder();

        return EsUtils.bulkCreateData(client, xContentBuilder, index, type, "");
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
                        boolean isEqual = false;
                        //如果不是该账号则判断下一个预警信息
                        if (!isEqual) {
                            continue;
                        }
                }

                /**如果是事件预警则先判断时间**/
                if (warningMapping.getMonitor_type_id() == 2 && newsEsCommon.getCreating_time() != null) {
                    String postTimeStr = newsEsCommon.getCreating_time();
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

                            if (newsEsCommon.getRelated_region() != null) {
                                List<Map<String, Object>> relatedRegions = newsEsCommon.getRelated_region();
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
                                Set<String> andSet = filter.getKeyWord(newsEsCommon.getContent(), 1);
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
                                Set<String> orSet = filter.getKeyWord(newsEsCommon.getContent(), 1);
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
                        Set<String> set = filter.getKeyWord(newsEsCommon.getContent(), 1);
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
                            excuteNewsWarn(warningMapping, allKeyWordSet, mysqlConn);
                        }
                    } else if (wordRalation == 2) {
                        if (allWarningLegth > 0) {
                            excuteNewsWarn(warningMapping, allKeyWordSet, mysqlConn);
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

    public void excuteNewsWarn(WarningMapping warningMapping, Set<String> set, Connection mysqlConn) {
        //当所属账号包含关键字预警时写入mysql
        KeywordOutput eventOutput = new KeywordOutput();
        eventOutput.setId(InfoIdUtils.get32UUID());
        eventOutput.setUser_id(warningMapping.getUser_id());
        eventOutput.setInfo_id(newsEsCommon.getInfo_id());
        eventOutput.setWarning_conf_id(warningMapping.getWarning_conf_id());
        eventOutput.setMonitor_type_id(warningMapping.getMonitor_type_id());
        eventOutput.setMonitor_entity_id(warningMapping.getMonitor_entity_id());
//                        eventOutput.setMonitor_entity_name(warningMapping.getMonitor_entity_id());//事件有
        eventOutput.setWarning_type_id(4);//监控账号有
        eventOutput.setMatched_words(set.toString().replace("[", "").replace("]", ""));
        eventOutput.setAlarm_mode_ids(warningMapping.getAlarm_mode());
        eventOutput.setStatus(2);
        eventOutput.setAuthor(newsEsCommon.getAccount_nick_name());
        if (newsEsCommon.getSummary() != null) {
            eventOutput.setSummary(newsEsCommon.getSummary());
        } else {
            if (newsEsCommon.getContent() != null && newsEsCommon.getContent().length() > 1000) {
                String newStr = newsEsCommon.getContent().toString().substring(0, 999);
                eventOutput.setSummary(newStr);//Data truncation: Data too long for column 'summary'
            } else {
                eventOutput.setSummary(newsEsCommon.getContent());
            }
        }
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
        eventOutput.setContent(newsEsCommon.getContent());
        eventOutput.setSite_id(newsEsCommon.getSite_id());
        eventOutput.setSite_type_id(newsEsCommon.getSite_type_id());
        eventOutput.setCreate_post_time(newsEsCommon.getCreating_time());
        eventOutput.setWarning_time(TimeUtils.getTime());
//                        eventOutput.setAbnormal_time();
//                        eventOutput.setNormal_place();
//                        eventOutput.setAbnormal_place();
        EventForeUtils eventForeUtils = new EventForeUtils(mysqlConn);
        try {
            boolean flag = eventForeUtils.saveObject(eventOutput);//写入数据库
            eventForeUtils.releasePstmt();
            if (flag) {
                logger.info("insert into event warning succeed from news");
            } else {
                logger.info("insert into event warning failure from news");
            }

        } catch (Exception e) {
            e.printStackTrace();
            eventForeUtils.releasePstmt();
            logger.error("event input mysql error from news:" + e);
        }
    }


}
