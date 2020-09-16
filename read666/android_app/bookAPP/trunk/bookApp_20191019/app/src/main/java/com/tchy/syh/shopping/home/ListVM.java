package com.tchy.syh.shopping.home;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import android.os.Bundle;
import android.util.Log;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.book.entity.AdsResp;
import com.tchy.syh.book.entity.ExtraBean;
import com.tchy.syh.common.CommonViewModel;
import com.tchy.syh.common.CommonWebViewActivity;
import com.tchy.syh.search.SearchFragment;
import com.tchy.syh.shopping.ApiService;
import com.tchy.syh.shopping.entity.ShopBookResp;
import com.tchy.syh.shopping.list.ListFragment;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class ListVM extends BaseViewModel {

    public ObservableList<ListItemVM> listItems = new ObservableArrayList<>();
    public ObservableField<String> bonus=new ObservableField<>();
    public ObservableList<CommonViewModel> bannerList = new ObservableArrayList<>();
    public ObservableInt bannerPageCount = new ObservableInt();
    public ObservableInt bannerIndex = new ObservableInt(0);
    public ItemBinding<CommonViewModel> bannerItemBinding = ItemBinding.of(BR.vm, R.layout.common_image_fragment);
    public final BindingViewPagerAdapter.PageTitles<CommonViewModel> pageTitles = new BindingViewPagerAdapter.PageTitles<CommonViewModel>() {
        @Override
        public CharSequence getPageTitle(int position, CommonViewModel item) {
            return "Item " + position;
        }
    };
    Disposable d;
    public void bannerAutoPlay() {
        d = Observable.interval(3000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(v -> {
                            if (bannerList != null && bannerList.size() > 0) {

                                if (bannerIndex.get() < bannerList.size() - 1) {
                                    bannerIndex.set(bannerIndex.get() + 1);
                                } else {
                                    bannerIndex.set(0);
                                }
                            } else {
                                     if (d != null) {                                     d.dispose();                                 }

                            }
                        }
                );
    }

    public ListVM(Context context) {
        super(context);
    }
    public ObservableField<ExtraBean.DataBean> extra1 = new ObservableField<>();
    public  ObservableField<ExtraBean.DataBean> extra2 = new ObservableField<>();
    public  ObservableField<ExtraBean.DataBean> extra3 = new ObservableField<>();
    public  ObservableField<ExtraBean.DataBean> extra4 = new ObservableField<>();
    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据
        bannerAutoPlay();
        List obs = new ArrayList();
        obs.add(getList());
        obs.add(getAds());
        obs.add(getExtra(15));

        Observable.merge(obs).buffer(obs.size()).subscribe(v -> {

            for (Object item : (List<Object>) v) {

                if (item instanceof AdsResp) {
                    bannerPageCount.set(((AdsResp) item).getData().size());
                    for (int i = 0; i < ((AdsResp) item).getData().size(); i++) {
                        bannerList.add(new CommonViewModel(context,((AdsResp) item).getData().get(i).getImg(),((AdsResp) item).getData().get(i).getLink(),i));
                    }
                }
                if (item instanceof ExtraBean) {
                    if(((ExtraBean) item).getData()==null||((ExtraBean) item).getData().size()==0){
                        continue;
                    }

                    if(((ExtraBean) item).getData().size()>0){
                        extra1.set(((ExtraBean)item).getData().get(0));
                    }
                    if(((ExtraBean) item).getData().size()>1){
                        extra2.set(((ExtraBean)item).getData().get(1));
                    }
                    if(((ExtraBean) item).getData().size()>2){
                        extra3.set(((ExtraBean)item).getData().get(2));
                    }
                    if(((ExtraBean) item).getData().size()>3){
                        extra4.set(((ExtraBean)item).getData().get(3));
                    }



                }
                if(item instanceof ShopBookResp){
                    Observable.fromIterable(((ShopBookResp)item).getData().getList()).subscribe(b ->
                            {
                                listItems.add(new ListItemVM(context, b));
                            }
                    );

                }


            }

        }, e -> {
            Log.e("sort", "onCreate: "+e.toString() );


        }, () -> {
        });

    }
    public Observable<AdsResp> getAds() {
        HashMap<String, Object> params = new HashMap();
        params.put("id", 14);
        params.put("num", 5);

        Observable<AdsResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .ads(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        return observable;
    }
    public Observable<ExtraBean> getExtra(int index) {
        HashMap<String, Object> params = new HashMap();
        params.put("id", index);
        params.put("num", 4);
        Observable<ExtraBean> observable = RetrofitClient.getInstance().create(ApiService.class)
                .extrasList(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        return observable;
    }

    public Observable<ShopBookResp> getList() {
        HashMap<String, Object> params = new HashMap();

        params.put("access_token", SPUtils.getInstance().getString("token"));
//        params.put("page", this.pageNum);
        Observable<ShopBookResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .book_list(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
       return observable;

    }


    public final ItemBinding itemBinding = ItemBinding.of(BR.vm,R.layout.shop_list_item);
    @Override
    public void onDestroy() {
        super.onDestroy();
             if (d != null) {                                     d.dispose();                                 }

    }
    public  BindingCommand moreClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            startContainerActivity(ListFragment .class.getCanonicalName());
        }
    });
   public  BindingCommand searchClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle=new Bundle();
            bundle.putInt("type",3);
            startContainerActivity(SearchFragment.class.getCanonicalName(),bundle);
        }
    });
    public BindingCommand extraClick1 = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle=new Bundle();
            bundle.putString("url",extra1.get().getLink());
            startActivity(CommonWebViewActivity.class,bundle);
        }
    });
    public BindingCommand extraClick2 = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle=new Bundle();
            bundle.putString("url",extra2.get().getLink());
            startActivity(CommonWebViewActivity.class,bundle);
        }
    });
    public BindingCommand extraClick3 = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle=new Bundle();
            bundle.putString("url",extra3.get().getLink());
            startActivity(CommonWebViewActivity.class,bundle);
        }
    });
    public BindingCommand extraClick4 = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle=new Bundle();
            bundle.putString("url",extra4.get().getLink());
            startActivity(CommonWebViewActivity.class,bundle);
        }
    });

}
