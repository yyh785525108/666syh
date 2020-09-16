package com.tchy.syh.book.book_home;

import android.content.Context;
import androidx.databinding.ObservableField;
import android.os.Bundle;
import android.os.Parcelable;

import com.tchy.syh.book.book_list.BookListFragment;
import com.tchy.syh.book.entity.LearnCate;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

public class NavItemVM extends BaseViewModel {
    public ObservableField<String > src=new ObservableField<>();
    public ObservableField<String > title=new ObservableField<>();
    public String id;
    public List<LearnCate.DataBean> categorys;

    public NavItemVM (Context context, String src, String title, String id , List categorys){
        super(context);
        this.src.set(src);
        this.title.set(title);
        this.id=id;
        this.categorys=categorys;
    }
    public BindingCommand navClickCmd=new BindingCommand(()->{
        Bundle bundle=new Bundle();
        bundle.putString("id",id);
        bundle.putParcelableArrayList("categorys",(ArrayList<? extends Parcelable>) categorys);
        startContainerActivity(BookListFragment.class.getCanonicalName(),bundle);

    });

}
