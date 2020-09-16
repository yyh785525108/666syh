package com.tchy.syh.common;

import android.content.Context;
import androidx.databinding.ObservableField;
import android.os.Bundle;

import com.tchy.syh.book.book_list.BookListFragment;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

public class CommonViewModel extends BaseViewModel {
    public int index=0;

    public CommonViewModel (Context context, String s, String l,int index){
        this(context,s,l,index,"","");
    }
    public CommonViewModel (Context context, String s, String l,int index,String title){
        this(context,s,l,index,title,"");
    }
    public CommonViewModel (Context context, String s, String l,int index,String title,String id){
        super(context);
        this.src.set(s);
        this.link.set(l);
        this.index=index;
        this.title.set(title);
        this.id.set(id);
    }
    public ObservableField<String> id=new ObservableField<>();
    public ObservableField<String> src=new ObservableField<>();
    public ObservableField<String> link=new ObservableField<>();
    public ObservableField<String> title=new ObservableField<>();
    public BindingCommand clickCmd=new BindingCommand(()->{
        Bundle bundle=new Bundle();
        bundle.putString("url",link.get());
       startActivity(CommonWebViewActivity.class,bundle);

    });
    public BindingCommand navClickCmd=new BindingCommand(()->{
        Bundle bundle=new Bundle();
        bundle.putString("id",id.get());
        startContainerActivity(BookListFragment.class.getCanonicalName(),bundle);

    });
}
