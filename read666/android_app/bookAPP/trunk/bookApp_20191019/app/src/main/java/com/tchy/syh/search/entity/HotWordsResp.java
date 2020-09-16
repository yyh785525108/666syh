package com.tchy.syh.search.entity;

import java.io.Serializable;
import java.util.List;

public class HotWordsResp implements Serializable {


    /**
     * status : 1
     * info : success
     * data : [{"id":"12","title":"666书友会宣传片"},{"id":"13","title":"就这样心想事成"},{"id":"21","title":"富爸爸穷爸爸"},{"id":"117","title":"失落的百年致富圣经"},{"id":"130","title":"与神对话"},{"id":"119","title":"演讲的力量"},{"id":"128","title":"强者的逻辑"},{"id":"137","title":"幸福的方法"},{"id":"124","title":"货币战争"},{"id":"121","title":"抱怨的艺术"}]
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
         * id : 12
         * title : 666书友会宣传片
         */

        private String id;
        private String title;

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
    }
}
