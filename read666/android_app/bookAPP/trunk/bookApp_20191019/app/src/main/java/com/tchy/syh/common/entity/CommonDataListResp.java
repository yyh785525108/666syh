package com.tchy.syh.common.entity;

import java.io.Serializable;
import java.util.List;

public class CommonDataListResp implements Serializable {

    /**
     * status : 1
     * info : success
     * data : []
     */

    private int status;
    private String info;
    private List<?> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
