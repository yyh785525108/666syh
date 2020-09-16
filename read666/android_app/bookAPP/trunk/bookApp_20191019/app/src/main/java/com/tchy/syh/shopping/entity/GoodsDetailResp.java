package com.tchy.syh.shopping.entity;

public class GoodsDetailResp {

    /**
     * status : 1
     * info : success
     * data : {"id":127,"title":"把信送给加西亚","desc":"企业家，政府机关和员工的职业精神读本！","thumb":"http://imsyh.7seme.com/image/1/2/201809/2_201809031105205143.jpg","sale_num":0,"integral":1999,"price":58,"content":""}
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
         * id : 127
         * title : 把信送给加西亚
         * desc : 企业家，政府机关和员工的职业精神读本！
         * thumb : http://imsyh.7seme.com/image/1/2/201809/2_201809031105205143.jpg
         * sale_num : 0
         * integral : 1999
         * price : 58
         * content :
         */

        private int id;
        private String title;
        private String desc;
        private String thumb;
        private int sale_num;
        private int integral;
        private double price;
        private String content;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public int getSale_num() {
            return sale_num;
        }

        public void setSale_num(int sale_num) {
            this.sale_num = sale_num;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
