package com.tchy.syh.search.hot_words;

import androidx.databinding.Observable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.custom.FlowChipView;
import com.tchy.syh.databinding.SearchHotwordsFragBinding;
import com.tchy.syh.search.entity.HotWordsResp;

import java.util.List;

import me.goldze.mvvmhabit.base.BaseFragment;


public class HotWordsFragment extends BaseFragment<SearchHotwordsFragBinding, HotWordsVM> {
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.search_hotwords_frag;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public HotWordsVM initViewModel() {
        return new HotWordsVM(this.getContext(),getArguments().getInt("type"));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.hotBean.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {

                List<HotWordsResp.DataBean> listHot=viewModel.hotBean.get().getData();

                if(listHot!=null){
                    for(int i=0;i<listHot.size();i++){
                        FlowChipView view1=new FlowChipView(getContext(),i);
                        view1.setText(listHot.get(i).getTitle());
                        view1.setOnClickListener(v->{

                            tagClick(((FlowChipView)v).getText().toString());
                        });
                        binding.hotwords.addView(view1);
                    }
                }

            }
        });
        viewModel.historyBean.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                List<String> listHistory=viewModel.historyBean.get().getData();
                if(listHistory!=null){
                    for(int i=0;i<listHistory.size();i++){
                        FlowChipView view1=new FlowChipView(getContext(),-1);
                        view1.setText(listHistory.get(i).toString());
                        view1.setOnClickListener(v->{

                            tagClick(((FlowChipView)v).getText().toString());
                        });
                        binding.historys.addView(view1);
                    }
                }
            }
        });
    }
    public void tagClick(String text){
        me.goldze.mvvmhabit.bus.Messenger .getDefault().send(text,"search" );
    }
}
