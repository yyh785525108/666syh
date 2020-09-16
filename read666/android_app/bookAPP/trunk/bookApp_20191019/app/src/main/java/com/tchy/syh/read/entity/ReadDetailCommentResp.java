package com.tchy.syh.read.entity;

import java.io.Serializable;
import java.util.List;

public class ReadDetailCommentResp implements Serializable {

    /**
     * status : 1
     * info : success
     * data : {"total_count":"2","count":2,"list":[{"id":"19","uid":"35155","nickname":"谭月媛","avatar":"http://xxx","news_id":"77","lovenum":0,"content":"前20条我基本都占有","add_time":"1509524374","is_love":0},{"id":"18","uid":"35155","nickname":"谭月媛","avatar":"http://xxxx","news_id":"77","lovenum":0,"content":"我占前20条","add_time":"1509524317","is_love":0}]}
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
         * total_count : 2
         * count : 2
         * list : [{"id":"19","uid":"35155","nickname":"谭月媛","avatar":"http://xxx","news_id":"77","lovenum":0,"content":"前20条我基本都占有","add_time":"1509524374","is_love":0},{"id":"18","uid":"35155","nickname":"谭月媛","avatar":"http://xxxx","news_id":"77","lovenum":0,"content":"我占前20条","add_time":"1509524317","is_love":0}]
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
             * id : 19
             * uid : 35155
             * nickname : 谭月媛
             * avatar : http://xxx
             * news_id : 77
             * lovenum : 0
             * content : 前20条我基本都占有
             * add_time : 1509524374
             * is_love : 0
             */

            private String id;
            private String uid;
            private String nickname;
            private String avatar;
            private String news_id;
            private int lovenum;
            private String content;
            private String add_time;
            private int is_love;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getNews_id() {
                return news_id;
            }

            public void setNews_id(String news_id) {
                this.news_id = news_id;
            }

            public int getLovenum() {
                return lovenum;
            }

            public void setLovenum(int lovenum) {
                this.lovenum = lovenum;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public int getIs_love() {
                return is_love;
            }

            public void setIs_love(int is_love) {
                this.is_love = is_love;
            }
        }
    }
}
