package com.tchy.syh.listen.detail;

import android.graphics.Color;


import com.tchy.syh.listen.entity.ListenEntity;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.RxBus;


public class AudioItemViewModel extends BaseViewModel {

    public  String cn;
    public  String sort;
    public  int textColor = Color.parseColor("#000000");
    public ListenEntity.AudioItemEntity mAudioEntity;
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxBus.getDefault().post(mAudioEntity);
        }
    });

    public AudioItemViewModel(ListenEntity.AudioItemEntity entity) {
        this.mAudioEntity=entity;
        this.sort=entity.getSort()+1+"";
        this.cn=mAudioEntity.getComment_num()+"";
    }


}
