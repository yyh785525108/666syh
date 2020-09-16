package com.tchy.syh.my.entity;

import java.util.List;

public class BonusResp {

    /**
     * status : 1
     * info : success
     * data : {"total_count":"6","count":6,"list":[{"id":"175805","money":"5","note":"签到送奖学金","time":"1536545724"},{"id":"167435","money":"5","note":"签到送奖学金","time":"1535723207"},{"id":"160917","money":"5","note":"签到送奖学金","time":"1535092456"},{"id":"149870","money":"5","note":"签到送奖学金","time":"1533959513"},{"id":"148884","money":"5","note":"签到送奖学金","time":"1533862933"},{"id":"147444","money":"2","note":"完成新手任务【同步微信】赠送奖学金","time":"1533715400"}]}
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
         * total_count : 6
         * count : 6
         * list : [{"id":"175805","money":"5","note":"签到送奖学金","time":"1536545724"},{"id":"167435","money":"5","note":"签到送奖学金","time":"1535723207"},{"id":"160917","money":"5","note":"签到送奖学金","time":"1535092456"},{"id":"149870","money":"5","note":"签到送奖学金","time":"1533959513"},{"id":"148884","money":"5","note":"签到送奖学金","time":"1533862933"},{"id":"147444","money":"2","note":"完成新手任务【同步微信】赠送奖学金","time":"1533715400"}]
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
             * id : 175805
             * money : 5
             * note : 签到送奖学金
             * time : 1536545724
             */

            private String id;
            private String money;
            private String note;
            private String time;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getMoney() {
                return money;
            }

            public void setMoney(String money) {
                this.money = money;
            }

            public String getNote() {
                return note;
            }

            public void setNote(String note) {
                this.note = note;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }
}
