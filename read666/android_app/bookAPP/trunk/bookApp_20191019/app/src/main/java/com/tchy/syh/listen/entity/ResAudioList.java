package com.tchy.syh.listen.entity;

import java.util.List;

public class ResAudioList {


    private String total_count;
    private int count;
    private List<ListenEntity.AudioItemEntity> list;

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

    public List<ListenEntity.AudioItemEntity> getList() {
        return list;
    }

    public void setList(List<ListenEntity.AudioItemEntity> list) {
        this.list = list;
    }


}
