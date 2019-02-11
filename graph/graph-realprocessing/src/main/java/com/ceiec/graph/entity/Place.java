package com.ceiec.graph.entity;

import java.util.List;

/**
 * Created by zoumengcheng on 2017/12/4.
 */
public class Place {
    private List<String> contained_within;

    private Attributes attributes;

    private String id;

    private String name;

    private String country;

    private String country_code;

    private String url;

    private String full_name;

    private BoundingBox bounding_box;

    private String place_type;

    public void setContained_within(List<String> contained_within){
        this.contained_within = contained_within;
    }
    public List<String> getContained_within(){
        return this.contained_within;
    }
    public void setAttributes(Attributes attributes){
        this.attributes = attributes;
    }
    public Attributes getAttributes(){
        return this.attributes;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setCountry(String country){
        this.country = country;
    }
    public String getCountry(){
        return this.country;
    }
    public void setCountry_code(String country_code){
        this.country_code = country_code;
    }
    public String getCountry_code(){
        return this.country_code;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
    public void setFull_name(String full_name){
        this.full_name = full_name;
    }
    public String getFull_name(){
        return this.full_name;
    }
    public void setBounding_box(BoundingBox bounding_box){
        this.bounding_box = bounding_box;
    }
    public BoundingBox getBounding_box(){
        return this.bounding_box;
    }
    public void setPlace_type(String place_type){
        this.place_type = place_type;
    }
    public String getPlace_type(){
        return this.place_type;
    }

    @Override
    public String toString() {
        return "{" +
                "contained_within=" + contained_within +
                ", attributes=" + attributes +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", country_code='" + country_code + '\'' +
                ", url='" + url + '\'' +
                ", full_name='" + full_name + '\'' +
                ", bounding_box=" + bounding_box +
                ", place_type='" + place_type + '\'' +
                '}';
    }
}
