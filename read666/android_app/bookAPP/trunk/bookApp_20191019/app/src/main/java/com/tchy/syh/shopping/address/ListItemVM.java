package com.tchy.syh.shopping.address;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.tchy.syh.common.entity.CommonDataListResp;
import com.tchy.syh.common.entity.CommonResp;
import com.tchy.syh.shopping.ApiService;
import com.tchy.syh.shopping.detail.GoodsDetailFrag;
import com.tchy.syh.shopping.entity.AddressResp;
import com.tchy.syh.shopping.entity.ShopBookResp;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;

import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;


public class ListItemVM extends BaseViewModel {

    public AddressResp.DataBean bean;


    public ListItemVM(Context context, AddressResp.DataBean bean){
        super(context);
        this.bean=bean;

    }

    public BindingCommand itemClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            setDefault();
        }
    });
    public  BindingCommand modifyClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle=new Bundle();
            Gson gson=new Gson();
            String json=gson.toJson(bean);
            bundle.putString("bean",json );
            startContainerActivity(ModFragment.class.getCanonicalName(),bundle);
        }
    });
    public  BindingCommand delClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            AlertDialog.Builder builder=new AlertDialog.Builder(context);
            builder.setTitle("提示");
            builder.setMessage("确认删除？");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    del();
                    dialog.cancel();
                }
            });
            builder.create().show();

        }
    });

    public BindingCommand select = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Messenger.getDefault().send(bean,"submitRefresh" );
            ((Activity)context).finish();
        }
    }
    );
    public void del(){



        HashMap<String, Object> params = new HashMap();
        params.put("id", bean.getId());
        params.put("m", "del");

        params.put("access_token", SPUtils.getInstance().getString("token"));
//        params.put("page", this.pageNum);
        Observable<CommonDataListResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .addressModList(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(res -> {
            if(res.getStatus()==1){
                Messenger.getDefault().sendNoMsg("addressRefresh");
            }


        }, e -> {

        });

    }
    public void setDefault(){
        HashMap<String, Object> params = new HashMap();
        params.put("id", bean.getId());
        params.put("m", "set_default");
        params.put("access_token", SPUtils.getInstance().getString("token"));
//        params.put("page", this.pageNum);
        Observable<CommonDataListResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .addressModList(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(res -> {
            if(res.getStatus()==1){
                Messenger.getDefault().sendNoMsg("addressRefresh");
            }

        }, e -> {

        });

    }
}
