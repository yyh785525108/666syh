package com.tchy.syh.shopping.detail;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.tchy.syh.R;

import me.goldze.mvvmhabit.BR;
import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.base.BaseViewModel;

public class GoodsDetailFrag extends BaseFragment {
    @Override
    public int initContentView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return R.layout.shop_item_detail;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public BaseViewModel initViewModel() {
        return new DetailVm(getContext(),getArguments().getString("id"));
    }
}
