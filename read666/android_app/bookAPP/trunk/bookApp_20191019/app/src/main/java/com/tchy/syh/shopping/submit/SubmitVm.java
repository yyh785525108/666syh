package com.tchy.syh.shopping.submit;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import android.os.Bundle;

import com.google.gson.Gson;
import com.tchy.syh.R;
import com.tchy.syh.app.AppApplication;
import com.tchy.syh.cons.WeixinCons;
import com.tchy.syh.shopping.ApiService;
import com.tchy.syh.shopping.address.ListFragment;
import com.tchy.syh.shopping.entity.AddressResp;
import com.tchy.syh.shopping.entity.GoodsDetailResp;
import com.tchy.syh.shopping.entity.OrderSubmitResp;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;

import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;

public class SubmitVm extends BaseViewModel {
    public ObservableField<GoodsDetailResp.DataBean> bean=new ObservableField<>();
    public ObservableField<String> comment=new ObservableField<>();
    public ObservableField<String> totalMoney=new ObservableField<>();
    public ObservableInt payType=new ObservableInt();
    public ObservableField<String> price=new ObservableField<>();
    public ObservableInt num =new ObservableInt(1);

    public String id ;
    public SubmitVm(Context context, String id){
        super(context);
        this.id=id;
    }
    OrderSubmitResp.DataBean resp;
    @Override
    public void onCreate() {
        super.onCreate();
        getDetail();
        getAddress();
        Messenger.getDefault().register(context, "submitRefresh", AddressResp.DataBean.class, new BindingConsumer<AddressResp.DataBean>() {
            @Override
            public void call(AddressResp.DataBean dataBean) {
                address.set(dataBean);
            }
        });

        Messenger.getDefault().register(context, "paySuccess", new BindingAction() {
            @Override
            public void call() {
                Bundle bundle=new Bundle();
                Gson gson=new Gson();
                if(resp!=null){
                    bundle.putString("bean", gson.toJson(resp) );
                    bundle.putInt("payType", payType.get());
                    startContainerActivity(SubmitResultFrag.class.getCanonicalName(),bundle);
                    ((Activity)context).finish();
                }

            }
        });

    }

    public void payCallBack(){

    }
    public ObservableField<AddressResp.DataBean> address=new ObservableField<>();
    public void getAddress(){
        HashMap<String, Object> params = new HashMap();
        params.put("access_token", SPUtils.getInstance().getString("token"));

        Observable<AddressResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .address(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {
            if(res.getStatus()==-1){
                address.set(null);
                return ;
            }
            if(res.getData()!=null&&res.getData().size()>0){
                for (AddressResp.DataBean dataBean : res.getData()) {
                    if(dataBean.getIs_default()==1){
                        address.set(dataBean);
                        return;
                    }

                }
                address.set(res.getData().get(0));
            }else{
                address.set(null);
            }
        }, e -> {
            e.printStackTrace();
        });
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
            updatePrice();
            updateMoney();
        }, e -> {
            e.printStackTrace();
        });
    }
    public BindingCommand submitClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            submit();
        }
    });
    public void submit(){
        if(payType.get()>2){
            ToastUtil.toastBottom("暂不支持，请选择其他支付方式");
            return;
        }
        if(address.get()==null){
            ToastUtil.toastBottom("请选择地址");
            return ;
        }
        HashMap<String, Object> params = new HashMap();
        params.put("book_id", id);
        params.put("pay_type", payType.get()==0?0:1);
        params.put("liuyan",comment.get());
        params.put("address_id",address.get().getId());

        params.put("num", num.get());

        params.put("access_token", SPUtils.getInstance().getString("token"));

        Observable<OrderSubmitResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .book_buy(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(v -> {
            resp=v.getData();
            if(v.getStatus()!=1){
                ToastUtil.toastBottom(v.getInfo());
            }else{
                if(v.getData().getWxpay()!=null){
                    PayReq request = new PayReq();
                    request.appId =v.getData().getWxpay().getAppid();
                    request.partnerId = WeixinCons.PARTNER_ID;
                    request.prepayId= v.getData().getWxpay().getPrepayid();
                    request.packageValue = "Sign=WXPay";
                    request.nonceStr= v.getData().getWxpay().getNoncestr();
                    request.timeStamp= v.getData().getWxpay().getTimestamp();
                    request.sign=v.getData().getWxpay().getSign();
                    AppApplication.mWxApi.sendReq(request);
                }else{
                    Messenger.getDefault().sendNoMsg("paySuccess");
                }



            }


        }, e -> {
            e.printStackTrace();
        });
    }

    public BindingCommand plus=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            num.set(num.get()+1);
            updateMoney();

        }
    });
    public BindingCommand mins=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(num.get()>1){
                num.set(num.get()-1);
            }
            updateMoney();
        }
    });
    public void updateMoney(){
        String temp;
        if(payType.get()==0){
            temp=  context.getResources().getString(R.string.point,bean.get().getIntegral()*num.get());

        }else{
            temp=  context.getResources().getString(R.string.totalmoney,String.valueOf(bean.get().getPrice()*num.get()));
        }
        totalMoney.set(temp);
    }
    public void updatePrice(){
        String temp;
        if(payType.get()==0){
            temp=  context.getResources().getString(R.string.point,bean.get().getIntegral());

        }else{
            temp=  context.getResources().getString(R.string.totalmoney,String.valueOf(bean.get().getPrice()));
        }
        price.set(temp);
    }
    public BindingCommand typeChange=new BindingCommand(new BindingConsumer() {
        @Override
        public void call(Object o) {
            int index=(int )o;
            payType.set(index);
            updateMoney();
            updatePrice();
        }
    });

    public BindingCommand addressClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startContainerActivity(ListFragment .class.getCanonicalName());
        }
    });
}
