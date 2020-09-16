package com.tchy.syh.common.entity;

import java.io.Serializable;

public class CommonNoDataResp implements Serializable {

    /**
     * status : 1
     * info : 签到成功
     */

    private int status;
    private String info;

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
}
