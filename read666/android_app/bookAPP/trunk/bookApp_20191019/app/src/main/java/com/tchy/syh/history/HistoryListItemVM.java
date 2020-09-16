package com.tchy.syh.history;

import android.content.Context;
import android.os.Bundle;

import com.tchy.syh.book.book_detail.BookVideoPlayerActivity;
import com.tchy.syh.history.entity.HistoryResp;
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


public class HistoryListItemVM extends BaseViewModel {
    public HistoryResp.DataBean.ListBean bean ;
    boolean isAudio=false;
    public HistoryListItemVM(Context context, HistoryResp.DataBean.ListBean bean ,boolean isAudio){
        super(context);
        this.bean=bean;
        this.isAudio=isAudio;
    }

    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {


            if(isAudio){
                requestAudioDetail(bean.getId());
            }else{
                Bundle mBundle = new Bundle();
                mBundle.putString("id",bean.getId());
                startActivity(BookVideoPlayerActivity.class, mBundle);
            }

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
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}