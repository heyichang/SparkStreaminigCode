package com.ceiec.graph.entity;

/**
 * Created by thinkpad on 2017/11/28.
 */
public class Small {
    private int h;

    private String resize;

    private int w;

    public void setH(int h){
        this.h = h;
    }
    public int getH(){
        return this.h;
    }
    public void setResize(String resize){
        this.resize = resize;
    }
    public String getResize(){
        return this.resize;
    }
    public void setW(int w){
        this.w = w;
    }
    public int getW(){
        return this.w;
    }

    @Override
    public String toString() {
        return "{" +
                "h=" + h +
                ", resize='" + resize + '\'' +
                ", w=" + w +
                '}';
    }
}
