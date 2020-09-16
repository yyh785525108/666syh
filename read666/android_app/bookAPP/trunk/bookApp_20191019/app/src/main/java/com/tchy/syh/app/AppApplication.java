package com.tchy.syh.app;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.bugtags.library.Bugtags;
import com.lzx.musiclibrary.cache.CacheConfig;
import com.lzx.musiclibrary.manager.MusicLibrary;
import com.lzx.musiclibrary.manager.MusicManager;
import com.lzx.musiclibrary.notification.NotificationCreater;
import com.lzx.musiclibrary.utils.BaseUtil;
import com.lzy.ninegrid.NineGridView;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.squareup.picasso.Picasso;
import com.taobao.sophix.SophixManager;
import com.tchy.syh.R;
import com.tchy.syh.cons.WeixinCons;
import com.tchy.syh.home.HomeActivity;
import com.tchy.syh.listen.MusicPlayerListener;
import com.tchy.syh.listen.base.Density;
import com.tchy.syh.userAccount.userLogin.LoginActivity;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseApplication;
import me.goldze.mvvmhabit.crash.CaocConfig;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class AppApplication extends BaseApplication {
    private MusicPlayerListener mMusicListener;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
//        LeakCanary.install(this);
        //开启打印日志
//        SPUtils.getInstance().put("token", "c42adaa43599572035bf7320e4bd316b");
        Bugtags.start("31e18aea32627449c9c07f10aa61a360", this, Bugtags.BTGInvocationEventBubble);
        ToastUtils.setBgResource(R.drawable.toast_filled_round_corner_bg);
        ToastUtils.setMessageColor(getResources().getColor(R.color.white));
//       ToastUtils.setGravity(Gravity.CENTER, 0,0 );
//       ToastUtils.setView();
        KLog.init(true);
        //九宫格图片工具
        NineGridView.setImageLoader(new PicassoImageLoader());
        //初始化全局异常崩溃
        initCrash();
        registToWX();
        VideoOptionModel videoOptionModel =
                new VideoOptionModel(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 30);
        List<VideoOptionModel> list = new ArrayList<>();
        list.add(videoOptionModel);
        GSYVideoManager.instance().setOptionModelList(list);
        GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);
        GSYVideoManager.instance().setVideoType(getApplicationContext(), GSYVideoType.IJKEXOPLAYER2);
        Density.setDensity(this);
        initAudio();
        UMConfigure.init(this, "你的应用在友盟上的APPKEY", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        PlatformConfig.setWeixin("你的微信APPID", "你的微信AppSecret");//微信APPID和AppSecret
        PlatformConfig.setQQZone("你的QQAPPID", "你的QQAppSecret");//QQAPPID和AppSecret
        PlatformConfig.setSinaWeibo("你的微博APPID", "你的微博APPSecret", "微博的后台配置回调地址");//微博
    }

    /**
     * 初始化音频播放
     */
    private void initAudio() {
        if (BaseUtil.getCurProcessName(this).equals("com.tchy.syh")) {

            //边播边存配置
            CacheConfig cacheConfig = new CacheConfig.Builder()
                    .setOpenCacheWhenPlaying(true)   //在播放的时候 进行cache
                    .setCachePath(getExternalCacheDir() + "/Music/Cache/") //cache的位置是这个地址
                    .build();
            //通知栏模块的配置
            NotificationCreater creater = new NotificationCreater.Builder()
                    .setTargetClass("com.tchy.syh.home.HomeActivity")  //设置通知栏的类是HomeActivity
                    .setCreateSystemNotification(true)          //设置为系统级别的通知栏
                    .build();
            //音乐播放封装库
            MusicLibrary musicLibrary = new MusicLibrary.Builder(this)
                    .setAutoPlayNext(false) //是否在播放完当前歌曲后自动播放下一首
                    .setNotificationCreater(creater) //通知栏配置
                    .setCacheConfig(cacheConfig) //边播边存配置
                    .build();
            musicLibrary.init(); //音乐播放封装库的初始化
            mMusicListener = new MusicPlayerListener(this);
            MusicManager.get().addPlayerEventListener(mMusicListener);
        }
    }

    public static IWXAPI mWxApi;

    private void registToWX() {
        //AppConst.WEIXIN.APP_ID是指你应用在微信开放平台上的AppID，记得替换。
        mWxApi = WXAPIFactory.createWXAPI(this, WeixinCons.APP_ID, false);
        // 将该app注册到微信
        mWxApi.registerApp(WeixinCons.APP_ID);
    }

    private void initCrash() {
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.mipmap.ic_launcher) //错误图标
                .restartActivity(HomeActivity.class) //重新启动后的activity
//                .errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
//                .eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply();
    }
    /** Picasso 加载 */
    private class PicassoImageLoader implements NineGridView.ImageLoader {

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {

            Picasso.with(context).load(url)//
                    .placeholder(R.drawable.ic_default_color)//
                    .error(R.drawable.ic_default_color)//
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }
}
