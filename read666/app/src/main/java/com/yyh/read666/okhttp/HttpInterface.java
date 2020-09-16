package com.yyh.read666.okhttp;

import com.yyh.read666.okhttp.OkHttpUtils;

public interface HttpInterface {


    /**
     * 微信授权回调
     * @param unionid 微信授权返回unionid
     * @param openid 	微信授权返回openid
     * @param nickname 	昵称
     * @param headimgurl 头像
     * @return
     */
    public void wx_callback(String unionid, String openid, String headimgurl, String nickname,  OkHttpUtils.MyNetCall myNetCall);
    /**
     * 登录接口
     * @param username 手机号
     * @param password 	密码
     * @return
     */
    public void login(String username, String password,  OkHttpUtils.MyNetCall myNetCall);


    /**
     * 获取书籍列表
     * @param cat_id 分类id
     * @param keyword 搜索词
     * @param orderby 排序。time:更新时间;hot:播放量;
     * @param page 页码,从0开始
     * @param myNetCall
     */
    public  void learn_items_book_get(String cat_id, String keyword,String orderby,String page, OkHttpUtils.MyNetCall myNetCall);

    /**
     * 获取视频列表
     * @param keyword 搜索词
     * @param orderby 排序。time:更新时间;hot:播放量;
     * @param page 页码,从0开始
     * @param myNetCall
     */
    public  void learn_items_video_get(String keyword, String orderby,String page, OkHttpUtils.MyNetCall myNetCall);

    /**
     * 获取音频列表
     * @param orderby 排序。time:更新时间;hot:播放量;
     * @param page 页码,从0开始
     * @param myNetCall
     */
    public void learn_items_audio_get( String orderby,String page, OkHttpUtils.MyNetCall myNetCall);

    /**
     * 学习分类
     * @param type 类型。默认book。book:解书分类；audio:音频专辑；news:文章分类
     * @param myNetCall
     */
    public void learn_items_cate_get(String type, OkHttpUtils.MyNetCall myNetCall);


    /**
     * 猜你喜欢
     * @param update 是否更新。用于刷新，1 or 0
     * @param myNetCall
     */
    public void learn_rec_book(String access_token,String update, OkHttpUtils.MyNetCall myNetCall);


    /**
     * 首页书籍接口
     * @param myNetCall
     */
    public void learn_home_book(String access_token, OkHttpUtils.MyNetCall myNetCall);

    /***
     * 书籍搜索
     * @param access_token
     * @param cat_id 	分类id
     * @param keyword 	搜索词
     * @param page 	页码。默认0
     * @param myNetCall
     */
    public void learn_search(String access_token,String cat_id, String keyword, String page,  OkHttpUtils.MyNetCall myNetCall);

    /***
     * 热门搜索
     * @param m 	固定值。hot_search
     * @param myNetCall
     */
    public void learn_search(String m, OkHttpUtils.MyNetCall myNetCall);

    /***
     * 搜索历史
     * @param access_token
     * @param m 	固定值。history
     * @param myNetCall
     */
    public void learn_search(String access_token,String m, OkHttpUtils.MyNetCall myNetCall);

    /***
     * 新课推荐
     * @param myNetCall
     */
    public void learn_new_audio(OkHttpUtils.MyNetCall myNetCall);
    /***
     * 今日热评，查询当天最热门的一条评论
     * @param myNetCall
     */
    public void learn_comment_index(OkHttpUtils.MyNetCall myNetCall);

    /***
     * 首页每日一听
     * @param access_token
     * @param myNetCall
     */
    public void learn_rec_audio(String access_token,OkHttpUtils.MyNetCall myNetCall);

    /**
     * 获取书籍列表，页面链接：http://wx.666syh.com/info/learn_info?id=275
     * @param access_token
     * @param id
     * @param myNetCall
     */
    public void learn_info(String access_token,String id,OkHttpUtils.MyNetCall myNetCall);

    /**
     * 固定值m=play_log 获取指定内容播放时间      	固定值m=video_info获取视频分集内容      	固定值m=auido_info获取音频专栏指定音频
     * @param access_token
     * @param id
     * @param m  固定值m=play_log   	固定值m=video_info  	固定值m=auido_info
     * @param myNetCall
     */
    public void learn_info(String access_token,String id,String m,OkHttpUtils.MyNetCall myNetCall);

    /**
     * 获取指定书籍相关书籍,	学习点赞，    会员点赞
     * @param access_token
     * @param id
     * @param m   	固定值related 获取指定书籍相关书籍             	固定值like  学习点赞   	固定值member_zan  会员点赞
     * @param myNetCall
     */
    public void learn(String access_token,String id,String m,OkHttpUtils.MyNetCall myNetCall);

    /**
     * 广告列表
     * @param access_token
     * @param id
     * @param num
     * @param myNetCall
     */
    public void ads_items_get(String access_token,String id,String num,OkHttpUtils.MyNetCall myNetCall);


    /**
     * 获取帐户信息(代理专区) http://wx.666syh.com/center
     * @param access_token
     * @param myNetCall
     */
    public void  user_member(String access_token,OkHttpUtils.MyNetCall myNetCall);

    /**
     * 获取帐户信息(代理专区)    获取积分记录  获取收支明细
     * @param access_token
     * @param m    固定值fensi 获取帐户信息(代理专区)      固定值moneylog 获取积分记录   固定值rmblog 获取收支明细    	固定值tixianlog 获取提现记录  固定值info 获取个人信息
     * @param myNetCall
     */
    public void  user_member(String access_token,String m,OkHttpUtils.MyNetCall myNetCall);


    /**
     * 个人信息设置
     * @param access_token
     * @param m
     * @param truename
     * @param email
     * @param birthday
     * @param profession
     * @param intro
     * @param myNetCall
     */
    public void  user_member(String access_token,String m,String truename,String email,String birthday,String profession,String intro,OkHttpUtils.MyNetCall myNetCall);

    /**
     * 获取修改密码
     * @param access_token
     * @param m  	固定值password
     * @param myNetCall
     */
    public void  user_member(String access_token,String m,String old_pwd,String pwd,String pwd2,OkHttpUtils.MyNetCall myNetCall);


    /**
     * 首页词条分类
     * @param myNetCall
     */
    public void  cate_index(OkHttpUtils.MyNetCall myNetCall);

    /**
     * 获取通知(活动)列表 http://wx.666syh.com/notice
     * @param access_token
     * @param myNetCall
     */
    public void notice_notice(String access_token,String page,OkHttpUtils.MyNetCall myNetCall);

    /**
     * 获取通知(活动)详情
     * @param access_token
     * @param m
     * @param id
     * @param myNetCall
     */
    public void notice_notice(String access_token,String m,String id,OkHttpUtils.MyNetCall myNetCall);

    /**
     * 获取短信验证码
     * @param access_token
     * @param m
     * @param mobile
     * @param myNetCall
     */
    public void user_member(String access_token,String m,String mobile,OkHttpUtils.MyNetCall myNetCall);

    /**
     * 	固定值bind_mobile:绑定手机号      固定值unbind:验证手机(解绑)    固定值change_mobile:更换手机
     * @param access_token
     * @param m
     * @param mobile
     * @param code
     * @param myNetCall
     */
    public void user_member(String access_token,String m,String mobile,String code,OkHttpUtils.MyNetCall myNetCall);


    /**
     * 生成授权码
     * @param access_token
     * @param myNetCall
     */
    public void user_auth_qrcode(String access_token,OkHttpUtils.MyNetCall myNetCall);

    /**
     * 意见反馈
     * @param access_token
     * @param content
     * @param contact
     * @param myNetCall
     */
    public void user_feedback(String access_token,String content,String contact,OkHttpUtils.MyNetCall myNetCall);

    /**
     * 词条分类 类型。0：人生智慧,1:财商智慧
     * @param access_token
     * @param type  类型。0：人生智慧,1:财商智慧
     * @param myNetCall
     */
    public void citiao_cate(String access_token,String type,OkHttpUtils.MyNetCall myNetCall);

    /**
     * 词条信息
     * @param access_token
     * @param id   	词条id
     * @param myNetCall
     */
    public void citiao_citiao(String access_token,String id,OkHttpUtils.MyNetCall myNetCall);

    /**
     * 固定值book  词条必读书籍,,,固定值news  词条相关文章(必读干货)   固定值topic  词条相关话题(相关讨论)，固定值ads  课程学习（广告）
     * @param access_token
     * @param m
     * @param id
     * @param page
     * @param myNetCall
     */
    public void citiao_citiao(String access_token,String m,String id,String page,OkHttpUtils.MyNetCall myNetCall);



    public void items_comment_get(String access_token,String id,String album_id,String uid,String page,OkHttpUtils.MyNetCall myNetCall);

    /**
     * 作业列表
     * @param access_token
     * @param id

     * @param page
     * @param myNetCall
     */
    public void learn_book_work(String access_token,String id,String time,String page,OkHttpUtils.MyNetCall myNetCall);

    /**
     * 音频指定推荐
     * @param access_token
     * @param id

     * @param myNetCall
     */
    public void learn_audio_tuijian(String access_token,String id,OkHttpUtils.MyNetCall myNetCall);

    /**
     * 书籍提问列表
     * @param access_token
     * @param id
     * @param page
     * @param myNetCall
     */
    public void learn_book_ask(String access_token,String id,String page,OkHttpUtils.MyNetCall myNetCall);

    /**
     * 书籍提问/回答
     * @param access_token
     * @param id
     * @param page
     * @param myNetCall
     */
    public void learn_book_ask(String access_token,String m,String id,String pid,String content,OkHttpUtils.MyNetCall myNetCall);

    public void learn_book_ask(String access_token,String m,String id,String pn,OkHttpUtils.MyNetCall myNetCall);
    /**
     * 书提问点赞
     * @param access_token
     * @param m  固定值zan
     * @param page
     * @param myNetCall
     */
    public void learn_book_askDianzan(String access_token,String m,String id,OkHttpUtils.MyNetCall myNetCall);

    public void learn_comment(String access_token,String m,String id,OkHttpUtils.MyNetCall myNetCall);

    public void learn_comment(String access_token,String m,String id,String album_id,String pid,String content,OkHttpUtils.MyNetCall myNetCall);


    public void learn_book_work(String access_token,String m,String id,OkHttpUtils.MyNetCall myNetCall);

    public void learn_book_work(String access_token,String m,String id,String title1,String title2,String title3,String note,String resolve,String is_sync,OkHttpUtils.MyNetCall myNetCall);


    public void learn_shujia(String access_token,String m,String id,OkHttpUtils.MyNetCall myNetCall);

    //今日达人
    public void rjj_member_top(String access_token,OkHttpUtils.MyNetCall myNetCall);

    //打赏接口
    public void learn_shang(String access_token,String id,String album_id,String money,String pay_type,OkHttpUtils.MyNetCall myNetCall);


    public void user_buyvip(String access_token,String user_rank,String pay_type,OkHttpUtils.MyNetCall myNetCall);

    public void learn_buy(String access_token,String id,String mobile,String pay_type,OkHttpUtils.MyNetCall myNetCall);

    public void rjj_blog(String access_token,String m,String content,String attach,OkHttpUtils.MyNetCall myNetCall);

    public void check_token(String access_token,String token, OkHttpUtils.MyNetCall myNetCall);


}
