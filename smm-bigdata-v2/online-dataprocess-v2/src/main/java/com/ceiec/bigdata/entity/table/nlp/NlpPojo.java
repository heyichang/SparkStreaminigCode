package com.ceiec.bigdata.entity.table.nlp;

/**
 * Created by heyichang on 2018/3/20.
 */
public class NlpPojo {
    private String id;
    private String language;
    private String content;
    private Integer fans_num;
    private Integer streaming;

    public NlpPojo() {

    }


    public NlpPojo(String id, String language, String content ,Integer fans_num ,Integer from_where) {
        this.id = id;
        this.language = language;
        this.content = content;
        this.fans_num = fans_num;
        this.streaming = from_where;
    }

    public NlpPojo(String id, String language, String content ,Integer fans_num ) {
        this.id = id;
        this.language = language;
        this.content = content;
        this.fans_num = fans_num;

    }

    public NlpPojo(String id, String language, String content ) {
        this.id = id;
        this.language = language;
        this.content = content;
    }

    public NlpPojo(String id, String language) {
        this.id = id;
        this.language = language;
    }

    public Integer getStreaming() {
        return streaming;
    }

    public void setStreaming(Integer streaming) {
        this.streaming = streaming;
    }

    public Integer getFans_num() {
        return fans_num;
    }

    public void setFans_num(Integer fans_num) {
        this.fans_num = fans_num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "NlpPojo{" +
                "id='" + id + '\'' +
                ", language='" + language + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
