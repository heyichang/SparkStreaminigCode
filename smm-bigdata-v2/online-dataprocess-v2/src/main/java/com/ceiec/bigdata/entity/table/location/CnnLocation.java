package com.ceiec.bigdata.entity.table.location;

/**
 * Created by heyichang on 2018/3/26.
 */
public enum CnnLocation {
    CNN_LOCATION1(-84.3944,33.7586,"244011","244"),
    CNN_LOCATION2(-111.0374,40.0246,"244045","244"),
    CNN_LOCATION3(-118.0374,34.0246,"244005","244"),
    CNN_LOCATION4(-90.0374,35.0246,"244043","244");
    // 成员变量
    private double lat;
    private double lon;
    private String p_region_id;
    private String region_id;

    private CnnLocation(double lon, double lat,String region_id,String p_region_id) {
        this.lat = lat;
        this.lon = lon;
        this.p_region_id = p_region_id;
        this.region_id = region_id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getP_region_id() {
        return p_region_id;
    }

    public void setP_region_id(String p_region_id) {
        this.p_region_id = p_region_id;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }
}
