package com.tchy.syh.my.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MissionResp implements Serializable {

    /**
     * status : 1
     * info : success
     * data : {"daily":[{"key":"see","desc":"观看视频","point":2,"id":0,"status":-1},{"key":"comment","desc":"发表评论","point":2,"id":0,"status":-1},{"key":"share","desc":"分享视频","point":2,"id":0,"status":-1}],"new":[{"key":"sys_weixin","desc":"关注公众号","point":2,"id":707825,"status":0},{"key":"mobile","desc":"绑定手机","point":2,"id":0,"status":-1}],"sign_times":0}
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
         * daily : [{"key":"see","desc":"观看视频","point":2,"id":0,"status":-1},{"key":"comment","desc":"发表评论","point":2,"id":0,"status":-1},{"key":"share","desc":"分享视频","point":2,"id":0,"status":-1}]
         * new : [{"key":"sys_weixin","desc":"关注公众号","point":2,"id":707825,"status":0},{"key":"mobile","desc":"绑定手机","point":2,"id":0,"status":-1}]
         * sign_times : 0
         */

        private int sign_times;
        private List<DailyBean> daily;
        @SerializedName("new")
        private List<NewBean> newX;

        public int getSign_times() {
            return sign_times;
        }

        public void setSign_times(int sign_times) {
            this.sign_times = sign_times;
        }

        public List<DailyBean> getDaily() {
            return daily;
        }

        public void setDaily(List<DailyBean> daily) {
            this.daily = daily;
        }

        public List<NewBean> getNewX() {
            return newX;
        }

        public void setNewX(List<NewBean> newX) {
            this.newX = newX;
        }

        public static class DailyBean {
            /**
             * key : see
             * desc : 观看视频
             * point : 2
             * id : 0
             * status : -1
             */

            private String key;
            private String desc;
            private int point;
            private int id;
            private int status;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public int getPoint() {
                return point;
            }

            public void setPoint(int point) {
                this.point = point;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }

        public static class NewBean {
            /**
             * key : sys_weixin
             * desc : 关注公众号
             * point : 2
             * id : 707825
             * status : 0
             */

            private String key;
            private String desc;
            private int point;
            private int id;
            private int status;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public int getPoint() {
                return point;
            }

            public void setPoint(int point) {
                this.point = point;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
