package com.tchy.syh.fav.collect.entity;

import java.io.Serializable;
import java.util.List;

public class ReadListResp implements Serializable {

    /**
     * status : 1
     * info : success
     * data : [{"title":"书名","thumb":"图片","author":"作者","fid":"分类ID","fname":"分类名","type":"","description":"简介","play_num":"阅读量","comment_num":"评论量","like_num":"点赞量","collect_num":"收藏量","dashang_num":"打赏量","my_price":"售价","user_rank":"学习等级","total_list":"总集数","cur_list":"已经更新集数","add_time":"发布时间"}]
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
         * title : 书名
         * thumb : 图片
         * author : 作者
         * fid : 分类ID
         * fname : 分类名
         * type :
         * description : 简介
         * play_num : 阅读量
         * comment_num : 评论量
         * like_num : 点赞量
         * collect_num : 收藏量
         * dashang_num : 打赏量
         * my_price : 售价
         * user_rank : 学习等级
         * total_list : 总集数
         * cur_list : 已经更新集数
         * add_time : 发布时间
         */

        private String title;
        private String thumb;
        private String author;
        private String fid;
        private String fname;
        private String type;
        private String description;
        private String play_num;
        private String comment_num;
        private String like_num;
        private String collect_num;
        private String dashang_num;
        private String my_price;
        private String user_rank;
        private String total_list;
        private String cur_list;
        private String add_time;

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

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPlay_num() {
            return play_num;
        }

        public void setPlay_num(String play_num) {
            this.play_num = play_num;
        }

        public String getComment_num() {
            return comment_num;
        }

        public void setComment_num(String comment_num) {
            this.comment_num = comment_num;
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

        public String getDashang_num() {
            return dashang_num;
        }

        public void setDashang_num(String dashang_num) {
            this.dashang_num = dashang_num;
        }

        public String getMy_price() {
            return my_price;
        }

        public void setMy_price(String my_price) {
            this.my_price = my_price;
        }

        public String getUser_rank() {
            return user_rank;
        }

        public void setUser_rank(String user_rank) {
            this.user_rank = user_rank;
        }

        public String getTotal_list() {
            return total_list;
        }

        public void setTotal_list(String total_list) {
            this.total_list = total_list;
        }

        public String getCur_list() {
            return cur_list;
        }

        public void setCur_list(String cur_list) {
            this.cur_list = cur_list;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }
}
