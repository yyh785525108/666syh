package com.tchy.syh.read.detail;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tchy.syh.app.AppApplication;
import com.tchy.syh.comment.entity.CommentLoveResp;
import com.tchy.syh.read.ApiService;
import com.tchy.syh.read.entity.ReadDetailResp;
import com.tchy.syh.userAccount.userLogin.LoginActivity;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
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

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.utils.ConvertUtils;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.StringUtils;

public class ReadDetailVm extends BaseViewModel {
    public String id;
    public ObservableInt collectNum = new ObservableInt();
    public ObservableInt isCollect = new ObservableInt();
    public ObservableInt upNum = new ObservableInt();
    public ObservableInt isUp = new ObservableInt();

    public ObservableField<ReadDetailResp.DataBean> bean = new ObservableField<>();

    public ReadDetailVm(Context context, String id) {
        super(context);
        this.id = id;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Messenger.getDefault().register(context, "refresh", String.class, new BindingConsumer<String>() {
            @Override
            public void call(String s) {
                getDetail();
            }
        });
        getDetail();
    }


    public void share(int type) {
        if(StringUtils.isEmpty(SPUtils.getInstance().getString("token"))){
            startActivity(LoginActivity.class);
            return ;
        }
        Glide.with(context).asBitmap().load(bean.get().getThumb()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {


                WXWebpageObject webpage=new WXWebpageObject();
                webpage.webpageUrl=bean.get().getUrl();
                WXMediaMessage msg = new WXMediaMessage(webpage);  //这个对象是用来包裹发送信息的对象
                msg.title=bean.get().getTitle();
                msg.description=bean.get().getDescription();
                Bitmap thumbBitmap = Bitmap.createScaledBitmap(resource, 150, 150, true);
                msg.thumbData = ConvertUtils.bitmap2Bytes(thumbBitmap, Bitmap.CompressFormat.JPEG);

                SendMessageToWX.Req req = new SendMessageToWX.Req();    //创建一个请求对象
                req.message = msg;  //把msg放入请求对象中
                if (type == 0) {
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                } else {
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                }
                req.transaction = "webpage"+id;  //这个tag要唯一,用于在回调中分辨是哪个分享请求
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
    public BindingCommand backClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ((Activity) context).finish();
        }
    });
    public BindingCommand commentClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle = new Bundle();
            bundle.putString("id", bean.get().getId());
            bundle.putString("title", bean.get().getTitle());
            startContainerActivity(ReadDetailCommentFragment.class.getCanonicalName(), bundle);
        }
    });
    public BindingCommand upClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            addUp();
        }
    });
    public BindingCommand collectClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            addCollect();
        }
    });


    public void getDetail() {
        HashMap<String, Object> params = new HashMap();
        params.put("id", id);
        params.put("access_token", SPUtils.getInstance().getString("token"));
        Observable<ReadDetailResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .news_info(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(res -> {

            if (res.getStatus() != 1) {
                if (res.getStatus() == 10005 || res.getStatus() == 10002) {
                    ToastUtil.toastBottom("请先登录");
                    startActivity(LoginActivity.class);
                }
                return;
            }
            isCollect.set(res.getData().getIs_collect());
            collectNum.set(Integer.parseInt(res.getData().getCollect_num()));
            upNum.set(Integer.parseInt(res.getData().getLike_num()));
            isUp.set(res.getData().getIs_zan());

            bean.set(res.getData());


        }, e -> {
            e.printStackTrace();
        });

    }

    public void addCollect() {
        HashMap<String, Object> params = new HashMap();

        params.put("id", bean.get().getId());
        params.put("type", "news");
        params.put("access_token", SPUtils.getInstance().getString("token"));


        Observable<CommentLoveResp> observable = RetrofitClient.getInstance().create(com.tchy.syh.book.ApiService.class)
                .member_collect(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {

            if (res.getStatus() != 1) {
                ToastUtil.toastBottom(res.getInfo());
                return;

            }
            collectNum.set(res.getData().getNum());
            isCollect.set(res.getData().getIs_add());

//            upNum.set(bean.getLike_num());
//            isUp.set(bean.getIs_zan());

            ToastUtil.toastBottom(res.getData().getIs_add() == 1 ? "收藏成功" : "取消收藏");

        }, e -> {

            e.printStackTrace();
        });
    }

    public void addUp() {
        HashMap<String, Object> params = new HashMap();

        params.put("id", bean.get().getId());
        params.put("type", "news");
        params.put("access_token", SPUtils.getInstance().getString("token"));


        Observable<CommentLoveResp> observable = RetrofitClient.getInstance().create(com.tchy.syh.book.ApiService.class)
                .member_like(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {

            if (res.getStatus() != 1) {
                ToastUtil.toastBottom(res.getInfo());
                return;

            }
            upNum.set(res.getData().getNum());
            isUp.set(res.getData().getIs_add());
//            upNum.set(bean.getLike_num());
//            isUp.set(bean.getIs_zan());

            ToastUtil.toastBottom(res.getData().getIs_add() == 1 ? "点赞成功" : "取消点赞");

        }, e -> {

            e.printStackTrace();
        });
    }
}
