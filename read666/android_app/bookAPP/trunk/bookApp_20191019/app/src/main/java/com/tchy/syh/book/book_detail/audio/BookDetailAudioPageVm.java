package com.tchy.syh.book.book_detail.audio;

import androidx.lifecycle.ViewModel;
import android.content.Context;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableDouble;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import android.util.Log;

import com.lzx.musiclibrary.aidl.model.AlbumInfo;
import com.lzx.musiclibrary.aidl.model.AudioInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.tchy.syh.book.book_detail.BookDetailVm;
import com.tchy.syh.book.book_detail.entity.BookDetailResp;
import com.tchy.syh.common.CommonBottomBarVM;
import com.tchy.syh.utils.ToastUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.utils.SPUtils;


public class BookDetailAudioPageVm extends BaseViewModel {
    public ObservableInt current=new ObservableInt();
    public ObservableInt duration=new ObservableInt();
    public ObservableField<CommonBottomBarVM> bottomVM=new ObservableField<>();
    public ObservableField<BookDetailResp.DataBean> dataBean = new ObservableField<>();
    public ObservableField<String> content = new ObservableField<>();

    public ObservableBoolean playState=new ObservableBoolean(false);
    public ObservableBoolean isMediaPlayerPrepared=new ObservableBoolean(false);
    public ObservableBoolean hasMedia=new ObservableBoolean(false);
    public BookDetailAudioPageVm(Context context){
        super(context);
    }



    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据




    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer!=null){
            timer.dispose();
            timer=null;
        }

    }
    Disposable timer;
    public void timer(boolean isStart){
        if(isStart){

            if(timer!=null&&!timer.isDisposed()){
                timer.dispose();
                timer=null;
                return ;
            }
            timer=Observable.interval(1,1, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(v->{
                        if(this.current.get()<this.duration.get()){
                            this.current.set(this.current.get()+1);
                        }

                    });


        }else{
            if(timer!=null&&!timer.isDisposed()){
                timer.dispose();
                timer=null;
            }
        }

    }
    AudioInfo audioInfo;
    public void covert(){
        audioInfo = new AudioInfo();
        audioInfo.setSongId("book"+dataBean.get().getId());
        audioInfo.setSongName(dataBean.get().getTitle());
        if (dataBean.get().getRank() == null) {
            dataBean.get().setRank("0");
        }
        if (Integer.parseInt(dataBean.get().getRank()) > SPUtils.getInstance().getInt("level")) {
            audioInfo.setDuration(13*60*1000);
        }else{
            audioInfo.setDuration(-2);
        }

        audioInfo.setType("none");
//                audioInfo.setDownloadUrl(viewModel.dataBean.get().getDefault_mp3());
        audioInfo.setSongUrl(dataBean.get().getDefault_mp3());
        audioInfo.setSongCover(dataBean.get().getPic());//封面
        audioInfo.setGenre("singlebook");
        AlbumInfo albumInfo = new AlbumInfo();
        albumInfo.setAlbumName(dataBean.get().getTitle());
        albumInfo.setAlbumCover("");
        audioInfo.setAlbumInfo(albumInfo);
        audioInfo.setArtistId(dataBean.get().getFid());//书的id
        audioInfo.setTrackNumber(1);
    }
    public BindingCommand play=new BindingCommand(()->{
        if(!hasMedia.get()){
            ToastUtil.toastBottom("暂无音频内容");
            return;
        }
//        playState.set(!playState.get());
        if(MusicManager.get().getCurrPlayingMusic()==null){
            MusicManager.get().playMusicByInfo(audioInfo,false);
        }else{
            Log.d("sort", ": "+this.dataBean.get().getId());
            Log.d("sort", ": "+MusicManager.get().getCurrPlayingMusic().getSongId());
            if(("book"+this.dataBean.get().getId()).equals(MusicManager.get().getCurrPlayingMusic().getSongId())){
                if(MusicManager.get().getStatus()==3){
                    MusicManager.get().pauseMusic();
                }else{
                    MusicManager.get().playMusicByInfo(audioInfo,false);
                }
            }else{
                MusicManager.get().playMusicByInfo(audioInfo,false);
            }
        }


    });
    boolean userClickFlag=false;
    public String contentValue;
    @Override
    public void registerRxBus() {
        super.registerRxBus();
        Messenger.getDefault().register(context, BookDetailVm.BOOK_DETAIL_UPDATE, BookDetailResp.DataBean.class, new BindingConsumer< BookDetailResp.DataBean>() {
            @Override
            public void call(final  BookDetailResp.DataBean bean) {
                //删除选择对话框
                dataBean.set(bean);
                bottomVM.set(new CommonBottomBarVM(context,bean,false));
                covert();
                contentValue=bean.getContent_fee();
            }

        });



    }

    @Override
    public void removeRxBus() {
        super.removeRxBus();
        Messenger.getDefault().unregister(context);
        contentValue=null;

    }
}
