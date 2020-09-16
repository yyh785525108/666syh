package com.tchy.syh.collect.entity;

import java.util.List;

public class CollectResp {

    /**
     * status : 1
     * info : success
     * data : {"total_count":"1","count":1,"list":[{"id":"825","fid":"25","title":"高考满分作文出来了，震撼8亿人。。。。。。（看完泪目）","thumb":"http://imsyh.7seme.com/image/1/2/201806/2_201806081652254408.jpg","description":"当下社会现状，被一个高中毕业生破解了：滿分作文出炉，看了感触真深刻。800字的文章表达的如此完美：格局大、立意高，十八岁的孩子那么懂事！能写出如此霸气的作文，不得不让我们佩服！","comment_num":"1","type":"news","view_num":"211748","collect_num":"409","add_time":"1535096755","fname":"时事热点"}]}
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
         * list : [{"id":"825","fid":"25","title":"高考满分作文出来了，震撼8亿人。。。。。。（看完泪目）","thumb":"http://imsyh.7seme.com/image/1/2/201806/2_201806081652254408.jpg","description":"当下社会现状，被一个高中毕业生破解了：滿分作文出炉，看了感触真深刻。800字的文章表达的如此完美：格局大、立意高，十八岁的孩子那么懂事！能写出如此霸气的作文，不得不让我们佩服！","comment_num":"1","type":"news","view_num":"211748","collect_num":"409","add_time":"1535096755","fname":"时事热点"}]
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
             * id : 825
             * fid : 25
             * title : 高考满分作文出来了，震撼8亿人。。。。。。（看完泪目）
             * thumb : http://imsyh.7seme.com/image/1/2/201806/2_201806081652254408.jpg
             * description : 当下社会现状，被一个高中毕业生破解了：滿分作文出炉，看了感触真深刻。800字的文章表达的如此完美：格局大、立意高，十八岁的孩子那么懂事！能写出如此霸气的作文，不得不让我们佩服！
             * comment_num : 1
             * type : news
             * view_num : 211748
             * collect_num : 409
             * add_time : 1535096755
             * fname : 时事热点
             */

            private String id;
            private String fid;
            private String title;
            private String thumb;
            private String description;
            private String comment_num;

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }

            private String duration;
            private String type;
            private String view_num;
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

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getView_num() {
                return view_num;
            }

            public void setView_num(String view_num) {
                this.view_num = view_num;
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
