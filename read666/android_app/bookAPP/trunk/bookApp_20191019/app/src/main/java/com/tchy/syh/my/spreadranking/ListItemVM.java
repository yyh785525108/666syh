package com.tchy.syh.my.spreadranking;

import android.content.Context;

import com.tchy.syh.my.entity.SpreadListResp;

import me.goldze.mvvmhabit.base.BaseViewModel;


public class ListItemVM extends BaseViewModel {

    public  SpreadListResp.DataBean.ListBean first;
    public SpreadListResp.DataBean.ListBean second;
    public  SpreadListResp.DataBean.ListBean third;
    public  SpreadListResp.DataBean.ListBean curr;
    public   SpreadListResp.DataBean.ListBean self;


    public ListItemVM(Context context, SpreadListResp.DataBean.ListBean first,SpreadListResp.DataBean.ListBean second,SpreadListResp.DataBean.ListBean third,SpreadListResp.DataBean.ListBean self){
        super(context);
        this.first=first;
        this.second=second;
        this.third=third;
        this.self=self;

    }
    public ListItemVM(Context context,SpreadListResp.DataBean.ListBean curr){
        super(context);
        this.curr=curr;
    }

}
