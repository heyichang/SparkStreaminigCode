package com.ceiec.graph.entity.facebook;

/**
 * Created by thinkpad on 2018/2/9.
 */
public class FaceBookPlace {
    private FaceBookLocation location;
    private String name;
    private String id;

    public FaceBookLocation getLocation() {
        return location;
    }

    public void setLocation(FaceBookLocation location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
