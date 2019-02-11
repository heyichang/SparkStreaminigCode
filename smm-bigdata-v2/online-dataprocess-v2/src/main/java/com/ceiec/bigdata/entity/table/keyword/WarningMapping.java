package com.ceiec.bigdata.entity.table.keyword;

import java.sql.Timestamp;

public class WarningMapping {
    private Integer warning_conf_id;
    private Integer monitor_type_id;
    private String monitor_entity_id;
    private String account_id;
    private String user_id;
    private Integer switch_status;
    private String key_words;
    private Integer words_relation;
    private String warning_threshold;
    private String warning_type;
    private String alarm_mode;
    private String entity_name;
    private String event_name;
    private String theme_name;
    private String nick_name;
    private Timestamp start_time;
    private Timestamp end_time;
    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Timestamp getStart_time() {
        return start_time;
    }

    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
    }

    public Timestamp getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Timestamp end_time) {
        this.end_time = end_time;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getTheme_name() {
        return theme_name;
    }

    public void setTheme_name(String theme_name) {
        this.theme_name = theme_name;
    }

    public String getEntity_name() {
        return entity_name;
    }

    public void setEntity_name(String entity_name) {
        this.entity_name = entity_name;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Integer getSwitch_status() {
        return switch_status;
    }

    public void setSwitch_status(Integer switch_status) {
        this.switch_status = switch_status;
    }

    public String getKey_words() {
        return key_words;
    }

    public void setKey_words(String key_words) {
        this.key_words = key_words;
    }

    public Integer getWords_relation() {
        return words_relation;
    }

    public void setWords_relation(Integer words_relation) {
        this.words_relation = words_relation;
    }

    public String getWarning_threshold() {
        return warning_threshold;
    }

    public void setWarning_threshold(String warning_threshold) {
        this.warning_threshold = warning_threshold;
    }

    public String getWarning_type() {
        return warning_type;
    }

    public void setWarning_type(String warning_type) {
        this.warning_type = warning_type;
    }

    public String getAlarm_mode() {
        return alarm_mode;
    }

    public void setAlarm_mode(String alarm_mode) {
        this.alarm_mode = alarm_mode;
    }

    @Override
    public String toString() {
        return "WarningMapping{" +
                "warning_conf_id=" + warning_conf_id +
                ", monitor_type_id=" + monitor_type_id +
                ", monitor_entity_id='" + monitor_entity_id + '\'' +
                ", account_id='" + account_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", switch_status=" + switch_status +
                ", key_words='" + key_words + '\'' +
                ", words_relation=" + words_relation +
                ", warning_threshold='" + warning_threshold + '\'' +
                ", warning_type='" + warning_type + '\'' +
                ", alarm_mode='" + alarm_mode + '\'' +
                ", entity_name='" + entity_name + '\'' +
                ", event_name='" + event_name + '\'' +
                ", theme_name='" + theme_name + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", start_time=" + start_time +
                ", end_time=" + end_time +
                ", country='" + country + '\'' +
                '}';
    }
}
