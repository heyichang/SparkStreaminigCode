package com.ceiec.graph.entity;

import java.util.List;

/**
 * Created by zoumengcheng on 2017/12/4.
 */
public class BoundingBox {
    private String type;

    private List<List<List<Double>>> coordinates;

    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setCoordinates(List<List<List<Double>>> coordinates){
        this.coordinates = coordinates;
    }
    public List<List<List<Double>>> getCoordinates(){
        return this.coordinates;
    }

    @Override
    public String toString() {
        return "{" +
                "type='" + type + '\'' +
                ", coordinates=" + coordinates +
                '}';
    }
}
