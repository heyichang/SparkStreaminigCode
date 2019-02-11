package com.ceiec.graph.util.locationutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by zoumengcheng on 2017/11/7.
 */
public class RegistLocationHttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(RegistLocationHttpUtils.class);

    public final static  String sendPost(String postData ) {
        try {
            // 创建url资源
            URL url = new URL("http://172.16.3.74:18080/gis/world_district/getProvinceListByName.do");//172.16.3.74 192.168.19.120
            // 建立http连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置允许输出
            conn.setDoOutput(true);

            conn.setDoInput(true);

            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("POST");
            // 设置维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置文件字符集:
            conn.setRequestProperty("Charset", "UTF-8");
            //转换为字节数组
            byte[] data = (postData.toString()).getBytes();
            // 设置文件长度
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));

            // 设置文件类型:
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            // 开始连接请求
            conn.connect();
            OutputStream out = conn.getOutputStream();
            // 写入请求的字符串
            out.write((postData.toString()).getBytes());
            out.flush();
            out.close();

           // System.out.println(conn.getResponseCode());

            // 请求返回的状态
            if (conn.getResponseCode() == 200) {
               // System.out.println("连接成功");
                // 请求返回的数据
                InputStream in = conn.getInputStream();
                String a = null;
                try {
                    byte[] data2 = new byte[in.available()];
                    in.read(data2);
                    // 转成字符串
                    a = new String(data2);
                    return a;
                } catch (Exception e1) {
                    //Auto-generated catch block
                    e1.printStackTrace();
                    logger.error("RegistLocation http requst error");
                }
            } else {
                System.out.println("no++");
            }

        } catch (Exception e) {

        }
        return null;
    }
}
