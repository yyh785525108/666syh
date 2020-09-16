package com.tchy.syh.my.entity;

import java.util.List;

public class RecommandListResp {

    /**
     * status : 1
     * info : success
     * data : [{"id":1,"title":"商界","thumb":"http://imsyh.7seme.com/image/1/1/201809/1_201809041516483297.jpg","vote_num":1,"is_vote":1},{"id":2,"title":"优势谈判","thumb":"http://imsyh.7seme.com/image/1/1/201809/1_201809041518092002.jpg","vote_num":0,"is_vote":0}]
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
         * title : 商界
         * thumb : http://imsyh.7seme.com/image/1/1/201809/1_201809041516483297.jpg
         * vote_num : 1
         * is_vote : 1
         */

        private int id;
        private String title;
        private String thumb;
        private int vote_num;
        private int is_vote;

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

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public int getVote_num() {
            return vote_num;
        }

        public void setVote_num(int vote_num) {
            this.vote_num = vote_num;
        }

        public int getIs_vote() {
            return is_vote;
        }

        public void setIs_vote(int is_vote) {
            this.is_vote = is_vote;
        }
    }
}
