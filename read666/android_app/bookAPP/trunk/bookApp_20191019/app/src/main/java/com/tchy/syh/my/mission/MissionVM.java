package com.tchy.syh.my.mission;

import android.content.Context;
import androidx.databinding.ObservableField;

import com.tchy.syh.my.ApiService;
import com.tchy.syh.my.entity.MissionResp;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;

import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;


public class MissionVM extends BaseViewModel {
    public ObservableField<MissionResp.DataBean> bean=new ObservableField<>();

    public MissionVM(Context context) {
        super(context);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据

        getMissions();

    }
    public void getMissions(){
        HashMap<String, Object> params = new HashMap();

        params.put("access_token", SPUtils.getInstance().getString("token"));
//        params.put("page", this.pageNum);
        Observable<MissionResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .task(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(v -> {
            bean.set(v.getData());
        },e -> {
            e.printStackTrace();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
