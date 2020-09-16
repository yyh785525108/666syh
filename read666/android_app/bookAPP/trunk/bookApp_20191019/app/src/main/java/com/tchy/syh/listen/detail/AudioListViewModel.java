package com.tchy.syh.listen.detail;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.widget.Toast;

import com.lzx.musiclibrary.aidl.model.AudioInfo;
import com.lzx.musiclibrary.constans.PlayMode;
import com.lzx.musiclibrary.manager.MusicManager;
import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.cons.HttpCons;
import com.tchy.syh.listen.ApiService;
import com.tchy.syh.listen.PlayerEvent;
import com.tchy.syh.listen.base.AdaptationViewModel;
import com.tchy.syh.listen.database.AlbumPlayProgress;
import com.tchy.syh.listen.database.AudioHistory;
import com.tchy.syh.listen.database.AudioHistoryDao;
import com.tchy.syh.listen.database.AudioPlayProgress;
import com.tchy.syh.listen.database.MusicDatabase;
import com.tchy.syh.listen.entity.AudioDetailEntity;
import com.tchy.syh.listen.entity.ListenEntity;
import com.tchy.syh.listen.entity.PayAudioEvent;
import com.tchy.syh.listen.entity.ResAudioList;
import com.tchy.syh.listen.entity.ResponseWrapper;
import com.tchy.syh.listen.entity.UpdateAudioDescEvent;
import com.tchy.syh.listen.play.AudioPlayFragment;

import com.tchy.syh.utils.FormUtil;
import com.tchy.syh.utils.FormatUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.bus.RxSubscriptions;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class AudioListViewModel extends AdaptationViewModel {
    private static final String TAG = "lai==>";
    //书信息

    private final AudioDetailEntity detailEntity;
    //加载列表是否成功
    public ObservableBoolean loadSuccess = new ObservableBoolean(false);
    //改变列表在播item的样式的标记
    public ObservableBoolean updateItemStyle = new ObservableBoolean(false);
    //给RecyclerView添加ObservableList
    public ObservableList<AudioItemViewModel> observableList = new ObservableArrayList<>();
    //给RecyclerView添加ItemView
    public ItemBinding<AudioItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_audio);
    //播放控制文字  [全部播放，暂停播放，继续播放]
    ObservableField<String> mControlText = new ObservableField<>("");
    //是否倒序显示
    ObservableBoolean isReverse = new ObservableBoolean(false);
    //音频控制器
    private MusicManager mMusicManager;

    private Disposable mMusicSubscription;
    //订阅者
    private Disposable mSubscription;
    private List<AudioInfo> songList;

    public AudioListViewModel(Context context, ListenEntity.ListenItemEntity mArgument, AudioDetailEntity audioDetailEntity) {
        super(context);

        this.detailEntity = audioDetailEntity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMusicManager = MusicManager.get();
        mMusicManager.setPlayMode(PlayMode.PLAY_IN_ORDER);
        reqAudioList();
        //reqTest(0);
    }

    /**
     * 判断播放的音频是否在此列表中
     *
     * @return
     */
    private boolean isPlayingThisList() {
        String artistId = mMusicManager.getCurrPlayingMusic().getArtistId();
        String id = detailEntity.getId();
        return artistId.equals(id);
    }

    /**
     * 从服务器获取音频列表
     */
    @SuppressLint("CheckResult")
    private void reqAudioList() {
        Map<String, String> para = new HashMap<>();
        para.put("appkey", HttpCons.APP_KEY);
        para.put("access_token", SPUtils.getInstance().getString("token"));
        para.put("id", detailEntity.getId());
        String sig = FormUtil.genSig(para);
        para.put("sig", sig);
        RetrofitClient.getInstance().create(ApiService.class)
                .listAudioData(para)
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .subscribe((Consumer<ResponseWrapper<ResAudioList>>) listResponseWrapper -> {

                    //请求成功
                    if (listResponseWrapper.isSuccess()) {
                        loadSuccess.set(true);
                        AlbumPlayProgress albumPlayProgressById = MusicDatabase.getInstance(context).albumPlayProgressDao().getAlbumPlayProgressById(detailEntity.getId());

                        List<ListenEntity.AudioItemEntity> list = listResponseWrapper.getData().getList();
                        if (list != null && !list.isEmpty()) {
                            int size = list.size();
                            for (int i = 0; i < size; i++) {
                                ListenEntity.AudioItemEntity entity = list.get(i);
                                entity.setSort(i);
                                AudioItemViewModel audioItemViewModel = new AudioItemViewModel(entity);
                                if (albumPlayProgressById != null && i == albumPlayProgressById.getProgerss()) {
                                    audioItemViewModel.textColor = Color.parseColor("#EB4449");
                                    RxBus.getDefault().post(new UpdateAudioDescEvent(entity.getContent()));
                                } else {
                                    audioItemViewModel.textColor = Color.parseColor("#000000");
                                }
                                observableList.add(audioItemViewModel);
                            }
                            songList = convertSongInfoList();
                        }
                    } else {
                        loadSuccess.set(false);
                        ToastUtil.toastBottom(listResponseWrapper.getInfo());
                        // TODO: 2018/7/24 处理请求失败的情况
                    }
                }, (Consumer<ResponseThrowable>) throwable -> {
                    loadSuccess.set(false);
                    // TODO: 2018/7/24 处理请求失败的情况
                });
    }


    private List<ListenEntity.AudioItemEntity> tempList = new ArrayList<>();


    /**
     * 注册RxBus
     */
    @Override
    public void registerRxBus() {
        super.registerRxBus();
        mSubscription = RxBus.getDefault().toObservable(ListenEntity.AudioItemEntity.class)
                .subscribe(entity -> handAudioItemClick(entity));
        mMusicSubscription = RxBus.getDefault().toObservable(PlayerEvent.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(playerEvent -> {
                    if (playerEvent != null) {
                        int eventType = playerEvent.EventType;
                        if (eventType == MusicManager.MSG_PLAYER_START ||
                                eventType == MusicManager.MSG_PLAYER_PAUSE ||
                                eventType == MusicManager.MSG_PLAYER_STOP ||
                                eventType == MusicManager.MSG_PLAYER_ERROR) {
                            updateControlText();
                        }
                        if (eventType == MusicManager.MSG_MUSIC_CHANGE) {
                            updatePlayingStyle();
                        }
                    }
                });
        RxSubscriptions.add(mSubscription);
        RxSubscriptions.add(mMusicSubscription);
    }


    /**
     * 点击音频item
     *
     * @param entity
     */
    private void handAudioItemClick(ListenEntity.AudioItemEntity entity) {

        if (!"none".equals(entity.getPurview())) {
            RxBus.getDefault().post(new PayAudioEvent(entity.getPrice()));
            RxBus.getDefault().post(new UpdateAudioDescEvent(entity.getDescription()));
            return;
        }
        List<AudioInfo> playList = mMusicManager.getPlayList();
        if (playList == null || playList.isEmpty()) {
            //音乐列表为空--》加入播放列表，并播放音乐
            mMusicManager.playMusic(songList, entity.getSort());
            setPlayProgress(entity.getId(), entity);
            addToHistory(songList);
        } else {
            boolean isPlayingCurrentShowList = playList.get(0).getArtistId().equals(detailEntity.getId());
            if (isPlayingCurrentShowList) {
                //播放的是当前显示列表中的音频--》判断是否在播放
                boolean playing = mMusicManager.isPlaying();
                if (playing) {
                    //正在播放==》
                    AudioInfo currPlayingMusic = mMusicManager.getCurrPlayingMusic();
                    if (!entity.getId().equals(currPlayingMusic.getSongId())) {
                        //点击的不是正在播放的音频--》播放点击的音乐
                        mMusicManager.playMusicByIndex(entity.getSort());
                        setPlayProgress(entity.getId(), entity);
                    }
                } else {
                    //没有在播放--》播放点击的音乐
                    mMusicManager.playMusicByIndex(entity.getSort());
                    setPlayProgress(entity.getId(), entity);
                }
            } else {
                //播放的不是当前显示列表中的音频 --》加入播放列表，并播放音乐
                mMusicManager.playMusic(songList, entity.getSort());
                setPlayProgress(entity.getId(), entity);
                addToHistory(songList);
            }
        }
        MusicDatabase.getInstance(context).albumPlayProgressDao().saveProgress(new AlbumPlayProgress(detailEntity.getId(), entity.getSort()));
        Bundle bundle = new Bundle();
        bundle.putParcelable("entity", detailEntity);
        bundle.putString("id", entity.getId());
        startAdaptationContainerActivity(AudioPlayFragment.class.getCanonicalName(), bundle, "height");
        RxBus.getDefault().post(new UpdateAudioDescEvent(entity.getContent()));
    }


    public BindingCommand reverseList = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isReverse.set(!isReverse.get());
            Collections.reverse(observableList);
            List<AudioInfo> playList = mMusicManager.getPlayList();
            Collections.reverse(playList);
        }
    });

    /**
     * 点击 全部播放/暂停播放/继续播放
     */
    public BindingCommand onControlTextClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            switch (mControlText.get()) {
                case "全部播放":
                    if (!"none".equals(songList.get(0).getType())) {
                        Toast.makeText(context, "收费音频", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mMusicManager.playMusic(songList, 0);
                    MusicDatabase.getInstance(context).albumPlayProgressDao().saveProgress(new AlbumPlayProgress(detailEntity.getId(), 0));
                    addToHistory(songList);
                    break;
                case "暂停播放":
                    mMusicManager.pauseMusic();
                    break;
                case "继续播放":
                    int progress = MusicDatabase.getInstance(context).albumPlayProgressDao().getAlbumPlayProgressById(detailEntity.getId()).getProgerss();
                    AudioInfo audioInfo = songList.get(progress);
                    if (!"none".equals(audioInfo.getType())) {
                        Toast.makeText(context, "收费音频", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mMusicManager.playMusic(songList, progress);
                    setPlayProgress(audioInfo.getSongId());
                    addToHistory(songList);
                    break;
            }
        }
    });

    /**
     * 设置播放进度
     *
     * @param audioId
     */
    private void setPlayProgress(String audioId, ListenEntity.AudioItemEntity entity) {
        AudioPlayProgress audioPlayProgressById = MusicDatabase.getInstance(context).audioPlayProgressDao().getAudioPlayProgressById(audioId);
        if (audioPlayProgressById != null) {

            if (audioPlayProgressById.getProgerss() > FormatUtil.musicTime2Int(entity.getDuration())) {
                mMusicManager.seekTo(0);
            } else {
                mMusicManager.seekTo(audioPlayProgressById.getProgerss());
            }

        }
    }

    /**
     * 设置播放进度
     *
     * @param audioId
     */
    private void setPlayProgress(String audioId) {
        AudioPlayProgress audioPlayProgressById = MusicDatabase.getInstance(context).audioPlayProgressDao().getAudioPlayProgressById(audioId);
        if (audioPlayProgressById != null) {
            mMusicManager.seekTo(audioPlayProgressById.getProgerss());
        }
    }

    /**
     * 将列表加入历史
     *
     * @param list
     */
    @SuppressLint("CheckResult")
    private void addToHistory(final List<AudioInfo> list) {
        AudioHistoryDao lastPlayListDao = MusicDatabase.getInstance(context.getApplicationContext()).audioHistoryDao();
        Observable.fromArray(list).subscribeOn(Schedulers.io()).subscribe(audioInfos -> {

            long currentTimeMillis = System.currentTimeMillis();
            lastPlayListDao.clearAll();
            for (AudioInfo info : audioInfos) {
                AudioHistory history = new AudioHistory();
                history.setAudioId(info.getSongId());
                history.setAudioAddTime(currentTimeMillis + "");
                history.setAudioCover(info.getSongCover());
                history.setDuration(info.getDuration() + "");
                history.setAudioTitle(info.getSongName());
                history.setAudioUpdateTime(currentTimeMillis + "");
                history.setAudioCover(info.getSongCover());
                history.setAudioUrl(info.getSongUrl());
                history.setAlbumName(info.getArtist());
                history.setAlbumId(info.getArtistId());
                history.setSort(info.getTrackNumber());
                history.setIsFree(info.getType());

                history.setCollectNum(info.getGenre());
                history.setCommentNum(info.getCountry());
                history.setIsCollect(info.getFavorites());
                history.setIsZan(Integer.parseInt(info.getVersions()));
                history.setZanNum(Integer.parseInt(info.getSongHDCover()));
                lastPlayListDao.insert(history);
            }
        });
    }

    /**
     * 转换为音乐播放列表
     *
     * @return
     */
    @NonNull
    private List<AudioInfo> convertSongInfoList() {
        List<AudioInfo> list = new ArrayList<>();
        for (AudioItemViewModel audio : observableList) {
            AudioInfo audioInfo = new AudioInfo();
            ListenEntity.AudioItemEntity mAudioEntity = audio.mAudioEntity;
            audioInfo.setSongId(mAudioEntity.getId());//音频id
            audioInfo.setSongName(mAudioEntity.getTitle());//音频名称
            audioInfo.setDuration(FormatUtil.getString2Long(mAudioEntity.getDuration()));//音频时长
            audioInfo.setSongUrl(mAudioEntity.getDefault_video());//音频url地址
            audioInfo.setArtistId(detailEntity.getId());//专辑id
            audioInfo.setArtist(detailEntity.getTitle());//专辑名称
            audioInfo.setTrackNumber(audio.mAudioEntity.getSort());//专辑序号
            audioInfo.setSongCover(mAudioEntity.getThumb());//音频封面
            String purview = mAudioEntity.getPurview();
            audioInfo.setType("none".equals(purview) ? "none" : ("" + detailEntity.getPrice()));//音频收费类型
            audioInfo.setGenre(detailEntity.getPlay_num() + "");//收藏数
            audioInfo.setCountry(detailEntity.getComment_num() + "");//评论数
            audioInfo.setFavorites(detailEntity.getIs_collect());//是否收藏
            audioInfo.setVersions(detailEntity.getIs_zan() + "");//是否点赞
            audioInfo.setSongHDCover(detailEntity.getLike_num() + "");//点赞数
            list.add(audioInfo);
        }
        return list;
    }


    /**
     * 移除rxbus
     */
    @Override
    public void removeRxBus() {
        super.removeRxBus();
        RxSubscriptions.remove(mSubscription);
        RxSubscriptions.remove(mMusicSubscription);
    }

    /**
     * 更新在播放item的样式
     */
    public void updatePlayingStyle() {
        String songId = mMusicManager.getCurrPlayingMusic().getSongId();
        for (int i = 0; i < observableList.size(); i++) {
            AudioItemViewModel audioItemViewModel = observableList.get(i);
            if (songId.equals(audioItemViewModel.mAudioEntity.getId())) {
                audioItemViewModel.textColor = Color.parseColor("#EB4449");
            } else {
                audioItemViewModel.textColor = Color.parseColor("#000000");
            }
        }
        updateItemStyle.set(!updateItemStyle.get());
    }

    /**
     * 更新控制文字
     */
    public void updateControlText() {
        //  获取 上一次播放的集数
        AlbumPlayProgress albumPlayProgressById = MusicDatabase.getInstance(context).albumPlayProgressDao().getAlbumPlayProgressById(detailEntity.getId());
        if (albumPlayProgressById != null) {
            //判断是否在播放,并且播放的是当前列表的音频

            boolean playing = mMusicManager.isPlaying();
            if (playing && isPlayingThisList()) {
                //显示 暂停播放
                mControlText.set("暂停播放");
            } else {
                mControlText.set("继续播放");
            }
        } else {
            //显示全部播放
            mControlText.set("全部播放");
        }
    }
}
