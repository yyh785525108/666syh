package com.tchy.syh.collect;

import android.content.Context;
import android.os.Bundle;

import com.tchy.syh.collect.entity.CollectResp;
import com.tchy.syh.read.detail.ReadDetailFragment;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

public class CollectArticleItemVM extends BaseViewModel{
    public CollectResp.DataBean.ListBean bean ;
    public CollectArticleItemVM(Context context,CollectResp.DataBean.ListBean bean){
        this.context=context;
        this.bean=bean;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public BindingCommand itemClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle =new Bundle();
            bundle.putString("id",bean.getId() );
            startContainerActivity(ReadDetailFragment.class.getCanonicalName(),bundle);
        }
    });

}
