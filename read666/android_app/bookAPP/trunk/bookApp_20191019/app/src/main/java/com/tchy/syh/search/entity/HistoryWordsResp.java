package com.tchy.syh.search.entity;

import java.util.List;

public class HistoryWordsResp {

    /**
     * status : 1
     * info : ok
     * data : ["好","就这样心想事成","s","是","书","h","哦哦0","哦哦","啊","啊0"]
     */

    private int status;
    private String info;
    private List<String> data;

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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
