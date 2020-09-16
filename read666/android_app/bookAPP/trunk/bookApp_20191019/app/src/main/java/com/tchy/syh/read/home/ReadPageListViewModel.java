package com.tchy.syh.read.home;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import android.util.Log;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.book.entity.LearnCate;
import com.tchy.syh.read.ApiService;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.http.NetworkUtil;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;


public class ReadPageListViewModel extends BaseViewModel {
    public ObservableBoolean isInitFinish=new ObservableBoolean(false);
    public ObservableInt notifyNum = new ObservableInt();
    public ObservableInt hasNetwork = new ObservableInt();

    private int itemIndex = 0;
    public ObservableField<String> search = new ObservableField<>("");
    public ObservableInt pagerIndex = new ObservableInt(0);

    public ObservableList<ReadPageItemViewModel> pageList = new ObservableArrayList<>();
    public ItemBinding<ReadPageItemViewModel> readListPageBinding = ItemBinding.of(BR.vm, R.layout.read_list_pager_item_frag);


    public final BindingViewPagerAdapter.PageTitles<ReadPageItemViewModel> pageTitles = new BindingViewPagerAdapter.PageTitles<ReadPageItemViewModel>() {
        @Override
        public CharSequence getPageTitle(int position, ReadPageItemViewModel item) {
            return categoryList.get(position).getName();
        }
    };

    public ReadPageListViewModel(Context context) {
        super(context);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据

        if(!NetworkUtil.isNetworkAvailable(context)){
            hasNetwork.set(8);
        }else{
            hasNetwork.set(0);
        }
        LearnCate.DataBean bean=new LearnCate.DataBean("","全部");
        categoryList.clear();
        this.categoryList.add(bean);


        Log.d("sort", "onCreate: " + pageList.size());


    }
    public BindingCommand refreshNetworkClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            onCreate();
            getCategory();
        }
    });

    Disposable d = null;

    public ObservableList<LearnCate.DataBean> categoryList = new ObservableArrayList<>();

    public void getCategory() {
        HashMap<String, Object> params = new HashMap();
        params.put("type", "news");
        Observable<LearnCate> observable = RetrofitClient.getInstance().create(ApiService.class)
                .learn_cate(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(res -> {

            if (res.getStatus() != 1) {
                return;
            }
//            for (LearnCate.DataBean bean:res.getData()) {
//                this.categoryList.add(bean);
//            }
            isInitFinish.set(true);
            this.categoryList.addAll(res.getData());
            Observable.fromIterable(this.categoryList).subscribe(bean -> {
                        ReadPageItemViewModel pageItemViewModel = new ReadPageItemViewModel(context, bean);
                        pageList.add(pageItemViewModel);
//                        pageItemViewModel.onCreate();
                    },e->{},()->{
                     isInitFinish.set(true);
                    }

            );
        },e->{

        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
