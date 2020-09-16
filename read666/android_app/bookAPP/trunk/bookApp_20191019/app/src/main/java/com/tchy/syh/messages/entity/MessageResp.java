package com.tchy.syh.messages.entity;

import java.io.Serializable;
import java.util.List;

public class MessageResp implements Serializable {


    /**
     * status : 1
     * info : success
     * data : {"total_count":"1","count":1,"list":[{"id":"1","title":"测试测试","contents":"随便写几句","user_rank":0,"add_time":"2018-06-24 08:28:27"}]}
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
         * list : [{"id":"1","title":"测试测试","contents":"随便写几句","user_rank":0,"add_time":"2018-06-24 08:28:27"}]
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
             * id : 1
             * title : 测试测试
             * contents : 随便写几句
             * user_rank : 0
             * add_time : 2018-06-24 08:28:27
             */

            private String id;
            private String title;
            private String contents;
            private int user_rank;
            private String add_time;

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

            public String getContents() {
                return contents;
            }

            public void setContents(String contents) {
                this.contents = contents;
            }

            public int getUser_rank() {
                return user_rank;
            }

            public void setUser_rank(int user_rank) {
                this.user_rank = user_rank;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }
        }
    }
}
