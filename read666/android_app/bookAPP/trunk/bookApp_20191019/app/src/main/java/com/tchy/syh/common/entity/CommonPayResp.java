package com.tchy.syh.common.entity;

import com.google.gson.annotations.SerializedName;

public class CommonPayResp {

    /**
     * status : 1
     * info : success
     * data : {"appid":"wx0a86121d5dea9203","partnerid":"1513637051","timestamp":"1536565438","noncestr":"k9r0swpkbv99ftk06ma902rpj57wk4qx","prepayid":"wx10154358394032edf5c52b4b1278953304","package":"Sign=WXPay","sign":"EA82D6EB3DFC45D213BD1B2A4F393353"}
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
         * appid : wx0a86121d5dea9203
         * partnerid : 1513637051
         * timestamp : 1536565438
         * noncestr : k9r0swpkbv99ftk06ma902rpj57wk4qx
         * prepayid : wx10154358394032edf5c52b4b1278953304
         * package : Sign=WXPay
         * sign : EA82D6EB3DFC45D213BD1B2A4F393353
         */

        private String appid;
        private String partnerid;
        private String timestamp;
        private String noncestr;
        private String prepayid;
        @SerializedName("package")
        private String packageX;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
