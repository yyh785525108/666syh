package com.yyh.read666.music;

import android.media.MediaPlayer;

public class MediaUtil {
    private static MediaPlayer mediaPlayer;
    public static boolean MEDIA_IS_INIT =false;
    public static  MediaPlayer getMediaPlayer(){
        if (mediaPlayer!=null){
            return mediaPlayer;
        }else {
            mediaPlayer=new MediaPlayer();
            MEDIA_IS_INIT =true;
            return mediaPlayer;
        }
    }
}
