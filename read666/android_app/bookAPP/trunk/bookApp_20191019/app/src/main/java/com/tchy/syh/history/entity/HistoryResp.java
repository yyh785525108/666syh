package com.tchy.syh.history.entity;

import java.io.Serializable;
import java.util.List;

public class HistoryResp implements Serializable {


    /**
     * status : 1
     * info : success
     * data : {"total_count":"3","count":3,"list":[{"id":"52","title":"秘密","thumb":"http://xxx","src":"http://xxx","played_time":"25"},{"id":"85","title":"每日一听更新至第130期：创业公司人多力量大？","thumb":"http://xxx","src":"http://xxx","played_time":"255","sub_title":"第130期：创业公司人多力量大？"},{"id":"37","title":"人性的弱点","thumb":"http://xxx","src":"http://xxx","played_time":"315"}]}
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
         * total_count : 3
         * count : 3
         * list : [{"id":"52","title":"秘密","thumb":"http://xxx","src":"http://xxx","played_time":"25"},{"id":"85","title":"每日一听更新至第130期：创业公司人多力量大？","thumb":"http://xxx","src":"http://xxx","played_time":"255","sub_title":"第130期：创业公司人多力量大？"},{"id":"37","title":"人性的弱点","thumb":"http://xxx","src":"http://xxx","played_time":"315"}]
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
             * id : 52
             * title : 秘密
             * thumb : http://xxx
             * src : http://xxx
             * played_time : 25
             * sub_title : 第130期：创业公司人多力量大？
             */

            private String id;
            private String title;
            private String thumb;
            private String src;
            private String played_time;
            private String sub_title;

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

            public String getSrc() {
                return src;
            }

            public void setSrc(String src) {
                this.src = src;
            }

            public String getPlayed_time() {
                return played_time;
            }

            public void setPlayed_time(String played_time) {
                this.played_time = played_time;
            }

            public String getSub_title() {
                return sub_title;
            }

            public void setSub_title(String sub_title) {
                this.sub_title = sub_title;
            }
        }
    }
}
