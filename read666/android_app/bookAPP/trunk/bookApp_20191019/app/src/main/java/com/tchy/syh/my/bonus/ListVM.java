package com.tchy.syh.my.bonus;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableList;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.my.ApiService;
import com.tchy.syh.my.entity.BonusResp;
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

public class ListVM extends BaseViewModel {

    public ObservableList<ListItemVM> listItems = new ObservableArrayList<>();
    public ObservableField<String> bonus=new ObservableField<>();

    public final ItemBinding itemBinding = ItemBinding.of(BR.vm,R.layout.my_bonus_item);

    public ListVM(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据
        bonus.set(SPUtils.getInstance().getString("integral"));
        getList();

    }

    public void getList() {
        HashMap<String, Object> params = new HashMap();

        params.put("access_token", SPUtils.getInstance().getString("token"));
//        params.put("page", this.pageNum);
        Observable<BonusResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .integral_log(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(v -> {
            Observable.fromIterable(v.getData().getList()).subscribe(item ->
                    {
                        listItems.add(new ListItemVM(context, item));
                    }
            );
        },e -> {
            e.printStackTrace();
        });

    }
    public BindingCommand shopClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            startContainerActivity(com.tchy.syh.shopping.home.ListFragment.class.getCanonicalName());
        }
    });
    public BindingCommand recordsClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startContainerActivity(com.tchy.syh.my.bonusRecord.ListFragment.class.getCanonicalName());
        }
    });
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
