package com.ceiec.graph.entity.table.updatetwitter;

/**
 * Created by zoumengcheng on 2018/1/19.
 */
public class AccountOverDue {
    private int record_id;
    private String account_id;
    private int overdue_type;
    private String overdue_content;
    private String account_name;
    private int noticed;
    private int modified;

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public int getRecord_id() {
        return record_id;
    }

    public void setRecord_id(int record_id) {
        this.record_id = record_id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public int getOverdue_type() {
        return overdue_type;
    }

    public void setOverdue_type(int overdue_type) {
        this.overdue_type = overdue_type;
    }

    public String getOverdue_content() {
        return overdue_content;
    }

    public void setOverdue_content(String overdue_content) {
        this.overdue_content = overdue_content;
    }

    public int getNoticed() {
        return noticed;
    }

    public void setNoticed(int noticed) {
        this.noticed = noticed;
    }

    public int getModified() {
        return modified;
    }

    public void setModified(int modified) {
        this.modified = modified;
    }

    @Override
    public String toString() {
        return "AccountOverDue{" +
                "record_id=" + record_id +
                ", account_id='" + account_id + '\'' +
                ", overdue_type=" + overdue_type +
                ", overdue_content='" + overdue_content + '\'' +
                ", noticeTimes=" + noticed +
                ", modified=" + modified +
                '}';
    }
}
