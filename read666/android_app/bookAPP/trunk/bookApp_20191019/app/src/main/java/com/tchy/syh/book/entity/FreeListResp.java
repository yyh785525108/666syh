package com.tchy.syh.book.entity;

import java.io.Serializable;
import java.util.List;

public class FreeListResp implements Serializable {

    /**
     * status : 1
     * info : success
     * data : [{"bookname":"书名","thumb":" 图片","type":"","intro":" 简介","booknum":"订阅量","add_time":"发布时间"}]
     */

    private int status;
    private String info;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * bookname : 书名
         * thumb :  图片
         * type :
         * intro :  简介
         * booknum : 订阅量
         * add_time : 发布时间
         */

        private String bookname;
        private String thumb;
        private String type;
        private String intro;
        private String booknum;
        private String add_time;

        public String getBookname() {
            return bookname;
        }

        public void setBookname(String bookname) {
            this.bookname = bookname;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getBooknum() {
            return booknum;
        }

        public void setBooknum(String booknum) {
            this.booknum = booknum;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }
}
