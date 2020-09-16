package com.tchy.syh.collect;

import android.content.Context;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import android.os.Bundle;

import com.tchy.syh.collect.entity.CollectResp;
import com.tchy.syh.listen.detail.AudioDetailFragment;
import com.tchy.syh.listen.entity.AudioDetailEntity;
import com.tchy.syh.listen.entity.ResponseWrapper;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;

public class CollectAudioItemVM extends BaseViewModel{

    public CollectResp.DataBean.ListBean bean ;
    public CollectAudioItemVM(Context context, CollectResp.DataBean.ListBean bean){
        this.context=context;
        this.bean=bean;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public BindingCommand itemClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            requestAudioDetail(bean.getId());
        }
    });
    private void requestAudioDetail(String id) {
        HashMap<String, Object> params = new HashMap();
        params.put("id", id);
        params.put("access_token", SPUtils.getInstance().getString("token"));
        Observable<ResponseWrapper<AudioDetailEntity>> observable = RetrofitClient.getInstance().create(ApiService.class)
                .learn_info(FormdataRequestUtil.getFormDataRequestParams(params))
                .delay(500, TimeUnit.MILLISECONDS)
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {
            if (res.isSuccess()) {
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("detail", res.getData());
                startContainerActivity(AudioDetailFragment.class.getCanonicalName(), mBundle);
            } else {
                if (res.getStatus() == 1005) {
                    ToastUtil.toastBottom("未登录或登录状态失效");
                }
            }

        }, e -> {
            e.printStackTrace();
        });

    }

}
