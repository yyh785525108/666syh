package com.tchy.syh.shopping.address;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import android.os.Bundle;

import com.google.gson.Gson;
import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.shopping.ApiService;
import com.tchy.syh.shopping.entity.AddressResp;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class ListVM extends BaseViewModel {

    public ObservableList<ListItemVM> listItems = new ObservableArrayList<>();

    Disposable d;

    public ListVM(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据
        getList();

        Messenger.getDefault().register(context, "addressRefresh", new BindingAction() {
            @Override
            public void call() {
                getList();
            }
        });
    }

    public void getList() {
        HashMap<String, Object> params = new HashMap();

        params.put("access_token", SPUtils.getInstance().getString("token"));
//        params.put("page", this.pageNum);
        Observable<AddressResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .address(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(res -> {
            if (res.getStatus() == 1) {
                listItems.clear();
                for (AddressResp.DataBean dataBean : res.getData()) {
                    listItems.add(new ListItemVM(context, dataBean));
                }
            }else{
                listItems.clear();
            }


        }, e -> {

        });

    }



    public BindingCommand createClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            Bundle bundle = new Bundle();
            Gson gson = new Gson();
            String json = gson.toJson(new AddressResp.DataBean());
            bundle.putString("bean", json);
            startContainerActivity(ModFragment.class.getCanonicalName(), bundle);
        }
    });

    public final ItemBinding itemBinding = ItemBinding.of(BR.vm, R.layout.shop_address_list_item);

    @Override
    public void onDestroy() {
        super.onDestroy();
             if (d != null) {                                     d.dispose();                                 }

    }


}
