package com.yyh.read666.utils;

import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;

public class MediaPlayerUtil {
    public static boolean setPlaySpeed(MediaPlayer mediaPlayer,float speed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PlaybackParams params = mediaPlayer.getPlaybackParams();
            params.setSpeed(speed);
            mediaPlayer.setPlaybackParams(params);
            mediaPlayer.pause();
            mediaPlayer.start();
            return true;
        }
        return false;
    }
}
