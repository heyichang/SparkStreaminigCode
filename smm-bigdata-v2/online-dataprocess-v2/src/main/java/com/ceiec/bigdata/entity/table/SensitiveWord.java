package com.ceiec.bigdata.entity.table;

/**
 * Created by heyichang on 2017/12/5.
 */
public class SensitiveWord {
    private int sensitive_word_id ;
    private String name;
    private String comment;

    public int getSensitive_word_id() {
        return sensitive_word_id;
    }

    public void setSensitive_word_id(int sensitive_word_id) {
        this.sensitive_word_id = sensitive_word_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "{" +
                "\"sensitive_word_id\"" +":"+ sensitive_word_id +
                ",\"name\""+":" +"\""+ name+"\"" +
                ", \"comment\""+":" + "\""+comment+"\""  +
                "}";
    }
}
