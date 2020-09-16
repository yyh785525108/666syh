package com.tchy.syh.search;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.Messenger;


public class SearchVM extends BaseViewModel {
    public ObservableField<String> searchWords=new ObservableField<>();
    public SearchVM(Context context){
        super(context);
    }

    public BindingCommand cancelClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ((Activity)context).finish();
        }
    });

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        Messenger.getDefault().register(context, "search", String.class, new BindingConsumer<String>() {
            @Override
            public void call(String s) {
                searchWords.set(s);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
