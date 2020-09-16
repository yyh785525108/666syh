package com.tchy.syh.my.entity;

import java.io.Serializable;

public class MyResp implements Serializable {

    /**
     * status : 1
     * info : success
     * data : {"uid":"2238493","nickname":"Melee","avatar":"http://imsyh.7seme.com/avatar/2018/0702/2018070217294585122.jpg","level":0,"level_name":"普通会员","deadline":0,"mobile":"","password":"634619","set_pwd":0,"parent_id":"5538","parent_name":"读书改变命运","fensi":0,"money":0,"integral":27,"sex":"保密","birthday":"0000-00-00","renzheng":0,"is_qiandao":1}
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
         * uid : 2238493
         * nickname : Melee
         * avatar : http://imsyh.7seme.com/avatar/2018/0702/2018070217294585122.jpg
         * level : 0
         * level_name : 普通会员
         * deadline : 0
         * mobile :
         * password : 634619
         * set_pwd : 0
         * parent_id : 5538
         * parent_name : 读书改变命运
         * fensi : 0
         * money : 0
         * integral : 27
         * sex : 保密
         * birthday : 0000-00-00
         * renzheng : 0
         * is_qiandao : 1
         */

        private String uid;
        private String nickname;
        private String avatar;
        private int level;
        private String level_name;
        private int deadline;
        private String mobile;
        private String password;
        private int set_pwd;
        private String parent_id;
        private String parent_name;
        private int fensi;
        private double money;
        private int integral;
        private String sex;
        private String birthday;
        private String renzheng;
        private int is_qiandao;

        public String getIs_new() {
            return is_new;
        }

        public void setIs_new(String is_new) {
            this.is_new = is_new;
        }

        private String is_new;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getLevel_name() {
            return level_name;
        }

        public void setLevel_name(String level_name) {
            this.level_name = level_name;
        }

        public int getDeadline() {
            return deadline;
        }

        public void setDeadline(int deadline) {
            this.deadline = deadline;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getSet_pwd() {
            return set_pwd;
        }

        public void setSet_pwd(int set_pwd) {
            this.set_pwd = set_pwd;
        }

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
        }

        public String getParent_name() {
            return parent_name;
        }

        public void setParent_name(String parent_name) {
            this.parent_name = parent_name;
        }

        public int getFensi() {
            return fensi;
        }

        public void setFensi(int fensi) {
            this.fensi = fensi;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String  getRenzheng() {
            return renzheng;
        }

        public void setRenzheng(String renzheng) {
            this.renzheng = renzheng;
        }

        public int getIs_qiandao() {
            return is_qiandao;
        }

        public void setIs_qiandao(int is_qiandao) {
            this.is_qiandao = is_qiandao;
        }
    }
}
