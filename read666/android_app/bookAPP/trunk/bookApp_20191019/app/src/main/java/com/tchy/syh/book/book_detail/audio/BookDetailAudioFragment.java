package com.tchy.syh.book.book_detail.audio;

import android.animation.ObjectAnimator;
import androidx.databinding.Observable;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ViewUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.SeekBar;

import com.lzx.musiclibrary.aidl.listener.OnPlayerEventListener;
import com.lzx.musiclibrary.aidl.model.AudioInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.BookDetailAudioBinding;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.StringUtils;


public class BookDetailAudioFragment extends BaseFragment<BookDetailAudioBinding, BookDetailAudioPageVm> {
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.book_detail_audio;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public BookDetailAudioPageVm initViewModel() {
        return new BookDetailAudioPageVm(this.getContext());
    }


    public void mediaPause() {
//        if(this.mediaPlayer!=null){
//            if(viewModel!=null)
//                viewModel.playState.set(false);
//        }
    }

    public void mediaRelease() {
//        if(this.mediaPlayer!=null){
//            mediaPlayer.stop();
//            mediaPlayer.reset();
//            mediaPlayer.release();
//            mediaPlayer=null;
//        }
    }

    public String audioUrl = "";
    //    MediaPlayer mediaPlayer;
    ObjectAnimator objectAnimator;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        objectAnimator = ObjectAnimator.ofFloat(binding.circleImageView, "rotation", 0f, 360f);//添加旋转动画，旋转中心默认为控件中点
        objectAnimator.setDuration(6000);//设置动画时间
        objectAnimator.setInterpolator(new LinearInterpolator());//动画时间线性渐变
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setRepeatMode(ObjectAnimator.RESTART);
//        mediaPlayer = new MediaPlayer();
        binding.progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    viewModel.current.set(progress * viewModel.duration.get() / 100);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int newTime = seekBar.getProgress() * viewModel.duration.get() / 100;
                seekTo(newTime);
            }
        });
        binding.btnFF.setOnClickListener(v -> {
            if (!viewModel.hasMedia.get())
                return;
            int newTime = viewModel.current.get() + 15;
            if (newTime > viewModel.duration.get()) {
                newTime = viewModel.duration.get();
            }
            seekTo(newTime);
        });
        binding.btnFP.setOnClickListener(v -> {
            if (!viewModel.hasMedia.get())
                return;
            int newTime = viewModel.current.get() - 15;
            if (newTime < 0) {
                newTime = 0;
            }
            seekTo(newTime);
        });

        MusicManager.get().addPlayerEventListener(new OnPlayerEventListener() {
            @Override
            public void onMusicSwitch(AudioInfo audioInfo) {
                Log.d("sort", "onMusicSwitch:1 " + MusicManager.get().getDuration());
//                if (audioInfo.getSongId().equals(MusicManager.get().getCurrPlayingMusic().getSongId())) {
//                    viewModel.timer(false);
//                    viewModel.current.set(0);
//                    MusicManager.get().seekTo(0);
//                    objectAnimator.end();
//                    viewModel.playState.set(false);
////                }
                if (viewModel == null) {
                    return;
                }
                viewModel.playState.set(false);
            }

            @Override
            public void onPlayerStart() {
                if (viewModel == null) {
                    return;
                }
                viewModel.isMediaPlayerPrepared.set(true);
                viewModel.playState.set(true);

                Log.d("sort", "onPlayerStart:1 " + MusicManager.get().getDuration());
            }

            @Override
            public void onPlayerPause() {
                if (viewModel == null) {
                    return;
                }
                viewModel.playState.set(false);

            }

            @Override
            public void onPlayCompletion() {
                if (viewModel == null) {
                    return;
                }
                Log.d("sort", "onPlayCompletion:1 " + MusicManager.get().getDuration());
                if (viewModel.audioInfo.getSongId().equals(MusicManager.get().getCurrPlayingMusic().getSongId())) {
                    viewModel.timer(false);
                    viewModel.current.set(0);
                    MusicManager.get().seekTo(0);
                    objectAnimator.end();
                    viewModel.playState.set(false);
                }
            }

            @Override
            public void onPlayerStop() {
                if (viewModel == null) {
                    return;
                }
                viewModel.current.set(0);
                Log.d("sort", "onPlayerStop:1 " + MusicManager.get().getDuration());
                viewModel.playState.set(false);
            }

            @Override
            public void onError(String s) {
                if (viewModel == null) {
                    return;
                }
                Log.d("sort", "onError:1 " + s);
                viewModel.playState.set(false);
            }

            @Override
            public void onAsyncLoading(boolean b) {
                if (b) {
                    if (viewModel != null && viewModel.duration != null) {
                        if(MusicManager.get().getCurrPlayingMusic().getDuration()==-2){
                            viewModel.duration.set(MusicManager.get().getDuration() / 1000);

                        }else{
                            viewModel.duration.set((int)MusicManager.get().getCurrPlayingMusic().getDuration()/ 1000);

                        }
                    }
                }

            }
        });

//
    }



    @Override
    public void initViewObservable() {
        super.initViewObservable();

        viewModel.dataBean.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                Log.d("sort", "onPropertyChanged: ");
                if (StringUtils.isEmpty(viewModel.dataBean.get().getDefault_mp3())) {
                    binding.progressBar.setEnabled(false);
                    return;
                }
//                MusicManager.get().playMusic(all,0,false);
                viewModel.hasMedia.set(true);
                binding.progressBar.setEnabled(true);
                if (MusicManager.get().getCurrPlayingMusic() != null) {

                    if (MusicManager.get().getCurrPlayingMusic().getSongId().equals("book"+viewModel.dataBean.get().getId())) {
                        viewModel.current.set((int) MusicManager.get().getProgress() / 1000);
                        viewModel.duration.set(MusicManager.get().getDuration() / 1000);
                        if (MusicManager.get().getStatus() == 3) {
                            viewModel.isMediaPlayerPrepared.set(true);
                            viewModel.playState.set(true);
                        }
                    }


                }



//
            }
        });
        viewModel.current.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(viewModel.dataBean.get().getRank()==null){
                    return ;
                }
                if (Integer.parseInt(viewModel.dataBean.get().getRank()) > SPUtils.getInstance().getInt("level")) {
                    if (viewModel.current.get() >= 13 * 60) {
                        MusicManager.get().stopMusic();
                        viewModel.current.set(0);
                    }
                }
            }
        });
        viewModel.playState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {

                if (viewModel.hasMedia.get()) {


                    if (viewModel.playState.get()) {
                        viewModel.timer(true);

                        Log.d("sort", "onViewCreated:1 " + MusicManager.get().getDuration());

                        if (objectAnimator.isPaused()) {
                            objectAnimator.resume();
                        } else {
                            objectAnimator.start();
                        }

                    } else {


                        viewModel.timer(false);
                        objectAnimator.pause();
                    }
                }
            }
        });
    }

    public void seekTo(int newTime) {
        if(MusicManager.get().getCurrPlayingMusic()==null){
            return ;
        }
        viewModel.current.set(newTime);

        if (MusicManager.get().getCurrPlayingMusic().getSongId().equals(viewModel.audioInfo.getSongId())) {
            MusicManager.get().seekTo(newTime * 1000);
        }

    }

    @Override
    public void onDestroy() {
        if (viewModel != null && viewModel.content != null)
            viewModel.content.set("");
        super.onDestroy();
        objectAnimator.cancel();
        objectAnimator = null;

        
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (viewModel != null) {
            if (isVisibleToUser) {
                viewModel.content.set(viewModel.contentValue);
            } else {
                viewModel.content.set("");
            }
        }
    }
}
