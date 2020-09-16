package com.tchy.syh.book.book_list;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import android.os.Bundle;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.book.ApiService;
import com.tchy.syh.book.entity.LearnCate;
import com.tchy.syh.search.SearchFragment;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;


public class BookListViewModel extends BaseViewModel {
    public ObservableBoolean isInitFinish = new ObservableBoolean(false);

    public ObservableInt notifyNum = new ObservableInt();
    public ObservableField<String> search = new ObservableField<>("");
    public ObservableInt pagerIndex = new ObservableInt(0);
    public List<LearnCate.DataBean> cates=new ArrayList<>();
    public ObservableList<BookListItemViewModel> pageList = new ObservableArrayList<>();
    public ItemBinding<BookListItemViewModel> bookListPagerBinding = ItemBinding.of(BR.vm, R.layout.book_list_pager_item_frag);


    public ObservableField<LearnCate.DataBean> cate1=new ObservableField<>();

    public final BindingViewPagerAdapter.PageTitles<BookListItemViewModel> pageTitles = new BindingViewPagerAdapter.PageTitles<BookListItemViewModel>() {
        @Override
        public CharSequence getPageTitle(int position, BookListItemViewModel item) {
            return cates.get(position).getName();
        }
    };
    public Observable<LearnCate> getCategory() {
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

            cates.addAll(res.getData());
            isInitFinish.set(true);

            for (int i = 0; i <this.cates.size() ; i++) {
                LearnCate.DataBean bean= this.cates.get(i);
                BookListItemViewModel bookListItemViewModel=new BookListItemViewModel(context,i,bean.getId());
                pageList.add(bookListItemViewModel);
                bookListItemViewModel.onCreate();

            }
//            if(res.getData().size()>=3){
//                cate2.set(res.getData().get(0));
//                cate3.set(res.getData().get(1));
//                cate4.set(res.getData().get(2));
//            }

            setCurrentPage();
//            Observable.fromIterable(res.getData()).subscribe(bean -> {
//                        VideoPageItemVM pageItemViewModel = new VideoPageItemVM(context, bean);
//                        pageList.add(pageItemViewModel);
//                        pageItemViewModel.onCreate();
//                    },e->{},()->{
//                        isInitFinish.set(true);
//                    }
//
//            );
        },e -> {
            e.printStackTrace();
            isInitFinish.set(true);

        });
        return observable;
    }
    private String id="";
    public BookListViewModel(Context context) {
        super(context);
        LearnCate.DataBean bean =new LearnCate.DataBean("","全部");
        cates.add(bean);
        cate1.set(bean);
    }


    public void setId(String id, List<LearnCate.DataBean> cates){
        this.id=id;
//        this.cates.addAll(cates);
//        for (int i = 0; i <this.cates.size() ; i++) {
//            LearnCate.DataBean bean= this.cates.get(i);
//            BookListItemViewModel bookListItemViewModel=new BookListItemViewModel(context,i,bean.getId());
//            pageList.add(bookListItemViewModel);
//            bookListItemViewModel.onCreate();
//        }
    }
    public void setCurrentPage(){
        if(this.id!=null&&this.id.length()>0){
            for (int i = 0; i <cates.size() ; i++) {
               LearnCate.DataBean bean= cates.get(i);
               if(id.equals(bean.getId())){
                    pagerIndex.set(i);
               }

            }
        }else{
            pagerIndex.set(0);
        }

    }
    @Override
    public void onCreate() {
        super.onCreate();
        getCategory();


    }

    public BindingCommand leftClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ((Activity)context).finish();
        }
    });
    public BindingCommand rightClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle=new Bundle();
            bundle.putInt("type",0);
            startContainerActivity(SearchFragment.class.getCanonicalName(),bundle );
        }
    });
    Disposable d=null;

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
    public ObservableInt type=new ObservableInt(0);
    public BindingCommand typechanged=new BindingCommand(new BindingConsumer<Integer>() {
        @Override
        public void call(Integer o) {
            pagerIndex.set(o);
        }
    });
    @Override
    public void removeRxBus() {
        super.removeRxBus();
    }
}
