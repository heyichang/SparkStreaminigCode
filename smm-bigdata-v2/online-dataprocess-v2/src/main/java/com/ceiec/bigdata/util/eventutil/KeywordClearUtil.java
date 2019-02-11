package com.ceiec.bigdata.util.eventutil;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.currentTimeMillis;

public class KeywordClearUtil {
    public static void main(String[] args) {


    }

    public static Set<String> getCompletedSet(String text, Set<String> matchedWordSet) {
        int setSize = matchedWordSet.size();
        Set<String> newSet = new HashSet<String>();
        Iterator<String> setIterator = matchedWordSet.iterator();
        while (setIterator.hasNext()) {
            String matchedWord = setIterator.next();
            boolean flag = isCompletedWord(text, matchedWord);

            if (flag == true) {
                newSet.add(matchedWord);
            }
        }

        return newSet;

    }


    public static boolean isCompletedWord(String text, String matchedWord ) {
        boolean isMatched = false;
        if (matchedWord != null && !matchedWord.trim().equals("") && text != null) {
            String newText = text;
            String notClearedWord = getNotClearedWord(newText, matchedWord);
            if (notClearedWord != null) {
                int a = 0;
                while (newText.contains(matchedWord)) {
                    notClearedWord = getNotClearedWord(newText, matchedWord);
                    if (notClearedWord != null) {
                        String clearedWord = getclearedWord(notClearedWord);
                        isMatched = compareWord(clearedWord, matchedWord);
                        if (isMatched) {
                            return isMatched;
                        }
                        List<Integer> list =getCharsIndex(newText,matchedWord);
                        int allLen = newText.length();
                        String start = newText.substring(0,list.get(0));
                        String end = newText.substring(list.get(1) );
                        String endStr = start + end;
                        newText = endStr;
//                        System.out.println(notClearedWord);
//                        newText = newText.replaceAll(notClearedWord, "");

                    }
                    a ++;
                    if(a >20 ){
                        break;
                    }
                }
            }

        }
        return isMatched;

    }

    public static boolean compareWord(String clearedWord, String matchedWord) {

        boolean isMatched = clearedWord.equals(matchedWord);
        return isMatched;

    }


    public static String getNotClearedWord(String text, String matchedWord) {
        if (text != null && matchedWord != null) {
            int allStrLen = text.length();
            //匹配词的长度
            int matchedWordlen = matchedWord.length();
            //匹配词在原文开始的index
            int wordStartIndex = text.indexOf(matchedWord);
            //匹配词在原文结束的index
            int wordEndIndex = wordStartIndex + matchedWordlen;
            boolean isconfirmedWord = false;
            //匹配词在原文中的样子
            String newStr = null;
            //匹配词在中间时
            if (wordStartIndex > 0 && wordEndIndex < allStrLen) {
                int allLen = wordStartIndex + matchedWordlen + 1;
                int stratIndex = wordStartIndex - 1;
                newStr = getOriginalWord(text, stratIndex, allLen);
            }
            //匹配词在最前面时
            else if (wordStartIndex == 0 && wordEndIndex < allStrLen) {
                int allLen = wordStartIndex + matchedWordlen + 1;
                int stratIndex = wordStartIndex;
                newStr = getOriginalWord(text, stratIndex, allLen);
            }
            //匹配词在最后面时
            else if (wordStartIndex > 0 && wordEndIndex == allStrLen) {
                int allLen = wordStartIndex + matchedWordlen;
                int stratIndex = wordStartIndex - 1;
                newStr = getOriginalWord(text, stratIndex, allLen);
            }
            return newStr;
        }
        return null;
    }

    public static List<Integer> getCharsIndex(String text, String matchedWord) {
        if (text != null && matchedWord != null) {
            List<Integer> list = new ArrayList<>();

            int allStrLen = text.length();
            //匹配词的长度
            int matchedWordlen = matchedWord.length();
            //匹配词在原文开始的index
            int wordStartIndex = text.indexOf(matchedWord);
            //匹配词在原文结束的index
            int wordEndIndex = wordStartIndex + matchedWordlen;
            boolean isconfirmedWord = false;
            //匹配词在原文中的样子
            String newStr = null;
            //匹配词在中间时
            if (wordStartIndex > 0 && wordEndIndex < allStrLen) {
                int allLen = wordStartIndex + matchedWordlen + 1;
                int stratIndex = wordStartIndex - 1;
                list.add(stratIndex);
                list.add(allLen);

            }
            //匹配词在最前面时
            else if (wordStartIndex == 0 && wordEndIndex < allStrLen) {
                int allLen = wordStartIndex + matchedWordlen + 1;
                int stratIndex = wordStartIndex;
                list.add(stratIndex);
                list.add(allLen);
            }
            //匹配词在最后面时
            else if (wordStartIndex > 0 && wordEndIndex == allStrLen) {
                int allLen = wordStartIndex + matchedWordlen;
                int stratIndex = wordStartIndex - 1;
                list.add(stratIndex);
                list.add(allLen);
            }
            return list;
        }
        return null;
    }


    public static String getOriginalWord(String originWordString, int startIndex, int endIndex) {

        String notClearedWord = originWordString.substring(startIndex, endIndex);

        return notClearedWord;
    }


    public static String getclearedWord(String notClearedWord) {

        String regEx = "[`~!@#$%^&*()+=|{}:;',\\[\\].<>/?~！@#￥%……&*（）()——+|{}【】\uD83E\uDD29‘；：”“’。，、？]";//'
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(notClearedWord);
        String clearedWord = m.replaceAll("").trim();
//        System.out.println("regex : " + clearedWord);
        return clearedWord;
    }
}
