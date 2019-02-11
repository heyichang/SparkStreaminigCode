package com.ceiec.bigdata.entity.twitter.es;

/**
 * Created by heyichang on 2017/10/31.
 */
public class Location {
    private double lat;

    private double lon;

    public void setLat(double lat){
        this.lat = lat;
    }
    public double getLat(){
        return this.lat;
    }
    public void setLon(double lon){
        this.lon = lon;
    }
    public double getLon(){
        return this.lon;
    }

    @Override
    public String toString() {
        return "{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
