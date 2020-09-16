package com.tchy.syh.shopping.entity;

import java.util.List;

public class AddressResp {

    /**
     * status : 1
     * info : success
     * data : [{"id":"3816","consignee":"张三","mobile":"13800138000","province":"230","city":"256","district":"259","address":"什么路多少号","is_default":0}]
     */

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

    public static class DataBean {
        /**
         * id : 3816
         * consignee : 张三
         * mobile : 13800138000
         * province : 230
         * city : 256
         * district : 259
         * address : 什么路多少号
         * is_default : 0
         */

        private String id;
        private String consignee;
        private String mobile;
        private String province;

        public String getProvinceStr() {
            return provinceStr;
        }

        public void setProvinceStr(String provinceStr) {
            this.provinceStr = provinceStr;
        }

        public String getCityStr() {
            return cityStr;
        }

        public void setCityStr(String cityStr) {
            this.cityStr = cityStr;
        }

        public String getDistrictStr() {
            return districtStr;
        }

        public void setDistrictStr(String districtStr) {
            this.districtStr = districtStr;
        }

        private String provinceStr;
        private String city;
        private String cityStr;
        private String district;
        private String districtStr;
        private String address;
        private int is_default;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getIs_default() {
            return is_default;
        }

        public void setIs_default(int is_default) {
            this.is_default = is_default;
        }
    }
}
