package com.tchy.syh.listen.detail;

import android.annotation.SuppressLint;
import androidx.databinding.Observable;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.app.AppApplication;
import com.tchy.syh.cons.HttpCons;
import com.tchy.syh.cons.WeixinCons;
import com.tchy.syh.databinding.FragmentAudioDetailBinding;
import com.tchy.syh.listen.ApiService;
import com.tchy.syh.listen.entity.AudioDetailEntity;
import com.tchy.syh.listen.entity.CollectEvent;
import com.tchy.syh.listen.entity.CollectResult;

import com.tchy.syh.listen.entity.PayAudioEvent;
import com.tchy.syh.listen.entity.PayInfo;
import com.tchy.syh.listen.entity.ResponseWrapper;

import com.tchy.syh.listen.view.PayDialogWithPhone;
import com.tchy.syh.utils.FormUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.HashMap;
import java.util.Map;


import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;


public class AudioDetailFragment extends BaseFragment<FragmentAudioDetailBinding, AudioDetailViewModel> implements PayDialogWithPhone.OnConfirmListener {
    private AudioDetailPagerAdapter mAdapter;

    //private ListenEntity.ListenItemEntity mEntity;
    private AudioDetailEntity detailEntity;
    private PayDialogWithPhone payDialog;
    private Disposable disposable;

    private Disposable d2;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        disposable = RxBus.getDefault().toObservable(PayAudioEvent.class).subscribe(payAudioEvent -> {
            showPayPop(payAudioEvent.money);
        });
        d2 = RxBus.getDefault().toObservable(CollectEvent.class).subscribe(collectEvent -> {
            binding.itbCollect.setIcon(collectEvent.state == 0 ? R.mipmap.star_collect : R.mipmap.star_collectted);
            //binding.itbCollect.setButtonText("" + collectEvent.num);
            binding.itbCollect.setButtonTextColor(collectEvent.state == 0 ? Color.BLACK : Color.parseColor("#F2504E"));
        });
    }

    private void showPayPop(double money) {
        payDialog = new PayDialogWithPhone();
        payDialog.setOnConfirmListener(this);
        Bundle bundle = new Bundle();
        bundle.putDouble("price", money);
        payDialog.setArguments(bundle);
        payDialog.show(this.getChildFragmentManager(), "");
    }

    @SuppressLint("CheckResult")
    private void payByWeChat(String phone) {
        Map<String, String> param = new HashMap<>();
        param.put("appkey", HttpCons.APP_KEY);
        param.put("access_token", SPUtils.getInstance().getString("token"));
        param.put("id", detailEntity.getId());
        param.put("mobile", phone);
        String sig = FormUtil.genSig(param);
        param.put("sig", sig);
        io.reactivex.Observable<ResponseWrapper<PayInfo>> observable = RetrofitClient.getInstance().create(ApiService.class)
                .learn_buy(param)
                .compose(RxUtils.bindToLifecycle(this)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer());// 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
        observable.subscribe(payInfoResponseWrapper -> {
            if (payInfoResponseWrapper.isSuccess()) {
                PayReq request = new PayReq();
                request.appId = payInfoResponseWrapper.getData().getAppid();
                request.partnerId = WeixinCons.PARTNER_ID;
                request.prepayId = payInfoResponseWrapper.getData().getPrepayid();
                request.packageValue = "Sign=WXPay";
                request.nonceStr = payInfoResponseWrapper.getData().getNoncestr();
                request.timeStamp = payInfoResponseWrapper.getData().getTimestamp();
                request.sign = payInfoResponseWrapper.getData().getSign();
                AppApplication.mWxApi.sendReq(request);
            } else {
                ToastUtil.toastBottom(payInfoResponseWrapper.getInfo());
            }
        }, throwable -> {
            ToastUtil.toastBottom(throwable.getMessage());
        });
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_audio_detail;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public AudioDetailViewModel initViewModel() {
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            detailEntity = mBundle.getParcelable("detail");

        }
        return new AudioDetailViewModel(getContext(), detailEntity);
    }

    @Override
    public void initParam() {
        super.initParam();

    }

    @Override
    public void initData() {
        super.initData();
        mAdapter = new AudioDetailPagerAdapter(getChildFragmentManager());
        binding.viewPager.setAdapter(mAdapter);
        binding.tablayout.setupWithViewPager(binding.viewPager);
        binding.itbCollect.setIcon(detailEntity.getIs_collect() == 0 ? R.mipmap.star_collect : R.mipmap.star_collectted);
        binding.itbCollect.setButtonTextColor(detailEntity.getIs_collect() == 0 ? Color.BLACK : Color.parseColor("#F2504E"));
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.collectResultObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                CollectResult collectResult = viewModel.collectResultObservable.get();
                binding.itbCollect.setIcon(collectResult.getIs_add() == 0 ? R.mipmap.star_collect : R.mipmap.star_collectted);
                binding.itbCollect.setButtonTextColor(collectResult.getIs_add() == 0 ? Color.BLACK : Color.parseColor("#F2504E"));
            }
        });
    }

    @Override
    public void onConfirm(PayDialogWithPhone.PayBeanWithPhone v) {
        if (v.type == 0) {
            payByWeChat(v.phone);
        } else {
            ToastUtil.toastBottom("暂不支持，请选择其他支付方式");
        }
    }


    /**
     * viewpage适配器
     */
    public class AudioDetailPagerAdapter extends FragmentPagerAdapter {
        private BaseFragment myFragment = null;

        public AudioDetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                myFragment = AudioListFragment.newInstance(detailEntity);
                return myFragment;
            } else {
                myFragment = AudioDescFragment.newInstance(detailEntity.getContent());
                return myFragment;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "音频(" + detailEntity.getTotal_album() + ")" : "简介";
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    public boolean onBackPressed() {

        return super.onBackPressed();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
            disposable = null;
        }
        if (d2 != null) {
            d2.dispose();
            d2 = null;
        }
    }
}
