package com.tchy.syh.daily_advance.entity;

import android.os.Parcel;
import android.os.Parcelable;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DailyAdvanceListResp implements Serializable {


    /**
     * status : 1
     * info : success
     * data : {"total_count":"373","count":10,"list":[{"id":"10933","uid":"2517728","puid":0,"learn_id":"52","album_id":0,"content":"打赏2.00元","reply_num":0,"lovenum":0,"add_time":"1532080556","love_id":null,"avatar":"http://syh.7seme.com/images/default-avatar.jpg","nickname":"2517728","comment_list":[]},{"id":"10930","uid":"2499139","puid":0,"learn_id":"52","album_id":0,"content":"看了好几本书，周老师，讲的很好","reply_num":0,"lovenum":0,"add_time":"1532074554","love_id":null,"avatar":"http://syh.7seme.com/images/default-avatar.jpg","nickname":"2499139","comment_list":[]},{"id":"10928","uid":"2516723","puid":0,"learn_id":"52","album_id":0,"content":"打赏2.00元","reply_num":0,"lovenum":0,"add_time":"1532071242","love_id":null,"avatar":"http://syh.7seme.com/images/default-avatar.jpg","nickname":"2516723","comment_list":[]},{"id":"10922","uid":"2232881","puid":0,"learn_id":"52","album_id":0,"content":"打赏2.00元","reply_num":0,"lovenum":0,"add_time":"1532066293","love_id":null,"avatar":"http://imsyh.7seme.com/avatar/2018/0717/2018071719471869318.jpg","nickname":"孙四＋会员＋可乐姐","comment_list":[]},{"id":"10918","uid":"1152733","puid":0,"learn_id":"52","album_id":0,"content":"打赏2.00元","reply_num":0,"lovenum":0,"add_time":"1532063296","love_id":null,"avatar":"http://syh.7seme.com/images/default-avatar.jpg","nickname":"1152733","comment_list":[]},{"id":"10911","uid":"681010","puid":0,"learn_id":"52","album_id":0,"content":"打赏2.00元","reply_num":0,"lovenum":0,"add_time":"1532057859","love_id":null,"avatar":"http://imsyh.7seme.com/avatar/2018/0718/2018071819481385529.jpg","nickname":"孙鸿燕","comment_list":[]},{"id":"10902","uid":"2255691","puid":0,"learn_id":"52","album_id":0,"content":"打赏2.00元","reply_num":0,"lovenum":"1","add_time":"1532051413","love_id":null,"avatar":"http://imsyh.7seme.com/avatar/2018/0712/2018071219062491627.jpg","nickname":"静15147325991","comment_list":[]},{"id":"10884","uid":"2509930","puid":0,"learn_id":"52","album_id":0,"content":"打赏2.00元","reply_num":"1","lovenum":"1","add_time":"1532026817","love_id":null,"avatar":"http://syh.7seme.com/images/default-avatar.jpg","nickname":"2509930","comment_list":[]},{"id":"10879","uid":"1950422","puid":0,"learn_id":"52","album_id":0,"content":"打赏100.00元","reply_num":0,"lovenum":"2","add_time":"1532015423","love_id":null,"avatar":"http://imsyh.7seme.com/avatar/2018/0717/2018071720101138367.jpg","nickname":"南充鼎典广告字牌厂","comment_list":[]},{"id":"10878","uid":"16533","puid":0,"learn_id":"52","album_id":0,"content":"打赏10.00元","reply_num":0,"lovenum":0,"add_time":"1532014843","love_id":null,"avatar":"http://imsyh.7seme.com/avatar/2018/0719/2018071912561089612.jpg","nickname":"A L\u2006i\u2006n\u2006g","comment_list":[]}]}
     */

    private int status;
    private String info;
    private DailyAdvanceListResp.DataBean data;

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

    public DailyAdvanceListResp.DataBean getData() {
        return data;
    }

    public void setData(DailyAdvanceListResp.DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * total_count : 373
         * count : 10
         * list : [{"id":"10933","uid":"2517728","puid":0,"learn_id":"52","album_id":0,"content":"打赏2.00元","reply_num":0,"lovenum":0,"add_time":"1532080556","love_id":null,"avatar":"http://syh.7seme.com/images/default-avatar.jpg","nickname":"2517728","comment_list":[]},{"id":"10930","uid":"2499139","puid":0,"learn_id":"52","album_id":0,"content":"看了好几本书，周老师，讲的很好","reply_num":0,"lovenum":0,"add_time":"1532074554","love_id":null,"avatar":"http://syh.7seme.com/images/default-avatar.jpg","nickname":"2499139","comment_list":[]},{"id":"10928","uid":"2516723","puid":0,"learn_id":"52","album_id":0,"content":"打赏2.00元","reply_num":0,"lovenum":0,"add_time":"1532071242","love_id":null,"avatar":"http://syh.7seme.com/images/default-avatar.jpg","nickname":"2516723","comment_list":[]},{"id":"10922","uid":"2232881","puid":0,"learn_id":"52","album_id":0,"content":"打赏2.00元","reply_num":0,"lovenum":0,"add_time":"1532066293","love_id":null,"avatar":"http://imsyh.7seme.com/avatar/2018/0717/2018071719471869318.jpg","nickname":"孙四＋会员＋可乐姐","comment_list":[]},{"id":"10918","uid":"1152733","puid":0,"learn_id":"52","album_id":0,"content":"打赏2.00元","reply_num":0,"lovenum":0,"add_time":"1532063296","love_id":null,"avatar":"http://syh.7seme.com/images/default-avatar.jpg","nickname":"1152733","comment_list":[]},{"id":"10911","uid":"681010","puid":0,"learn_id":"52","album_id":0,"content":"打赏2.00元","reply_num":0,"lovenum":0,"add_time":"1532057859","love_id":null,"avatar":"http://imsyh.7seme.com/avatar/2018/0718/2018071819481385529.jpg","nickname":"孙鸿燕","comment_list":[]},{"id":"10902","uid":"2255691","puid":0,"learn_id":"52","album_id":0,"content":"打赏2.00元","reply_num":0,"lovenum":"1","add_time":"1532051413","love_id":null,"avatar":"http://imsyh.7seme.com/avatar/2018/0712/2018071219062491627.jpg","nickname":"静15147325991","comment_list":[]},{"id":"10884","uid":"2509930","puid":0,"learn_id":"52","album_id":0,"content":"打赏2.00元","reply_num":"1","lovenum":"1","add_time":"1532026817","love_id":null,"avatar":"http://syh.7seme.com/images/default-avatar.jpg","nickname":"2509930","comment_list":[]},{"id":"10879","uid":"1950422","puid":0,"learn_id":"52","album_id":0,"content":"打赏100.00元","reply_num":0,"lovenum":"2","add_time":"1532015423","love_id":null,"avatar":"http://imsyh.7seme.com/avatar/2018/0717/2018071720101138367.jpg","nickname":"南充鼎典广告字牌厂","comment_list":[]},{"id":"10878","uid":"16533","puid":0,"learn_id":"52","album_id":0,"content":"打赏10.00元","reply_num":0,"lovenum":0,"add_time":"1532014843","love_id":null,"avatar":"http://imsyh.7seme.com/avatar/2018/0719/2018071912561089612.jpg","nickname":"A L\u2006i\u2006n\u2006g","comment_list":[]}]
         */

        private String total_count;
        private int count;
        private List<DailyAdvanceListResp.DataBean.ListBean> list;

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

        public List<DailyAdvanceListResp.DataBean.ListBean> getList() {
            return list;
        }

        public void setList(List<DailyAdvanceListResp.DataBean.ListBean> list) {
            this.list = list;
        }

        public static class ListBean  implements Parcelable {
            //public static class ListBean {
            /**
             * id : 10933
             * uid : 2517728
             * puid : 0
             * learn_id : 52
             * album_id : 0
             * content : 打赏2.00元
             * reply_num : 0
             * lovenum : 0
             * add_time : 1532080556
             * love_id : null
             * avatar : http://syh.7seme.com/images/default-avatar.jpg
             * nickname : 2517728
             * comment_list : []
             */

            private String id;
            private String uid;
            private int puid;
            private String learn_id;
            private int album_id;
            private String content;
            private int image_num;
            private int reply_num;
            private int lovenum;
            private String add_time;
            private String love_id;
            private String avatar;
            private String nickname;
            private String learn_rank;

            private int type;  //text:0 image :1  书评: 2

            public String getLearn_rank() {
                return learn_rank;
            }

            public void setLearn_rank(String learn_rank) {
                this.learn_rank = learn_rank;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getImage_num() {
                return image_num;
            }

            public void setImage_num(int image_num) {
                this.image_num = image_num;
            }

            private int is_love;

            public int getIs_love() {
                return is_love;
            }

            public void setIs_love(int is_love) {
                this.is_love = is_love;
            }

            private List<DailyAdvanceListResp.DataBean.ListBean> daily_list;

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

            public int getPuid() {
                return puid;
            }

            public void setPuid(int puid) {
                this.puid = puid;
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

            public String getLove_id() {
                return love_id;
            }

            public void setLove_id(String love_id) {
                this.love_id = love_id;
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

            public List<ListBean> getDaily_list() {
                return daily_list;
            }

            public void setDaily_list(List<ListBean> daily_list) {
                this.daily_list = daily_list;
            }

            public ListBean() {
            }


            @Override public int describeContents() { return 0; }
            @Override public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.id);
                dest.writeString(this.uid);
                dest.writeInt(this.puid);
                dest.writeString(this.learn_id);
                dest.writeInt(this.album_id);
                dest.writeString(this.content);
                dest.writeInt(this.reply_num);
                dest.writeInt(this.lovenum);
                dest.writeString(this.add_time);
                dest.writeString(this.love_id);
                dest.writeString(this.avatar);
                dest.writeString(this.nickname);
                dest.writeInt(this.type);
                dest.writeInt(this.image_num);
                dest.writeInt(this.is_love);
                dest.writeTypedList(this.daily_list);
            }
            protected ListBean(Parcel in) {
                this.id = in.readString();
                this.uid = in.readString();
                this.puid = in.readInt();
                this.learn_id = in.readString();
                this.album_id = in.readInt();
                this.content = in.readString();
                this.reply_num = in.readInt();
                this.lovenum = in.readInt();
                this.add_time = in.readString();
                this.love_id = in.readString();
                this.avatar = in.readString();
                this.nickname = in.readString();
                this.type = in.readInt();
                this.image_num = in.readInt();
                this.is_love = in.readInt();
                this.daily_list = in.createTypedArrayList(DailyAdvanceListResp.DataBean.ListBean.CREATOR);
            }
            public static final Creator<DailyAdvanceListResp.DataBean.ListBean> CREATOR = new Creator<DailyAdvanceListResp.DataBean.ListBean>(){
                @Override public DailyAdvanceListResp.DataBean.ListBean createFromParcel(Parcel source) {
                    return new DailyAdvanceListResp.DataBean.ListBean(source);
                }
                @Override public DailyAdvanceListResp.DataBean.ListBean[] newArray(int size) {
                    return new DailyAdvanceListResp.DataBean.ListBean[size];
                }
            };
        }
    }
}
