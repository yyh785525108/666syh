package com.tchy.syh.book.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class LearnCate implements Serializable {


    /**
     * status : 1
     * info : success
     * data : [{"id":"19","name":"家庭幸福","icon":"http://imsyh.7seme.com/image/1/1/201711/1_201711201651368177.png","type":"book"},{"id":"9","name":"心灵成长","icon":"http://imsyh.7seme.com/image/1/1/201711/1_201711201639506576.png","type":"book"},{"id":"12","name":"事业腾飞","icon":"http://imsyh.7seme.com/image/1/1/201711/1_201711201736453492.png","type":"book"}]
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

    public static class DataBean  implements Parcelable{
        /**
         * id : 19
         * name : 家庭幸福
         * icon : http://imsyh.7seme.com/image/1/1/201711/1_201711201651368177.png
         * type : book
         */
        public DataBean (String id,String name){
            this.id=id;
            this.name=name;
        }
        private String id;
        private String name;
        private String icon;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(name);


        }
        public static final Creator<DataBean> CREATOR= new Creator<DataBean>() {

            /**
             * 供外部类反序列化本类数组使用
             */
            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }

            /**
             * 从Parcel中读取数据
             */
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }
        };
        public DataBean(Parcel source) {
            id = source.readString();
            name=source.readString();
        }
    }
}
