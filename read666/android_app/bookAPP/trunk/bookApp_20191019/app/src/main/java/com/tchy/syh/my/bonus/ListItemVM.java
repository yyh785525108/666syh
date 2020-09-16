package com.tchy.syh.my.bonus;

import android.content.Context;

import com.tchy.syh.my.entity.BonusResp;
import com.tchy.syh.my.entity.SpreadListResp;

import me.goldze.mvvmhabit.base.BaseViewModel;


public class ListItemVM extends BaseViewModel {

    public BonusResp.DataBean.ListBean bean;



    public ListItemVM(Context context, BonusResp.DataBean.ListBean bean){
        super(context);
        this.bean=bean;

    }


}
