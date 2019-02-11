package com.ceiec.graph.utils.neo4jUtil;

import com.ceiec.graph.utils.InfoIdUtils;

/**
 * @author:heyichang
 * @description:关于neo4jUtils的一些方法
 * @date:Created in 9:35 2018-03-15
 */
public class Neo4jUtils {

    public static String getAcccountId(String screenName){
        //生成拼接字符窜
        StringBuffer sb = new StringBuffer();
        //拼接twitter网址
        sb.append("https://twitter.com/").append(screenName);
        String accoundId = InfoIdUtils.generate32MD5ID(sb.toString());
        return accoundId;
    }
    public static String getHomeUrl(String screenName){
        //生成拼接字符窜
        StringBuffer sb = new StringBuffer();
        //拼接twitter网址
        return sb.append("https://twitter.com/").append(screenName).toString();
    }
}
