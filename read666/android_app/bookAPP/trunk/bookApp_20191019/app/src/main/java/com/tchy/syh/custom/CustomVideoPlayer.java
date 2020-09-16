package com.tchy.syh.custom;


import android.content.Context;
import android.util.AttributeSet;

import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import cn.qqtheme.framework.util.ScreenUtils;

public class CustomVideoPlayer extends StandardGSYVideoPlayer {
    public CustomVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public CustomVideoPlayer(Context context) {
        super(context);
    }

    public CustomVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void init(Context context) {
        super.init(context);

    }
}
