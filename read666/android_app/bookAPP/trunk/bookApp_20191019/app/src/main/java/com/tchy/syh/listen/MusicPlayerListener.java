package com.tchy.syh.listen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import com.google.gson.Gson;
import com.lzx.musiclibrary.aidl.listener.OnPlayerEventListener;
import com.lzx.musiclibrary.aidl.model.AudioInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.lzx.musiclibrary.manager.TimerTaskManager;
import com.tchy.syh.cons.HttpCons;
import com.tchy.syh.listen.database.AlbumPlayProgress;
import com.tchy.syh.listen.database.AudioPlayProgress;
import com.tchy.syh.listen.database.MusicDatabase;
import com.tchy.syh.listen.entity.PayInfo;
import com.tchy.syh.listen.entity.ResponseWrapper;
import com.tchy.syh.utils.FormUtil;
import com.tchy.syh.utils.RetrofitClient;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;

import static com.lzx.musiclibrary.manager.MusicManager.MSG_BUFFERING;
import static com.lzx.musiclibrary.manager.MusicManager.MSG_MUSIC_CHANGE;
import static com.lzx.musiclibrary.manager.MusicManager.MSG_PLAYER_ERROR;
import static com.lzx.musiclibrary.manager.MusicManager.MSG_PLAYER_PAUSE;
import static com.lzx.musiclibrary.manager.MusicManager.MSG_PLAYER_START;
import static com.lzx.musiclibrary.manager.MusicManager.MSG_PLAYER_STOP;
import static com.lzx.musiclibrary.manager.MusicManager.MSG_PLAY_COMPLETION;

public class MusicPlayerListener implements OnPlayerEventListener {
    private static final String TAG = "MusicPlayerListener==>";
    private final TimerTaskManager mTimerTaskManager;
    private final MusicManager mMusicManager;
    private final Context mContext;

    public MusicPlayerListener(Context context) {
        mContext = context;
        mMusicManager = MusicManager.get();
        mTimerTaskManager = new TimerTaskManager();
        mTimerTaskManager.setUpdateProgressTask(new Runnable() {
            @Override
            public void run() {
                handProgress();
            }
        });
        mTimerTaskManager.scheduleSeekBarUpdate();
    }

    private void handProgress() {
        AudioInfo audioInfo = mMusicManager.getCurrPlayingMusic();
        if (audioInfo != null) {
            long progress = mMusicManager.getProgress();
            MusicDatabase.getInstance(mContext).audioPlayProgressDao().saveProgress(new AudioPlayProgress(audioInfo.getSongId(), (int) progress));
        }
    }

    @Override
    public void onMusicSwitch(AudioInfo music) {
        Log.d(TAG, "onMusicSwitch: "+music.getFavorites());
        if (!"none".equals(music.getType())) {
            mMusicManager.pauseMusic();
            return;
        }
        int trackNumber = music.getTrackNumber();
        String artistId = music.getArtistId();
        MusicDatabase.getInstance(mContext).albumPlayProgressDao().saveProgress(new AlbumPlayProgress(artistId, trackNumber));
        RxBus.getDefault().post(new PlayerEvent(MSG_MUSIC_CHANGE, music));
    }

    @SuppressLint("CheckResult")
    @Override
    public void onPlayerStart() {
        if (!"none".equals(mMusicManager.getCurrPlayingMusic().getType())) {
            mMusicManager.pauseMusic();
            return;
        }
        Log.d(TAG, "onPlayerStart: ");
        AudioInfo currPlayingMusic = MusicManager.get().getCurrPlayingMusic();
        RxBus.getDefault().post(new PlayerEvent(MSG_PLAYER_START));
        Map<String, String> param = new HashMap<>();
        param.put("appkey", HttpCons.APP_KEY);
        param.put("access_token", SPUtils.getInstance().getString("token"));
        param.put("m", "add");
        param.put("album_id", currPlayingMusic.getSongId());
        param.put("id", currPlayingMusic.getArtistId());
        String sig = FormUtil.genSig(param);
        param.put("sig", sig);
        Log.e(TAG, "onPlayerStart: "+new Gson().toJson(param) );
        RetrofitClient.getInstance().create(ApiService.class)
                .history_list(param)
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer())
                .subscribe(o -> {

                }, throwable -> {

                });
    }

    @Override
    public void onPlayerPause() {
        Log.d(TAG, "onPlayerPause: ");
        RxBus.getDefault().post(new PlayerEvent(MSG_PLAYER_PAUSE));
    }

    @Override
    public void onPlayCompletion() {
        Log.d(TAG, "onPlayCompletion: ");
        RxBus.getDefault().post(new PlayerEvent(MSG_PLAY_COMPLETION));
        if (mMusicManager.hasNext()) {
           if (!"none".equals(mMusicManager.getNextMusic().getType())){
               Toast.makeText(mContext, "当前音频已播完，下一个音频为收费音频", Toast.LENGTH_SHORT).show();
           }else {
               mMusicManager.playNext();
           }
        }
    }

    @Override
    public void onPlayerStop() {
        Log.d(TAG, "onPlayerStop: ");
        RxBus.getDefault().post(new PlayerEvent(MSG_PLAYER_STOP));
    }

    @Override
    public void onError(String errorMsg) {
        Log.d(TAG, "onError: ");
        RxBus.getDefault().post(new PlayerEvent(MSG_PLAYER_ERROR));
    }

    @Override
    public void onAsyncLoading(boolean isFinishLoading) {
        if (!"none".equals(mMusicManager.getCurrPlayingMusic().getType())) {
            mMusicManager.pauseMusic();
            return;
        }
        Log.d(TAG, "onAsyncLoading: ");
        RxBus.getDefault().post(new PlayerEvent(MSG_BUFFERING, isFinishLoading));
    }
}
