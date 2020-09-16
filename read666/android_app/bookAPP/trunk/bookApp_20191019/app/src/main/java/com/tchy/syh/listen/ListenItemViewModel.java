package com.tchy.syh.listen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;


import com.tchy.syh.R;
import com.tchy.syh.listen.base.AdaptationViewModel;
import com.tchy.syh.listen.detail.AudioDetailFragment;
import com.tchy.syh.listen.entity.AudioDetailEntity;
import com.tchy.syh.listen.entity.ListenEntity;
import com.tchy.syh.listen.entity.ResponseWrapper;
import com.tchy.syh.userAccount.userLogin.LoginActivity;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.TimeFormatUtil;
import com.tchy.syh.utils.ToastUtil;


import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;


public class ListenItemViewModel extends AdaptationViewModel {
    public ListenEntity.ListenItemEntity listenItemEntity;
    public Drawable drawableImg;
    public String updateInfo;
    public Spannable priceInfo;


    public ListenItemViewModel(Context context, ListenEntity.ListenItemEntity entity) {
        super(context);
        this.listenItemEntity = entity;
        drawableImg = ContextCompat.getDrawable(context, R.mipmap.ic_launcher);
        updateInfo = "更新：" + TimeFormatUtil.formatLatest(listenItemEntity.getUpdate_time()) + "/第" + listenItemEntity.getUpdate_album() + "期";
        String price = listenItemEntity.getPrice();
        String vipprice = listenItemEntity.getVipprice();
        String priceInfoPrefix = "价格:" + ("0.00".equals(price) ? "免费" : price) ;
        String priceInfoSuffix= "  VIP:" + ("0.00".equals(vipprice) ? "免费" : vipprice);
        priceInfo =new SpannableString(priceInfoPrefix+priceInfoSuffix);
        priceInfo.setSpan(new ForegroundColorSpan(Color.parseColor("#EA2626")), priceInfoPrefix.length(), priceInfo.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //requestAudioDetail(listenItemEntity.getId());
            //跳转到详情界面,传入条目的实体对象

            requestAudioDetail(listenItemEntity.getId());

        }
    });


    @SuppressLint("CheckResult")
    private void requestAudioDetail(String id) {
//        showDialog();
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
//            dismissDialog();
            if (res.isSuccess()) {
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("detail", res.getData());
                startContainerActivity(AudioDetailFragment.class.getCanonicalName(), mBundle);
            } else {
                if(res.getStatus()==10005||res.getStatus()==10002){
                    ToastUtil.toastBottom("请先登录");
                    startActivity(LoginActivity.class);
                }
            }

        }, e -> {
            dismissDialog();
            e.printStackTrace();
        });

    }


}
