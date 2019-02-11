package com.ceiec.bigdata.entity.table.keyword;

public class KeywordOutput {
    private String id ;
    private String user_id;
    private Integer warning_conf_id;
    private Integer monitor_type_id;
    private String monitor_entity_id;
    private String monitor_entity_name;
    private Integer warning_type_id;
    private String matched_words;
    private String alarm_mode_ids;
    private Integer status;
    private String info_id;
    private String author;
    private String summary;
    private String content;
    private Integer site_id;
    private String create_post_time;
    private String warning_time;
    private String abnormal_time;
    private String normal_place;
    private String abnormal_place;
    private Integer site_type_id;

    public Integer getSite_type_id() {
        return site_type_id;
    }

    public void setSite_type_id(Integer site_type_id) {
        this.site_type_id = site_type_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Integer getWarning_conf_id() {
        return warning_conf_id;
    }

    public void setWarning_conf_id(Integer warning_conf_id) {
        this.warning_conf_id = warning_conf_id;
    }

    public Integer getMonitor_type_id() {
        return monitor_type_id;
    }

    public void setMonitor_type_id(Integer monitor_type_id) {
        this.monitor_type_id = monitor_type_id;
    }

    public String getMonitor_entity_id() {
        return monitor_entity_id;
    }

    public void setMonitor_entity_id(String monitor_entity_id) {
        this.monitor_entity_id = monitor_entity_id;
    }

    public String getMonitor_entity_name() {
        return monitor_entity_name;
    }

    public void setMonitor_entity_name(String monitor_entity_name) {
        this.monitor_entity_name = monitor_entity_name;
    }

    public Integer getWarning_type_id() {
        return warning_type_id;
    }

    public void setWarning_type_id(Integer warning_type_id) {
        this.warning_type_id = warning_type_id;
    }

    public String getMatched_words() {
        return matched_words;
    }

    public void setMatched_words(String matched_words) {
        this.matched_words = matched_words;
    }

    public String getAlarm_mode_ids() {
        return alarm_mode_ids;
    }

    public void setAlarm_mode_ids(String alarm_mode_ids) {
        this.alarm_mode_ids = alarm_mode_ids;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getInfo_id() {
        return info_id;
    }

    public void setInfo_id(String info_id) {
        this.info_id = info_id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSite_id() {
        return site_id;
    }

    public void setSite_id(Integer site_id) {
        this.site_id = site_id;
    }

    public String getCreate_post_time() {
        return create_post_time;
    }

    public void setCreate_post_time(String create_post_time) {
        this.create_post_time = create_post_time;
    }

    public String getWarning_time() {
        return warning_time;
    }

    public void setWarning_time(String warning_time) {
        this.warning_time = warning_time;
    }

    public String getAbnormal_time() {
        return abnormal_time;
    }

    public void setAbnormal_time(String abnormal_time) {
        this.abnormal_time = abnormal_time;
    }

    public String getNormal_place() {
        return normal_place;
    }

    public void setNormal_place(String normal_place) {
        this.normal_place = normal_place;
    }

    public String getAbnormal_place() {
        return abnormal_place;
    }

    public void setAbnormal_place(String abnormal_place) {
        this.abnormal_place = abnormal_place;
    }

    @Override
    public String toString() {
        return "KeywordOutput{" +
                "id='" + id + '\'' +
                ", user_id=" + user_id +
                ", warning_conf_id=" + warning_conf_id +
                ", monitor_type_id=" + monitor_type_id +
                ", monitor_entity_id='" + monitor_entity_id + '\'' +
                ", monitor_entity_name='" + monitor_entity_name + '\'' +
                ", warning_type_id=" + warning_type_id +
                ", matched_words='" + matched_words + '\'' +
                ", alarm_mode_ids='" + alarm_mode_ids + '\'' +
                ", status=" + status +
                ", info_id='" + info_id + '\'' +
                ", author='" + author + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", site_id='" + site_id + '\'' +
                ", create_post_time='" + create_post_time + '\'' +
                ", warning_time='" + warning_time + '\'' +
                ", abnormal_time='" + abnormal_time + '\'' +
                ", normal_place='" + normal_place + '\'' +
                ", abnormal_place='" + abnormal_place + '\'' +
                '}';
    }
}
