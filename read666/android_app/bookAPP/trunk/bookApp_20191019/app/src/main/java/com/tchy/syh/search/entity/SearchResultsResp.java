package com.tchy.syh.search.entity;

import java.io.Serializable;
import java.util.List;

public class SearchResultsResp implements Serializable {


    /**
     * status : 1
     * info : success
     * data : {"total_count":"1","count":1,"list":[{"id":"82","fid":"12","title":"影响力","type":"book","thumb":"http://imsyh.7seme.com/image/1/2/201803/2_201803111816203577.jpg","description":"这是一本可以操控别人和影响别人的一本心理学巨作","comment_num":"367","view_num":"99499","like_num":"681","collect_num":"127","add_time":"1520762520","fname":"事业腾飞"}]}
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
         * list : [{"id":"82","fid":"12","title":"影响力","type":"book","thumb":"http://imsyh.7seme.com/image/1/2/201803/2_201803111816203577.jpg","description":"这是一本可以操控别人和影响别人的一本心理学巨作","comment_num":"367","view_num":"99499","like_num":"681","collect_num":"127","add_time":"1520762520","fname":"事业腾飞"}]
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
             * id : 82
             * fid : 12
             * title : 影响力
             * type : book
             * thumb : http://imsyh.7seme.com/image/1/2/201803/2_201803111816203577.jpg
             * description : 这是一本可以操控别人和影响别人的一本心理学巨作
             * comment_num : 367
             * view_num : 99499
             * like_num : 681
             * collect_num : 127
             * add_time : 1520762520
             * fname : 事业腾飞
             */

            private String id;
            private String fid;
            private String title;
            private String type;
            private String thumb;
            private String description;
            private String comment_num;
            private String view_num;
            private String like_num;
            private String collect_num;
            private String add_time;
            private String fname;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getFid() {
                return fid;
            }

            public void setFid(String fid) {
                this.fid = fid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getComment_num() {
                return comment_num;
            }

            public void setComment_num(String comment_num) {
                this.comment_num = comment_num;
            }

            public String getView_num() {
                return view_num;
            }

            public void setView_num(String view_num) {
                this.view_num = view_num;
            }

            public String getLike_num() {
                return like_num;
            }

            public void setLike_num(String like_num) {
                this.like_num = like_num;
            }

            public String getCollect_num() {
                return collect_num;
            }

            public void setCollect_num(String collect_num) {
                this.collect_num = collect_num;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public String getFname() {
                return fname;
            }

            public void setFname(String fname) {
                this.fname = fname;
            }
        }
    }
}
