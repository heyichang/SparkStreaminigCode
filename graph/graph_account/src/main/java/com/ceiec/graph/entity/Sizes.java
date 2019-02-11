package com.ceiec.graph.entity;

/**
 * Created by thinkpad on 2017/11/28.
 */
public class Sizes {
    private Medium medium;

    private Thumb thumb;

    private Large large;

    private Small small;

    public void setMedium(Medium medium){
        this.medium = medium;
    }
    public Medium getMedium(){
        return this.medium;
    }
    public void setThumb(Thumb thumb){
        this.thumb = thumb;
    }
    public Thumb getThumb(){
        return this.thumb;
    }
    public void setLarge(Large large){
        this.large = large;
    }
    public Large getLarge(){
        return this.large;
    }
    public void setSmall(Small small){
        this.small = small;
    }
    public Small getSmall(){
        return this.small;
    }

    @Override
    public String toString() {
        return "{" +
                "medium=" + medium +
                ", thumb=" + thumb +
                ", large=" + large +
                ", small=" + small +
                '}';
    }
}
