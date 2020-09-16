package com.tchy.syh.listen.entity;

import java.util.List;

public class ResListen {



    private String total_count;
    private int count;
    private List<ListenEntity.ListenItemEntity> list;

    public String getTotal_count() {
        return total_count;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ListenEntity.ListenItemEntity> getList() {
        return list;
    }

    public void setList(List<ListenEntity.ListenItemEntity> list) {
        this.list = list;
    }

}
