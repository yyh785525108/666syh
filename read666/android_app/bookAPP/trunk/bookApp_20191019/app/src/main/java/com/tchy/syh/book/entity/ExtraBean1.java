package com.tchy.syh.book.entity;

import java.io.Serializable;
import java.util.List;

public class ExtraBean1 implements Serializable {

    /**
     * status : 1
     * info : ok
     * data : [{"id":"40","name":"1","link":"","img":"http://imsyh.7seme.com/image/1/2/201808/2_201808201128515471.jpg","apppage":""}]
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
         * id : 40
         * name : 1
         * link :
         * img : http://imsyh.7seme.com/image/1/2/201808/2_201808201128515471.jpg
         * apppage :
         */

        private String id;
        private String name;
        private String link;
        private String img;
        private Object apppage;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public Object getApppage() {
            return apppage;
        }

        public void setApppage(Object apppage) {
            this.apppage = apppage;
        }
    }
}
