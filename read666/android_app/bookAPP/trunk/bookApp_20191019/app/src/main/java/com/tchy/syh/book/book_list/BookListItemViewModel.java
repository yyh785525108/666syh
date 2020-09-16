package com.tchy.syh.book.book_list;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import android.text.TextUtils;
import android.util.Log;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.book.ApiService;
import com.tchy.syh.book.entity.BookListResp;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;


public class BookListItemViewModel extends BaseViewModel {
    public ObservableInt notifyNum = new ObservableInt();
    private int itemIndex = 0;
    private int pageNum = 0;
    public ObservableBoolean isRefreshFinish = new ObservableBoolean(true);
    public ObservableBoolean isLoadMoreFinish = new ObservableBoolean(true);


    public ObservableField<String> search = new ObservableField<>("");
    public ObservableList<BookNewItemVm> booksList = new ObservableArrayList<>();
    public ItemBinding<BookNewItemVm> booksItemBinding = ItemBinding.of(BR.vm, R.layout.book_new_item);


    private int index;
    private String id;
    public BookListItemViewModel(Context context, int index,String id) {
        super(context);
        this.index = index;
        this.id=id;
        Log.d("sort", "VideoPageItemVM: " + this.index);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据
        Log.d("sort", "onCreate111111111: " + this.index);
        getBooks(true);

    }


    public void getBooks(boolean isRefresh) {

        HashMap<String, Object> params = new HashMap();
        if(!TextUtils.isEmpty(id))
            params.put("fid", id);
        params.put("type", 1);
        params.put("orderby", 0);
        params.put("page", this.pageNum);
        Observable<BookListResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .learn(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {

            if (res.getStatus() != 1) {
                return;

            }
            if(pageNum>50){
                ToastUtil.toastBottom("没有更多内容了！");
                return ;
            }
            if(pageNum>0&&res.getData().getList().size()==0){
                ToastUtil.toastBottom("没有更多内容了！");
                return ;
            }
            if (isRefresh)
                booksList.clear();

            Observable.fromIterable(res.getData().getList()).subscribe(bean -> {
                        booksList.add(new BookNewItemVm(context, bean));
                    }
            );



        }, e -> {

            e.printStackTrace();
        },()->{});
        if (isRefresh) {
            this.isRefreshFinish.set(true);
        } else
            this.isLoadMoreFinish.set(true);
    }

    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isRefreshFinish.set(false);
            pageNum = 0;
            //重新请求
            getBooks(true);

        }
    });
    //上拉加载
    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isLoadMoreFinish.set(false);
            pageNum++;
            getBooks(false);
        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
