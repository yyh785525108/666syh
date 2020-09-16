package com.tchy.syh.messages;

import android.content.Context;

import com.tchy.syh.messages.entity.MessageResp;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;


public class MessageListItemVM extends BaseViewModel {
    public MessageResp.DataBean.ListBean bean ;
    public MessageListItemVM(Context context,MessageResp.DataBean.ListBean bean ){
        super(context);
        this.bean=bean;
    }
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}