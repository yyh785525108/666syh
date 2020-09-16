package com.tchy.syh.shopping.detail;

import android.content.Context;
import androidx.databinding.ObservableField;
import android.os.Bundle;
import android.text.TextUtils;

import com.tchy.syh.shopping.ApiService;
import com.tchy.syh.shopping.entity.GoodsDetailResp;
import com.tchy.syh.shopping.submit.SubmitFrag;
import com.tchy.syh.userAccount.userLogin.LoginActivity;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;

import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;

public class DetailVm extends BaseViewModel {
    public ObservableField<GoodsDetailResp.DataBean> bean=new ObservableField<>();
    public String id ;
    public DetailVm(Context context,String id){
        super(context);
        this.id=id;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        getDetail();
    }

    public void getDetail(){
        HashMap<String, Object> params = new HashMap();
        params.put("id", id);
        Observable<GoodsDetailResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .book_info(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {
            if(res.getStatus()==-1){
                return ;
            }
            bean.set(res.getData());
        }, e -> {
            e.printStackTrace();
        });
    }
    public BindingCommand submitClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(TextUtils.isEmpty(SPUtils.getInstance().getString("token"))){

                startActivity(LoginActivity.class);
                return;
            }

            Bundle bundle=new Bundle();
            bundle.putString("id",id);
            startContainerActivity(SubmitFrag.class.getCanonicalName(),bundle);
        }
    });
}
