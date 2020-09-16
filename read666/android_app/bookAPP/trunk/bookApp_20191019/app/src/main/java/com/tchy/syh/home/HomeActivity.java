package com.tchy.syh.home;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.addresspicker.huichao.addresspickerlibrary.CityPickerDialog;
import com.addresspicker.huichao.addresspickerlibrary.InitAreaTask;
import com.addresspicker.huichao.addresspickerlibrary.address.City;
import com.addresspicker.huichao.addresspickerlibrary.address.County;
import com.addresspicker.huichao.addresspickerlibrary.address.Province;
import com.bumptech.glide.Glide;
import com.lzx.musiclibrary.manager.MusicManager;
import com.tchy.syh.BR;
import com.tchy.syh.BuildConfig;
import com.tchy.syh.R;
import com.tchy.syh.book.ApiService;
import com.tchy.syh.book.book_home.BookFragment;
import com.tchy.syh.book.entity.VerionBean;
import com.tchy.syh.custom.share.ShareDialogBlurView;
import com.tchy.syh.daily_advance.daily_home.DailyFragment;
import com.tchy.syh.databinding.HomeActivityBinding;
import com.tchy.syh.listen.database.AlbumPlayProgress;
import com.tchy.syh.listen.database.AudioHistory;
import com.tchy.syh.listen.database.MusicDatabase;
import com.tchy.syh.my.MyFragment;
import com.tchy.syh.read.home.ReadListFragment;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;

/**
 * 使用的框架是MVVM框架
 *
 */
public class HomeActivity extends BaseActivity<HomeActivityBinding, HomeViewModel> implements InitAreaTask.onLoadingAddressListener {

    ArrayList<BaseFragment> fragments = new ArrayList<>(); //存放Fragment的List
    private Animation playAnim;  //播放动画

    //initContentView
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.home_activity;
    }

    @Override
    public int initVariableId() {
        return BR.vm;       //获取View的id
    }

    @Override
    public HomeViewModel initViewModel() {
        return new HomeViewModel(this);  //生成一个ViewModel
    }

    @Override
    public void initViewObservable() {

        playAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_play);
        LinearInterpolator lin = new LinearInterpolator();
        playAnim.setInterpolator(lin);

        //监听下拉刷新完成
        viewModel.vpIndex.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                binding.vp.setCurrentItem(viewModel.vpIndex.get(), false);

            }
        });
        viewModel.playState.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                updatePlayState();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePlayState();
    }

    private void updatePlayState() {
        MusicManager musicManager = MusicManager.get();
        if (musicManager.isPlaying()) {
            String songCover = musicManager.getCurrPlayingMusic().getSongCover();
            Glide.with(getApplicationContext())
                    .load(songCover)
                    .into(binding.btnPlay);
            binding.icPlay.setVisibility(View.INVISIBLE);
            binding.btnPlay.startAnimation(playAnim);
        } else {
            binding.icPlay.setVisibility(View.VISIBLE);
            binding.btnPlay.clearAnimation();
            MusicDatabase instance = MusicDatabase.getInstance(getApplicationContext());
            List<AudioHistory> audioHistories = instance.audioHistoryDao().listAudioHistory();
            if (audioHistories != null && !audioHistories.isEmpty()) {
                AlbumPlayProgress playProgress = instance.albumPlayProgressDao().getAlbumPlayProgressById(audioHistories.get(0).getAlbumId());
                if (playProgress.getProgerss() > audioHistories.size()) {
                    return;
                }
                try {
                    Glide.with(getApplicationContext())
                            .load(audioHistories.get(playProgress.getProgerss()).getAudioCover())
                            .into(binding.btnPlay);
                } catch (Exception e) {

                }

            } else {
                binding.icPlay.setVisibility(View.GONE);
            }
        }
    }

    ShareDialogBlurView shareDialogBlurView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        me.goldze.mvvmhabit.bus.Messenger.getDefault().register(this, "share", new BindingAction() {
            @Override
            public void call() {
                shareDialogBlurView = new ShareDialogBlurView(HomeActivity.this);
                ((ViewGroup) binding.getRoot()).addView(shareDialogBlurView);
                shareDialogBlurView.setVisibility(View.VISIBLE);
            }
        });

        SPUtils.getInstance().put("isFirstStartup", false);
        fragments.add(new BookFragment());
        //fragments.add(new BookListFragment());
        fragments.add(new ReadListFragment());

        fragments.add(new DailyFragment());
        fragments.add(new MyFragment());
        this.binding.vp.setScanScroll(false);
        this.binding.vp.setOffscreenPageLimit(4);
        this.binding.vp.setAdapter(new HomePagerAdapter(this.getSupportFragmentManager()));
        this.binding.vp.setCurrentItem(0);
        if (provinces == null) {
            provinces = new ArrayList<>();
        }
        HashMap map=new HashMap<>();

        io.reactivex.Observable<VerionBean> observable = RetrofitClient.getInstance().create(ApiService.class)
                .check_version(FormdataRequestUtil.getFormDataRequestParams(map))
                .compose(RxUtils.bindToLifecycle(this)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer());// 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
        observable.subscribe(o -> {
//                    BuildConfig.VERSION_CODE

                    if( Integer.parseInt(o.getData().getVersionCode())>BuildConfig.VERSION_CODE){
                        showUpdate(o);
                    }
        }, throwable -> ToastUtil.toastBottom(((Throwable) throwable).getMessage()));


    }
    public void showUpdate(VerionBean bean){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("更新提示");

        builder.setMessage(bean.getData().getVersionName()+"\n"+bean.getData().getVersionDesc());
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    viewModel.downloadFile(bean.getData().getDownLink());
            }
        });
        builder.create().show();
    }

    public class HomePagerAdapter extends FragmentPagerAdapter {

        public HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public BaseFragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    public long firstTime = 0;

    @Override
    public void onBackPressed() {
        if (shareDialogBlurView != null && shareDialogBlurView.getVisibility() == View.VISIBLE) {
            shareDialogBlurView.setVisibility(View.GONE);
            ((ViewGroup) binding.getRoot()).removeView(shareDialogBlurView);
            shareDialogBlurView = null;
            return;
        }
        long currTime = new Date().getTime();
        if (currTime - firstTime <= 2000) {
//            this.finishAffinity();
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);

        } else {
            ToastUtil.toastBottom("再次按下返回键回到桌面");
            firstTime = new Date().getTime();
        }
    }

    private ArrayList<Province> provinces;
    private Dialog progressDialog;

    private void showAddressDialog() {//初始化地址数据后，显示地址选择框
        new CityPickerDialog(HomeActivity.this, provinces, null, null, null,
                new CityPickerDialog.onCityPickedListener() {

                    @Override
                    public void onPicked(Province selectProvince,
                                         City selectCity, County selectCounty) {
                        StringBuilder address = new StringBuilder();
                        address.append(
                                selectProvince != null ? selectProvince
                                        .getAreaName() : "")
                                .append(selectCity != null ? selectCity
                                        .getAreaName() : "");
                        if (selectCounty != null) {
                            String areaName = selectCounty.getAreaName();
                            if (areaName != null) {
                                address.append(areaName);
                            }
                        }
                        Toast.makeText(HomeActivity.this, address.toString(), Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    @Override
    public void onLoadFinished() {//加载完成的监听
        showAddressDialog();
    }

    @Override
    public void onLoading() {//加载中监听

    }
}
