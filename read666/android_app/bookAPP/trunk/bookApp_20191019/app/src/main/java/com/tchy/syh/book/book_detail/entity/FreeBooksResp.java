package com.tchy.syh.book.book_detail.entity;

import java.io.Serializable;
import java.util.List;

public class FreeBooksResp implements Serializable {


    /**
     * status : 1
     * info : success
     * data : {"total_count":"2","count":2,"list":[{"id":"72","fid":"9","bookname":"力量","thumb":"http://imsyh.7seme.com/image/1/2/201801/2_201801171512267496.jpg@1e_240w_350h_1c_0i_1o_80Q_1x.jpg","intro":"爱是驱动你的力量，爱是世界上最强大同时也是最不为人知的力量","booknum":"114251","add_time":"1516172827","fname":"心灵成长"},{"id":"21","fid":"12","bookname":"富爸爸穷爸爸","thumb":"http://imsyh.7seme.com/image/1/1/201710/1_201710281708025157.jpg@1e_240w_350h_1c_0i_1o_80Q_1x.jpg","intro":"中国财商教父周子老师亲自解读","booknum":"337025","add_time":"1507781938","fname":"事业腾飞"}]}
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
         * list : [{"id":"72","fid":"9","bookname":"力量","thumb":"http://imsyh.7seme.com/image/1/2/201801/2_201801171512267496.jpg@1e_240w_350h_1c_0i_1o_80Q_1x.jpg","intro":"爱是驱动你的力量，爱是世界上最强大同时也是最不为人知的力量","booknum":"114251","add_time":"1516172827","fname":"心灵成长"},{"id":"21","fid":"12","bookname":"富爸爸穷爸爸","thumb":"http://imsyh.7seme.com/image/1/1/201710/1_201710281708025157.jpg@1e_240w_350h_1c_0i_1o_80Q_1x.jpg","intro":"中国财商教父周子老师亲自解读","booknum":"337025","add_time":"1507781938","fname":"事业腾飞"}]
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
             * id : 72
             * fid : 9
             * bookname : 力量
             * thumb : http://imsyh.7seme.com/image/1/2/201801/2_201801171512267496.jpg@1e_240w_350h_1c_0i_1o_80Q_1x.jpg
             * intro : 爱是驱动你的力量，爱是世界上最强大同时也是最不为人知的力量
             * booknum : 114251
             * add_time : 1516172827
             * fname : 心灵成长
             */

            private String id;
            private String fid;
            private String bookname;
            private String thumb;
            private String intro;
            private String booknum;
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

            public String getBookname() {
                return bookname;
            }

            public void setBookname(String bookname) {
                this.bookname = bookname;
            }

            public String getThumb() {
                return thumb;
            }

            public void setThumb(String thumb) {
                this.thumb = thumb;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public String getBooknum() {
                return booknum;
            }

            public void setBooknum(String booknum) {
                this.booknum = booknum;
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
