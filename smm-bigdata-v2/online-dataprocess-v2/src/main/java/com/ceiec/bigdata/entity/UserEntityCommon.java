package com.ceiec.bigdata.entity;

public class UserEntityCommon {

    private String content;
    private String source_type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    @Override
    public String toString() {
        return "UserEntityCommon{" +
                "content='" + content + '\'' +
                ", source_type='" + source_type + '\'' +
                '}';
    }
}
