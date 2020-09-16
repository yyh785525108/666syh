package com.tchy.syh.my;

import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.RecommandBookListBinding;

import me.goldze.mvvmhabit.base.BaseFragment;


public class RecommandBookListFragment extends BaseFragment<RecommandBookListBinding, RecommandBookListVM> {
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.recommand_book_list;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }


    @Override
    public RecommandBookListVM initViewModel() {
        return new RecommandBookListVM(this.getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }






}
