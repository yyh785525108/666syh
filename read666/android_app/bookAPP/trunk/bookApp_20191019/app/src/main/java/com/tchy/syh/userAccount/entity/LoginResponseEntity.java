package com.tchy.syh.userAccount.entity;

public class LoginResponseEntity {

    /**
     * status : 1
     * info : success
     * data : {"uid":"1","username":"12154545","nickname":"张三","mobile":"13800138000","avatar":"http://img.7seme.com/avatar/2016/0310/5779_20160310181013470.jpg","parent_id":"2","parent":"李四","level":"4","rank":"1","access_token":"f04e271a89b92854c44b5fc95ea8270a"}
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
         * uid : 1
         * username : 12154545
         * nickname : 张三
         * mobile : 13800138000
         * avatar : http://img.7seme.com/avatar/2016/0310/5779_20160310181013470.jpg
         * parent_id : 2
         * parent : 李四
         * level : 4
         * rank : 1
         * access_token : f04e271a89b92854c44b5fc95ea8270a
         */

        private String uid;
        private String username;
        private String nickname;
        private String mobile;
        private String avatar;
        private String parent_id;
        private String parent;
        private String level;
        private String rank;
        private String access_token;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }
    }
}
