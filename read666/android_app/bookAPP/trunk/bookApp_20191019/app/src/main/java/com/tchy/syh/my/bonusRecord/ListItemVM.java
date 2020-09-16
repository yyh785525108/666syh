package com.tchy.syh.my.bonusRecord;

import android.content.Context;

import com.tchy.syh.my.entity.BonusResp;
import com.tchy.syh.orders.entity.OrderResp;

import me.goldze.mvvmhabit.base.BaseViewModel;


public class ListItemVM extends BaseViewModel {

    public OrderResp.DataBean.ListBean bean;



    public ListItemVM(Context context, OrderResp.DataBean.ListBean bean){
        super(context);
        this.bean=bean;

    }


}
