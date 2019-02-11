package com.ceiec.bigdata.entity;

public class Education {
    private String name;
    private String begin;
    private String end;
    private String degree;
    private String major;
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

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @Override
    public String toString() {
        return "Education{" +
                "name='" + name + '\'' +
                ", begin='" + begin + '\'' +
                ", end='" + end + '\'' +
                ", degree='" + degree + '\'' +
                ", major='" + major + '\'' +
                '}';
    }
}
