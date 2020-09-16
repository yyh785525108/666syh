package com.tchy.syh.listen;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;


import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.cons.HttpCons;
import com.tchy.syh.listen.base.AdaptationViewModel;
import com.tchy.syh.listen.entity.ListenEntity;
import com.tchy.syh.listen.entity.ResListen;
import com.tchy.syh.listen.entity.ResponseWrapper;
import com.tchy.syh.userAccount.userLogin.LoginActivity;
import com.tchy.syh.utils.FormUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.http.NetworkUtil;
import me.goldze.mvvmhabit.http.ResponseThrowable;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class ListenViewModel extends AdaptationViewModel {
    private static final int ACTION_FLAG_REQ_DATA = 1;
    private static final int ACTION_FLAG_REFRESH_DATA = 2;
    private static final int ACTION_FLAG_LOADMORE_DATA = 3;
    public ObservableInt hasNetwork=new ObservableInt(0);
    private int currentPage=0;
    public ListenViewModel(Context context) {
        super(context);
    }

    public ObservableBoolean isFinishRefreshing = new ObservableBoolean(false);
    public ObservableBoolean isFinishLoadMore = new ObservableBoolean(false);


    @Override
    public void onCreate() {
        super.onCreate();
        if(!NetworkUtil.isNetworkAvailable(context)){
            hasNetwork.set(8);
        }else{
            hasNetwork.set(0);
        }
        requestListenData(ACTION_FLAG_REQ_DATA);
    }
    public BindingCommand refreshNetworkClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            onCreate();
        }
    });

    @SuppressLint("CheckResult")
    private void requestListenData(final int actionFlag) {
        if (actionFlag==ACTION_FLAG_LOADMORE_DATA){
            currentPage++;
        }else {
            currentPage=0;
        }
        Map<String, String> para = new HashMap<>();
        para.put("appkey", HttpCons.APP_KEY);
        para.put("access_token", SPUtils.getInstance().getString("token"));
        para.put("type", "2");
        para.put("page",currentPage+"");
        String sig = FormUtil.genSig(para);
        para.put("sig", sig);

        RetrofitClient.getInstance().create(ApiService.class)
                .listListenData(para)
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .subscribe((Consumer<ResponseWrapper<ResListen>>) responseWrapper -> {
                    //关闭对话框
                    dismissDialog();
                    //清除列表
                    if (actionFlag != ACTION_FLAG_LOADMORE_DATA) {
                        observableList.clear();
                    }
                    //刷新完成收回
                    isFinishRefreshing.set(!isFinishRefreshing.get());
                    isFinishLoadMore.set(!isFinishLoadMore.get());
                    //请求成功
                    if (responseWrapper.isSuccess()) {
                        //将实体赋给全局变量，双向绑定动态添加Item

                        List<ListenEntity.ListenItemEntity> list = responseWrapper.getData().getList();
                        if (list==null||list.isEmpty()){
                            ToastUtil.toastBottom("没有更多了");
                            return;
                        }
                        for (ListenEntity.ListenItemEntity entity : list) {
                            //observableList.add(new ListenItemViewModel(context, entity));

                            //动态添加Item
                            observableList.add(new ListenItemViewModel(context, entity));
                        }
                    } else {
                        //code错误时也可以定义Observable回调到View层去处理
                        ToastUtil.toastBottom(responseWrapper.getInfo());
                        if(responseWrapper.getStatus()==10005||responseWrapper.getStatus()==10002){
                            startActivity(LoginActivity.class);
                        }
                    }
                }, (Consumer<ResponseThrowable>) throwable -> {
                    dismissDialog();

                   /* ToastUtil.toastBottom(throwable.message);
                    throwable.printStackTrace();*/

                });
    }


    //给RecyclerView添加ObservableList
    public ObservableList<ListenItemViewModel> observableList = new ObservableArrayList<>();
    //给RecyclerView添加ItemView
    public ItemBinding<ListenItemViewModel> itemBinding = ItemBinding.of(BR.viewModel, R.layout.item_listen);

    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            requestListenData(ACTION_FLAG_LOADMORE_DATA);
        }
    });
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            requestListenData(ACTION_FLAG_REFRESH_DATA);
        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
