package com.ceiec.bigdata.entity;

public class EventsEntity {
    private String content;
    private String time;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "EventsEntity{" +
                "content='" + content + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
