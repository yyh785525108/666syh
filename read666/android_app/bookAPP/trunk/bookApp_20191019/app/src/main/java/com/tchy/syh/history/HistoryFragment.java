package com.tchy.syh.history;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.HistoryFragBinding;

import me.goldze.mvvmhabit.base.BaseFragment;


public class HistoryFragment extends BaseFragment<HistoryFragBinding, HistoryVM> {
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.history_frag;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public HistoryVM initViewModel() {
        return new HistoryVM(this.getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("sort", "onViewCreated: ");
//        binding.vp.setOffscreenPageLimit(3);

    }

}
