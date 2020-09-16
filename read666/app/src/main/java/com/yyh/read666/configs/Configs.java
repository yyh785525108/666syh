package com.yyh.read666.configs;

import android.os.Handler;
import android.os.Message;

public class Configs {
    /**
     * 本项目的appkey
     */
    public static final String APPKEY="3592478874";
    /**
     * 本项目的appseceret
     */
    public static final String APPSECRECT="b5ee16278fb625af8631584c2bb79de9";
    /**
     * 接口基本地址
     */
    public static final String BASE_URL="http://syh.7seme.com/app/";

    /**
     * 微信的appkey
     */
    public static final String APP_ID4WX="wx0a86121d5dea9203";
    /**
     * 微信的appseceret
     */
    public static final String APPSECRECT4WX="690bf2926629a7b5564f65ef68d6cbce";

    /**
     * 本项目存放历史记录的share地址
     */
    public static final String SHARE_NAME="damaiquan";

    public static int whereFrom=0;//总共有两个地方需要播放音频，所以，如果原先是进入音频播放页，切换到视频时，需要初始化，反之亦然。
    public static Handler Music_UIhandle ;

    public  static boolean isFirst=true;


}
