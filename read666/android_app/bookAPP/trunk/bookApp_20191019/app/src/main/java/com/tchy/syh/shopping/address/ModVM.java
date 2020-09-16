package com.tchy.syh.shopping.address;

import android.app.Activity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;
import android.text.TextUtils;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.common.entity.CommonResp;
import com.tchy.syh.shopping.ApiService;
import com.tchy.syh.shopping.entity.AddressDataBean;
import com.tchy.syh.shopping.entity.AddressResp;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.StringUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class ModVM extends BaseViewModel {

    public ObservableField<String> result=new ObservableField<>();
    public ObservableField<String> name=new ObservableField<>();
    public ObservableField<String> mobile=new ObservableField<>();
    public ObservableField<String> detail=new ObservableField<>();

        public ObservableField<AddressResp.DataBean> address=new ObservableField<>();
    public List<AddressDataBean> datas=new ArrayList<>();


    Disposable d;

    public ModVM(Context context,AddressResp.DataBean bean) {
        super(context);
//        this.add.setValue(bean);
        this.address.set(bean);
        name.set(bean.getConsignee());
        mobile.set(bean.getMobile());
        detail.set(bean.getAddress());



    }
    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据


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
            if(res.getStatus()==1){

            }


        }, e -> {

        });

    }
    public void save(){
        if(StringUtils.isEmpty(name.get())){
            ToastUtil.toastBottom("请输入收件人姓名");
            return;
        }
        if(StringUtils.isEmpty(detail.get())){
            ToastUtil.toastBottom("请输入详细地址");
            return;
        }
        HashMap<String, Object> params = new HashMap();
        params.put("m", "save");
        params.put("id", TextUtils.isEmpty(address.get().getId())?0:address.get().getId());
        params.put("consignee",name.get());
        params.put("mobile", mobile.get());
        params.put("province", address.get().getProvince());
        params.put("city", address.get().getCity());
        params.put("district", address.get().getDistrict());
        params.put("address",detail.get());
        params.put("access_token", SPUtils.getInstance().getString("token"));
//        params.put("page", this.pageNum);
        Observable<CommonResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .addressMod(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(res -> {
            if(res.getStatus()==1){
                Messenger.getDefault().sendNoMsg("addressRefresh");
                ((Activity)context).finish();
            }else{
                ToastUtil.toastBottom(res.getInfo());
            }


        }, e -> {
            e.printStackTrace();
        });
    }
    public  BindingCommand createClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            save();
        }
    });

    public final ItemBinding itemBinding = ItemBinding.of(BR.vm,R.layout.shop_address_list_item);
    @Override
    public void onDestroy() {
        super.onDestroy();
             if (d != null) {                                     d.dispose();                                 }

    }


}
