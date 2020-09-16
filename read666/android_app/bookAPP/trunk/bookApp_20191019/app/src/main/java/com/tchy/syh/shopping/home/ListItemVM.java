package com.tchy.syh.shopping.home;

import android.content.Context;
import android.os.Bundle;

import com.tchy.syh.book.entity.ExtraBean;
import com.tchy.syh.my.entity.BonusResp;
import com.tchy.syh.shopping.detail.GoodsDetailFrag;
import com.tchy.syh.shopping.entity.ShopBookResp;

import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;


public class ListItemVM extends BaseViewModel {

    public ShopBookResp.DataBean.ListBean bean;


    public ListItemVM(Context context, ShopBookResp.DataBean.ListBean bean){
        super(context);
        this.bean=bean;

    }

    public BindingCommand itemClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle=new Bundle();
            bundle.putString("id", bean.getId());
            startContainerActivity(GoodsDetailFrag.class.getCanonicalName(),bundle);
        }
    });

}
