package com.tchy.syh.search.hot_words;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;

import com.tchy.syh.search.ApiService;
import com.tchy.syh.search.entity.HistoryWordsResp;
import com.tchy.syh.search.entity.HotWordsResp;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;

import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.http.NetworkUtil;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;


public class HotWordsVM extends BaseViewModel {

    public ObservableField<HotWordsResp> hotBean=new ObservableField<>();
    public ObservableField<HistoryWordsResp> historyBean=new ObservableField<>();

    public ObservableInt hasNetwork=new ObservableInt();
    public int type;
    public HotWordsVM(Context context,int type) {
        super(context);
        this.type=type;
    }
    public BindingCommand refreshNetworkClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            onCreate();
        }
    });
    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据
        if(!NetworkUtil.isNetworkAvailable(context)){
            hasNetwork.set(8);
        }else{
            hasNetwork.set(0);
        }
        getHotExtra();
        getHistoryExtra();

    }
    public void getHotExtra(){
        HashMap<String, Object> params = new HashMap();
        params.put("m", "hot_key");
        switch (type){
            case 0:
                params.put("type", "book");
                break;
            case 1:
                params.put("type", "audio");

                break;
            case 2:
                params.put("type", "news");

                break;
            case 3 :
                params.put("type", "goods");

                break;
        }


        Observable<HotWordsResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .hot(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {
            if(res.getStatus()==-1){
                return ;
            }
            hotBean.set(res);
        }, e -> {
            e.printStackTrace();
        });
    }
    public void getHistoryExtra(){
        HashMap<String, Object> params = new HashMap();
        params.put("m", "history");
        switch (type){
            case 0:
                params.put("type", "book");
                break;
            case 1:
                params.put("type", "audio");

                break;
            case 2:
                params.put("type", "news");

                break;
            case 3 :
                params.put("type", "goods");

                break;
        }
        params.put("access_token", SPUtils.getInstance().getString("token"));

        Observable<HistoryWordsResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .history(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {
            if(res.getStatus()==-1){
                return ;
            }
            historyBean.set(res);
        }, e -> {
            e.printStackTrace();
        });
    }

    @Override
    public void onDestroy() {

    }
}
