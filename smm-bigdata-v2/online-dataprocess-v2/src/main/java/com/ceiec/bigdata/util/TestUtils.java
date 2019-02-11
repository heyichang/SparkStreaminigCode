package com.ceiec.bigdata.util;

import com.alibaba.fastjson.JSON;
import com.ceiec.bigdata.entity.table.AccountLastTime;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by heyichang on 2017/10/31.
 */
public class TestUtils {
    public static String getRadom() {
        int max = 10;
        int min = 0;
        int s = getIntRadom(max,min);
        return String.valueOf(s);
    }

    public static String getRadom1Or2() {
        int max = 2;
        int min = 1;
        int s = getIntRadom(max,min);
        return String.valueOf(s);
    }

    public static String get10To100Radom() {
        int max = 100;
        int min = 10;
        int s = getIntRadom(max,min);
        return String.valueOf(s);
    }
    public static String getSiteTypeId() {
        int max=4;
        int min=1;
        Random random = new Random();

        int s = random.nextInt(max)%(max-min+1) + min;
        return String.valueOf(s);
    }


    public static int getSiteId() {
        int a = TestUtils.getRadom(1,60);
        int b = TestUtils.getRadom(100,104);
        int c = TestUtils.getRadom(200,204);
        int d = 301;
        int e = 302;
        int[] f = new int[]{a,b,c,d,e};
        int g = TestUtils.getRadom(0,5);
        return f[g];
    }

    public static String getLangue() {
        int max = 6;
        int min = 0;
        int s = getIntRadom(max,min);
        String[] sts = {"En", "Ch", "Ja", "Sp", "Ru", "Ko", "Am"};
        return sts[s];
    }

    public static int getIntRadom(int max,int min){
        Random random = new Random();

        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }

    public static int get1To4IntRadom(){
        int max = 4;
        int min = 1;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }

    public static int getRadom(int min ,int max){

        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }

    public static int getSiteType(int index){

        if (1 <= index && index <= 60) {
            return 5;
        }
        else if (100 <= index && index <= 104) {
            return 3;
        }
        else if (200 <= index && index <= 204) {
            return 4;
        }
        else if (index == 301) {
            return 1;
        }
        else if (index == 302) {
            return 2;
        }
        return 0;

    }




    public static Double getLonDoubleRadom() {
        final int MinLon = 73 ;
        final int MaxLon = 135 ;
        final int MinLat = 17 ;
        final int MaxLat = 53 ;
        DecimalFormat df = new DecimalFormat(".00");
        df.setMaximumFractionDigits(2);
        String Lon = randomLonLat( MinLon,  MaxLon,  MinLat,  MaxLat,  "Lon");
        Double dou = Double.valueOf(Lon);
        return Double.valueOf(df.format(dou));
    }

    public static Double getLatDoubleRadom() {
        final int MinLon = 73 ;
        final int MaxLon = 135 ;
        final int MinLat = 17 ;
        final int MaxLat = 53 ;
        DecimalFormat df = new DecimalFormat(".00");
        df.setMaximumFractionDigits(2);
        String Lon = randomLonLat( MinLon,  MaxLon,  MinLat,  MaxLat,  "Lat");
        Double dou = Double.valueOf(Lon);
        return Double.valueOf(df.format(dou));
    }

    public static String randomLonLat(double MinLon, double MaxLon, double MinLat, double MaxLat, String type) {
        Random random = new Random();
        BigDecimal db = new BigDecimal(Math.random() * (MaxLon - MinLon) + MinLon);
        String lon = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();// 小数后6位
        db = new BigDecimal(Math.random() * (MaxLat - MinLat) + MinLat);
        String lat = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
        if (type.equals("Lon")) {
            return lon;
        } else {
            return lat;
        }
    }
    public static void timeTest(List<String> list ) throws ParseException {
        for (String str :list){
            AccountLastTime eventForeInfo = JSON.parseObject(str, AccountLastTime.class);
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = sf.format(eventForeInfo.getLatest_time());
            System.out.print(eventForeInfo.getAccount_id()+"  "+dateStr);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            TimeZone timeZoneGMT = TimeZone.getTimeZone("GMT+00:00");
            //获取格林威治的时区sdf.setTimeZone(timeZoneGMT);
            Calendar cal = Calendar.getInstance();
            cal.setTimeZone(timeZoneGMT);
            cal.add(Calendar.DATE, -1);
            cal.add(Calendar.HOUR_OF_DAY, -8);
            String str2 =  sdf.format(cal.getTime());
            //System.out.println(str);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" ,Locale.ENGLISH);
            Date date = sdf2.parse(str2);
            //System.out.println( sdf2.parse(str));
            boolean flag = date.before(eventForeInfo.getLatest_time());
            System.out.println(flag);
        }
    }


    public static void main(String[] args) {
        for (int i=0;i<100;i++)
            System.out.println(getRadom1Or2());
    }
}
