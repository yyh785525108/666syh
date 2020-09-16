package com.tchy.syh.search.result_page;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.book.book_list.BookNewItemVm;
import com.tchy.syh.book.entity.BookListResp;
import com.tchy.syh.search.ApiService;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;

import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;


public class ResultsVM extends BaseViewModel {
    public ResultsVM(Context context,String searchKey,int type) {
        super(context);
        this.searchKey=searchKey;
        this.type=type;
    }
    public String searchKey="";
    private int pageNum = 0;
    public ObservableBoolean isRefreshFinish = new ObservableBoolean(true);
    public ObservableBoolean isLoadMoreFinish = new ObservableBoolean(true);
    public ObservableList<BookNewItemVm> resultList = new ObservableArrayList<>();

    public ItemBinding<BookNewItemVm> resultItemBinding = ItemBinding.of(new OnItemBind<BookNewItemVm>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, BookNewItemVm item) {
            switch (type) {
                case 0:
                    itemBinding.set(BR.vm, R.layout.book_new_item);
                    break;
                case 1:
                    itemBinding.set(BR.vm, R.layout.search_audio_item);
                    break;
                case 2:
                    itemBinding.set(BR.vm, R.layout.search_news_item);
                    break;
                default:
                    itemBinding.set(BR.vm, R.layout.shop_goods_item);
            }
        }
    });

    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据
        getResult(true);

    }
    public int type;

    public void getResult(boolean isRefresh) {

        HashMap<String, Object> params = new HashMap();
        params.put("keyword", searchKey);
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
        params.put("page", this.pageNum);
        params.put("access_token", SPUtils.getInstance().getString("token"));

        Observable<BookListResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .search(FormdataRequestUtil.getFormDataRequestParams(params))
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
                resultList.clear();

            Observable.fromIterable(res.getData().getList()).subscribe(bean -> {
                        resultList.add(new BookNewItemVm(context, bean));
                    }
            );
            if(isRefresh&&resultList.size()==0){
//                ToastUtil.toastBottom("没有找到相关信息");
            }
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
            getResult(true);

        }
    });
    //上拉加载
    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isLoadMoreFinish.set(false);
            pageNum++;
            getResult(false);
        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();
        Messenger.getDefault().unregister(this);
    }

    @Override
    public void registerRxBus() {
        super.registerRxBus();
        Messenger.getDefault().register(this, "search_action",String.class,  s->{
            this.searchKey=s;
            this.getResult(true);
        });
    }
}
