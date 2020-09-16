package com.tchy.syh.collect.entity;

import java.io.Serializable;
import java.util.List;

public class CategoryResp implements Serializable {

    /**
     * status : 1
     * info : success
     * data : [{"id":1,"name":"xxxxxxxx","icon":"xxxxx","type":""}]
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
         * id : 1
         * name : xxxxxxxx
         * icon : xxxxx
         * type :
         */

        private int id=1;
        private String name="2";
        private String icon="3";
        private String type="4";

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
