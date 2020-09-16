package com.tchy.syh.orders.entity;

import java.util.List;

public class OrderResp {


    /**
     * status : 1
     * info : success
     * data : {"total_count":"1","count":1,"list":[{"id":668,"order_sn":"18911100601212","goods_name":"把信送给加西亚","thumb":"http://imsyh.7seme.com/image/1/2/201809/2_201809031105205143.jpg","num":1,"goods_jefen":0,"goods_amount":58,"amount":39.98,"jifen":0,"time":1536631561,"pay_status":"0","pay_status_str":"未付款","shipping_status":"0","shipping_status_str":"未发货","desc":"企业家，政府机关和员工的职业精神读本！"}]}
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
         * total_count : 1
         * count : 1
         * list : [{"id":668,"order_sn":"18911100601212","goods_name":"把信送给加西亚","thumb":"http://imsyh.7seme.com/image/1/2/201809/2_201809031105205143.jpg","num":1,"goods_jefen":0,"goods_amount":58,"amount":39.98,"jifen":0,"time":1536631561,"pay_status":"0","pay_status_str":"未付款","shipping_status":"0","shipping_status_str":"未发货","desc":"企业家，政府机关和员工的职业精神读本！"}]
         */

        private String total_count;
        private int count;
        private List<ListBean> list;

        public String getTotal_count() {
            return total_count;
        }

        public void setTotal_count(String total_count) {
            this.total_count = total_count;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 668
             * order_sn : 18911100601212
             * goods_name : 把信送给加西亚
             * thumb : http://imsyh.7seme.com/image/1/2/201809/2_201809031105205143.jpg
             * num : 1
             * goods_jefen : 0
             * goods_amount : 58
             * amount : 39.98
             * jifen : 0
             * time : 1536631561
             * pay_status : 0
             * pay_status_str : 未付款
             * shipping_status : 0
             * shipping_status_str : 未发货
             * desc : 企业家，政府机关和员工的职业精神读本！
             */

            private int id;
            private String order_sn;
            private String goods_name;
            private String thumb;
            private int num;
            private int goods_jefen;
            private String goods_amount;
            private double amount;
            private int jifen;
            private int time;
            private String pay_status;
            private String pay_status_str;
            private String shipping_status;
            private String shipping_status_str;
            private String desc;

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

            public String getGoods_name() {
                return goods_name;
            }

            public void setGoods_name(String goods_name) {
                this.goods_name = goods_name;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public int getGoods_jefen() {
                return goods_jefen;
            }

            public void setGoods_jefen(int goods_jefen) {
                this.goods_jefen = goods_jefen;
            }

            public String getGoods_amount() {
                return goods_amount;
            }

            public void setGoods_amount(String goods_amount) {
                this.goods_amount = goods_amount;
            }

            public double getAmount() {
                return amount;
            }

            public void setAmount(double amount) {
                this.amount = amount;
            }

            public int getJifen() {
                return jifen;
            }

            public void setJifen(int jifen) {
                this.jifen = jifen;
            }

            public int getTime() {
                return time;
            }

            public void setTime(int time) {
                this.time = time;
            }

            public String getPay_status() {
                return pay_status;
            }

            public void setPay_status(String pay_status) {
                this.pay_status = pay_status;
            }

            public String getPay_status_str() {
                return pay_status_str;
            }

            public void setPay_status_str(String pay_status_str) {
                this.pay_status_str = pay_status_str;
            }

            public String getShipping_status() {
                return shipping_status;
            }

            public void setShipping_status(String shipping_status) {
                this.shipping_status = shipping_status;
            }

            public String getShipping_status_str() {
                return shipping_status_str;
            }

            public void setShipping_status_str(String shipping_status_str) {
                this.shipping_status_str = shipping_status_str;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }
        }
    }
}
