package com.tchy.syh.listen.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.ObservableField;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tchy.syh.app.AppApplication;
import com.tchy.syh.cons.HttpCons;
import com.tchy.syh.listen.ApiService;
import com.tchy.syh.listen.base.AdaptationViewModel;
import com.tchy.syh.listen.comment.AudioCommentActivity;
import com.tchy.syh.listen.database.MusicDatabase;
import com.tchy.syh.listen.entity.AudioDetailEntity;
import com.tchy.syh.listen.entity.CollectResult;
import com.tchy.syh.listen.entity.ResponseWrapper;
import com.tchy.syh.userAccount.userLogin.LoginActivity;
import com.tchy.syh.utils.FormUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.TimeFormatUtil;
import com.tchy.syh.utils.ToastUtil;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.util.HashMap;
import java.util.Map;

import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

import me.goldze.mvvmhabit.utils.ConvertUtils;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.StringUtils;


public class AudioDetailViewModel extends AdaptationViewModel {

    public String collectNum;
    public String commentNum;
    public String playNum;
    public AudioDetailEntity detailEntity;
    public String updateInfo;
    public String priceInfo;
    public String vipPriceInfo;
    public ObservableField<CollectResult> collectResultObservable = new ObservableField<>();

    public AudioDetailViewModel(Context context, AudioDetailEntity entity) {
        super(context);
        this.detailEntity = entity;
        this.collectNum = entity.getCollect_num() + "";
        this.commentNum = entity.getComment_num()+"";
        this.playNum=entity.getPlay_num()+"";

        updateInfo = "更新：" + TimeFormatUtil.formatLatest(detailEntity.getUpdate_time()) + "/第" + entity.getUpdate_album() + "期";
        double price = detailEntity.getPrice();
        double vipprice = detailEntity.getVipprice();
        priceInfo = "价格:" + (price == 0 ? "免费" : price) ;
        vipPriceInfo="VIP:" + (vipprice == 0 ? "免费" : vipprice);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    /**
     * 点击评论
     */
    public BindingCommand onClickComment = new BindingCommand(() -> {
        Intent intent=new Intent(context,AudioCommentActivity.class);

        intent.putExtra("title", detailEntity.getTitle());
        intent.putExtra("cover", detailEntity.getPic());
        intent.putExtra("sub_title", detailEntity.getKeyword());
        intent.putExtra("book_id",detailEntity.getId());
        intent.putExtra("audio_id","");

        context.startActivity(intent);

    });
    public BindingCommand onClickCollect = new BindingCommand(() -> {
        Map<String, String> para = new HashMap<>();
        para.put("appkey", HttpCons.APP_KEY);
        para.put("access_token", SPUtils.getInstance().getString("token"));
        para.put("id", detailEntity.getId());
        para.put("type", "learn");
        String sig = FormUtil.genSig(para);
        para.put("sig", sig);

        RetrofitClient.getInstance().create(ApiService.class)
                .member_collect(para)
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .subscribe(o -> {

                    ResponseWrapper<CollectResult> responseWrapper = (ResponseWrapper<CollectResult>) o;
                    ToastUtil.toastBottom(responseWrapper.getInfo());
                    if (responseWrapper.isSuccess()) {
                        CollectResult data = responseWrapper.getData();
                        collectResultObservable.set(data);
                        updateList(data);
                    }
                }, throwable -> ToastUtil.toastBottom(((Throwable) throwable).getMessage()));
    });

    /**
     * 更新列表中的收藏状态
     *
     * @param data
     */
    private void updateList(CollectResult data) {
        MusicDatabase.getInstance(context).audioHistoryDao().updateCollect((int) data.getIs_add());

    }
    public void share(int type) {
        if(StringUtils.isEmpty(SPUtils.getInstance().getString("token"))){
            startActivity(LoginActivity.class);
            return ;
        }
        Glide.with(context).asBitmap().load(detailEntity.getPic()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                WXMusicObject muObj = new WXMusicObject();
//                muObj.musicUrl  = detailEntity.getDefault_mp3();

                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl =detailEntity.getUrl();

                WXMediaMessage msg = new WXMediaMessage(webpage);  //这个对象是用来包裹发送信息的对象
                msg.title =detailEntity.getTitle();
//                msg.mediaObject = muObj;
                msg.description = detailEntity.getDescription();
                Bitmap thumbBitmap = Bitmap.createScaledBitmap(resource, 150, 150, true);
                msg.thumbData = ConvertUtils.bitmap2Bytes(thumbBitmap, Bitmap.CompressFormat.JPEG);

                SendMessageToWX.Req req = new SendMessageToWX.Req();    //创建一个请求对象
                req.message = msg;  //把msg放入请求对象中
                if (type == 0) {
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                } else {
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                }
                req.transaction = "webpage" + detailEntity.getId();  //这个tag要唯一,用于在回调中分辨是哪个分享请求
                boolean b = AppApplication.mWxApi.sendReq(req);   //如果调用成功微信,会返回true
            }
        });

    }

    public BindingCommand shareClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ShareAction mShareAction = new ShareAction((Activity) context).setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE).setShareboardclickCallback(new ShareBoardlistener() {
                @Override
                public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                    Log.d("sort", "onclick: " + share_media.getName());
                    switch (share_media) {
                        case WEIXIN:
                            share(0);
                            break;
                        case WEIXIN_CIRCLE:
                            share(1);
                            break;
                    }
                }
            });
            ShareBoardConfig config = new ShareBoardConfig();
            config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_BOTTOM);//分享面板在中间出现
            config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR);// 圆角背景
            config.setCancelButtonText("取消");//取消按钮
            mShareAction.open(config);//打开分享面板
        }
    });

}
