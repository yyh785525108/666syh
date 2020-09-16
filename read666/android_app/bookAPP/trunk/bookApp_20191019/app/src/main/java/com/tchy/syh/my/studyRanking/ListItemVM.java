package com.tchy.syh.my.studyRanking;

import android.content.Context;

import com.tchy.syh.my.entity.StudyListResp;

import me.goldze.mvvmhabit.base.BaseViewModel;


public class ListItemVM extends BaseViewModel {
    public  StudyListResp.DataBean.ListBean first;
    public StudyListResp.DataBean.ListBean second;
    public  StudyListResp.DataBean.ListBean third;
    public  StudyListResp.DataBean.ListBean curr;
    public   StudyListResp.DataBean.ListBean self;


    public ListItemVM(Context context, StudyListResp.DataBean.ListBean first,StudyListResp.DataBean.ListBean second,StudyListResp.DataBean.ListBean third,StudyListResp.DataBean.ListBean self){
        super(context);
        this.first=first;
        this.second=second;
        this.third=third;
        this.self=self;

    }
    public ListItemVM(Context context,StudyListResp.DataBean.ListBean curr){
        super(context);
        this.curr=curr;
    }



}
