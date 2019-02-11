package com.ceiec.bigdata.entity.table;

public class RegionInfo {
    private String region_id;
    private String parent_region_id;
    private String full_name;
    private String shorthand;
    private Integer level;

    public String getShorthand() {
        return shorthand;
    }

    public void setShorthand(String shorthand) {
        this.shorthand = shorthand;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }

    public String getParent_region_id() {
        return parent_region_id;
    }

    public void setParent_region_id(String parent_region_id) {
        this.parent_region_id = parent_region_id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }


    @Override
    public String toString() {
        return "{" +
                "\"region_id\"" +":"+ "\"" +region_id +"\"" +
                ",\"parent_region_id\""+":" +"\"" + parent_region_id +"\"" +
                ",\"full_name\""+":" +"\""+ full_name+"\"" +
                ", \"shorthand\""+":" + "\""+shorthand+"\""  +
                ", \"level\""+":" +  level   +
                "}";
    }
}
