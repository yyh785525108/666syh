package com.tchy.syh.my.bonusRecord;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.MyRecordsBinding;

import me.goldze.mvvmhabit.base.BaseFragment;


public class ListFragment extends BaseFragment<MyRecordsBinding,ListVM> {
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.my_records;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }


    @Override
    public ListVM initViewModel() {
        return new ListVM(this.getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }






}
