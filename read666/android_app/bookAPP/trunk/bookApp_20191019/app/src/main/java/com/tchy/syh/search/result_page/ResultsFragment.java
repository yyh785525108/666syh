package com.tchy.syh.search.result_page;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.SearchResultFragBinding;

import me.goldze.mvvmhabit.base.BaseFragment;


public class ResultsFragment extends BaseFragment<SearchResultFragBinding, ResultsVM> {
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.search_result_frag;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public ResultsVM initViewModel() {
        Log.d("sort", "initViewModel: "+getArguments().getInt("type"));
        return new ResultsVM(this.getContext(),this.getArguments().getString("search_key"),getArguments().getInt("type"));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);


    }
}

