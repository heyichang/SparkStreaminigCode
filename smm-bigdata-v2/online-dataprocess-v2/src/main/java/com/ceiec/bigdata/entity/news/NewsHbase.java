package com.ceiec.bigdata.entity.news;

import com.ceiec.bigdata.util.InfoIdUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * Created by heyichang on 2017/12/4.
 */
public class NewsHbase {

    private String rowkey;
    private String crawlid;
    private String site_name;
    private String create_time;
    private String raw;
    private String data;
    private String url;



    public NewsHbase(String newsJson) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            @SuppressWarnings({ "unchecked"})
            Map<String, Object> result = mapper.readValue(newsJson, Map.class);
            if(result.get(NewsConstants.URL)!=null){
                this.rowkey = result.get(NewsConstants.CRAWLID) + InfoIdUtils.generate32MD5ID((String) result.get("url"));
            }
            if( result.get(NewsConstants.CRAWLID) != null) {
                this.crawlid = (String) result.get(NewsConstants.CRAWLID);
            }
            if(result.get(NewsConstants.SITE_NAME) != null) {
                this.site_name = (String)result.get(NewsConstants.SITE_NAME);
            }
            try {
                this.create_time = String.valueOf((Integer) result.get(NewsConstants.CREATE_TIME));
            }catch (NumberFormatException e){
                this.create_time = String.valueOf((Integer) result.get(NewsConstants.CREATE_TIME));
            }

            this.raw = (String)result.get(NewsConstants.RAW);
            this.url = (String)result.get(NewsConstants.URL);
            this.data = mapper.writeValueAsString(result.get(NewsConstants.DATA));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public String getCrawlid() {
        return crawlid;
    }

    public void setCrawlid(String crawlid) {
        this.crawlid = crawlid;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "{" +
                "rowkey='" + rowkey + '\'' +
                ", crawlid='" + crawlid + '\'' +
                ", site_name='" + site_name + '\'' +
                ", create_time='" + create_time + '\'' +
                ", raw='" + raw + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
