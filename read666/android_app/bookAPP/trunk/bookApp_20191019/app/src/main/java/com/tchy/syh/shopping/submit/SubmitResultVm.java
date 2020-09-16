package com.tchy.syh.shopping.submit;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.tchy.syh.shopping.entity.OrderSubmitResp;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;

public class SubmitResultVm extends BaseViewModel {
    public ObservableField<OrderSubmitResp.DataBean> bean=new ObservableField<>();
    public int type;
    public ObservableField<String> price =new ObservableField<>();
    public SubmitResultVm(Context context,OrderSubmitResp.DataBean bean,int type){
        super(context);
        this.bean.set(bean);
        this.type=type;
        if(type==0){
            price .set(bean.getJifen()+"积分");
        }else{
            price .set("¥ "+bean.getAmount()+"元");
        }

    }
    @Override
    public void onCreate() {
        super.onCreate();


    }
    public BindingCommand goOnShopping=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ((Activity)context).finish();
        }
    });
    public BindingCommand checkOrders=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startContainerActivity(com.tchy.syh.orders.ListFragment  .class.getCanonicalName());
        }
    });
}
