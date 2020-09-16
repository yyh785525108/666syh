package com.tchy.syh.my.entity;

import java.io.Serializable;
import java.util.List;

public class SpreadListResp implements Serializable {

    /**
     * status : 1
     * info : success
     * data : {"list":[{"pm":1,"uid":5538,"nickname":"读书改变命运","avatar":"http://imsyh.7seme.com/avatar/2018/0620/2018062010421251803.jpg","fans":52507},{"pm":2,"uid":10057,"nickname":"常德王 18520532888","avatar":"http://imsyh.7seme.com/avatar/2018/0730/2018073017401046262.jpg","fans":23006},{"pm":3,"uid":106260,"nickname":"陈夏天  17665493468","avatar":"http://imsyh.7seme.com/avatar/2018/0719/2018071915551866460.jpg","fans":20524},{"pm":4,"uid":9,"nickname":"晏亚洲","avatar":"http://imsyh.7seme.com/avatar/2018/0727/2018072709594676041.jpg","fans":14709},{"pm":5,"uid":141684,"nickname":"王向东","avatar":"http://imsyh.7seme.com/avatar/2018/0812/2018081212221112010.jpg","fans":12715},{"pm":6,"uid":479762,"nickname":"扬帆起航","avatar":"http://imsyh.7seme.com/avatar/2018/0810/2018081000001433138.jpg","fans":10531},{"pm":7,"uid":39983,"nickname":"666书友会 锁粉 导师～刘锦书","avatar":"http://imsyh.7seme.com/avatar/2018/0812/2018081221110870706.jpg","fans":10020},{"pm":8,"uid":135,"nickname":"郭柳言","avatar":"http://imsyh.7seme.com/avatar/2018/0723/2018072310282254241.jpg","fans":9379},{"pm":9,"uid":18,"nickname":"黎富贵","avatar":"http://imsyh.7seme.com/avatar/2018/0724/2018072411563288756.jpg","fans":7915},{"pm":10,"uid":6,"nickname":"柯昌荣","avatar":"http://imsyh.7seme.com/avatar/2018/0812/2018081219553039939.jpg","fans":7481},{"pm":11,"uid":7,"nickname":"陆晓广","avatar":"http://imsyh.7seme.com/avatar/2018/0812/2018081200133781655.jpg","fans":7434},{"pm":12,"uid":41225,"nickname":"互联网微营销导师*覃几华","avatar":"http://imsyh.7seme.com/avatar/2018/0501/2018050122191797721.jpg","fans":6601},{"pm":13,"uid":91988,"nickname":"满应余~感恩有你","avatar":"http://imsyh.7seme.com/avatar/2018/0616/2018061608075698170.jpg","fans":5568},{"pm":14,"uid":528483,"nickname":"闫寓菡爱喜悦和平","avatar":"http://imsyh.7seme.com/avatar/2018/0731/2018073119223055905.jpg","fans":5479},{"pm":15,"uid":217,"nickname":"朱大庆   666书友会","avatar":"http://imsyh.7seme.com/avatar/2018/0813/2018081323041587445.jpg","fans":5372},{"pm":16,"uid":7643,"nickname":"易凤蔚℡¹³\u2079 ²\u2074\u2079º \u2079\u2078\u2079\u2077","avatar":"http://imsyh.7seme.com/avatar/2018/0813/2018081320562194073.jpg","fans":5307},{"pm":17,"uid":6604,"nickname":"财神哥","avatar":"http://imsyh.7seme.com/avatar/2018/0811/2018081107004756663.jpg","fans":5241},{"pm":18,"uid":980757,"nickname":"黄运武","avatar":"http://imsyh.7seme.com/avatar/2018/0811/2018081109351841505.jpg","fans":5121},{"pm":19,"uid":72653,"nickname":"尧登伟18623018534","avatar":"http://imsyh.7seme.com/avatar/2018/0616/2018061620093273523.jpg","fans":5058},{"pm":20,"uid":663171,"nickname":"李志勇（18355969663）","avatar":"http://imsyh.7seme.com/avatar/2018/0810/2018081019184052506.jpg","fans":5034},{"pm":21,"uid":6146,"nickname":"张家二小姐","avatar":"http://imsyh.7seme.com/avatar/2018/0808/2018080821350928589.jpg","fans":4867},{"pm":22,"uid":1147101,"nickname":"广州海前盛～盛钱包激活返150-周总","avatar":"http://imsyh.7seme.com/avatar/2018/0722/2018072221141994616.jpg","fans":4765},{"pm":23,"uid":2,"nickname":"彭丽华","avatar":"http://imsyh.7seme.com/avatar/2018/0724/2018072414354731505.jpg","fans":4735},{"pm":24,"uid":254,"nickname":"黎鑫","avatar":"http://imsyh.7seme.com/avatar/2018/0803/2018080316360947991.jpg","fans":4728},{"pm":25,"uid":195181,"nickname":"赵星-13267192768","avatar":"http://imsyh.7seme.com/avatar/2018/0724/2018072419333437807.jpg","fans":4550},{"pm":26,"uid":207060,"nickname":"鲍艳丽","avatar":"http://imsyh.7seme.com/avatar/2018/0519/2018051918130113584.jpg","fans":4461},{"pm":27,"uid":75374,"nickname":"视商168互动直播","avatar":"http://imsyh.7seme.com/avatar/2018/0717/2018071722353884505.jpg","fans":4421},{"pm":28,"uid":353190,"nickname":"荆盼~666书友会","avatar":"http://imsyh.7seme.com/avatar/2018/0717/2018071719043118796.jpg","fans":4216},{"pm":29,"uid":971302,"nickname":"筱呤～13877733071","avatar":"http://imsyh.7seme.com/avatar/2018/0723/2018072316021147149.jpg","fans":4166},{"pm":30,"uid":8,"nickname":"黎鑫~666书友会","avatar":"http://imsyh.7seme.com/avatar/2018/0330/2018033015061328993.jpg","fans":3971}],"mine":{"uid":2238493,"nickname":"Melee","avatar":"http://imsyh.7seme.com/avatar/2018/0702/2018070217294585122.jpg","pm":0,"fans":0}}
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
         * list : [{"pm":1,"uid":5538,"nickname":"读书改变命运","avatar":"http://imsyh.7seme.com/avatar/2018/0620/2018062010421251803.jpg","fans":52507},{"pm":2,"uid":10057,"nickname":"常德王 18520532888","avatar":"http://imsyh.7seme.com/avatar/2018/0730/2018073017401046262.jpg","fans":23006},{"pm":3,"uid":106260,"nickname":"陈夏天  17665493468","avatar":"http://imsyh.7seme.com/avatar/2018/0719/2018071915551866460.jpg","fans":20524},{"pm":4,"uid":9,"nickname":"晏亚洲","avatar":"http://imsyh.7seme.com/avatar/2018/0727/2018072709594676041.jpg","fans":14709},{"pm":5,"uid":141684,"nickname":"王向东","avatar":"http://imsyh.7seme.com/avatar/2018/0812/2018081212221112010.jpg","fans":12715},{"pm":6,"uid":479762,"nickname":"扬帆起航","avatar":"http://imsyh.7seme.com/avatar/2018/0810/2018081000001433138.jpg","fans":10531},{"pm":7,"uid":39983,"nickname":"666书友会 锁粉 导师～刘锦书","avatar":"http://imsyh.7seme.com/avatar/2018/0812/2018081221110870706.jpg","fans":10020},{"pm":8,"uid":135,"nickname":"郭柳言","avatar":"http://imsyh.7seme.com/avatar/2018/0723/2018072310282254241.jpg","fans":9379},{"pm":9,"uid":18,"nickname":"黎富贵","avatar":"http://imsyh.7seme.com/avatar/2018/0724/2018072411563288756.jpg","fans":7915},{"pm":10,"uid":6,"nickname":"柯昌荣","avatar":"http://imsyh.7seme.com/avatar/2018/0812/2018081219553039939.jpg","fans":7481},{"pm":11,"uid":7,"nickname":"陆晓广","avatar":"http://imsyh.7seme.com/avatar/2018/0812/2018081200133781655.jpg","fans":7434},{"pm":12,"uid":41225,"nickname":"互联网微营销导师*覃几华","avatar":"http://imsyh.7seme.com/avatar/2018/0501/2018050122191797721.jpg","fans":6601},{"pm":13,"uid":91988,"nickname":"满应余~感恩有你","avatar":"http://imsyh.7seme.com/avatar/2018/0616/2018061608075698170.jpg","fans":5568},{"pm":14,"uid":528483,"nickname":"闫寓菡爱喜悦和平","avatar":"http://imsyh.7seme.com/avatar/2018/0731/2018073119223055905.jpg","fans":5479},{"pm":15,"uid":217,"nickname":"朱大庆   666书友会","avatar":"http://imsyh.7seme.com/avatar/2018/0813/2018081323041587445.jpg","fans":5372},{"pm":16,"uid":7643,"nickname":"易凤蔚℡¹³\u2079 ²\u2074\u2079º \u2079\u2078\u2079\u2077","avatar":"http://imsyh.7seme.com/avatar/2018/0813/2018081320562194073.jpg","fans":5307},{"pm":17,"uid":6604,"nickname":"财神哥","avatar":"http://imsyh.7seme.com/avatar/2018/0811/2018081107004756663.jpg","fans":5241},{"pm":18,"uid":980757,"nickname":"黄运武","avatar":"http://imsyh.7seme.com/avatar/2018/0811/2018081109351841505.jpg","fans":5121},{"pm":19,"uid":72653,"nickname":"尧登伟18623018534","avatar":"http://imsyh.7seme.com/avatar/2018/0616/2018061620093273523.jpg","fans":5058},{"pm":20,"uid":663171,"nickname":"李志勇（18355969663）","avatar":"http://imsyh.7seme.com/avatar/2018/0810/2018081019184052506.jpg","fans":5034},{"pm":21,"uid":6146,"nickname":"张家二小姐","avatar":"http://imsyh.7seme.com/avatar/2018/0808/2018080821350928589.jpg","fans":4867},{"pm":22,"uid":1147101,"nickname":"广州海前盛～盛钱包激活返150-周总","avatar":"http://imsyh.7seme.com/avatar/2018/0722/2018072221141994616.jpg","fans":4765},{"pm":23,"uid":2,"nickname":"彭丽华","avatar":"http://imsyh.7seme.com/avatar/2018/0724/2018072414354731505.jpg","fans":4735},{"pm":24,"uid":254,"nickname":"黎鑫","avatar":"http://imsyh.7seme.com/avatar/2018/0803/2018080316360947991.jpg","fans":4728},{"pm":25,"uid":195181,"nickname":"赵星-13267192768","avatar":"http://imsyh.7seme.com/avatar/2018/0724/2018072419333437807.jpg","fans":4550},{"pm":26,"uid":207060,"nickname":"鲍艳丽","avatar":"http://imsyh.7seme.com/avatar/2018/0519/2018051918130113584.jpg","fans":4461},{"pm":27,"uid":75374,"nickname":"视商168互动直播","avatar":"http://imsyh.7seme.com/avatar/2018/0717/2018071722353884505.jpg","fans":4421},{"pm":28,"uid":353190,"nickname":"荆盼~666书友会","avatar":"http://imsyh.7seme.com/avatar/2018/0717/2018071719043118796.jpg","fans":4216},{"pm":29,"uid":971302,"nickname":"筱呤～13877733071","avatar":"http://imsyh.7seme.com/avatar/2018/0723/2018072316021147149.jpg","fans":4166},{"pm":30,"uid":8,"nickname":"黎鑫~666书友会","avatar":"http://imsyh.7seme.com/avatar/2018/0330/2018033015061328993.jpg","fans":3971}]
         * mine : {"uid":2238493,"nickname":"Melee","avatar":"http://imsyh.7seme.com/avatar/2018/0702/2018070217294585122.jpg","pm":0,"fans":0}
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
             * pm : 0
             * fans : 0
             */

            private int uid;
            private String nickname;
            private String avatar;
            private int pm;
            private int fans;

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

            public int getFans() {
                return fans;
            }

            public void setFans(int fans) {
                this.fans = fans;
            }
        }

        public static class ListBean {
            /**
             * pm : 1
             * uid : 5538
             * nickname : 读书改变命运
             * avatar : http://imsyh.7seme.com/avatar/2018/0620/2018062010421251803.jpg
             * fans : 52507
             */

            private int pm;
            private int uid;
            private String nickname;
            private String avatar;
            private int fans;

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

            public int getFans() {
                return fans;
            }

            public void setFans(int fans) {
                this.fans = fans;
            }
        }
    }
}
