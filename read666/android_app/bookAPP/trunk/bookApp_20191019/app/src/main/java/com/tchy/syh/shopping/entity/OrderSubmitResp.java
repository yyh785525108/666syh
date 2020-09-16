package com.tchy.syh.shopping.entity;

import com.google.gson.annotations.SerializedName;

public class OrderSubmitResp {


    /**
     * status : 1
     * info : success
     * data : {"id":1,"order_sn":"18911175656881","consignee":"张三","mobile":"13800138000","address":"山西省阳泉市矿区什么路多少号","amount":39.98,"wxpay":{"appid":"wx0a86121d5dea9203","partnerid":"xxxx","timestamp":"1536659816","noncestr":"v398gp7wm7gej7mcsbfkt8o0pu8ti5pu","prepayid":"xxxxx","package":"Sign=WXPay","sign":"xxxxxx"}}
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
         * id : 1
         * order_sn : 18911175656881
         * consignee : 张三
         * mobile : 13800138000
         * address : 山西省阳泉市矿区什么路多少号
         * amount : 39.98
         * wxpay : {"appid":"wx0a86121d5dea9203","partnerid":"xxxx","timestamp":"1536659816","noncestr":"v398gp7wm7gej7mcsbfkt8o0pu8ti5pu","prepayid":"xxxxx","package":"Sign=WXPay","sign":"xxxxxx"}
         */

        private int id;
        private String order_sn;
        private String consignee;
        private String mobile;
        private String address;
        private double amount;

        public int getJifen() {
            return jifen;
        }

        public void setJifen(int jifen) {
            this.jifen = jifen;
        }

        private int jifen;
        private WxpayBean wxpay;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public String getConsignee() {
            return consignee;
        }

        public void setConsignee(String consignee) {
            this.consignee = consignee;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public WxpayBean getWxpay() {
            return wxpay;
        }

        public void setWxpay(WxpayBean wxpay) {
            this.wxpay = wxpay;
        }

        public static class WxpayBean {
            /**
             * appid : wx0a86121d5dea9203
             * partnerid : xxxx
             * timestamp : 1536659816
             * noncestr : v398gp7wm7gej7mcsbfkt8o0pu8ti5pu
             * prepayid : xxxxx
             * package : Sign=WXPay
             * sign : xxxxxx
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
}
