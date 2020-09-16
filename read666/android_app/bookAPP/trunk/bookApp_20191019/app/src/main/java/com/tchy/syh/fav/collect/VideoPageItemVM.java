package com.tchy.syh.fav.collect;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.history.HistoryListItemVM;
import com.tchy.syh.history.entity.HistoryResp;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;

import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;


public class VideoPageItemVM extends BaseViewModel {

    public ObservableInt notifyNum = new ObservableInt();
    private int itemIndex = 0;
    private int pageNum = 0;
    public ObservableBoolean isRefreshFinish = new ObservableBoolean(true);
    public ObservableBoolean isLoadMoreFinish = new ObservableBoolean(true);


    public ObservableField<String> id = new ObservableField<>("1");
    public ObservableList<HistoryListItemVM> list = new ObservableArrayList<>();
    public ItemBinding<HistoryListItemVM> itemBindings = ItemBinding.of(BR.vm, R.layout.history_list_item);


    public VideoPageItemVM(Context context) {
        super(context);
        onCreate();
    }




    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据
        getList(true);

    }


    public void getList(boolean isRefresh) {

        HashMap<String, Object> params = new HashMap();

        params.put("page", pageNum);
        params.put("type", "book");
        params.put("access_token", SPUtils.getInstance().getString("token"));
        Observable<HistoryResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .history_list(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {

            if (res.getStatus() != 1) {
                return;

            }
            if (isRefresh)
                list.clear();

            Observable.fromIterable(res.getData().getList()).subscribe(bean -> {



                list.add(new HistoryListItemVM(context, bean,false));
                    }
            );
            if (isRefresh)
                this.isRefreshFinish.set(true);
            else
                this.isLoadMoreFinish.set(true);


        }, e -> {
            if (isRefresh) {
                this.isRefreshFinish.set(true);
            } else
                this.isLoadMoreFinish.set(true);
            e.printStackTrace();
        });
    }

    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isRefreshFinish.set(false);
            pageNum = 0;
            //重新请求
            getList(true);

        }
    });
    //上拉加载
    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isLoadMoreFinish.set(false);
            pageNum++;
//            getList(false);
        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
