package com.ceiec.graph.entity.table.location;

import java.util.List;

/**
 * Created by zoumengcheng on 2017/12/19.
 */
public class LocationResponse {
    private String code ;
    private String message;
    private List<ResponseValue> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public List<ResponseValue> getData() {
        return data;
    }

    public void setData(List<ResponseValue> data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return "LocationResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

}
