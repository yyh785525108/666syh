package com.tchy.syh.wxapi.entity;

import org.json.JSONObject;

import java.io.Serializable;

public class WXCallBackResp implements Serializable {


    /**
     * status : 1
     * info : success
     * data : {"uid":"2238493","nickname":"Melee","mobile":"","avatar":"http://imsyh.7seme.com/avatar/2018/0702/2018070217294585122.jpg","access_token":"4112730b0d729ead1bf5cb0974d7868d","last_time":1531712634,"password":"634619"}
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
         * uid : 2238493
         * nickname : Melee
         * mobile :
         * avatar : http://imsyh.7seme.com/avatar/2018/0702/2018070217294585122.jpg
         * access_token : 4112730b0d729ead1bf5cb0974d7868d
         * last_time : 1531712634
         * password : 634619
         */

        private String uid;
        private String nickname;
        private String mobile;
        private String avatar;
        private String access_token;
        private int last_time;
        private String password;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public int getLast_time() {
            return last_time;
        }

        public void setLast_time(int last_time) {
            this.last_time = last_time;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
