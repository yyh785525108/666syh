package com.tchy.syh.book.book_detail;

import android.app.Activity;
import android.content.Context;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tchy.syh.app.AppApplication;
import com.tchy.syh.book.book_detail.entity.BookDetailResp;
import com.tchy.syh.common.CommonBottomBarVM;
import com.tchy.syh.common.entity.CommonDataListResp;
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
import java.util.concurrent.TimeUnit;

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

public class BookDetailVm extends BaseViewModel {
    //    public BookListResp.DataBean.ListBean entity;
    public static final int BOOK_DETAIL_UPDATE = 0x000001;
    public String id;
    public String name;
    public ObservableField<CommonBottomBarVM> bottomVM = new ObservableField<>();
    public ObservableBoolean isshareShow = new ObservableBoolean();

    public BookDetailVm(Context context) {
        super(context);
        this.id = "";
    }

    public BookDetailVm(Context context, String id, String name) {
        super(context);
        this.id = id;
        this.name = name;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getDetail();
        Messenger.getDefault().register(context, "refresh", String.class, new BindingConsumer<String>() {
            @Override
            public void call(String s) {
                getDetail();
            }
        });

    }

    public void share(int type) {
        if(StringUtils.isEmpty(SPUtils.getInstance().getString("token"))){
            startActivity(LoginActivity.class);
            return ;
        }
        Glide.with(context).asBitmap().load(dataBean.get().getPic()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {


                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = dataBean.get().getUrl();
                WXMediaMessage msg = new WXMediaMessage(webpage);  //这个对象是用来包裹发送信息的对象
                msg.title = dataBean.get().getTitle();
                msg.description = dataBean.get().getDescription();
                Bitmap thumbBitmap = Bitmap.createScaledBitmap(resource, 150, 150, true);
//                resource.recycle();
                msg.thumbData = ConvertUtils.bitmap2Bytes(thumbBitmap, Bitmap.CompressFormat.JPEG);

                SendMessageToWX.Req req = new SendMessageToWX.Req();    //创建一个请求对象
                req.message = msg;  //把msg放入请求对象中
                if (type == 0) {
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                } else {
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                }
                req.transaction = "webpage" + id;  //这个tag要唯一,用于在回调中分辨是哪个分享请求
                boolean b = AppApplication.mWxApi.sendReq(req);   //如果调用成功微信,会返回true
                wancheng("share");


            }
        });

    }
    public void wancheng(String type){
        HashMap<String, Object> params = new HashMap();
        params.put("m", "wancheng");
        params.put("type",type);
        params.put("access_token", SPUtils.getInstance().getString("token"));
        Observable<CommonDataListResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .task(FormdataRequestUtil.getFormDataRequestParams(params))
                .delay(500, TimeUnit.MILLISECONDS)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {});
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

    public ObservableField<BookDetailResp.DataBean> dataBean = new ObservableField<>();
    public ObservableList pagerList = new ObservableArrayList();
    public ObservableInt pagerIndex = new ObservableInt();
    public BindingCommand typeChanged = new BindingCommand(new BindingConsumer<Integer>() {
        @Override
        public void call(Integer o) {
            pagerIndex.set(o);
        }
    });
    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });

    public void getDetail() {
        HashMap<String, Object> params = new HashMap();
        params.put("id", id);
        params.put("access_token", SPUtils.getInstance().getString("token"));
        Observable<BookDetailResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .learn_info(FormdataRequestUtil.getFormDataRequestParams(params))
                .delay(500, TimeUnit.MILLISECONDS)
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
//            navList.add(new CommonViewModel(context, "","-1",1,"全部"));
            res.getData().setId(id);
            dataBean.set(res.getData());
            bottomVM.set(new CommonBottomBarVM(context, res.getData(), false));
            Messenger.getDefault().send(res.getData(), BOOK_DETAIL_UPDATE);

//            SearchResultsResp.DataBean bean = new SearchResultsResp.DataBean();
//            bean.setName("splash_activity");
//            bean.setLink("http://www.baidu.com");
//            bean.setImg("https://pic.chinaz.com/2018/0416/18041617310389080.jpg");

        }, e -> {
            e.printStackTrace();
        });

    }


}
