package com.tchy.syh.my;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tchy.syh.R;
import com.tchy.syh.databinding.VipBinding;
import com.tchy.syh.dialog.PayDialog;
import com.tchy.syh.userAccount.account_management.modify_mobile.ModMobileFragment;
import com.tchy.syh.utils.ToastUtil;

import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.StringUtils;

public class VipFragment extends BaseFragment<VipBinding, VIPVM> {

    double price = 366.00;

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.vip;
    }

    @Override
    public int initVariableId() {
        return 0;
    }

    @Override
    public VIPVM initViewModel() {
        return new VIPVM(getContext());
    }

    PayDialog dialog;
    Disposable d;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String imageUrl = SPUtils.getInstance().getString("avatar");
        String nickName = SPUtils.getInstance().getString("nickname");
        Glide.with(getContext()).load(imageUrl).apply((new RequestOptions()).encodeQuality(50).circleCrop()).into(binding.avatar);
        binding.nickName.setText(nickName);
        dialog = new PayDialog();
        binding.submit.setOnClickListener(v -> {
            if (StringUtils.isEmpty(SPUtils.getInstance().getString("mobile"))) {
                ToastUtil.toastBottom("请先绑定手机");
                viewModel.startContainerActivity(ModMobileFragment.class.getCanonicalName());
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putDouble("price", price);
            dialog.setArguments(bundle);
            dialog.show(this.getChildFragmentManager(), "");
        });

        d = RxBus.getDefault().toObservable(PayDialog.PayBean.class).subscribe(v -> {
            //todo 发起微信支付
            if (v.type == 0) {
                viewModel.sign();
            } else {
                ToastUtil.toastBottom("暂不支持，请选择其他支付方式");
            }

        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (d != null) {
            d.dispose();
        }
        d = null;
        dialog = null;
    }
}
