package com.ceiec.bigdata.entity.table.location;

/**
 * Created by heyichang on 2017/12/19.
 */
public class RegistLocationRequst {
    private String id ;
    private String name;

    public RegistLocationRequst() {

    }

    public RegistLocationRequst(String id, String location) {
        this.id = id;
        this.name = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "RegistLocationRequst{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
