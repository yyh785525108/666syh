package com.yyh.read666.okhttp;

import com.yyh.read666.configs.Configs;

import java.util.HashMap;
import java.util.Map;

public class HttpImplement implements HttpInterface {

    @Override
    public void wx_callback(String unionid, String openid, String headimgurl, String nickname,  OkHttpUtils.MyNetCall myNetCall){
        Map<String, String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("unionid",unionid);
        map.put("openid",openid);
        map.put("nickname",nickname);
        map.put("headimgurl",headimgurl);
        map.put("sig",SigUtil.getSig(map));


        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.wx_callback,map,myNetCall);
    }


    @Override
    public void login(String username, String password, OkHttpUtils.MyNetCall myNetCall) {
        Map<String, String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("username",username);
        map.put("password",password);
        map.put("sig",SigUtil.getSig(map));


        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.login,map,myNetCall);
    }

    @Override
    public void learn_items_book_get(String cat_id, String keyword, String orderby, String page, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        if (cat_id!=null){
            map.put("cat_id",cat_id);
        }
        if (keyword!=null){
            map.put("keyword",keyword);
        }
        if (orderby!=null){
            map.put("orderby",orderby);
        }
        if (page!=null){
            map.put("page",page);
        }
        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_items_book_get,map,myNetCall);
    }

    @Override
    public void learn_items_video_get(String keyword, String orderby, String page, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);

        if (keyword!=null){
            map.put("keyword",keyword);
        }
        if (orderby!=null){
            map.put("orderby",orderby);
        }
        if (page!=null){
            map.put("page",page);
        }
        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_items_video_get,map,myNetCall);
    }

    @Override
    public void learn_items_audio_get(String orderby, String page, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);

        if (orderby!=null){
            map.put("orderby",orderby);
        }
        if (page!=null){
            map.put("page",page);
        }
        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_items_audio_get,map,myNetCall);
    }

    @Override
    public void learn_items_cate_get(String type, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        if (type!=null){
            map.put("type",type);
        }
        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_items_cate_get,map,myNetCall);
    }

    @Override
    public void learn_rec_book(String access_token, String update, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);

        if (access_token!=null){
            map.put("access_token",access_token);
        }
        if (update!=null){
            map.put("update",update);
        }
        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_rec_book,map,myNetCall);
    }

    @Override
    public void learn_home_book(String access_token, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);

        if (access_token!=null){
            map.put("access_token",access_token);
        }

        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_home_book,map,myNetCall);
    }

    @Override
    public void learn_search(String access_token, String cat_id, String keyword, String page, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);

        if (access_token!=null){
            map.put("access_token",access_token);
        }
        if (cat_id!=null){
            map.put("cat_id",cat_id);
        }
        if (keyword!=null){
            map.put("keyword",keyword);
        }
        if (page!=null){
            map.put("page",page);
        }

        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_search,map,myNetCall);
    }

    @Override
    public void learn_search(String m, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);

        if (m!=null){
            map.put("m",m);
        }


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_search,map,myNetCall);
    }

    @Override
    public void learn_search(String access_token, String m, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        if (access_token!=null){
            map.put("access_token",access_token);
        }
        if (m!=null){
            map.put("m",m);
        }

        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_search,map,myNetCall);

    }

    @Override
    public void learn_new_audio(OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_new_audio,map,myNetCall);
    }

    @Override
    public void learn_comment_index(OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_comment_index,map,myNetCall);
    }

    @Override
    public void learn_rec_audio(String access_token, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        if (access_token!=null){
            map.put("access_token",access_token);
        }


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_rec_audio,map,myNetCall);
    }

    @Override
    public void learn_info(String access_token, String id, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        if (access_token!=null){
            map.put("access_token",access_token);
        }
        if (id!=null){
            map.put("id",id);
        }

        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_info,map,myNetCall);
    }

    @Override
    public void learn_info(String access_token, String id, String m, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        if (access_token!=null){
            map.put("access_token",access_token);
        }
        if (id!=null){
            map.put("id",id);
            map.put("m",m);

        }

        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_info,map,myNetCall);
    }

    @Override
    public void learn(String access_token, String id, String m, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        if (access_token!=null){
            map.put("access_token",access_token);
        }
        if (m!=null){
            map.put("m",m);
            if (m.equals("member_zan")){
                map.put("uid",id);
            }else {
                map.put("id",id);
            }
        }else {
            if (id!=null){
                map.put("id",id);
            }
        }


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn,map,myNetCall);
    }

    @Override
    public void ads_items_get(String access_token, String id, String num, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        if (access_token!=null){
            map.put("access_token",access_token);
        }
        if (id!=null){
            map.put("id",id);
        }
        if (num!=null){
            map.put("num",num);
        }

        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.ads_items_get,map,myNetCall);
    }

    @Override
    public void user_member(String access_token, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        if (access_token!=null){
            map.put("access_token",access_token);
        }


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.user_member,map,myNetCall);
    }

    @Override
    public void user_member(String access_token, String m, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        if (access_token!=null){
            map.put("access_token",access_token);
        }
        if (m!=null){
            map.put("m",m);
        }

        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.user_member,map,myNetCall);
    }

    @Override
    public void user_member(String access_token, String m, String truename, String email, String birthday, String profession, String intro, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        if (access_token!=null){
            map.put("access_token",access_token);
        }
        if (m!=null){
            map.put("m",m);
        }
        if (truename!=null){
            map.put("truename",truename);
        }
        if (email!=null){
            map.put("email",email);
        }
        if (birthday!=null){
            map.put("birthday",birthday);
        }
        if (profession!=null){
            map.put("profession",profession);
        }
        if (intro!=null){
            map.put("intro",intro);
        }

        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.user_member,map,myNetCall);
    }

    @Override
    public void user_member(String access_token, String m, String old_pwd, String pwd, String pwd2, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        if (access_token!=null){
            map.put("access_token",access_token);
        }
        if (m!=null){
            map.put("m",m);
        }
        if (old_pwd!=null){
            map.put("old_pwd",old_pwd);
        }
        if (pwd!=null){
            map.put("pwd",pwd);
        }
        if (pwd2!=null){
            map.put("pwd2",pwd2);
        }


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.user_member,map,myNetCall);
    }

    @Override
    public void cate_index(OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.cate_index,map,myNetCall);
    }

    @Override
    public void notice_notice(String access_token, String page,OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("page", page);


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.notice_notice,map,myNetCall);
    }

    @Override
    public void notice_notice(String access_token, String m, String id, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("m", m);
        map.put("id", id);


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.notice_notice,map,myNetCall);
    }

    @Override
    public void user_member(String access_token, String m, String mobile, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("m", m);
        map.put("mobile", mobile);


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.user_member,map,myNetCall);
    }

    @Override
    public void user_member(String access_token, String m, String mobile, String code, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("m", m);
        map.put("mobile", mobile);
        map.put("code", code);


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.user_member,map,myNetCall);
    }

    @Override
    public void user_auth_qrcode(String access_token, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.user_auth_qrcode,map,myNetCall);
    }

    @Override
    public void user_feedback(String access_token, String content, String contact, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("content", content);
        map.put("contact", contact);


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.user_feedback,map,myNetCall);
    }

    @Override
    public void citiao_cate(String access_token, String type, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("type", type);


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.citiao_cate,map,myNetCall);
    }

    @Override
    public void citiao_citiao(String access_token, String id, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("id", id);


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.citiao_citiao,map,myNetCall);
    }

    @Override
    public void citiao_citiao(String access_token, String m, String id, String page, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("m", m);
        map.put("id", id);
        map.put("page", page);


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.citiao_citiao,map,myNetCall);
    }

    @Override
    public void items_comment_get(String access_token, String id, String album_id, String uid, String page, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("id", id);
        map.put("album_id", album_id);
        map.put("uid", uid);
        map.put("page", page);


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.items_comment_get,map,myNetCall);
    }

    @Override
    public void learn_book_work(String access_token, String id, String time, String page, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("id", id);
        map.put("time", time);
        map.put("page", page);


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_book_work,map,myNetCall);
    }

    @Override
    public void learn_audio_tuijian(String access_token, String id, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("id", id);


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_audio_tuijian,map,myNetCall);
    }

    @Override
    public void learn_book_ask(String access_token, String id, String page, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("id", id);
        map.put("page", page);


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_book_ask,map,myNetCall);
    }

    @Override
    public void learn_book_ask(String access_token, String m, String id, String pid, String content, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("m", m);
        map.put("id", id);
        map.put("pid", pid);
        map.put("content", content);

        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_book_ask,map,myNetCall);
    }

    @Override
    public void learn_book_ask(String access_token, String m, String id, String pn, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("m", m);
        map.put("id", id);
        map.put("pn", pn);

        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_book_ask,map,myNetCall);
    }

    @Override
    public void learn_comment(String access_token, String m, String id, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("m", m);
        map.put("id", id);

        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_comment,map,myNetCall);
    }

    @Override
    public void learn_comment(String access_token, String m, String id, String album_id, String pid, String content, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("m", m);
        map.put("id", id);
        if (album_id!=null){
            map.put("album_id", album_id);
        }
        if (pid!=null){
            map.put("pid", pid);
        }
        if (content!=null){
            map.put("content", content);
        }


        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_comment,map,myNetCall);
    }

    @Override
    public void learn_book_work(String access_token, String m, String id, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("m", m);
        map.put("id", id);

        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_book_work,map,myNetCall);

    }

    @Override
    public void learn_book_work(String access_token, String m, String id, String title1, String title2, String title3, String note, String resolve, String is_sync, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("m", m);
        map.put("id", id);
        map.put("title1", title1);
        map.put("title2", title2);
        map.put("title3", title3);
        map.put("note", note);
        map.put("resolve", resolve);
        map.put("is_sync", is_sync);
        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_book_work,map,myNetCall);
    }

    @Override
    public void learn_shujia(String access_token, String m, String id, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("m", m);
        map.put("id", id);

        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_shujia,map,myNetCall);
    }

    @Override
    public void rjj_member_top(String access_token, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);

        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.rjj_member_top,map,myNetCall);
    }

    @Override
    public void learn_shang(String access_token, String id,String album_id,String money, String pay_type, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("id", id);
        if (album_id!=null){
            map.put("album_id", album_id);
        }
        map.put("money", money);
        map.put("pay_type", pay_type);

        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_shang,map,myNetCall);
    }

    @Override
    public void user_buyvip(String access_token,String user_rank,String pay_type,OkHttpUtils.MyNetCall myNetCall){
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        if (user_rank!=null){
            map.put("user_rank", user_rank);
        }
        if (pay_type!=null){
            map.put("pay_type", pay_type);
        }
        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.user_buyvip,map,myNetCall);
    }

    @Override
    public void learn_buy(String access_token, String id, String mobile, String pay_type, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("id", id);
        if (mobile!=null){
            map.put("mobile", mobile);
        }
        map.put("pay_type", pay_type);

        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_buy,map,myNetCall);
    }

    @Override
    public void rjj_blog(String access_token, String m, String content, String attach, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("m", m);
        map.put("content", content);
        map.put("attach", attach);
        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.rjj_blog,map,myNetCall);
    }

    @Override
    public void check_token(String access_token, String token, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("token", token);
        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.check_token,map,myNetCall);
    }

    @Override
    public void learn_book_askDianzan(String access_token, String m, String id, OkHttpUtils.MyNetCall myNetCall) {
        Map<String,String> map=new HashMap<>();
        map.put("appkey", Configs.APPKEY);
        map.put("access_token", access_token);
        map.put("m", m);
        map.put("id", id);

        map.put("sig", SigUtil.getSig(map));
        OkHttpUtils.getInstance().postDataAsyn(UrlConstant.learn_book_ask,map,myNetCall);
    }



}
