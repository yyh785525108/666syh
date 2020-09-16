package com.tchy.syh.listen.entity;


import java.util.List;

public class AudioCommentEntity {


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
         * id : 15143
         * uid : 2452902
         * learn_id : 85
         * album_id : 0
         * content : 打赏2.00元
         * reply_num : 0
         * lovenum : 0
         * add_time : 1535508943
         * avatar : http://syh.7seme.com/images/default-avatar.jpg
         * nickname : 2452902
         * comment_list : []
         * is_love : 0
         */

        private String id;
        private String uid;
        private String learn_id;
        private int album_id;
        private String content;
        private int reply_num;
        private int lovenum;
        private String add_time;
        private String avatar;
        private String nickname;
        private int is_love;
        private List<?> comment_list;

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

        public String getLearn_id() {
            return learn_id;
        }

        public void setLearn_id(String learn_id) {
            this.learn_id = learn_id;
        }

        public int getAlbum_id() {
            return album_id;
        }

        public void setAlbum_id(int album_id) {
            this.album_id = album_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getReply_num() {
            return reply_num;
        }

        public void setReply_num(int reply_num) {
            this.reply_num = reply_num;
        }

        public int getLovenum() {
            return lovenum;
        }

        public void setLovenum(int lovenum) {
            this.lovenum = lovenum;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getIs_love() {
            return is_love;
        }

        public void setIs_love(int is_love) {
            this.is_love = is_love;
        }

        public List<?> getComment_list() {
            return comment_list;
        }

        public void setComment_list(List<?> comment_list) {
            this.comment_list = comment_list;
        }
    }
}
