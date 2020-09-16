package com.tchy.syh.home;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.core.content.FileProvider;
import android.util.Log;

import com.lzx.musiclibrary.aidl.model.AudioInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.tchy.syh.book.book_detail.BookVideoPlayerActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tchy.syh.listen.PlayerEvent;
import com.tchy.syh.listen.base.AdaptationViewModel;
import com.tchy.syh.listen.base.Density;
import com.tchy.syh.listen.database.AlbumPlayProgress;
import com.tchy.syh.listen.database.AudioHistory;
import com.tchy.syh.listen.database.AudioPlayProgress;
import com.tchy.syh.listen.database.MusicDatabase;
import com.tchy.syh.listen.play.AudioPlayFragment;
import com.tchy.syh.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.http.DownLoadManager;
import me.goldze.mvvmhabit.http.download.ProgressCallBack;
import okhttp3.ResponseBody;


public class HomeViewModel extends AdaptationViewModel {
    private Disposable mSubscription;

    public HomeViewModel(Context context) {
        //要使用父类的context相关方法,记得加上这一句
        super(context);
    }

    ObservableInt vpIndex = new ObservableInt();
    ObservableBoolean playState = new ObservableBoolean(false);

    @Override
    public void onCreate() {
        super.onCreate();
        playState.set(!playState.get());
    }

    //表单修改点击事件

    //权限申请
    public BindingCommand permissionsClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //请求打开相机权限
            RxPermissions rxPermissions = new RxPermissions((Activity) context);
            rxPermissions.request(Manifest.permission.CAMERA)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean) {

                                ToastUtil.toastBottom("相机权限已经打开，直接跳入相机");
                            } else {
                                ToastUtil.toastBottom("权限被拒绝");
                            }
                        }
                    });

        }
    });


    public BindingCommand vpPageOnChange = new BindingCommand(new BindingConsumer<Integer>() {

        @Override
        public void call(Integer integer) {
            vpIndex.set(integer.intValue());
        }
    });


    public BindingCommand playClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            vpIndex.set(0);
        }
    });

    public BindingCommand onPlayClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            boolean isPlaying = MusicManager.get().isPlaying();
            if (isPlaying) {

                if ("singlebook".equals(MusicManager.get().getCurrPlayingMusic().getGenre())) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isAudio", true);
                    bundle.putString("id", MusicManager.get().getCurrPlayingMusic().getSongId().replace("book", ""));
                    bundle.putString("name", MusicManager.get().getCurrPlayingMusic().getSongName());
                    startActivity(BookVideoPlayerActivity.class, bundle);

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("id",MusicManager.get().getCurrPlayingMusic().getSongId() );
                    startAdaptationContainerActivity(AudioPlayFragment.class.getCanonicalName(), bundle, Density.HEIGHT);

                }
            } else {
                MusicDatabase musicDatabase = MusicDatabase.getInstance(context);
                List<AudioHistory> audioHistories = musicDatabase.audioHistoryDao().listAudioHistory();
                if (audioHistories == null || audioHistories.isEmpty()) {
                    ToastUtil.toastBottom("无历史播放");
                } else {
                    //上一次播放的集数
                    AlbumPlayProgress albumPlayProgressById = musicDatabase.albumPlayProgressDao().getAlbumPlayProgressById(audioHistories.get(0).getAlbumId());
                    AudioHistory audioHistory = audioHistories.get(albumPlayProgressById.getProgerss());
                    AudioPlayProgress audioPlayProgressById = musicDatabase.audioPlayProgressDao().getAudioPlayProgressById(audioHistory.getAudioId());
                    MusicManager.get().playMusic(convert2AudioList(audioHistories), albumPlayProgressById.getProgerss());
                    MusicManager.get().seekTo(audioPlayProgressById.getProgerss());
                    Bundle bundle = new Bundle();
                    bundle.putString("id",audioHistory.getAudioId() );
                    startAdaptationContainerActivity(AudioPlayFragment.class.getCanonicalName(),bundle, Density.HEIGHT);
                }
            }
        }
    });

    private List<AudioInfo> convert2AudioList(List<AudioHistory> audioHistories) {
        List<AudioInfo> list = new ArrayList<>();
        for (AudioHistory audioHistory : audioHistories) {
            AudioInfo audioInfo = new AudioInfo();
            audioInfo.setSongId(audioHistory.getAudioId());
            audioInfo.setSongName(audioHistory.getAudioTitle());
            audioInfo.setDuration(Long.parseLong(audioHistory.getDuration()));
            audioInfo.setSongUrl(audioHistory.getAudioUrl());
            audioInfo.setArtistId(audioHistory.getAlbumId());//书的id
            audioInfo.setTrackNumber(audioHistory.getSort());
            audioInfo.setArtist(audioHistory.getAlbumName());
            audioInfo.setSongCover(audioHistory.getAudioCover());//封面
            audioInfo.setType(audioHistory.getIsFree());
            audioInfo.setGenre(audioHistory.getCollectNum());//收藏数
            audioInfo.setCountry(audioHistory.getCommentNum());//评论数
            audioInfo.setFavorites(audioHistory.getIsCollect());//是否收藏
            audioInfo.setVersions(audioHistory.getIsZan() + "");
            audioInfo.setSongHDCover(audioHistory.getZanNum() + "");

            list.add(audioInfo);
        }
        return list;
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        mSubscription = RxBus.getDefault().toObservable(PlayerEvent.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(playerEvent -> playState.set(!playState.get()));
        RxSubscriptions.add(mSubscription);
    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(mSubscription);
    }

    //异常全局异常捕获
    public BindingCommand exceptionClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //伪造一个异常
            Integer.parseInt("a");
        }
    });


    public static void installApp(Context pContext, File pFile) {
        Log.d("sort", "installApp: " + pFile.getPath());
        if (null == pFile)
            return;
        if (!pFile.exists())
            return;
        Intent _Intent = new Intent();
        _Intent.setAction(Intent.ACTION_VIEW);
        Uri _uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            _Intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            _uri = FileProvider.getUriForFile(pContext, "com.tchy.syh.share", pFile);
        } else {
            _uri = Uri.fromFile(pFile);
            _Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        _Intent.setDataAndType(_uri, "application/vnd.android.package-archive");
        pContext.startActivity(_Intent);
    }

    public void downloadFile(String loadUrl) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在下载...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RxPermissions rxPermissions = new RxPermissions((Activity) context);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(v -> {
            if (v) {
                String destFileDir = Environment
                        .getExternalStorageDirectory().getPath();
                String destFileName = System.currentTimeMillis() + ".apk";//文件存放的名称
                DownLoadManager.getInstance().load(loadUrl, new ProgressCallBack<ResponseBody>(destFileDir, destFileName) {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseBody responseBody) {

                        ToastUtil.toastBottom("文件下载完成！");
                        installApp(context, new File(destFileDir, destFileName));
                    }

                    @Override
                    public void progress(final long progress, final long total) {
                        progressDialog.setMax((int) total);
                        progressDialog.setProgress((int) progress);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtil.toastBottom("文件下载失败！");
                    }
                });
            }
        });


    }


}
