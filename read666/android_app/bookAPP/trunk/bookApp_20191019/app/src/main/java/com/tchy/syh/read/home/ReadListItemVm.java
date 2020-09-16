package com.tchy.syh.read.home;

import android.content.Context;
import android.os.Bundle;

import com.tchy.syh.book.book_detail.BookVideoPlayerActivity;
import com.tchy.syh.book.entity.BookListResp;
import com.tchy.syh.read.detail.ReadDetailFragment;
import com.tchy.syh.read.entity.ReadListResp;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

public class ReadListItemVm extends BaseViewModel{
    public ReadListResp.DataBean.ListBean entity;
    public ReadListItemVm(Context context, ReadListResp.DataBean.ListBean entity) {
        super(context);
        this.entity = entity;
    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle mBundle = new Bundle();
            mBundle.putString("id", entity.getId());
            startContainerActivity(ReadDetailFragment.class.getCanonicalName(), mBundle);
        }
    });
}
