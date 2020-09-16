package com.tchy.syh.orders;

import android.content.Context;
import androidx.databinding.ObservableField;

import com.tchy.syh.orders.entity.OrderResp;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;


public class ListItemVM extends BaseViewModel {

    public OrderResp.DataBean.ListBean bean;
    public String totalVal;

    public ListItemVM(Context context, OrderResp.DataBean.ListBean bean){
        super(context);
        this.bean=bean;
        StringBuilder builder=new StringBuilder();
        builder.append(bean.getNum());
        builder.append("件商品");
        builder.append(" ");
        builder.append("合计：");
        if(bean.getAmount()>0){
            builder.append(bean.getAmount());
            builder.append("元");
        }else{
            builder.append(bean.getJifen());
            builder.append("积分");
        }

        this.totalVal=builder.toString();


    }

    public BindingCommand confirmClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });


}
