package com.tchy.syh.book.book_list;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tchy.syh.book.book_detail.BookVideoPlayerActivity;
import com.tchy.syh.book.entity.BookListResp;
import com.tchy.syh.history.ApiService;
import com.tchy.syh.listen.detail.AudioDetailFragment;
import com.tchy.syh.listen.entity.AudioDetailEntity;
import com.tchy.syh.listen.entity.ResponseWrapper;
import com.tchy.syh.read.detail.ReadDetailFragment;
import com.tchy.syh.shopping.detail.GoodsDetailFrag;
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

public class BookNewItemVm extends BaseViewModel{
    public BookListResp.DataBean.ListBean entity;
    public BookNewItemVm(Context context, BookListResp.DataBean.ListBean entity) {
        super(context);
        this.entity = entity;
    }

    public BindingCommand itemGoodsClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle mBundle = new Bundle();
            mBundle.putString("id",entity.getId());
            startContainerActivity(GoodsDetailFrag.class.getCanonicalName(),mBundle);
        }
    });
    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle mBundle = new Bundle();
            mBundle.putString("id",entity.getId());
            startActivity(BookVideoPlayerActivity.class, mBundle);
        }
    });
    public BindingCommand itemNewsClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle mBundle = new Bundle();
            mBundle.putString("id",entity.getId());
            startContainerActivity(ReadDetailFragment.class.getCanonicalName(),mBundle);
        }
    });
    public BindingCommand itemAudioClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //todo 跳转至音频详情
            Log.d("sort", "call: e"+entity.getId());
            requestAudioDetail(entity.getId());
//
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
