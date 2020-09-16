package com.tchy.syh.listen.play;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import androidx.databinding.ObservableLong;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lzx.musiclibrary.aidl.model.AudioInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.lzx.musiclibrary.manager.TimerTaskManager;
import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.app.AppApplication;
import com.tchy.syh.comment.CommentListItemVm;
import com.tchy.syh.comment.entity.CommentResp;
import com.tchy.syh.cons.HttpCons;
import com.tchy.syh.cons.WeixinCons;
import com.tchy.syh.listen.ApiService;
import com.tchy.syh.listen.PlayerEvent;
import com.tchy.syh.listen.base.AdaptationViewModel;


import com.tchy.syh.listen.database.MusicDatabase;
import com.tchy.syh.listen.entity.AudioDetailEntity;
import com.tchy.syh.listen.entity.CollectEvent;
import com.tchy.syh.listen.entity.CollectResult;
import com.tchy.syh.listen.entity.PayInfo;
import com.tchy.syh.listen.entity.ResponseWrapper;
import com.tchy.syh.listen.view.PayDialogWithPhone;
import com.tchy.syh.userAccount.userLogin.LoginActivity;
import com.tchy.syh.utils.FormUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.utils.ConvertUtils;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.StringUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class AudioPlayViewModel extends AdaptationViewModel implements PayDialogWithPhone.OnConfirmListener {
    public AudioDetailEntity entity;
    private MusicManager mMusicManager;
    private TimerTaskManager mTimerTaskManager;
    ObservableField<AudioInfo> mObservableSongInfo = new ObservableField<>();
    ObservableLong observableProgress = new ObservableLong(0);
    ObservableLong observableProgressBuffer = new ObservableLong(0);
    ObservableBoolean collectState = new ObservableBoolean(false);
    ObservableBoolean zanState = new ObservableBoolean(false);

    ObservableInt playState = new ObservableInt(-1);
    private Disposable mSubscription;
    //点击列表回调
    ObservableBoolean onClickList = new ObservableBoolean(false);
    private PayDialogWithPhone payDialog;


    public AudioPlayViewModel(Fragment fragment, AudioDetailEntity entity) {
        super(fragment.getContext());
        this.entity = entity;
        this.fragment = fragment;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicManager = MusicManager.get();
        collectState.set(!collectState.get());
        zanState.set(!zanState.get());
        mObservableSongInfo.set(mMusicManager.getCurrPlayingMusic());
        observableProgress.set(MusicManager.get().getProgress());
        mTimerTaskManager = new TimerTaskManager();
        mTimerTaskManager.setUpdateProgressTask(() -> updateProgress());
        mTimerTaskManager.scheduleSeekBarUpdate();

        new Handler().postDelayed(() -> {
            getConmments(false);
        }, 600);
    }

    public void share(int type) {
        if(StringUtils.isEmpty(SPUtils.getInstance().getString("token"))){
            startActivity(LoginActivity.class);
            return ;
        }
        Glide.with(context).asBitmap().load(entity.getPic()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                WXMusicObject muObj = new WXMusicObject();
                muObj.musicDataUrl = MusicManager.get().getCurrPlayingMusic().getDownloadUrl();
                muObj.musicDataUrl = MusicManager.get().getCurrPlayingMusic().getSongUrl();
                muObj.musicUrl=entity.getUrl();
                WXWebpageObject webpage = new WXWebpageObject();

                webpage.webpageUrl = entity.getUrl();
                Log.d("sort", webpage.webpageUrl);
                WXMediaMessage msg = new WXMediaMessage(webpage);  //这个对象是用来包裹发送信息的对象
                msg.title = entity.getTitle();

                msg.mediaObject = muObj;
                msg.description = entity.getDescription();
                Bitmap thumbBitmap = Bitmap.createScaledBitmap(resource, 150, 150, true);

                msg.thumbData = ConvertUtils.bitmap2Bytes(thumbBitmap, Bitmap.CompressFormat.JPEG);

                SendMessageToWX.Req req = new SendMessageToWX.Req();    //创建一个请求对象
                req.message = msg;  //把msg放入请求对象中
                if (type == 0) {
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                } else {
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                }
                req.transaction = "webpage" + entity.getId();  //这个tag要唯一,用于在回调中分辨是哪个分享请求
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

    private void updateProgress() {
        long progress = MusicManager.get().getProgress();
        observableProgress.set(progress);
        long bufferProgress = MusicManager.get().getBufferedPosition();
        observableProgressBuffer.set(bufferProgress);
    }

    /**
     * 上一首
     */
    public BindingCommand onPreClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (mMusicManager.hasPre()) {
                String type = mMusicManager.getPreMusic().getType();
                if (!"none".equals(type)) {
                    showPayPop(Double.parseDouble(type));
                } else {
                    mMusicManager.playPre();
                }

            } else {
                ToastUtil.toastBottom("没有上一集");
            }
        }
    });
    /**
     * 快进15秒
     */
    public BindingCommand onFastForword = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            mMusicManager.seekTo((int) (mMusicManager.getProgress() + 15 * 1000));
        }
    });


    public BindingCommand onCloseClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ((Activity) context).finish();
        }
    });


    /**
     * 暂停/播放
     */
    public BindingCommand onPlayClick = new BindingCommand(() -> {

        if (MusicManager.isPlaying()) {
            MusicManager.get().pauseMusic();
        } else {
            MusicManager.get().resumeMusic();
            if (mMusicManager.getProgress() >= mMusicManager.getCurrPlayingMusic().getDuration()) {
                mMusicManager.seekTo(0);
            }
        }
    });


    /**
     * 下一首
     */
    public BindingCommand onNextClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (mMusicManager.hasNext()) {
                String type = mMusicManager.getNextMusic().getType();
                if (!"none".equals(type)) {
                    showPayPop(Double.parseDouble(type));
                } else {
                    mMusicManager.playNext();
                }
            } else {
                ToastUtil.toastBottom("没有下一集");
            }
        }
    });
    /**
     * 显示列表
     */
    public BindingCommand onListClick = new BindingCommand(() -> onClickList.set(!onClickList.get()));


    @Override
    public void registerRxBus() {
        super.registerRxBus();
        mSubscription = RxBus.getDefault().toObservable(PlayerEvent.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(playerEvent -> onPlayEvent(playerEvent));
        RxSubscriptions.add(mSubscription);
    }

    private void onPlayEvent(PlayerEvent playerEvent) {
        playState.set(playerEvent.EventType);
        switch (playerEvent.EventType) {
            case MusicManager.MSG_MUSIC_CHANGE:
                mObservableSongInfo.set(playerEvent.audioInfo);
                break;
            default:
                break;
        }
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(mSubscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        entity = null;
        mTimerTaskManager.onRemoveUpdateProgressTask();

    }

    public BindingCommand onClickCollect = new BindingCommand(() -> {
        Map<String, String> para = new HashMap<>();
        para.put("appkey", HttpCons.APP_KEY);
        para.put("access_token", SPUtils.getInstance().getString("token"));
        para.put("id", mMusicManager.getCurrPlayingMusic().getArtistId());
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
                        updateListCollect(data);
                    }
                }, throwable -> ToastUtil.toastBottom(((Throwable) throwable).getMessage()));
    });


    public BindingCommand onClickZan = new BindingCommand(() -> {
        Map<String, String> para = new HashMap<>();
        para.put("appkey", HttpCons.APP_KEY);
        para.put("access_token", SPUtils.getInstance().getString("token"));
        para.put("id", mMusicManager.getCurrPlayingMusic().getArtistId());
        para.put("type", "learn");
        String sig = FormUtil.genSig(para);
        para.put("sig", sig);
        RetrofitClient.getInstance().create(ApiService.class)
                .member_like(para)
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .subscribe(o -> {

                    ResponseWrapper<CollectResult> responseWrapper = (ResponseWrapper<CollectResult>) o;
                    ToastUtil.toastBottom(responseWrapper.getInfo());
                    if (responseWrapper.isSuccess()) {
                        CollectResult data = responseWrapper.getData();
                        updateListZan(data);
                    }
                }, throwable -> ToastUtil.toastBottom(((Throwable) throwable).getMessage()));

    });

    /**
     * 更新列表中的收藏状态
     *
     * @param data
     */
    private void updateListCollect(CollectResult data) {
        MusicDatabase.getInstance(context).audioHistoryDao().updateCollect((int) data.getIs_add());
        collectState.set(!collectState.get());
        RxBus.getDefault().post(new CollectEvent((int) data.getIs_add(), (int) data.getNum()));
    }

    /**
     * 更新列表中的点赞状态
     *
     * @param data
     */
    private void updateListZan(CollectResult data) {
        MusicDatabase.getInstance(context).audioHistoryDao().updateZan((int) data.getIs_add(), (int) data.getNum());
        zanState.set(!zanState.get());
    }

    public void showPayPop(double money) {
        payDialog = new PayDialogWithPhone();
        payDialog.setOnConfirmListener(this);
        Bundle bundle = new Bundle();
        bundle.putDouble("price", money);
        payDialog.setArguments(bundle);
        payDialog.show(fragment.getChildFragmentManager(), "");

    }

    @SuppressLint("CheckResult")
    private void payByWeChat(String phone) {
        Map<String, String> param = new HashMap<>();
        param.put("appkey", HttpCons.APP_KEY);
        param.put("access_token", SPUtils.getInstance().getString("token"));
        param.put("id", mMusicManager.getCurrPlayingMusic().getArtistId());
        param.put("mobile", phone);
        String sig = FormUtil.genSig(param);
        param.put("sig", sig);
        io.reactivex.Observable<ResponseWrapper<PayInfo>> observable = RetrofitClient.getInstance().create(ApiService.class)
                .learn_buy(param)
                .compose(RxUtils.bindToLifecycle(fragment)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer());// 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
        observable.subscribe(payInfoResponseWrapper -> {
            if (payInfoResponseWrapper.isSuccess()) {
                PayReq request = new PayReq();
                request.appId = payInfoResponseWrapper.getData().getAppid();
                request.partnerId = WeixinCons.PARTNER_ID;
                request.prepayId = payInfoResponseWrapper.getData().getPrepayid();
                request.packageValue = "Sign=WXPay";
                request.nonceStr = payInfoResponseWrapper.getData().getNoncestr();
                request.timeStamp = payInfoResponseWrapper.getData().getTimestamp();
                request.sign = payInfoResponseWrapper.getData().getSign();
                AppApplication.mWxApi.sendReq(request);
            } else {
                ToastUtil.toastBottom(payInfoResponseWrapper.getInfo());
            }
        }, throwable -> {
            ToastUtil.toastBottom(throwable.getMessage());
        });
    }


    @Override
    public void onConfirm(PayDialogWithPhone.PayBeanWithPhone v) {
        if (v.type == 0) {
            payByWeChat(v.phone);
        } else {
            ToastUtil.toastBottom("暂不支持，请选择其他支付方式");
        }
    }

    /********************************************comment*******************************************/
    private int pageNum = 1;
    public ItemBinding itemBinding = ItemBinding.of(BR.vm, R.layout.comment_list_item);
    public ObservableBoolean isRefreshFinish = new ObservableBoolean(true);
    public ObservableBoolean isLoadMoreFinish = new ObservableBoolean(true);
    public ObservableList<CommentListItemVm> commentList = new ObservableArrayList<>();
    public ObservableInt commentNum = new ObservableInt(0);
    //上拉加载
    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isLoadMoreFinish.set(false);
            pageNum++;
            getConmments(false);
        }
    });
    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isRefreshFinish.set(true);
        }
    });

    @SuppressLint("CheckResult")
    public void getConmments(boolean isRefresh) {


        HashMap<String, String> para = new HashMap<>();
        para.put("appkey", HttpCons.APP_KEY);
        para.put("access_token", SPUtils.getInstance().getString("token"));
        AudioInfo currPlayingMusic = mMusicManager.getCurrPlayingMusic();
        String id;
        if (entity != null) {
            id = entity.getId();
        } else {
            if (currPlayingMusic != null) {
                id = currPlayingMusic.getArtistId();
            } else {
                return;
            }
        }
        para.put("id", id);
        if (currPlayingMusic != null) {
            if (!TextUtils.isEmpty(currPlayingMusic.getSongId())) {
                para.put("albumid", currPlayingMusic.getSongId());
            }
        }
        para.put("page", "" + pageNum);

        String sig = FormUtil.genSig(para);
        para.put("sig", sig);
        Observable<CommentResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .get_learn_comment(para)
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {

            if (res.getStatus() != 1) {
                return;
            }
            commentNum.set(Integer.parseInt(res.getData().getTotal_count()));
            if (isRefresh) {
                this.isRefreshFinish.set(true);
                commentList.clear();
                for (int i = 0; i < res.getData().getList().size(); i++) {
                    commentList.add(new CommentListItemVm(context, res.getData().getList().get(i), i + 1, true, true, id));
                }
            } else {
                this.isLoadMoreFinish.set(true);
                int size = res.getData().getList().size();
                if (size <= 0) {
                    Toast.makeText(context, "没有更多了", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < size; i++) {
                    commentList.add(new CommentListItemVm(context, res.getData().getList().get(i), i + 1, true, true, id));
                }
            }
        }, e -> {

            if (isRefresh) {
                this.isRefreshFinish.set(true);
            } else
                this.isLoadMoreFinish.set(true);
            e.printStackTrace();
        });
    }
}
