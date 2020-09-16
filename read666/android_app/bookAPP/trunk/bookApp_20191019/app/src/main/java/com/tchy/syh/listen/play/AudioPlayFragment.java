package com.tchy.syh.listen.play;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.databinding.Observable;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lzx.musiclibrary.aidl.model.AudioInfo;
import com.lzx.musiclibrary.manager.MusicManager;
import com.tchy.syh.R;
import com.tchy.syh.app.AppApplication;
import com.tchy.syh.book.book_detail.ApiService;

import com.tchy.syh.comment.entity.CommentLoveResp;
import com.tchy.syh.comment.entity.CommentResp;
import com.tchy.syh.common.entity.CommonPayResp;
import com.tchy.syh.common.entity.CommonResp;
import com.tchy.syh.cons.HttpCons;
import com.tchy.syh.cons.WeixinCons;
import com.tchy.syh.databinding.FragmentAudioPlayBinding;

import com.tchy.syh.dialog.CommentDialog;
import com.tchy.syh.dialog.PayDialog;

import com.tchy.syh.listen.comment.AudioCommentReplyActivity;
import com.tchy.syh.listen.comment.CommentAdapter;
import com.tchy.syh.listen.database.AudioHistory;
import com.tchy.syh.listen.database.AudioPlayProgress;
import com.tchy.syh.listen.database.MusicDatabase;
import com.tchy.syh.listen.entity.AudioDetailEntity;

import com.tchy.syh.listen.entity.CommentChangeUpdateEvent;
import com.tchy.syh.listen.view.ImageTextButton;
import com.tchy.syh.listen.view.RewardDialog;
import com.tchy.syh.utils.FormUtil;
import com.tchy.syh.utils.FormatUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.tchy.syh.BR;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;


public class AudioPlayFragment extends BaseFragment<FragmentAudioPlayBinding, AudioPlayViewModel> implements AudioPlayingListAdapter.OnItemClickListener, RewardDialog.OnRewardBeanConfirmListener {

    private AudioDetailEntity mEntity;
    private PopupWindow popupWindow;
    private AudioPlayingListAdapter playingListAdapter;
    private boolean isReverseList = false;
    private PayDialog payDialog;
    private RewardDialog dashangDialog;
    private Disposable d2;
    private CommentDialog commnetDialog;
    private Disposable d;
    private Disposable d3;
    private List<CommentResp.DataBean.ListBean> commentDatas = new ArrayList<>();
    private CommentAdapter adapter;
    private Animation playAnim;


    @Override
    public void initParam() {
        //获取列表传入的实体
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            mEntity = mBundle.getParcelable("entity");
            String id = mBundle.getString("id");
            Log.e("dangdang", "initParam: "+id );
        }
    }

    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.fragment_audio_play;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public AudioPlayViewModel initViewModel() {
        return new AudioPlayViewModel(this, mEntity);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_play);
        LinearInterpolator lin = new LinearInterpolator();
        playAnim.setInterpolator(lin);

        AudioInfo currPlayingMusic = MusicManager.get().getCurrPlayingMusic();

        binding.tvComment.setText(Integer.parseInt(currPlayingMusic.getCountry()) > 999 ? "999+" : currPlayingMusic.getCountry());

        binding.dashang.setOnClickListener(v -> {
            if (dashangDialog != null) {
                dashangDialog.dismiss();
                dashangDialog = null;
            }

            dashangDialog = new RewardDialog();
            dashangDialog.setListener(this);
            dashangDialog.show(getActivity().getSupportFragmentManager(), "dashang");
        });
        ViewOutlineProvider vop = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                //修改outline
                outline.setOval(0, 0, view.getWidth(), view.getHeight());
            }
        };

        binding.cover.setOutlineProvider(vop);

        d2 = RxBus.getDefault().toObservable(PayDialog.PayBean.class).subscribe(v -> {
            if (v.type == 0) {
                dashang(v.money);
            } else {
                ToastUtil.toastBottom("暂不支持，请选择其他支付方式");
            }


        });

        binding.etComment.setOnClickListener(view1 -> {
            if (commnetDialog != null) {
                commnetDialog.dismiss();
                commnetDialog = null;
            }
            commnetDialog = new CommentDialog();
            Bundle bundle = new Bundle();
            bundle.putString("type", "audio");
            commnetDialog.setArguments(bundle);
            commnetDialog.show(getActivity().getSupportFragmentManager(), "comment");
        });
        d3 = RxBus.getDefault().toObservable(CommentChangeUpdateEvent.class).subscribe(commentChangeUpdateEvent -> {
            commentDatas.remove(commentChangeUpdateEvent.position);
            commentDatas.add(commentChangeUpdateEvent.position, commentChangeUpdateEvent.listBean);
            adapter.notifyDataSetChanged();
        });
        d = RxBus.getDefault().toObservable(CommentDialog.CommentAudioBean.class).subscribe(v -> {
            HashMap<String, Object> params = new HashMap();
            if (getActivity() == null) {
                return;
            }
            params.put("id", currPlayingMusic.getArtistId());
            params.put("content", v.comment);
            params.put("access_token", SPUtils.getInstance().getString("token"));

            io.reactivex.Observable<CommonResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                    .add_audio_comment(FormdataRequestUtil.getFormDataRequestParams(params))
                    .compose(RxUtils.bindToLifecycle(this)) //请求与View周期同步
                    .compose(RxUtils.schedulersTransformer()) //线程调度
                    .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                    .doOnSubscribe(disposable -> {
                    });
            observable.subscribe(res -> {
                if (res.getStatus() == 1) {
                    if (commnetDialog != null) {
                        commnetDialog.dismiss();
                    }
                    ToastUtil.toastBottom("评论成功");
                } else {
                    ToastUtil.toastBottom(res.getInfo());
                }

            }, e -> {
                e.printStackTrace();
            });

        });
        binding.twinklingRefreshLayout.setEnableRefresh(false);
        binding.twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                pageNum++;
                getConmments(false);
            }
        });
        binding.rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CommentAdapter(getActivity(), commentDatas);
        adapter.setOnCommentClickListener(new CommentAdapter.OnCommentClickListener() {
            @Override
            public void onZanClick(int position) {
                upClick(commentDatas.get(position));
            }

            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), AudioCommentReplyActivity.class);
                CommentResp.DataBean.ListBean listBean = commentDatas.get(position);
                intent.putExtra("comment", listBean);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        binding.rv.setAdapter(adapter);
        getConmments(true);
    }

    @SuppressLint("CheckResult")
    public void dashang(double currMoney) {
        HashMap<String, Object> params = new HashMap();

        params.put("id", MusicManager.get().getCurrPlayingMusic().getArtistId());
        DecimalFormat format = new DecimalFormat("0.00");
        params.put("money", format.format(currMoney));
        params.put("access_token", SPUtils.getInstance().getString("token"));
//        params.put("page", this.pageNum);
        io.reactivex.Observable<CommonPayResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .shang(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(getContext())) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(v -> {
            if (v.getStatus() == 1) {
                PayReq request = new PayReq();
                request.appId = v.getData().getAppid();
                request.partnerId = WeixinCons.PARTNER_ID;
                request.prepayId = v.getData().getPrepayid();
                request.packageValue = "Sign=WXPay";
                request.nonceStr = v.getData().getNoncestr();
                request.timeStamp = v.getData().getTimestamp();
                request.sign = v.getData().getSign();
                AppApplication.mWxApi.sendReq(request);
            } else {

            }


        }, e -> {
            e.printStackTrace();
        });

    }

    /**
     * 显示播放列表
     */
    void bottomWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //列表
        LinearLayout layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.pop_audio_play_list, null);
        final RecyclerView recyclerView = layout.findViewById(R.id.list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        final ImageTextButton itb = layout.findViewById(R.id.tv_reverse);
        layoutManager.setStackFromEnd(isReverseList);
        layoutManager.setReverseLayout(isReverseList);//列表翻转
        recyclerView.setLayoutManager(layoutManager);
        playingListAdapter = new AudioPlayingListAdapter(getContext(), MusicManager.get().getPlayList());
        playingListAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(playingListAdapter);

        //点击列表的取消
        layout.findViewById(R.id.cancel).setOnClickListener(view -> {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        });
        layout.findViewById(R.id.tv_reverse).setOnClickListener(view -> {
            isReverseList = !isReverseList;
            layoutManager.setStackFromEnd(isReverseList);
            layoutManager.setReverseLayout(isReverseList);//列表翻转
            recyclerView.setAdapter(playingListAdapter);
            if (isReverseList) {
                itb.setButtonText("倒序");
                itb.setIcon(R.mipmap.desc);
            } else {
                itb.setButtonText("正序");
                itb.setIcon(R.mipmap.asc);
            }
        });


        popupWindow = new PopupWindow(layout,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //添加弹出、弹入的动画
        popupWindow.setAnimationStyle(R.style.ListenPopupWindow);
        int[] location = new int[2];
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
        binding.getRoot().getLocationOnScreen(location);
        popupWindow.setOnDismissListener(() -> {
            WindowManager.LayoutParams lp1 = getActivity().getWindow().getAttributes();
            lp1.alpha = 1f;
            getActivity().getWindow().setAttributes(lp1);
        });
        popupWindow.showAtLocation(binding.getRoot(), Gravity.LEFT | Gravity.BOTTOM, 0, -location[1]);
    }

    RequestOptions myOptions = new RequestOptions()
            .fitCenter()
            .dontAnimate();

    @Override
    public void onResume() {
        super.onResume();
        if (MusicManager.get().isPlaying()) {
            binding.playingPlay.setImageResource(R.mipmap.pause);
            binding.cover.startAnimation(playAnim);
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        binding.playSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                MusicManager.get().seekTo(seekBar.getProgress());
            }
        });
        viewModel.mObservableSongInfo.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                AudioInfo audioInfo = viewModel.mObservableSongInfo.get();
                binding.title.setText(audioInfo.getSongName());
                binding.mainTitle.setText(audioInfo.getArtist());
                binding.playSeek.setMax((int) audioInfo.getDuration());
                binding.collectNum.setText(audioInfo.getGenre() + "人订阅");
                binding.musicDuration.setText(FormatUtil.formatMusicTime(audioInfo.getDuration()));
                Glide.with(getContext())
                        .load(audioInfo.getSongCover())
                        .apply(myOptions)
                        .into(binding.cover);
                Glide.with(getContext())
                        .load(audioInfo.getSongCover())
                        .apply(myOptions)
                        .into(binding.subImage);
                binding.title2.setText(audioInfo.getArtist());
                //切换音频时如果正在显示列表则更新正在播放音频的样式
                if (popupWindow != null && popupWindow.isShowing()) {
                    if (playingListAdapter != null) {
                        playingListAdapter.update(MusicManager.get().getPlayList());
                    }
                }
            }
        });
        viewModel.observableProgress.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                long progress = viewModel.observableProgress.get();
                binding.playSeek.setProgress((int) progress);
                binding.musicDurationPlayed.setText(FormatUtil.formatMusicTime(progress));
            }
        });
        viewModel.observableProgressBuffer.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                binding.playSeek.setSecondaryProgress((int) viewModel.observableProgressBuffer.get());
            }
        });
        viewModel.onClickList.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                bottomWindow();
            }
        });

        viewModel.collectState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                try {
                    AudioHistory audioHistory = MusicDatabase.getInstance(getContext()).audioHistoryDao().listAudioHistory().get(0);
                    Log.e("收藏状态", "onPropertyChanged: " + audioHistory.getIsCollect());
                    if (audioHistory.getIsCollect() == 1) {
                        binding.actionCollect.setTextColor(Color.parseColor("#F2504E"));
                        binding.actionCollect.setText("已订阅");
                        binding.actionCollect.setBackgroundResource(R.drawable.filled_round_corner_red_bg_stroke);
                    } else {
                        binding.actionCollect.setTextColor(Color.WHITE);
                        binding.actionCollect.setText("订阅");
                        binding.actionCollect.setBackgroundResource(R.drawable.filled_round_corner_red_bg);
                    }
                } catch (Exception e) {
                }
            }
        });

        viewModel.zanState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                try {
                    AudioHistory audioHistory = MusicDatabase.getInstance(getContext()).audioHistoryDao().listAudioHistory().get(0);
                    if (audioHistory.getIsZan() == 1) {
                        Drawable top = getResources().getDrawable(R.mipmap.collection_selected);
                        binding.zan.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                        binding.zan.setText(FormatUtil.formatNum(audioHistory.getZanNum() + ""));
                    } else {
                        Drawable top = getResources().getDrawable(R.mipmap.collection);
                        binding.zan.setCompoundDrawablesWithIntrinsicBounds(null, top, null, null);
                        binding.zan.setText(FormatUtil.formatNum(audioHistory.getZanNum() + ""));
                    }
                } catch (Exception e) {
                }
            }
        });


        viewModel.playState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                int i = viewModel.playState.get();

                switch (i) {
                    case MusicManager.MSG_MUSIC_CHANGE:
                        binding.playingPlay.setImageResource(R.mipmap.pause);
                        binding.cover.startAnimation(playAnim);
                        break;
                    case MusicManager.MSG_PLAYER_START:
                        binding.playingPlay.setImageResource(R.mipmap.pause);
                        binding.cover.startAnimation(playAnim);
                        break;
                    case MusicManager.MSG_PLAYER_PAUSE:
                        binding.playingPlay.setImageResource(R.mipmap.icon_audio_play);
                        binding.cover.clearAnimation();
                        break;
                    case MusicManager.MSG_PLAY_COMPLETION:
                        binding.playingPlay.setImageResource(R.mipmap.icon_audio_play);
                        binding.cover.clearAnimation();
                        break;
                    case MusicManager.MSG_PLAYER_STOP:
                        binding.playingPlay.setImageResource(R.mipmap.icon_audio_play);
                        binding.cover.clearAnimation();
                    case MusicManager.MSG_PLAYER_ERROR:
                        binding.playingPlay.setImageResource(R.mipmap.icon_audio_play);
                        binding.cover.clearAnimation();
                        break;
                    case MusicManager.MSG_BUFFERING:
                        binding.playingPlay.setImageResource(R.mipmap.pause);
                        break;

                    default:
                        break;
                }
            }
        });
        viewModel.commentNum.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                binding.tvComment.setText(viewModel.commentNum.get() + "");
            }
        });
    }

    @Override
    public void onItemClick(AudioInfo audioInfo) {
        if (!"none".equals(audioInfo.getType())) {
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
            viewModel.showPayPop(Double.parseDouble(audioInfo.getType()));
            return;
        }
        MusicManager musicManager = MusicManager.get();
        if (!musicManager.getCurrPlayingMusic().getSongId().equals(audioInfo.getSongId())) {
            List<AudioInfo> playList = musicManager.getPlayList();
            for (int i = 0; i < playList.size(); i++) {
                AudioInfo ai = playList.get(i);
                if (audioInfo.getSongId().equals(ai.getSongId())) {
                    musicManager.playMusicByIndex(i);
                    setPlayProgress(ai.getSongId(), musicManager);
                }
            }
        }

    }

    private void setPlayProgress(String audioId, MusicManager musicManager) {
        AudioPlayProgress audioPlayProgressById = MusicDatabase.getInstance(getContext().getApplicationContext()).audioPlayProgressDao().getAudioPlayProgressById(audioId);
        if (audioPlayProgressById != null) {
            musicManager.seekTo(audioPlayProgressById.getProgerss());
        }
    }

    @Override
    public boolean onBackPressed() {

        return super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (d != null) {
            d.dispose();
            d = null;
        }
        if (d2 != null) {
            d2.dispose();
            d2 = null;
        }
        if (d3 != null) {
            d3.dispose();
            d3 = null;
        }
    }


    @Override
    public void onRewardBeanConfirm(RewardDialog.RewardBean rewardBean) {
        if (payDialog != null) {
            payDialog.dismiss();
            payDialog = null;
        }
        payDialog = new PayDialog();
        Bundle bundle = new Bundle();
        bundle.putDouble("price", rewardBean.money);
        bundle.putString("id", MusicManager.get().getCurrPlayingMusic().getArtistId());
        payDialog.setArguments(bundle);
        payDialog.show(getChildFragmentManager(), "pay");
    }

    /*****************************************评论**************************************************/

    int pageNum = 0;

    @SuppressLint("CheckResult")
    public void getConmments(boolean isRefresh) {
        HashMap<String, String> para = new HashMap<>();
        para.put("appkey", HttpCons.APP_KEY);
        para.put("access_token", SPUtils.getInstance().getString("token"));
        MusicManager musicManager = MusicManager.get();
        String id = musicManager.getCurrPlayingMusic().getArtistId();
        para.put("id", id);
        if (!TextUtils.isEmpty(musicManager.getCurrPlayingMusic().getSongId())) {
            para.put("albumid", musicManager.getCurrPlayingMusic().getSongId());
        }
        para.put("page", "" + pageNum);

        String sig = FormUtil.genSig(para);
        para.put("sig", sig);
        io.reactivex.Observable<CommentResp> observable = RetrofitClient.getInstance().create(com.tchy.syh.listen.ApiService.class)
                .get_learn_comment(para)
                .compose(RxUtils.bindToLifecycle(this)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {
            binding.twinklingRefreshLayout.finishLoadmore();
            binding.twinklingRefreshLayout.finishRefreshing();
            if (res.getStatus() != 1) {
                ToastUtil.toastBottom(res.getInfo());
                return;
            }
            if (isRefresh) {
                commentDatas.clear();
            }
            commentDatas.addAll(res.getData().getList());
            adapter.notifyDataSetChanged();
        }, e -> {


        });
    }

    private void upClick(CommentResp.DataBean.ListBean listBean) {
        HashMap<String, Object> params = new HashMap();
        params.put("comment_id", listBean.getId());
        params.put("access_token", SPUtils.getInstance().getString("token"));
        io.reactivex.Observable<CommentLoveResp> observable = RetrofitClient.getInstance().create(com.tchy.syh.book.book_detail.ApiService.class)
                .learn_commentlove(FormdataRequestUtil.getFormDataRequestParams(params))
                .delay(500, TimeUnit.MILLISECONDS)
                .compose(RxUtils.bindToLifecycle(this)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(v -> {
            if (v.getStatus() == 1) {
                listBean.setIs_love(v.getData().getIs_add());
                listBean.setLovenum(v.getData().getNum());
                adapter.notifyDataSetChanged();
            }
            ToastUtil.toastBottom(v.getInfo());
        });
    }
}
