package com.tchy.syh.shopping.submit;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.tchy.syh.R;
import com.tchy.syh.databinding.ShopOrderSubmitSuccessBinding;
import com.tchy.syh.shopping.entity.OrderSubmitResp;

import me.goldze.mvvmhabit.BR;
import me.goldze.mvvmhabit.base.BaseFragment;

public class SubmitResultFrag extends BaseFragment<ShopOrderSubmitSuccessBinding,SubmitResultVm> {
    @Override
    public int initContentView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return R.layout.shop_order_submit_success;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public SubmitResultVm initViewModel() {
        Gson gson=new Gson();

        return new SubmitResultVm(getContext(), gson.fromJson(getArguments().getString("bean"), OrderSubmitResp.DataBean.class),getArguments().getInt("payType"));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


}
