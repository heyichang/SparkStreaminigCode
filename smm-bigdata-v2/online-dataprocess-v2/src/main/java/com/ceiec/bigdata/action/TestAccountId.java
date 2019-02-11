package com.ceiec.bigdata.action;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * Created by heyichang on 2018/1/23.
 */
public class TestAccountId {
    public static void main(String[] args) {
        String str = "heihei and haha";
        try {
            List<String> str2 = JSON.parseArray(str, String.class);
            for (String str3:str2) {
                System.out.println(str3);
            }
        }catch (Exception e){
            System.out.println("does not array");
        }


    }



}
