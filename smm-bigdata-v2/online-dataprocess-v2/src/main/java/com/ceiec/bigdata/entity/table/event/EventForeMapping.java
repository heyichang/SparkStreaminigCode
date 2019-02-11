package com.ceiec.bigdata.entity.table.event;

/**
 * Created by heyichang on 2017/12/25.
 */
public class EventForeMapping {
    private int warning_conf_id;
    private int monitor_type_id;
    private String monitor_entity_id;
    private String user_id;
    private int switch_status;
    private String key_words;
    private String words_relation;
    private String warning_threshold;
    private String warning_type;
    private String alarm_mode;

    public int getWarning_conf_id() {
        return warning_conf_id;
    }

    public void setWarning_conf_id(int warning_conf_id) {
        this.warning_conf_id = warning_conf_id;
    }

    public int getMonitor_type_id() {
        return monitor_type_id;
    }

    public void setMonitor_type_id(int monitor_type_id) {
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

    public int getSwitch_status() {
        return switch_status;
    }

    public void setSwitch_status(int switch_status) {
        this.switch_status = switch_status;
    }

    public String getKey_words() {
        return key_words;
    }

    public void setKey_words(String key_words) {
        this.key_words = key_words;
    }

    public String getWords_relation() {
        return words_relation;
    }

    public void setWords_relation(String words_relation) {
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
}
