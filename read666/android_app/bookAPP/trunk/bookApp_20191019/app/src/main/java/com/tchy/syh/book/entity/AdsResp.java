package com.tchy.syh.book.entity;

import java.io.Serializable;
import java.util.List;

public class AdsResp implements Serializable {

    /**
     * status : 1
     * info : success
     * data : [{"id":"广告ID","name":"广告一","link":"链接一","img":"图片一"}]
     */
    private int type;
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

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static class DataBean {
        /**
         * id : 广告ID
         * name : 广告一
         * link : 链接一
         * img : 图片一
         */

        private String id;
        private String name;
        private String link;
        private String img;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }
}
