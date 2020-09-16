package com.tchy.syh.shopping.list;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import android.os.Bundle;
import android.util.Log;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.book.entity.LearnCate;
import com.tchy.syh.read.ApiService;
import com.tchy.syh.search.SearchFragment;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;


public class PageListViewModel extends BaseViewModel {
    public ObservableBoolean isInitFinish=new ObservableBoolean(false);
    public ObservableInt notifyNum = new ObservableInt();
    private int itemIndex = 0;
    public ObservableField<String> search = new ObservableField<>("");
    public ObservableInt pagerIndex = new ObservableInt(0);

    public ObservableList<PageItemViewModel> pageList = new ObservableArrayList<>();
    public ItemBinding<PageItemViewModel> readListPageBinding = ItemBinding.of(BR.vm, R.layout.shop_more_list_pager_item_frag);


    public final BindingViewPagerAdapter.PageTitles<PageItemViewModel> pageTitles = new BindingViewPagerAdapter.PageTitles<PageItemViewModel>() {
        @Override
        public CharSequence getPageTitle(int position, PageItemViewModel item) {
            return categoryList.get(position).getName();
        }
    };

    public PageListViewModel(Context context) {
        super(context);
        getCategory();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据
        LearnCate.DataBean bean=new LearnCate.DataBean("","全部");

        this.categoryList.add(bean);


        Log.d("sort", "onCreate: " + pageList.size());


    }


    Disposable d = null;

    public ObservableList<LearnCate.DataBean> categoryList = new ObservableArrayList<>();

    public void getCategory() {
        HashMap<String, Object> params = new HashMap();
        params.put("type", "book");
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
                        PageItemViewModel pageItemViewModel = new PageItemViewModel(context, bean);
                        pageList.add(pageItemViewModel);
//                        pageItemViewModel.onCreate();
                    },e->{},()->{
                     isInitFinish.set(true);
                    }

            );
        },e->{

        });

    }
    public  BindingCommand searchClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle=new Bundle();
            bundle.putInt("type",3);
            startContainerActivity(SearchFragment.class.getCanonicalName(),bundle);
        }
    });
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
