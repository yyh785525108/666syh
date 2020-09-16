package com.tchy.syh.my.spreadranking;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.my.ApiService;
import com.tchy.syh.my.entity.SpreadListResp;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;

import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;

public class ListVM extends BaseViewModel {

    public ObservableList<ListItemVM> listItems = new ObservableArrayList<>();


    public final ItemBinding itemBinding = ItemBinding.of(new OnItemBind<Object>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, Object item) {
            itemBinding.set(BR.vm, position == 0 ? R.layout.spread_list_header : R.layout.spread_list_item);
        }
    });

    public ListVM(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据

        fans_top();

    }

    public void fans_top() {
        HashMap<String, Object> params = new HashMap();

        params.put("access_token", SPUtils.getInstance().getString("token"));
//        params.put("page", this.pageNum);
        Observable<SpreadListResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .fans_top(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(v -> {
            listItems.add(new ListItemVM(context, v.getData().getList().get(0),v.getData().getList().get(1),v.getData().getList().get(2),v.getData().getMine()));
            Observable.fromIterable(v.getData().getList()).skip(3).subscribe(item ->
                    {
                        listItems.add(new ListItemVM(context, item));
                    }
            );
        },e -> {
            e.printStackTrace();
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
