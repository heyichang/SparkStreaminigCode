package com.ceiec.bigdata.entity.table.location;

/**
 * Created by heyichang on 2017/12/19.
 */
public class ResponseValue {
    private String id ;

    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ResponseValue{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
