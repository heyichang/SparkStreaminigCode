package com.ceiec.bigdata.util.nlputil;

import com.ceiec.bigdata.util.Constants;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by heyichang on 2017/11/7.
 */
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);


    /**
     *  http请求接口
     * @param content
     * @return
     */
    public static String getResultByPost(Map<String,String> urlParms , String content)  {
        String urls = "http://172.16.3.33:8188/text/processData";
        HttpEntity httpEntity = null;
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair(Constants.httpConstants.CONTENT, content));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, Consts.UTF_8);
        HttpPost httpPost = new HttpPost(urls + getUrlParms(urlParms));
        httpPost.setEntity(entity);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpPost);

            httpEntity = httpResponse.getEntity();
            String str = EntityUtils.toString(httpEntity);
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
                httpResponse.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getUrlParms(Map<String,String> map){
        Iterator iterator = map.entrySet().iterator();
        StringBuffer sb = new StringBuffer();
        sb.append("?");
        while(iterator.hasNext()){
            @SuppressWarnings({ "unchecked" })
            Map.Entry<String,String> elem = (Map.Entry<String, String>) iterator.next();
            sb.append(elem.getKey() + "=" + elem.getValue());
            sb.append("&");
        }
        sb.delete(sb.length()-1,sb.length());
        return  sb.toString();
    }

}
