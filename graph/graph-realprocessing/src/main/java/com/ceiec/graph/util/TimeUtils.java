package com.ceiec.graph.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by zoumengcheng on 2017/8/19.
 */
public class TimeUtils {
    private static final Logger logger = LoggerFactory.getLogger(TimeUtils.class);
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static void main(String[] args) throws ParseException {
        System.out.println(System.currentTimeMillis());
        System.out.println(getDate(System.currentTimeMillis()));
        System.out.println(getDate());

        // String s = "Mon Feb 13 08:00:00 GMT+08:00 2012";
        //Fri Aug 28 09:37:46 CST 2009
        String s = "Tue Nov 21 15:03:23 +0000 2017";
        String ss = "2017-11-21 11:03:23";
        String str = getSystemTime(s);
        String str2 = getCreatingTime(s);

        System.out.println(str);
        System.out.println("========");
        System.out.println(str2);
        System.out.println("=====");
        System.out.println(getTime());

    }

    public static String getSystemTime(String timeStr) {
        //System.setProperty("user.timezone","GMT +00");
        SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = sf.parse(timeStr);
            //  System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //System.out.println("user.timezone：" + System.getProperty("user.timezone"));
        TimeZone timeZone = TimeZone.getTimeZone("GMT+00:00"); //获取格林威治的时区
        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        myFmt.setTimeZone(timeZone);//设置系统时区
        return myFmt.format(date);

    }


    /**
     * 解析 ：EEE MMM dd hh:mm:ss z yyyy 时间格式转化为mmddyy ；hhmmss
     *
     * @param time
     * @return mmddyy ；hhmmss
     */
    public static String getCreatingTime(String time) {
        SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = sf.parse(transTime(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TimeZone timeZoneGMT = TimeZone.getTimeZone("GMT+00:00"); //获取格林威治的时区
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(timeZoneGMT);
        String result = sdf.format(date);
        // System.out.println(result);
        return result;
    }


    /**
     * 解析 ：EEE MMM dd hh:mm:ss z yyyy 时间格式转化为mmddyy
     *
     * @param time
     * @return mmddyy
     */
    public static String getCreatingDate(String time) {
        SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy", Locale.ENGLISH);
        Date date = null;
        try {

            date = sf.parse(transTime(time));
            //  System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TimeZone timeZoneGMT = TimeZone.getTimeZone("GMT+00:00"); //获取格林威治的时区
        sdf.setTimeZone(timeZoneGMT);
        String result = sdf.format(date);
        // System.out.println(result);
        return result;
    }


    /**
     * 获取当前日期
     *
     * @return mmddyy
     */
    public static String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TimeZone timeZoneGMT = TimeZone.getTimeZone("GMT+00:00"); //获取格林威治的时区
        sdf.setTimeZone(timeZoneGMT);
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime());
    }

    /**
     * 获取当前日期
     *
     * @return mmddyy
     */
    public static String getTimeForHbase() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        TimeZone timeZoneGMT = TimeZone.getTimeZone("GMT+00:00"); //获取格林威治的时区
        sdf.setTimeZone(timeZoneGMT);
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime());
    }

    /**
     * 获取当前时间
     *
     * @return mmddyy hh:mm:ss
     */
    public static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone timeZoneGMT = TimeZone.getTimeZone("GMT+00:00"); //获取格林威治的时区
        sdf.setTimeZone(timeZoneGMT);
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime());
    }

    /**
     * 将long时间转化为string时间格式
     *
     * @param time
     * @return
     */
    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return sdf.format(cal.getTime());
    }

    /**
     * 将long时间转化为string时间格式每一个小时
     *
     * @param time
     * @return
     */
    public static String getHour(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
    }


    /**
     * 解析 ：EEE MMM dd hh:mm:ss z yyyy 时间格式转化为mmddyy ；hhmmss
     *
     * @param time
     * @return mmddyy ；hhmmss
     */
    public static Short getCreatingTimeHour(String time) {
        String str = getCreatingTime(time).trim();
        String timeSlot = str.split(" ")[1].split(":")[0];
        return Short.valueOf(timeSlot);
    }

    /*
    * 将时间戳转换为时间
    */
    public static String stampToTime(String s) {
        try {
            String res;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long lt = new Long(s) * 1000;
            Date date = new Date(lt);
            TimeZone timeZoneGMT = TimeZone.getTimeZone("GMT+00:00"); //获取格林威治的时区
            simpleDateFormat.setTimeZone(timeZoneGMT);
            res = simpleDateFormat.format(date);
            return res;
        } catch (NumberFormatException e) {
            logger.error("input time error" + s);
        }
        return null;
    }

    /*
    * 将时间戳转换为时间
    */
    public static String stampToDate(String s) {
        try {
            String res;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            long lt = new Long(s) * 1000;
            Date date = new Date(lt);
            TimeZone timeZoneGMT = TimeZone.getTimeZone("GMT+00:00"); //获取格林威治的时区
            simpleDateFormat.setTimeZone(timeZoneGMT);
            res = simpleDateFormat.format(date);
            return res;
        } catch (NumberFormatException e) {
            logger.error("input time error" + s);
        }
        return null;
    }

    /*
   * 将时间戳转换为时间
   */
    public static Short stampToHour(String s) {
        try {
            String res;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
            long lt = new Long(s) * 1000;
            Date date = new Date(lt);
            TimeZone timeZoneGMT = TimeZone.getTimeZone("GMT+00:00"); //获取格林威治的时区
            simpleDateFormat.setTimeZone(timeZoneGMT);
            res = simpleDateFormat.format(date);
            return Short.valueOf(res.split(" ")[1]);
        } catch (NumberFormatException e) {
            logger.error("input time error" + s);
        }
        return null;
    }

    public static String transTime(String str) {
        String gmt_time = str.replaceAll("\\s*", "");
        //time.replace(" ","");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(gmt_time.substring(0, 3)).append(" ").append(gmt_time.substring(3, 6)).append(" ")
                .append(gmt_time.substring(6, 8)).append(" ").append(gmt_time.substring(8, 16)).append(" ")
                .append(gmt_time.substring(16, 21)).append(" ").append(gmt_time.substring(21));
        return stringBuffer.toString();
    }

}
