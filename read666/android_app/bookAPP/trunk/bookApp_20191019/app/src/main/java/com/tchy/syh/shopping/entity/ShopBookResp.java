package com.tchy.syh.shopping.entity;

import java.util.List;

public class ShopBookResp {

    /**
     * status : 1
     * info : success
     * data : {"total_count":"53","count":10,"list":[{"id":"127","title":"把信送给加西亚","thumb":"http://imsyh.7seme.com/image/1/2/201809/2_201809031105205143.jpg","desc":"企业家，政府机关和员工的职业精神读本！","integral":"1999","price":"58.00"},{"id":"125","title":"青春志","thumb":"http://imsyh.7seme.com/image/1/2/201808/2_201808271735439878.jpg","desc":"华夏文明的青春伊始，那时男子义气、女子多情","integral":"1999","price":"59.90"}]}
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
         * total_count : 53
         * count : 10
         * list : [{"id":"127","title":"把信送给加西亚","thumb":"http://imsyh.7seme.com/image/1/2/201809/2_201809031105205143.jpg","desc":"企业家，政府机关和员工的职业精神读本！","integral":"1999","price":"58.00"},{"id":"125","title":"青春志","thumb":"http://imsyh.7seme.com/image/1/2/201808/2_201808271735439878.jpg","desc":"华夏文明的青春伊始，那时男子义气、女子多情","integral":"1999","price":"59.90"}]
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
             * id : 127
             * title : 把信送给加西亚
             * thumb : http://imsyh.7seme.com/image/1/2/201809/2_201809031105205143.jpg
             * desc : 企业家，政府机关和员工的职业精神读本！
             * integral : 1999
             * price : 58.00
             */

            private String id;
            private String title;
            private String thumb;
            private String desc;
            private String integral;
            private String price;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getIntegral() {
                return integral;
            }

            public void setIntegral(String integral) {
                this.integral = integral;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }
    }
}
