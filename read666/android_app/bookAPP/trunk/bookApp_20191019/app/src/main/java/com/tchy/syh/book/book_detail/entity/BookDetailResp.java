package com.tchy.syh.book.book_detail.entity;

import java.io.Serializable;

public class BookDetailResp implements Serializable {


    /**
     * status : 1
     * info : success
     * data : {"title":"演讲的力量","keyword":null,"description":"让易发老师带领大家领略演说的魅力，瞬间成为演说高手","pic":"http://imsyh.7seme.com/image/1/2/201808/2_201808062003465209.jpg","author":"666书友会","press":"","words":"0.00","fid":"12","default_video":"http://imsyh.7seme.com/video/201808/Q6bDCfxD7W.mp4","default_mp3":"","type":"book","duration":"","play_num":34574,"comment_num":89,"like_num":3016,"collect_num":0,"dashang_num":2,"fname":"事业腾飞","ficon":"http://imsyh.7seme.com/image/1/1/201711/1_201711201736453492.png","purview":"none","content":"<section style=\"white-space: normal; font-family: 微软雅黑;\"><section><section class=\"_135editor\" style=\"border: 0px none; box-sizing: border-box;\"><p style=\"text-indent: 2em; line-height: 2em;\"><span style=\"color: rgb(101, 101, 101); font-family: dashicons, 微软雅黑, sans-serif; font-size: 18px;\">在《演讲的力量》中，克里斯·安德森分享了成功演讲的5大关键技巧\u2014\u2014联系、叙述、说明、说服与揭露\u2014\u2014教你如何发表一场具有影响力的简短演讲，展现更好的那一面。他在书中回答了经常被问到的关于演讲的问题：我该穿什么？怎么克服紧张？怎么设计我的演讲ppt？该如何设计演讲的起承转合？怎样开头和结尾，才能让人印象深刻？\u2026\u2026<\/span><\/p><p><br/><\/p><p><br/><\/p><\/section><\/section><\/section><p style=\"white-space: normal; text-align: center;\"><img src=\"http://imsyh.7seme.com/image/180806/201808062009028599.png\" alt=\"201808062009028599.png\"/><\/p><p><br/><\/p>","content_tuwen":"<section style=\"font-family: 微软雅黑;\"><section><section class=\"_135editor\" style=\"border: 0px none; box-sizing: border-box;\"><p style=\"text-indent: 2em; line-height: 2em;\"><span style=\"color: rgb(101, 101, 101); font-family: dashicons, 微软雅黑, sans-serif; font-size: 18px;\">在《演讲的力量》中，克里斯·安德森分享了成功演讲的5大关键技巧\u2014\u2014联系、叙述、说明、说服与揭露\u2014\u2014教你如何发表一场具有影响力的简短演讲，展现更好的那一面。他在书中回答了经常被问到的关于演讲的问题：我该穿什么？怎么克服紧张？怎么设计我的演讲ppt？该如何设计演讲的起承转合？怎样开头和结尾，才能让人印象深刻？\u2026\u2026<\/span><\/p><p><br/><\/p><p><br/><\/p><\/section><\/section><\/section><p style=\"text-align:center\"><img src=\"http://imsyh.7seme.com/image/180806/201808062009028599.png\" alt=\"201808062009028599.png\"/><\/p><p style=\"text-indent: 2em;\"><br/><\/p>","content_fee":"","content_goods":"","is_collect":0,"is_zan":0,"url":"http://syh.7seme.com/info/learn_info?id=119"}
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
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        /**
         * title : 演讲的力量
         * keyword : null
         * description : 让易发老师带领大家领略演说的魅力，瞬间成为演说高手
         * pic : http://imsyh.7seme.com/image/1/2/201808/2_201808062003465209.jpg
         * author : 666书友会
         * press :
         * words : 0.00
         * fid : 12
         * default_video : http://imsyh.7seme.com/video/201808/Q6bDCfxD7W.mp4
         * default_mp3 :
         * type : book
         * duration :
         * play_num : 34574
         * comment_num : 89
         * like_num : 3016
         * collect_num : 0
         * dashang_num : 2
         * fname : 事业腾飞
         * ficon : http://imsyh.7seme.com/image/1/1/201711/1_201711201736453492.png
         * purview : none
         * content : <section style="white-space: normal; font-family: 微软雅黑;"><section><section class="_135editor" style="border: 0px none; box-sizing: border-box;"><p style="text-indent: 2em; line-height: 2em;"><span style="color: rgb(101, 101, 101); font-family: dashicons, 微软雅黑, sans-serif; font-size: 18px;">在《演讲的力量》中，克里斯·安德森分享了成功演讲的5大关键技巧——联系、叙述、说明、说服与揭露——教你如何发表一场具有影响力的简短演讲，展现更好的那一面。他在书中回答了经常被问到的关于演讲的问题：我该穿什么？怎么克服紧张？怎么设计我的演讲ppt？该如何设计演讲的起承转合？怎样开头和结尾，才能让人印象深刻？……</span></p><p><br/></p><p><br/></p></section></section></section><p style="white-space: normal; text-align: center;"><img src="http://imsyh.7seme.com/image/180806/201808062009028599.png" alt="201808062009028599.png"/></p><p><br/></p>
         * content_tuwen : <section style="font-family: 微软雅黑;"><section><section class="_135editor" style="border: 0px none; box-sizing: border-box;"><p style="text-indent: 2em; line-height: 2em;"><span style="color: rgb(101, 101, 101); font-family: dashicons, 微软雅黑, sans-serif; font-size: 18px;">在《演讲的力量》中，克里斯·安德森分享了成功演讲的5大关键技巧——联系、叙述、说明、说服与揭露——教你如何发表一场具有影响力的简短演讲，展现更好的那一面。他在书中回答了经常被问到的关于演讲的问题：我该穿什么？怎么克服紧张？怎么设计我的演讲ppt？该如何设计演讲的起承转合？怎样开头和结尾，才能让人印象深刻？……</span></p><p><br/></p><p><br/></p></section></section></section><p style="text-align:center"><img src="http://imsyh.7seme.com/image/180806/201808062009028599.png" alt="201808062009028599.png"/></p><p style="text-indent: 2em;"><br/></p>
         * content_fee :
         * content_goods :
         * is_collect : 0
         * is_zan : 0
         * url : http://syh.7seme.com/info/learn_info?id=119
         */
        private String id;
        private String title;
        private Object keyword;
        private String description;
        private String pic;
        private String author;
        private String press;
        private String words;
        private String fid;
        private String default_video;
        private String default_mp3;
        private String type;
        private String duration;
        private int play_num;
        private int comment_num;
        private int like_num;
        private int collect_num;
        private int dashang_num;
        private String fname;
        private String ficon;
        private String purview;
        private String content;
        private String content_tuwen;
        private String content_fee;
        private String content_goods;
        private int is_collect;
        public String rank;

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        private int is_zan;
        private String url;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getKeyword() {
            return keyword;
        }

        public void setKeyword(Object keyword) {
            this.keyword = keyword;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getPress() {
            return press;
        }

        public void setPress(String press) {
            this.press = press;
        }

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public String getDefault_video() {
            return default_video;
        }

        public void setDefault_video(String default_video) {
            this.default_video = default_video;
        }

        public String getDefault_mp3() {
            return default_mp3;
        }

        public void setDefault_mp3(String default_mp3) {
            this.default_mp3 = default_mp3;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public int getPlay_num() {
            return play_num;
        }

        public void setPlay_num(int play_num) {
            this.play_num = play_num;
        }

        public int getComment_num() {
            return comment_num;
        }

        public void setComment_num(int comment_num) {
            this.comment_num = comment_num;
        }

        public int getLike_num() {
            return like_num;
        }

        public void setLike_num(int like_num) {
            this.like_num = like_num;
        }

        public int getCollect_num() {
            return collect_num;
        }

        public void setCollect_num(int collect_num) {
            this.collect_num = collect_num;
        }

        public int getDashang_num() {
            return dashang_num;
        }

        public void setDashang_num(int dashang_num) {
            this.dashang_num = dashang_num;
        }

        public String getFname() {
            return fname;
        }

        public void setFname(String fname) {
            this.fname = fname;
        }

        public String getFicon() {
            return ficon;
        }

        public void setFicon(String ficon) {
            this.ficon = ficon;
        }

        public String getPurview() {
            return purview;
        }

        public void setPurview(String purview) {
            this.purview = purview;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent_tuwen() {
            return content_tuwen;
        }

        public void setContent_tuwen(String content_tuwen) {
            this.content_tuwen = content_tuwen;
        }

        public String getContent_fee() {
            return content_fee;
        }

        public void setContent_fee(String content_fee) {
            this.content_fee = content_fee;
        }

        public String getContent_goods() {
            return content_goods;
        }

        public void setContent_goods(String content_goods) {
            this.content_goods = content_goods;
        }

        public int getIs_collect() {
            return is_collect;
        }

        public void setIs_collect(int is_collect) {
            this.is_collect = is_collect;
        }

        public int getIs_zan() {
            return is_zan;
        }

        public void setIs_zan(int is_zan) {
            this.is_zan = is_zan;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
