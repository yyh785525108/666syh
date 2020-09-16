package com.tchy.syh.my;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;

import com.tchy.syh.app.AppApplication;
import com.tchy.syh.my.entity.BuyVIPResp;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.StringUtils;


public class VIPVM extends BaseViewModel {
    @Override
    public void removeRxBus() {
        super.removeRxBus();
    }

    public VIPVM(Context context) {
        super(context);
        Messenger.getDefault().register(context, "paySuccess", new BindingAction() {
            @Override
            public void call() {
                Messenger.getDefault().sendNoMsg("myRefresh");
                ((Activity)context).finish();
            }
        });

    }


    public void sign() {
        HashMap<String, Object> params = new HashMap();
        String mobile=SPUtils.getInstance().getString("mobile");
        if(StringUtils.isEmpty(mobile)){
            ToastUtil.toastBottom("请前往我的->设置->账户与安全绑定手机号");
            return ;
        }
        params.put("mobile",  SPUtils.getInstance().getString("mobile"));
        params.put("rank",  SPUtils.getInstance().getInt("level"));

        params.put("access_token", SPUtils.getInstance().getString("token"));
//        params.put("page", this.pageNum);
        Observable<BuyVIPResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .buyvip(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(v -> {
            if (v.getStatus() == 1) {
                PayReq request = new PayReq();
                request.appId =v.getData().getAppid();
                request.partnerId =v.getData().getPartnerid();
                request.prepayId= v.getData().getPrepayid();
                request.packageValue = "Sign=WXPay";
                request.nonceStr= v.getData().getNoncestr();
                request.timeStamp= v.getData().getTimestamp();
                request.sign=v.getData().getSign();
                AppApplication.mWxApi.sendReq(request);

            } else {
                ToastUtil.toastBottom(v.getInfo());
            }


        }, e -> {
            e.printStackTrace();
        });

    }
}
