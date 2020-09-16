package com.tchy.syh.my;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import com.tchy.syh.R;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

import com.tchy.syh.BR;
import com.tchy.syh.my.entity.RecommandListResp;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;

import java.util.HashMap;

public class RecommandBookListVM extends BaseViewModel {

    public ObservableList<RecommandBookListItemVM> listItems = new ObservableArrayList<>();


    public ItemBinding<RecommandBookListItemVM> itemBinding = ItemBinding.of(BR.vm, R.layout.recommand_book_list_item);

    public RecommandBookListVM(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据

        getList();

    }

    public void getList(){
        HashMap<String, Object> params = new HashMap();

        params.put("access_token", SPUtils.getInstance().getString("token"));
//        params.put("page", this.pageNum);
        Observable<RecommandListResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .book_vote(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(v -> {
            if(v.getStatus()==1){
                Observable.fromIterable(v.getData()).subscribe(item->{
                    listItems.add(new RecommandBookListItemVM(context,item));
                });

            }else{

            }


        },e -> {
            e.printStackTrace();
        });

    }
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
