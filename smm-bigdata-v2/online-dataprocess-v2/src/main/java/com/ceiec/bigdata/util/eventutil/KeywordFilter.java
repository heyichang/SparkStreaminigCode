package com.ceiec.bigdata.util.eventutil;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by heyichang on 2017/11/13.
 */
public class KeywordFilter {

    @SuppressWarnings("rawtypes")
    private Map keyWordMap = null;
    public static int minMatchTYpe = 1;      //最小匹配规则
    public static int maxMatchType = 2;      //最大匹配规则


    /**
     * 构造函数，初始化敏感词库
     */
    public KeywordFilter(List<String> sensitiveWords) {
        keyWordMap = new KeyWordInit(sensitiveWords).initKeyWordByGenetor();
    }

    /**
     * 构造函数，初始化敏感词库
     */
    public KeywordFilter(Set<String> sensitiveWords) {
        keyWordMap = new KeyWordInit(sensitiveWords).initKeyWordByGenetor();
    }

    /**
     * 获取文字中的敏感词
     *
     * @param txt       文字
     * @param matchType 匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
     * @return
     * @author thinkpad
     * @date 2017年11月13日 下午5:10:52
     * @version 1.0
     */
    public Set<String> getKeyWord(String txt, int matchType) {
        Set<String> sensitiveWordSet = new HashSet<String>();
        if(txt!=null){
        for (int i = 0; i < txt.length(); i++) {
            int length = CheckSensitiveWord(txt, i, matchType);    //判断是否包含敏感字符
            if (length > 0) {    //存在,加入list中
                sensitiveWordSet.add(txt.substring(i, i + length));
                i = i + length - 1;    //减1的原因，是因为for会自增
            }
        }
        }
        return sensitiveWordSet;
    }



    /**
     * 检查文字中是否包含敏感字符，检查规则如下：<br>
     *
     * @param txt
     * @param beginIndex
     * @param matchType
     * @author thinkpad
     * @date 2017年11月13日 下午4:31:03
     * @return，如果存在，则返回敏感词字符的长度，不存在返回0
     * @version 1.0
     */
    @SuppressWarnings({"rawtypes"})
    public int CheckSensitiveWord(String txt, int beginIndex, int matchType) {
        boolean flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
        int matchFlag = 0;     //匹配标识数默认为0
        char word = 0;
        Map nowMap = keyWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);     //获取指定key
            if (nowMap != null) {     //存在，则判断是否为最后一个
                matchFlag++;     //找到相应key，匹配标识+1
                if ("1".equals(nowMap.get("isEnd"))) {       //如果为最后一个匹配规则,结束循环，返回匹配标识数
                    flag = true;       //结束标志位为true
                    if (KeywordFilter.minMatchTYpe == matchType) {    //最小规则，直接返回,最大规则还需继续查找
                        break;
                    }
                }
            } else {     //不存在，直接返回
                break;
            }
        }
        if (matchFlag < 2 || !flag) {        //长度必须大于等于1，为词
            matchFlag = 0;
        }
        return matchFlag;
    }


}
