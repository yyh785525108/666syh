package com.tchy.syh.comment.entity;

import java.io.Serializable;

public class CommentLoveResp implements Serializable {


    /**
     * status : 1
     * info : 点赞成功
     * data : {"num":1}
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
         * num : 1
         */

        private int num;
        private int is_add;

        public int getIs_add() {
            return is_add;
        }

        public void setIs_add(int is_add) {
            this.is_add = is_add;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
