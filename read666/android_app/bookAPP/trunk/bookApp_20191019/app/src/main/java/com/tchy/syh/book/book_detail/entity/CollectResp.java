package com.tchy.syh.book.book_detail.entity;

import java.io.Serializable;

public class CollectResp implements Serializable {

    /**
     * status : 1
     * info : success
     * data : {"id":0,"love":1}
     */

    private int status;
    private String info;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 0
         * love : 1
         */

        private int id;
        private int love;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLove() {
            return love;
        }

        public void setLove(int love) {
            this.love = love;
        }
    }
}
