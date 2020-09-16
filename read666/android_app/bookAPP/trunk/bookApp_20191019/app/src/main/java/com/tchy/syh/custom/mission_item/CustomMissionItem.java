package com.tchy.syh.custom.mission_item;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.tchy.syh.R;
import com.tchy.syh.databinding.MissionItemBinding;


public class CustomMissionItem extends RelativeLayout {
    MissionItemBinding binding;
    public CustomMissionItem(Context context) {
        super(context);
        init();
    }

    public CustomMissionItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void init(){
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        binding= DataBindingUtil.inflate(mInflater, R.layout.mission_item,this,true);
        MissionItemVM itemVM=new MissionItemVM(getContext());
        itemVM.getPointStatus.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(itemVM.getPointStatus.get()){
                    setActive(false,true);
                }
            }
        });
        binding.setVm(itemVM);

    }
    public void setName(String name){
        binding.getVm().name.set(name);

    }
    public void setPoint(int point ){
        binding.getVm().point.set(point);
    }
    public void setIcon(int resid){
        binding.icon.setImageResource(resid);
    }
    public void setId(int id){
        binding.getVm().id.set(id);
    }
    public void setActive(boolean active,boolean status){
        binding.getVm().active.set(active);
        if(active){
            binding.btn.setText("领取"+ binding.getVm().point.get()+"积分");
        }else{
            if(status){
                binding.btn.setBackground(null);
                binding.btn.setText("已完成");
            }else{

                binding.btn.setText("未完成");
            }
        }
    }
}
