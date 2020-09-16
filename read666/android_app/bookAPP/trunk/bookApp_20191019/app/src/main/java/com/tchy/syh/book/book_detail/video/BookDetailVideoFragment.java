package com.tchy.syh.book.book_detail.video;

import androidx.databinding.Observable;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.lzx.musiclibrary.manager.MusicManager;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.app.AppApplication;
import com.tchy.syh.book.book_detail.ApiService;
import com.tchy.syh.book.book_detail.BookVideoPlayerActivity;
import com.tchy.syh.book.book_detail.video.comment.BookDetailVideoCommentFragment;
import com.tchy.syh.book.book_detail.video.desp.BookDetailVideoDespFragment;
import com.tchy.syh.common.entity.CommonDataListResp;
import com.tchy.syh.common.entity.CommonPayResp;
import com.tchy.syh.common.entity.CommonResp;
import com.tchy.syh.cons.WeixinCons;
import com.tchy.syh.databinding.BookDetailVideoBinding;
import com.tchy.syh.dialog.CommentDialog;
import com.tchy.syh.dialog.DashangDialog;
import com.tchy.syh.dialog.PayDialog;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.qqtheme.framework.util.ScreenUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;


public class BookDetailVideoFragment extends BaseFragment<BookDetailVideoBinding, BookDetailVideoPageVm> {
    public static int LIMIT_DURATION = 13 * 60 * 1000;

    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.book_detail_video;
    }

    public void learn_time() {
        HashMap<String, Object> params = new HashMap();
        params.put("access_token", SPUtils.getInstance().getString("token"));
        io.reactivex.Observable<CommonDataListResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .learn_time(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(getContext())) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(v -> {
            Log.d("sort", "learn_time: ");
        }, e -> {
        });

    }

    public void wancheng(String type) {
        HashMap<String, Object> params = new HashMap();
        params.put("m", "wancheng");
        params.put("type", type);
        params.put("access_token", SPUtils.getInstance().getString("token"));
        io.reactivex.Observable<CommonDataListResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .task(FormdataRequestUtil.getFormDataRequestParams(params))
                .delay(500, TimeUnit.MILLISECONDS)
                .compose(RxUtils.bindToLifecycle(this)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {
        });
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public BookDetailVideoPageVm initViewModel() {
        return new BookDetailVideoPageVm(this.getContext());
    }

    //暂停播放
    public void pausePlayer() {
        if (binding == null) {
            return;
        }
        if (binding.player != null) {
            if (binding.player.getGSYVideoManager().isPlaying())
                binding.player.getGSYVideoManager().pause();
            isStudy = false;
        }
    }
    //停止播放
    public void releasePlayer() {
        if (binding == null) {
            return;
        }
        if (binding.player != null) {
            if (binding.player.getGSYVideoManager().isPlaying()) {
                binding.player.getGSYVideoManager().stop();
                isStudy = false;
            }
            binding.player.onVideoReset();
            binding.player.getGSYVideoManager().releaseMediaPlayer();
        }

    }

    OrientationUtils orientationUtils;
    BookDetailVideoCommentFragment commentFragment;
    BookDetailVideoDespFragment despFragment;
    CommentDialog commnetDialog;
    DashangDialog dashangDialog;
    PayDialog payDialog;
    Disposable d;
    public int currentPosition = 0;

    public boolean isStudy = false;
    Disposable timer;

    public void startTimer() {
        if (timer != null && !timer.isDisposed()) {
            timer.dispose();
            timer = null;
        }
        timer = io.reactivex.Observable.interval(1, 30, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> {
                    if (isStudy)
                        learn_time();
                });
    }

    @Override
    public void onResume() {
        startTimer();
        super.onResume();

    }

    List<Fragment> fragments = new ArrayList<>();
    boolean isDashangPay = false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Messenger.getDefault().register(this.getContext(), "yiyuanMobileBindingSuccess", new BindingAction() {
            @Override
            public void call() {
                isDashangPay = false;
                if (payDialog != null) {
                    payDialog.dismiss();
                    payDialog = null;
                }
                payDialog = new PayDialog();
                Bundle bundle = new Bundle();
                bundle.putDouble("price", 1);
                bundle.putString("id", id);
                payDialog.setArguments(bundle);
                payDialog.show(getChildFragmentManager(), "yiyuan");

            }
        });

//        ImageView imageView = new ImageView(getContext());
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageResource(R.mipmap.account);
//        binding.player.setThumbImageView(imageView);

        binding.bottom.setOnClickListener(v -> {

        });
        binding.etComment.setOnClickListener(v -> {
            if (commnetDialog != null) {
                commnetDialog.dismiss();
                commnetDialog = null;
            }

            commnetDialog = new CommentDialog();
            Bundle bundle = new Bundle();
            bundle.putString("type", "book");
            commnetDialog.setArguments(bundle);
            commnetDialog.show(getActivity().getSupportFragmentManager(), "comment");
        });
        binding.dashang.setOnClickListener(v -> {
            if (dashangDialog != null) {
                dashangDialog.dismiss();
                dashangDialog = null;
            }

            dashangDialog = new DashangDialog();
            dashangDialog.show(getActivity().getSupportFragmentManager(), "dashang");
        });

        RxBus.getDefault().toObservable(DashangDialog.DashangBean.class).subscribe(v -> {
            isDashangPay = true;
            if (payDialog != null) {
                payDialog.dismiss();
                payDialog = null;
            }
            payDialog = new PayDialog();
            Bundle bundle = new Bundle();
            bundle.putDouble("price", v.money);
            bundle.putString("id", id);
            payDialog.setArguments(bundle);
            payDialog.show(getChildFragmentManager(), "pay");
        });
        RxBus.getDefault().toObservable(PayDialog.PayBean.class).subscribe(v -> {
            if (isDashangPay) {
                if (v.type == 0) {
                    dashang(v.money);
                } else {
                    ToastUtil.toastBottom("暂不支持，请选择其他支付方式");
                }
            } else {
                if (v.type == 0) {
                    yiyuan(v.money);
                } else {
                    ToastUtil.toastBottom("暂不支持，请选择其他支付方式");
                }
            }


        });

        binding.player.setVideoAllCallBack(new VideoAllCallBack() {
            @Override
            public void onStartPrepared(String url, Object... objects) {
                MusicManager.get().pauseMusic();
                wancheng("see");
            }

            @Override
            public void onPrepared(String url, Object... objects) {

            }

            @Override
            public void onClickStartIcon(String url, Object... objects) {
                isStudy = true;
            }

            @Override
            public void onClickStartError(String url, Object... objects) {
                isStudy = false;
            }

            @Override
            public void onClickStop(String url, Object... objects) {
                isStudy = false;
            }

            @Override
            public void onClickStopFullscreen(String url, Object... objects) {
                isStudy = false;

            }

            @Override
            public void onClickResume(String url, Object... objects) {
                MusicManager.get().pauseMusic();
                isStudy = true;
            }

            @Override
            public void onClickResumeFullscreen(String url, Object... objects) {
                isStudy = true;
            }

            @Override
            public void onClickSeekbar(String url, Object... objects) {

            }

            @Override
            public void onClickSeekbarFullscreen(String url, Object... objects) {

            }

            @Override
            public void onAutoComplete(String url, Object... objects) {

            }

            @Override
            public void onEnterFullscreen(String url, Object... objects) {
                GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_16_9);
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);

            }

            @Override
            public void onQuitSmallWidget(String url, Object... objects) {

            }

            @Override
            public void onEnterSmallWidget(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekVolume(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekPosition(String url, Object... objects) {

            }

            @Override
            public void onTouchScreenSeekLight(String url, Object... objects) {

            }

            @Override
            public void onPlayError(String url, Object... objects) {
                isStudy = false;
            }

            @Override
            public void onClickStartThumb(String url, Object... objects) {
                isStudy = true;
            }

            @Override
            public void onClickBlank(String url, Object... objects) {

            }

            @Override
            public void onClickBlankFullscreen(String url, Object... objects) {

            }
        });
        d = RxBus.getDefault().toObservable(CommentDialog.CommentBookBean.class).subscribe(v -> {
            HashMap<String, Object> params = new HashMap();
            if (getActivity() == null) {
                return;
            }
            params.put("id", ((BookVideoPlayerActivity) getActivity()).book_id);
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
        int width = ScreenUtils.widthPixels(getContext());
        int height = width * 9 / 16;
        //设置播放的宽高
        binding.player.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        //调整触摸滑动快进的比例
        binding.player.setSeekRatio(100);
        //返回按键 不可见
        binding.player.getBackButton().setVisibility(View.GONE);
        //视频标题不可见
        binding.player.getTitleTextView().setVisibility(View.GONE);
        //设置横竖屏幕
        orientationUtils = new OrientationUtils(getActivity(), binding.player);
        /**
         * 进度回调
         */
        binding.player.setGSYVideoProgressListener(new GSYVideoProgressListener() {
            @Override
            public void onProgress(int progress, int secProgress, int currentPosition, int duration) {
                if (currentPosition > 0) {
                    if (!isUpdate)
                        api();
                }
//                viewModel.isShowVip.set(true);
                BookDetailVideoFragment.this.currentPosition = currentPosition;
                if (currentPosition > LIMIT_DURATION) {
                    if (viewModel.dataBean.get().getRank() == null) {
                        viewModel.dataBean.get().setRank("0");
                    }
                    if (Integer.parseInt(viewModel.dataBean.get().getRank()) > SPUtils.getInstance().getInt("level")) {
                        pausePlayer();
                        viewModel.isShowVip.set(true);
                    }
                }

            }
        });
        binding.player.setNeedShowWifiTip(true); //是否显示wifi标签
        binding.player.setRotateViewAuto(true);  //
        binding.player.setAutoFullWithSize(true);   //自动适应屏幕
        binding.player.setShowPauseCover(false);

        binding.player.setBottomShowProgressBarDrawable(getContext().getDrawable(R.drawable.progress_style), getContext().getDrawable(R.drawable.video_new_seekbar_thumb));
        binding.player.setBottomProgressBarDrawable(getContext().getDrawable(R.drawable.progress_style));
//        File cache=getContext().getExternalCacheDir().getPath()+"/video-cache";
//        ToastUtils.showLong(cache.get);
        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏\
        binding.player.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.player.startWindowFullscreen(getContext(), false, true);
            }
        });


//        binding.player.startPlayLogic();
//        binding.vp.setOffscreenPageLimit(3);

        commentFragment = new BookDetailVideoCommentFragment();

        despFragment = new BookDetailVideoDespFragment();
        fragments.add(despFragment);
        fragments.add(commentFragment);


//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.container, despFragment, "desp");
//        transaction.add(R.id.container, commentFragment, "comment");
//        transaction.hide(commentFragment);
//        transaction.show(despFragment);
//        transaction.commit();

        Messenger.getDefault().register(getContext(), "comment_count", CommentCountBean.class, new BindingConsumer<CommentCountBean>() {
            @Override
            public void call(CommentCountBean s) {
                if(s.getId().equals(((BookVideoPlayerActivity)getActivity()).book_id))
                    viewModel.commentCount.set(s.getCount());

            }
        });

        binding.vp.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
        binding.vp.setCurrentItem(0);
        binding.vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    binding.rg.check(R.id.desp);
                }else{
                    binding.rg.check(R.id.comment);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        binding.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switchFragment();
                if (group.getChildAt(0).getId() == checkedId)
                    binding.vp.setCurrentItem(0, true);
                else{
                    binding.vp.setCurrentItem(1, true);
                }

            }
        });

    }

    public void switchFragment() {
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        if (commentFragment.isHidden()) {
//            transaction.setCustomAnimations(R.anim.right_enter, R.anim.left_leave);
//            transaction.show(commentFragment);
//            transaction.hide(despFragment);
//        } else if (despFragment.isHidden()) {
//            transaction.setCustomAnimations(R.anim.left_enter, R.anim.right_leave);
//
//            transaction.show(despFragment);
//            transaction.hide(commentFragment);
//        }
//        transaction.commit();
    }

    public String id = "";
    public String rank = "";

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.dataBean.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String source1 = viewModel.dataBean.get().getDefault_video();
                rank = viewModel.dataBean.get().getRank();
                File file = getContext().getExternalCacheDir();
                File videoCacheDir = new File(file.getPath() + File.separator + "video");
                if (!videoCacheDir.exists()) {
                    videoCacheDir.mkdir();
                }
                binding.player.setUp(source1, true, videoCacheDir, "");
                id = viewModel.dataBean.get().getId();
                long currProgress = SPUtils.getInstance().getInt(VIDEO_TAG + viewModel.dataBean.get().getId(), 0);
                binding.player.setSeekOnStart(currProgress);

            }
        });
    }

    public static final String VIDEO_TAG = "v_";

    @Override
    public void onStop() {
        SPUtils.getInstance().put(VIDEO_TAG + id, currentPosition);
        timer.dispose();
        timer = null;
        super.onStop();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (d != null) {
            d.dispose();
        }
        RxBus.getDefault().reset();
        releasePlayer();

    }

    public void yiyuan(double currMoney) {
        HashMap<String, Object> params = new HashMap();

        params.put("m", "yiyuan");
        params.put("access_token", SPUtils.getInstance().getString("token"));
//        params.put("page", this.pageNum);
        io.reactivex.Observable<CommonPayResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .buyvip(FormdataRequestUtil.getFormDataRequestParams(params))
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

    public void dashang(double currMoney) {
        HashMap<String, Object> params = new HashMap();

        params.put("id", id);
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

    boolean isUpdate = false;

    public void api() {
        if (isUpdate) {
            return;
        }
        isUpdate = true;
        HashMap<String, Object> params = new HashMap();

        params.put("m", "add");
        params.put("id", id);
        params.put("access_token", SPUtils.getInstance().getString("token"));

        io.reactivex.Observable<CommonDataListResp> observable = RetrofitClient.getInstance().create(com.tchy.syh.history.ApiService.class)
                .mHistory_list(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(getContext())) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {

        }, e -> {

            e.printStackTrace();
        });
    }

}
