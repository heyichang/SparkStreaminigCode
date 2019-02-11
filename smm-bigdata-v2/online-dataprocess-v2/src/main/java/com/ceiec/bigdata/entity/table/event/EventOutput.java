package com.ceiec.bigdata.entity.table.event;

/**
 * Created by heyichang on 2017/12/25.
 */
public class EventOutput {

    private String id ;
    private int fore_warning_id;
    private String event_id;
    private String info_id;
    private String user_id;
    private String warning_time;
    private String alarm_mode_ids;
    private int status;
    private float score;
    private String matched;
    private String matched_words;
    private int warning_type;
    private String warning_content;
    private int site_id;
    private int sensitive_id;
    private String author;
    private String summary;
    private String image_urls;
    private String create_post_time;

    public String getCreate_post_time() {
        return create_post_time;
    }

    public void setCreate_post_time(String create_post_time) {
        this.create_post_time = create_post_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFore_warning_id() {
        return fore_warning_id;
    }

    public void setFore_warning_id(int fore_warning_id) {
        this.fore_warning_id = fore_warning_id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getInfo_id() {
        return info_id;
    }

    public void setInfo_id(String info_id) {
        this.info_id = info_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getWarning_time() {
        return warning_time;
    }

    public void setWarning_time(String warning_time) {
        this.warning_time = warning_time;
    }

    public String getAlarm_mode_ids() {
        return alarm_mode_ids;
    }

    public void setAlarm_mode_ids(String alarm_mode_ids) {
        this.alarm_mode_ids = alarm_mode_ids;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getMatched() {
        return matched;
    }

    public void setMatched(String matched) {
        this.matched = matched;
    }

    public String getMatched_words() {
        return matched_words;
    }

    public void setMatched_words(String matched_words) {
        this.matched_words = matched_words;
    }

    public int getWarning_type() {
        return warning_type;
    }

    public void setWarning_type(int warning_type) {
        this.warning_type = warning_type;
    }

    public String getWarning_content() {
        return warning_content;
    }

    public void setWarning_content(String warning_content) {
        this.warning_content = warning_content;
    }

    public int getSite_id() {
        return site_id;
    }

    public void setSite_id(int site_id) {
        this.site_id = site_id;
    }

    public int getSensitive_id() {
        return sensitive_id;
    }

    public void setSensitive_id(int sensitive_id) {
        this.sensitive_id = sensitive_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImage_urls() {
        return image_urls;
    }

    public void setImage_urls(String image_urls) {
        this.image_urls = image_urls;
    }
}
