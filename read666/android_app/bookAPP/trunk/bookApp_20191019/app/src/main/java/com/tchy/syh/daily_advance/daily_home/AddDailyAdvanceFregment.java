package com.tchy.syh.daily_advance.daily_home;

import androidx.databinding.Observable;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.AddDailyAdvanceFragmentBinding;

import me.goldze.mvvmhabit.base.BaseFragment;

public class AddDailyAdvanceFregment extends BaseFragment<AddDailyAdvanceFragmentBinding, AddDailyViewModel> {
    @Override
    public int initContentView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return R.layout.add_daily_advance_fragment;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public AddDailyViewModel initViewModel() {
        return new AddDailyViewModel(this.getContext());
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
