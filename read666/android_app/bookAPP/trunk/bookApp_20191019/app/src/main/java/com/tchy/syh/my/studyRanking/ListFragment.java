package com.tchy.syh.my.studyRanking;

import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.custom.CustomLoadingDialog;
import com.tchy.syh.databinding.StudyRankingListBinding;

import me.goldze.mvvmhabit.base.BaseFragment;


public class ListFragment extends BaseFragment<StudyRankingListBinding, ListVM> {
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.study_ranking_list;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }


    @Override
    public ListVM initViewModel() {
        return new ListVM(this.getContext());
    }
    CustomLoadingDialog dialog;
    public void createDialog(){
        if(dialog!=null){
            dialog.dismiss();
            dialog=null;
        }
        dialog=new CustomLoadingDialog(this.getContext());
        dialog.show();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createDialog();
        viewModel.paihan_list(dialog);
        binding.back.setOnClickListener(v->{
            getActivity().finish();
        });

    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.type.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                createDialog();
                viewModel.paihan_list(dialog);
                switch (viewModel.type.get()){
                    case 0:
                        binding.week.setScaleX(1.2f);
                        binding.week.setScaleY(1.2f);
                        binding.all.setScaleX(1.0f);
                        binding.all.setScaleY(1.0f);
                        break;
                    case 1:
                        binding.all.setScaleX(1.2f);
                        binding.all.setScaleY(1.2f);
                        binding.week.setScaleX(1.0f);
                        binding.week.setScaleY(1.0f);
                        break;
                }
            }
        });
    }
}
