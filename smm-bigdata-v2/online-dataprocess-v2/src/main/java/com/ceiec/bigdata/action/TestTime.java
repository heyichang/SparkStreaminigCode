package com.ceiec.bigdata.action;

import com.ceiec.bigdata.util.TimeUtils;

public class TestTime {
    public static void main(String[] args) {
        String time = TimeUtils.stampToTime("1051376400");
        System.out.println(time);
    }
}
