package com.tchy.syh.book.entity;

public class VerionBean {

    /**
     * status : 1
     * info : success
     * data : {"versionName":"666书友会","versionCode":"1.0.1","versionDesc":"产品上线","downLink":"http://imsyh.7seme.com/app/85yTckbwWc.apk","time":"2018-10-18"}
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
         * versionName : 666书友会
         * versionCode : 1.0.1
         * versionDesc : 产品上线
         * downLink : http://imsyh.7seme.com/app/85yTckbwWc.apk
         * time : 2018-10-18
         */

        private String versionName;
        private String versionCode;
        private String versionDesc;
        private String downLink;
        private String time;

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(String versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionDesc() {
            return versionDesc;
        }

        public void setVersionDesc(String versionDesc) {
            this.versionDesc = versionDesc;
        }

        public String getDownLink() {
            return downLink;
        }

        public void setDownLink(String downLink) {
            this.downLink = downLink;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
