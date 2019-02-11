package com.ceiec.bigdata.entity;

public class WorkExprience {
    private String name;
    private String begin;
    private String end;
    private String position;
    private String desc;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "WorkExprience{" +
                "name='" + name + '\'' +
                ", begin='" + begin + '\'' +
                ", end='" + end + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
