package com.tchy.syh.my.entity;

import java.io.Serializable;
import java.util.List;

public class StudyListResp implements Serializable {

    /**
     * status : 1
     * info : success
     * data : {"list":[{"pm":1,"uid":537080,"nickname":"干承龙13329948703家电专家","avatar":"http://imsyh.7seme.com/avatar/2018/0804/2018080411311113925.jpg","duration":"750小时40分钟"},{"pm":2,"uid":242700,"nickname":"刘丽锋","avatar":"http://imsyh.7seme.com/avatar/2018/0731/2018073119231964987.jpg","duration":"534小时40分钟"},{"pm":3,"uid":491523,"nickname":"春光懒困倚微风","avatar":"http://imsyh.7seme.com/avatar/2018/0503/2018050316230852486.jpg","duration":"527小时23分钟"},{"pm":4,"uid":190523,"nickname":"大红灯笼婚纱摄影","avatar":"http://imsyh.7seme.com/avatar/2018/0227/2018022723153756273.jpg","duration":"353小时58分钟"},{"pm":5,"uid":236181,"nickname":"王琳","avatar":"http://imsyh.7seme.com/avatar/2018/0806/2018080613343483160.jpg","duration":"332小时51分钟"},{"pm":6,"uid":163749,"nickname":"孙丽华13843024933","avatar":"http://imsyh.7seme.com/avatar/2018/0708/2018070806501912332.jpg","duration":"325小时53分钟"},{"pm":7,"uid":161581,"nickname":"连美桂","avatar":"http://imsyh.7seme.com/avatar/2018/0804/2018080411504439157.jpg","duration":"299小时9分钟"},{"pm":8,"uid":304981,"nickname":"304981","avatar":"http://syh.7seme.com/images/noface.gif","duration":"296小时28分钟"},{"pm":9,"uid":48817,"nickname":"角斗士","avatar":"http://imsyh.7seme.com/avatar/2017/1227/2017122721530525410.jpg","duration":"283小时32分钟"},{"pm":10,"uid":2922,"nickname":"楊健","avatar":"http://imsyh.7seme.com/avatar/2018/0718/2018071806412522618.jpg","duration":"282小时31分钟"},{"pm":11,"uid":438281,"nickname":"（奎尚朱）和人顺天","avatar":"http://imsyh.7seme.com/avatar/2018/0729/2018072919030227116.jpg","duration":"281小时54分钟"},{"pm":12,"uid":220360,"nickname":"梦想413","avatar":"http://imsyh.7seme.com/avatar/2018/0731/2018073107003684220.jpg","duration":"240小时41分钟"},{"pm":13,"uid":228745,"nickname":"向往","avatar":"http://imsyh.7seme.com/avatar/2018/0705/2018070510283490573.jpg","duration":"240小时31分钟"},{"pm":14,"uid":971362,"nickname":"一缕阳光","avatar":"http://imsyh.7seme.com/avatar/2018/0617/2018061718453287784.jpg","duration":"238小时40分钟"},{"pm":15,"uid":241503,"nickname":"赵志龙","avatar":"http://imsyh.7seme.com/avatar/2018/0623/2018062321440693098.jpg","duration":"233小时1分钟"},{"pm":16,"uid":197594,"nickname":"康婷小阳光陈英17347904959","avatar":"http://imsyh.7seme.com/avatar/2018/0106/2018010611022811148.jpg","duration":"216小时19分钟"},{"pm":17,"uid":188976,"nickname":"冼萍","avatar":"http://imsyh.7seme.com/avatar/2018/0314/2018031410170055896.jpg","duration":"215小时21分钟"},{"pm":18,"uid":4364,"nickname":"徐爱兰","avatar":"http://imsyh.7seme.com/avatar/2017/1013/2017101318064074693.jpg","duration":"201小时7分钟"},{"pm":19,"uid":69450,"nickname":"许辉_666书友会","avatar":"http://imsyh.7seme.com/avatar/2018/0810/2018081016215825631.jpg","duration":"194小时36分钟"},{"pm":20,"uid":327641,"nickname":"327641","avatar":"http://imsyh.7seme.com/avatar/2018/0814/2018081406230632066.jpg","duration":"184小时45分钟"},{"pm":21,"uid":410692,"nickname":"玲 ","avatar":"http://imsyh.7seme.com/avatar/2018/0403/2018040318054661510.jpg","duration":"180小时25分钟"},{"pm":22,"uid":217309,"nickname":"好易住移动别墅、箱房。租售。舒昌波","avatar":"http://imsyh.7seme.com/avatar/2018/0104/2018010419542725485.jpg","duration":"179小时23分钟"},{"pm":23,"uid":149138,"nickname":"AA分享快乐","avatar":"http://imsyh.7seme.com/avatar/2018/0412/2018041218544376013.jpg","duration":"174小时46分钟"},{"pm":24,"uid":142889,"nickname":"新思想!新动力!","avatar":"http://imsyh.7seme.com/avatar/2018/0521/2018052108261031833.jpg","duration":"172小时37分钟"},{"pm":25,"uid":688714,"nickname":"读书改变命运","avatar":"http://imsyh.7seme.com/avatar/2018/0813/2018081314425427323.jpg","duration":"170小时58分钟"},{"pm":26,"uid":324704,"nickname":"榛子哥-牛宝家13731463149","avatar":"http://imsyh.7seme.com/avatar/2018/0503/2018050312452150785.jpg","duration":"168小时5分钟"},{"pm":27,"uid":193144,"nickname":"刘荣18656817105","avatar":"http://imsyh.7seme.com/avatar/2018/0422/2018042222470983492.jpg","duration":"166小时49分钟"},{"pm":28,"uid":81738,"nickname":"代绍武","avatar":"http://imsyh.7seme.com/avatar/2018/0606/2018060615473167393.jpg","duration":"159小时39分钟"},{"pm":29,"uid":140510,"nickname":"自强不息","avatar":"http://imsyh.7seme.com/avatar/2018/0601/2018060120234328362.jpg","duration":"158小时35分钟"},{"pm":30,"uid":97042,"nickname":"明天会更好","avatar":"http://imsyh.7seme.com/avatar/2018/0810/2018081013521920356.jpg","duration":"156小时46分钟"}],"mine":{"uid":2238493,"nickname":"Melee","avatar":"http://imsyh.7seme.com/avatar/2018/0702/2018070217294585122.jpg","pm":34082,"duration":"0小时59分钟"}}
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
         * list : [{"pm":1,"uid":537080,"nickname":"干承龙13329948703家电专家","avatar":"http://imsyh.7seme.com/avatar/2018/0804/2018080411311113925.jpg","duration":"750小时40分钟"},{"pm":2,"uid":242700,"nickname":"刘丽锋","avatar":"http://imsyh.7seme.com/avatar/2018/0731/2018073119231964987.jpg","duration":"534小时40分钟"},{"pm":3,"uid":491523,"nickname":"春光懒困倚微风","avatar":"http://imsyh.7seme.com/avatar/2018/0503/2018050316230852486.jpg","duration":"527小时23分钟"},{"pm":4,"uid":190523,"nickname":"大红灯笼婚纱摄影","avatar":"http://imsyh.7seme.com/avatar/2018/0227/2018022723153756273.jpg","duration":"353小时58分钟"},{"pm":5,"uid":236181,"nickname":"王琳","avatar":"http://imsyh.7seme.com/avatar/2018/0806/2018080613343483160.jpg","duration":"332小时51分钟"},{"pm":6,"uid":163749,"nickname":"孙丽华13843024933","avatar":"http://imsyh.7seme.com/avatar/2018/0708/2018070806501912332.jpg","duration":"325小时53分钟"},{"pm":7,"uid":161581,"nickname":"连美桂","avatar":"http://imsyh.7seme.com/avatar/2018/0804/2018080411504439157.jpg","duration":"299小时9分钟"},{"pm":8,"uid":304981,"nickname":"304981","avatar":"http://syh.7seme.com/images/noface.gif","duration":"296小时28分钟"},{"pm":9,"uid":48817,"nickname":"角斗士","avatar":"http://imsyh.7seme.com/avatar/2017/1227/2017122721530525410.jpg","duration":"283小时32分钟"},{"pm":10,"uid":2922,"nickname":"楊健","avatar":"http://imsyh.7seme.com/avatar/2018/0718/2018071806412522618.jpg","duration":"282小时31分钟"},{"pm":11,"uid":438281,"nickname":"（奎尚朱）和人顺天","avatar":"http://imsyh.7seme.com/avatar/2018/0729/2018072919030227116.jpg","duration":"281小时54分钟"},{"pm":12,"uid":220360,"nickname":"梦想413","avatar":"http://imsyh.7seme.com/avatar/2018/0731/2018073107003684220.jpg","duration":"240小时41分钟"},{"pm":13,"uid":228745,"nickname":"向往","avatar":"http://imsyh.7seme.com/avatar/2018/0705/2018070510283490573.jpg","duration":"240小时31分钟"},{"pm":14,"uid":971362,"nickname":"一缕阳光","avatar":"http://imsyh.7seme.com/avatar/2018/0617/2018061718453287784.jpg","duration":"238小时40分钟"},{"pm":15,"uid":241503,"nickname":"赵志龙","avatar":"http://imsyh.7seme.com/avatar/2018/0623/2018062321440693098.jpg","duration":"233小时1分钟"},{"pm":16,"uid":197594,"nickname":"康婷小阳光陈英17347904959","avatar":"http://imsyh.7seme.com/avatar/2018/0106/2018010611022811148.jpg","duration":"216小时19分钟"},{"pm":17,"uid":188976,"nickname":"冼萍","avatar":"http://imsyh.7seme.com/avatar/2018/0314/2018031410170055896.jpg","duration":"215小时21分钟"},{"pm":18,"uid":4364,"nickname":"徐爱兰","avatar":"http://imsyh.7seme.com/avatar/2017/1013/2017101318064074693.jpg","duration":"201小时7分钟"},{"pm":19,"uid":69450,"nickname":"许辉_666书友会","avatar":"http://imsyh.7seme.com/avatar/2018/0810/2018081016215825631.jpg","duration":"194小时36分钟"},{"pm":20,"uid":327641,"nickname":"327641","avatar":"http://imsyh.7seme.com/avatar/2018/0814/2018081406230632066.jpg","duration":"184小时45分钟"},{"pm":21,"uid":410692,"nickname":"玲 ","avatar":"http://imsyh.7seme.com/avatar/2018/0403/2018040318054661510.jpg","duration":"180小时25分钟"},{"pm":22,"uid":217309,"nickname":"好易住移动别墅、箱房。租售。舒昌波","avatar":"http://imsyh.7seme.com/avatar/2018/0104/2018010419542725485.jpg","duration":"179小时23分钟"},{"pm":23,"uid":149138,"nickname":"AA分享快乐","avatar":"http://imsyh.7seme.com/avatar/2018/0412/2018041218544376013.jpg","duration":"174小时46分钟"},{"pm":24,"uid":142889,"nickname":"新思想!新动力!","avatar":"http://imsyh.7seme.com/avatar/2018/0521/2018052108261031833.jpg","duration":"172小时37分钟"},{"pm":25,"uid":688714,"nickname":"读书改变命运","avatar":"http://imsyh.7seme.com/avatar/2018/0813/2018081314425427323.jpg","duration":"170小时58分钟"},{"pm":26,"uid":324704,"nickname":"榛子哥-牛宝家13731463149","avatar":"http://imsyh.7seme.com/avatar/2018/0503/2018050312452150785.jpg","duration":"168小时5分钟"},{"pm":27,"uid":193144,"nickname":"刘荣18656817105","avatar":"http://imsyh.7seme.com/avatar/2018/0422/2018042222470983492.jpg","duration":"166小时49分钟"},{"pm":28,"uid":81738,"nickname":"代绍武","avatar":"http://imsyh.7seme.com/avatar/2018/0606/2018060615473167393.jpg","duration":"159小时39分钟"},{"pm":29,"uid":140510,"nickname":"自强不息","avatar":"http://imsyh.7seme.com/avatar/2018/0601/2018060120234328362.jpg","duration":"158小时35分钟"},{"pm":30,"uid":97042,"nickname":"明天会更好","avatar":"http://imsyh.7seme.com/avatar/2018/0810/2018081013521920356.jpg","duration":"156小时46分钟"}]
         * mine : {"uid":2238493,"nickname":"Melee","avatar":"http://imsyh.7seme.com/avatar/2018/0702/2018070217294585122.jpg","pm":34082,"duration":"0小时59分钟"}
         */

        private ListBean mine;
        private List<ListBean> list;

        public ListBean getMine() {
            return mine;
        }

        public void setMine(ListBean mine) {
            this.mine = mine;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class MineBean {
            /**
             * uid : 2238493
             * nickname : Melee
             * avatar : http://imsyh.7seme.com/avatar/2018/0702/2018070217294585122.jpg
             * pm : 34082
             * duration : 0小时59分钟
             */

            private int uid;
            private String nickname;
            private String avatar;
            private int pm;
            private String duration;

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
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

            public int getPm() {
                return pm;
            }

            public void setPm(int pm) {
                this.pm = pm;
            }

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }
        }

        public static class ListBean {
            /**
             * pm : 1
             * uid : 537080
             * nickname : 干承龙13329948703家电专家
             * avatar : http://imsyh.7seme.com/avatar/2018/0804/2018080411311113925.jpg
             * duration : 750小时40分钟
             */

            private int pm;
            private int uid;
            private String nickname;
            private String avatar;
            private String duration;

            public int getPm() {
                return pm;
            }

            public void setPm(int pm) {
                this.pm = pm;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
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

            public String getDuration() {
                return duration;
            }

            public void setDuration(String duration) {
                this.duration = duration;
            }
        }
    }
}
