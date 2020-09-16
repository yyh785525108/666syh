package com.tchy.syh.custom.mission_item;

import android.content.Context;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.tchy.syh.R;
import com.tchy.syh.common.entity.CommonDataListResp;
import com.tchy.syh.my.ApiService;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.SpannableUtil;
import com.tchy.syh.utils.ToastUtil;

import java.util.HashMap;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;

public class MissionItemVM extends BaseViewModel {
    public ObservableField<String> name=new ObservableField<>();
    public ObservableField<String> statusText=new ObservableField<>();
    public ObservableInt point =new ObservableInt();
    public ObservableInt id =new ObservableInt();
    public ObservableInt resid =new ObservableInt();
    public ObservableBoolean status =new ObservableBoolean(false);
    public ObservableBoolean getPointStatus =new ObservableBoolean(false);
    public ObservableBoolean active =new ObservableBoolean(false);
    public BindingCommand getPointClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            HashMap<String, Object> params = new HashMap();
            params.put("m", "get_point");
            params.put("id", id.get());
            params.put("access_token", SPUtils.getInstance().getString("token"));
//        params.put("page", this.pageNum);
            io.reactivex.Observable<CommonDataListResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                    .task1(FormdataRequestUtil.getFormDataRequestParams(params))
                    .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                    .compose(RxUtils.schedulersTransformer()) //线程调度
                    .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                    .doOnSubscribe(disposable -> {
                    });
            observable.subscribe(b -> {
                if (b.getStatus()==1){
                    getPointStatus.set(true);

                    ToastUtil.toastCenter(context, SpannableUtil.getColoredTextSpannable("+"+point.get()), R.string.get_point);
                }

            });


        }
    });
    public MissionItemVM(Context context){
        super(context);
    }
}
