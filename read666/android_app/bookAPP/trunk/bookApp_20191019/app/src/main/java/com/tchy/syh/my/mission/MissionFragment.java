package com.tchy.syh.my.mission;

import androidx.databinding.Observable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.custom.CustomMissionProgressTextView;
import com.tchy.syh.custom.mission_item.CustomMissionItem;
import com.tchy.syh.databinding.MissionFragBinding;
import com.tchy.syh.my.entity.MissionResp;

import java.util.List;

import me.goldze.mvvmhabit.base.BaseFragment;


public class MissionFragment extends BaseFragment<MissionFragBinding, MissionVM> {
    @Override
    public int initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return R.layout.mission_frag;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }

    @Override
    public MissionVM initViewModel() {
        return new MissionVM(this.getContext());
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();

        viewModel.bean.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {

                int day=viewModel.bean.get().getSign_times();
                int currPoint=0;
                List<MissionResp.DataBean.DailyBean> dailyList =viewModel.bean.get().getDaily();
                List<MissionResp.DataBean.NewBean> newerList =viewModel.bean.get().getNewX();
                int initPoint=5;
                for (int i = 0; i <7 ; i++) {

                    boolean flag= true;
                    if(i>=day){
                        flag=false;
                    }
                    CustomMissionProgressTextView t=new CustomMissionProgressTextView(getContext(),i,flag);

                   if(i<=day){
                       currPoint=initPoint+i;
                   }
                    t.setText("+"+(initPoint+i));
                    binding.containers.addView(t);
                }
                if(getArguments().getBoolean("isSign",false)){
                    binding.content.setText("+"+currPoint);
                    binding.content.setVisibility(View.VISIBLE);
                }

//        binding.signedDialog.getViewStub().inflate();
                binding.content.setOnClickListener(v->{
                    v.setVisibility(View.GONE);
                });

                for(MissionResp.DataBean.DailyBean bean :dailyList){
                    CustomMissionItem daily1=new CustomMissionItem(getContext());
                    daily1.setName(bean.getDesc());
                    daily1.setTag(bean.getId());
                    daily1.setPoint(bean.getPoint());
                    daily1.setId(bean.getId());

                    switch (bean.getStatus()){
                        case -1:
                            daily1.setActive(false,false);
                            break;
                        case 0:
                            daily1.setActive(true,true);
                            break;
                        case 1:
                            daily1.setActive(false,true);
                            break;
                    }
                    switch (bean.getKey()){
                        case "see":
                            daily1.setIcon(R.mipmap.clock);
                            break;
                        case "comment":
                            daily1.setIcon(R.mipmap.comment_red);

                            break;
                        case "share":
                            daily1.setIcon(R.mipmap.share_link);
                            break;
                    }

                    binding.dailyContainer.addView(daily1);

                }

                for(MissionResp.DataBean.NewBean bean :newerList){
                    CustomMissionItem daily1=new CustomMissionItem(getContext());
                    daily1.setName(bean.getDesc());
                    daily1.setTag(bean.getId());
                    daily1.setPoint(bean.getPoint());
                    daily1.setId(bean.getId());
                    switch (bean.getStatus()){
                        case -1:
                            daily1.setActive(false,false);
                            break;
                        case 0:
                            daily1.setActive(true,true);
                            break;
                        case 1:
                            daily1.setActive(false,true);
                            break;
                    }
                    switch (bean.getKey()){
                        case "sys_weixin":
                            daily1.setIcon(R.mipmap.info_mgt);
                            break;
                        case "mobile":
                            daily1.setIcon(R.mipmap.bind_mobile);
                            break;

                    }

                    binding.newerContainer.addView(daily1);

                }



            }
        });

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
