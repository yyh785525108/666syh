package com.tchy.syh.listen.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class ListenEntity {
    public static class ListenItemEntity implements Parcelable {



        private String id;
        private String fid;
        private String title;
        private String type;
        private String thumb;
        private String description;
        private String author;
        private String comment_num;
        private String view_num;
        private String like_num;
        private String collect_num;
        private String total_album;
        private String update_time;
        private String add_time;
        private String fname;
        private String price;
        private String vipprice;
        private int update_album;

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

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
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

        public String getTotal_album() {
            return total_album;
        }

        public void setTotal_album(String total_album) {
            this.total_album = total_album;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getVipprice() {
            return vipprice;
        }

        public void setVipprice(String vipprice) {
            this.vipprice = vipprice;
        }

        public int getUpdate_album() {
            return update_album;
        }

        public void setUpdate_album(int update_album) {
            this.update_album = update_album;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.fid);
            dest.writeString(this.title);
            dest.writeString(this.type);
            dest.writeString(this.thumb);
            dest.writeString(this.description);
            dest.writeString(this.author);
            dest.writeString(this.comment_num);
            dest.writeString(this.view_num);
            dest.writeString(this.like_num);
            dest.writeString(this.collect_num);
            dest.writeString(this.total_album);
            dest.writeString(this.update_time);
            dest.writeString(this.add_time);
            dest.writeString(this.fname);
            dest.writeString(this.price);
            dest.writeString(this.vipprice);
            dest.writeInt(this.update_album);
        }

        public ListenItemEntity() {
        }

        protected ListenItemEntity(Parcel in) {
            this.id = in.readString();
            this.fid = in.readString();
            this.title = in.readString();
            this.type = in.readString();
            this.thumb = in.readString();
            this.description = in.readString();
            this.author = in.readString();
            this.comment_num = in.readString();
            this.view_num = in.readString();
            this.like_num = in.readString();
            this.collect_num = in.readString();
            this.total_album = in.readString();
            this.update_time = in.readString();
            this.add_time = in.readString();
            this.fname = in.readString();
            this.price = in.readString();
            this.vipprice = in.readString();
            this.update_album = in.readInt();
        }

        public static final Creator<ListenItemEntity> CREATOR = new Creator<ListenItemEntity>() {
            @Override
            public ListenItemEntity createFromParcel(Parcel source) {
                return new ListenItemEntity(source);
            }

            @Override
            public ListenItemEntity[] newArray(int size) {
                return new ListenItemEntity[size];
            }
        };
    }



    public static class AudioItemEntity{

        private String id;
        private String learn_id;
        private String title;
        private String description;
        private String content;
        private String default_video;
        private String thumb;
        private double price;
        private String is_free;
        private String is_show;
        private String is_iframe;
        private int start_time;
        private String sortorder;
        private int comment_num;
        private String play_num;
        private String duration;
        private String update_time;
        private String add_time;
        private String purview;
        private int sort=0;
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLearn_id() {
            return learn_id;
        }

        public void setLearn_id(String learn_id) {
            this.learn_id = learn_id;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDefault_video() {
            return default_video;
        }

        public void setDefault_video(String default_video) {
            this.default_video = default_video;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getIs_free() {
            return is_free;
        }

        public void setIs_free(String is_free) {
            this.is_free = is_free;
        }

        public String getIs_show() {
            return is_show;
        }

        public void setIs_show(String is_show) {
            this.is_show = is_show;
        }

        public String getIs_iframe() {
            return is_iframe;
        }

        public void setIs_iframe(String is_iframe) {
            this.is_iframe = is_iframe;
        }

        public int getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public String getSortorder() {
            return sortorder;
        }

        public void setSortorder(String sortorder) {
            this.sortorder = sortorder;
        }

        public int getComment_num() {
            return comment_num;
        }

        public void setComment_num(int comment_num) {
            this.comment_num = comment_num;
        }

        public String getPlay_num() {
            return play_num;
        }

        public void setPlay_num(String play_num) {
            this.play_num = play_num;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getPurview() {
            return purview;
        }

        public void setPurview(String purview) {
            this.purview = purview;
        }
    }
}
