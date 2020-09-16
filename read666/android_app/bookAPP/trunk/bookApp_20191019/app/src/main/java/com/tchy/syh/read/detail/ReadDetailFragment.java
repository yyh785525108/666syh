package com.tchy.syh.read.detail;

import androidx.databinding.Observable;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.databinding.ReadDetailFragBinding;

import me.goldze.mvvmhabit.base.BaseFragment;


public class ReadDetailFragment extends BaseFragment<ReadDetailFragBinding, ReadDetailVm> {
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.read_detail_frag;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public ReadDetailVm initViewModel() {
        String id =getArguments().getString("id");
        return new ReadDetailVm(this.getContext(),id);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("sort", "onViewCreated: ");
        binding.web.getSettings().setJavaScriptEnabled(true);
        binding.web.getSettings().setUseWideViewPort(true);
        binding.web.getSettings().setLoadWithOverviewMode(true);
        binding.web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

    }
//       binding.vp.setCurrentItem(2);
//        viewModel.pagerIndex.set(2);


    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.bean.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                binding.web.loadUrl(viewModel.bean.get().getContent_link());
            }
        });
    }

    String id="";
    @Override
    public void initParam() {
        //获取列表传入的实体
        Bundle mBundle = getArguments();


        if (mBundle != null) {
           id=mBundle.getString("id");
        }
    }


}
