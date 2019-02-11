package com.ceiec.bigdata.util.siteidutil;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by heyichang on 2017/12/4.
 */
public class UrlFilter {


    public static String filterUrl(String url)  {

        URL u = null;
        try {
            u = new URL(url);
            return  u.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.err.println("url parse error: "+url);
        }

        return null;

    }

}
