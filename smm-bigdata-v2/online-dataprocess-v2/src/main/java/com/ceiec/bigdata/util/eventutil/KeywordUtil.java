package com.ceiec.bigdata.util.eventutil;

import java.util.*;

public class KeywordUtil {

    private Map<String, Object> dictionaryMap;

    public KeywordUtil(Set<String> wordSet) {
        this.dictionaryMap = handleToMap(wordSet);
    }

    public Map<String, Object> getDictionaryMap() {
        return dictionaryMap;
    }

    public void setDictionaryMap(Map<String, Object> dictionaryMap) {
        this.dictionaryMap = dictionaryMap;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> handleToMap(Set<String> wordSet) {
        if (wordSet == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>(wordSet.size());
        Map<String, Object> curMap = null;
        Iterator<String> ite = wordSet.iterator();
        while (ite.hasNext()) {
            String word = ite.next();
            curMap = map;
            int len = word.length();
            for (int i = 0; i < len; i++) {
                String key = String.valueOf(word.charAt(i));
                Map<String, Object> wordMap = (Map<String, Object>) curMap
                        .get(key);
                if (wordMap == null) {
                    wordMap = new HashMap<String, Object>();
                    wordMap.put("isEnd", "0");
                    curMap.put(key, wordMap);
                    curMap = wordMap;
                }
                else {
                    curMap = wordMap;
                }
                if (i == len - 1) {
                    curMap.put("isEnd", "1");
                }
            }
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public int checkWord(String text, int beginIndex) {
        if (dictionaryMap == null) {
            throw new RuntimeException("字典不能为空！");
        }
        boolean isEnd = false;
        int wordLength = 0;
        Map<String, Object> curMap = dictionaryMap;
        int len = text.length();
        for (int i = beginIndex; i < len; i++) {
            String key = String.valueOf(text.charAt(i));
            curMap = (Map<String, Object>) curMap.get(key);
            if (curMap == null) {
                break;
            }
            else {
                wordLength++;
                if ("1".equals(curMap.get("isEnd"))) {
                    isEnd = true;
                }
            }
        }
        if (!isEnd) {
            wordLength = 0;
        }
        return wordLength;
    }

    public Set<String> getWords(String text) {
        Set<String> wordSet = new HashSet<String>();
        int len = text.length();
        for (int i = 0; i < len; i++) {
            int wordLength = checkWord(text, i);
            if (wordLength > 0) {
                String word = text.substring(i, i + wordLength);
                wordSet.add(word);
                i = i + wordLength - 1;
            }
        }
        return wordSet;
    }

    public static void main(String[] args) {
        String[] strings = "hi AND hello,abc,hh,jiayou,bixu OR de".split(",");
        //将数组转list
        List<String> wordList = Arrays.asList(strings);
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.addAll(wordList);
        //生成总set集
        Set<String> allKeyWordSet = new HashSet<String>();
        int wordListLen = arrayList.size();
        if( wordListLen > 1){
            for (int i=0; i < wordListLen;i++ ){
                String keyWord = arrayList.get(i) ;
                if(keyWord.contains(" AND ")){
                    String[] keyWordAnd = keyWord.split(" AND ");
                    List<String> keyWordList = Arrays.asList(keyWordAnd);
                    KeywordFilter filter = new KeywordFilter(keyWordList);
                    Set<String> andSet = filter.getKeyWord("nasdnuguashgaf abc hello  de hi  he", 1);
                    if(andSet.size() == keyWordAnd.length){
                        //返回全部匹配的集合
                        allKeyWordSet.addAll(andSet);
                    }
                    arrayList.remove(i);
                    wordListLen--;
                }
                else if (keyWord.contains(" OR ")){
                    String[] keyWordOR = keyWord.split(" OR ");
                    List<String> keyWordList = Arrays.asList(keyWordOR);
                    KeywordFilter filter = new KeywordFilter(keyWordList);
                    Set<String> orSet = filter.getKeyWord("nasdnuguashgaf abc hello  de hi  he", 1);
                    if(orSet.size() > 0){
                        //返回匹配的集合
                        allKeyWordSet.addAll(orSet);
                    }
                    arrayList.remove(i);
                    wordListLen--;
                }
            }
        }

        //关键词预警
        if(arrayList.size() > 0){
            KeywordFilter filter = new KeywordFilter(arrayList);
            Set<String> set = filter.getKeyWord("nasdnuguashgaf abc hello  de hi  he", 1);
            allKeyWordSet.addAll(set);
        }
        System.out.println(allKeyWordSet);
    }

}
