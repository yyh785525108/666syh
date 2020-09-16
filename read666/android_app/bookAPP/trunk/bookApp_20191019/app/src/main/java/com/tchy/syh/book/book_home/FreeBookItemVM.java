package com.tchy.syh.book.book_home;

import android.content.Context;
import androidx.databinding.ObservableField;
import android.os.Bundle;

import com.tchy.syh.book.book_detail.BookVideoPlayerActivity;
import com.tchy.syh.book.book_detail.entity.FreeBooksResp;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

public class FreeBookItemVM extends BaseViewModel {
    public FreeBooksResp.DataBean.ListBean bean;


    public FreeBookItemVM(Context context, FreeBooksResp.DataBean.ListBean bean){
        super(context);
        this.bean=bean;

    }
    public BindingCommand navClickCmd=new BindingCommand(()->{
        Bundle bundle=new Bundle();
        bundle.putString("id",bean.getId());
        bundle.putString("name",bean.getBookname());
        startActivity(BookVideoPlayerActivity.class,bundle);

    });

}
