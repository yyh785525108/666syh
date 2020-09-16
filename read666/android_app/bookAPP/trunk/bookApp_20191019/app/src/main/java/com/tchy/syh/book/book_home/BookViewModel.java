package com.tchy.syh.book.book_home;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import android.os.Bundle;
import android.os.Parcelable;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.book.ApiService;
import com.tchy.syh.book.book_detail.entity.FreeBooksResp;
import com.tchy.syh.book.book_list.BookListFragment;
import com.tchy.syh.book.entity.AdsResp;
import com.tchy.syh.book.entity.BookListResp;
import com.tchy.syh.book.book_list.BookNewItemVm;
import com.tchy.syh.book.entity.ExtraBean;
import com.tchy.syh.book.entity.LearnCate;
import com.tchy.syh.common.CommonViewModel;
import com.tchy.syh.common.CommonWebViewActivity;
import com.tchy.syh.custom.CustomLoadingDialog;
import com.tchy.syh.listen.ListenFragment;
import com.tchy.syh.listen.detail.AudioDetailFragment;
import com.tchy.syh.listen.entity.AudioDetailEntity;
import com.tchy.syh.listen.entity.ResponseWrapper;
import com.tchy.syh.read.home.ReadListFragment;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

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
import me.goldze.mvvmhabit.http.NetworkUtil;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.tatarka.bindingcollectionadapter2.BindingViewPagerAdapter;
import me.tatarka.bindingcollectionadapter2.ItemBinding;


public class BookViewModel extends BaseViewModel {
    public ObservableInt hasNetwork=new ObservableInt(0);
    public ObservableField<String> selectFreeIntro = new ObservableField<>("");
    public ObservableField<String> selectFreeFName = new ObservableField<>("");
    public ObservableField<String> selectFreeName = new ObservableField<>("");
    public ObservableField<String> selectLimitTime = new ObservableField<>("00:00:00");
    public ObservableInt notifyNum = new ObservableInt();
    private int itemIndex = 0;
    public ObservableField<String> search = new ObservableField<>("");
    public ObservableInt bannerPageCount = new ObservableInt();
    public ObservableInt freePageCount = new ObservableInt();
    public ObservableInt bannerIndex = new ObservableInt(0);

    public ObservableList<CommonViewModel> bannerList = new ObservableArrayList<>();
    public ObservableList<FreeBookItemVM> freeBookList = new ObservableArrayList<>();
    public ObservableList<BookNewItemVm> recentBookList = new ObservableArrayList<>();
    public ObservableList<BookNewItemVm> dayBookList = new ObservableArrayList<>();
    public ObservableList<NavItemVM> navList = new ObservableArrayList<>();

    public ObservableField<ExtraBean.DataBean> extra1 = new ObservableField<>();
    public  ObservableField<ExtraBean.DataBean> extra2 = new ObservableField<>();
    public  ObservableField<ExtraBean.DataBean> extra3 = new ObservableField<>();
    public  ObservableField<ExtraBean.DataBean> extra4 = new ObservableField<>();

    public ObservableList<LearnCate.DataBean> navBeanList = new ObservableArrayList<>();

    public ObservableField<LearnCate.DataBean> nav1 = new ObservableField<>();
    public  ObservableField<LearnCate.DataBean> nav2 = new ObservableField<>();
    public  ObservableField<LearnCate.DataBean> nav3 = new ObservableField<>();

    public ItemBinding<CommonViewModel> freeBookItemBinding = ItemBinding.of(BR.vm, R.layout.cliped_image_fragment);
    public ItemBinding<CommonViewModel> bannerItemBinding = ItemBinding.of(BR.vm, R.layout.common_image_fragment_round);
    public ItemBinding<NavItemVM> navItemBinding = ItemBinding.of(BR.vm, R.layout.book_navigator_item);

    public ItemBinding<BookNewItemVm> bookNewItemBinding = ItemBinding.of(BR.vm, R.layout.book_new_item);
    public ItemBinding<BookNewItemVm> bookAudioItemBinding = ItemBinding.of(BR.vm, R.layout.book_audio_item);


    public final BindingViewPagerAdapter.PageTitles<CommonViewModel> pageTitles = new BindingViewPagerAdapter.PageTitles<CommonViewModel>() {
        @Override
        public CharSequence getPageTitle(int position, CommonViewModel item) {
            return "Item " + position;
        }
    };


    public BookViewModel(Context context) {
        super(context);
    }

    CustomLoadingDialog customBaseDialog;
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
//            Bundle bundle=new Bundle();
//            bundle.putString("url",extra2.get().getLink());
//            startActivity(CommonWebViewActivity.class,bundle);

            requestAudioDetail("85");


        }
    });
    private void requestAudioDetail(String id) {
        HashMap<String, Object> params = new HashMap();
        params.put("id", id);
        params.put("access_token", SPUtils.getInstance().getString("token"));
        Observable<ResponseWrapper<AudioDetailEntity>> observable = RetrofitClient.getInstance().create(com.tchy.syh.history.ApiService.class)
                .learn_info(FormdataRequestUtil.getFormDataRequestParams(params))
                .delay(500, TimeUnit.MILLISECONDS)
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {
            if (res.isSuccess()) {
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("detail", res.getData());
                startContainerActivity(AudioDetailFragment.class.getCanonicalName(), mBundle);
            } else {
                if (res.getStatus() == 1005) {
                    ToastUtil.toastBottom("未登录或登录状态失效");
                }
            }

        }, e -> {
            e.printStackTrace();
        });
    }
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

    public BindingCommand audioFuctionClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle=new Bundle();
            startContainerActivity(ListenFragment.class.getCanonicalName(),bundle);
        }
    });
    public BindingCommand shopFuctionClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle=new Bundle();
            startContainerActivity(ReadListFragment.class.getCanonicalName(),bundle);
        }
    });
    public BindingCommand refreshNetworkClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            onCreate();
        }
    });
    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据
        if(!NetworkUtil.isNetworkAvailable(context)){
            hasNetwork.set(8);
        }else{
            hasNetwork.set(0);
        }
        List obs = new ArrayList();
        obs.add(getAds());
        obs.add(getCategory());
        obs.add(getRecentBook(1));
        obs.add(getFreeBooks());
        obs.add(getExtra());
        navList.clear();
        navList.add(new NavItemVM(context, null,"全部", "", cates));
        customBaseDialog = new CustomLoadingDialog(context);
        customBaseDialog.show();
        Observable.merge(obs).buffer(obs.size()).subscribe(v -> {
            for (Object item : (List<Object>) v) {

                if (item instanceof AdsResp) {
                    for (int i = 0; i < ((AdsResp) item).getData().size(); i++) {
                        bannerList.add(new CommonViewModel(context,((AdsResp) item).getData().get(i).getImg(),((AdsResp) item).getData().get(i).getLink(),i));
                    }
                }
                if (item instanceof ExtraBean) {
                    extra1.set(((ExtraBean) item).getData().get(0));
                    if(((ExtraBean) item).getData().size()>1){
                        extra2.set(((ExtraBean) item).getData().get(1));
                    }
                    if(((ExtraBean) item).getData().size()>2){
                        extra3.set(((ExtraBean) item).getData().get(2));
                    }
                    if(((ExtraBean) item).getData().size()>3){
                        extra4.set(((ExtraBean) item).getData().get(3));
                    }



                }
                if (item instanceof FreeBooksResp) {


                    FreeBooksResp fs = (FreeBooksResp) item;
                    if(fs.getData().getList().size()>1){
                        for (int i = 0; i <99 ; i++) {
                            Observable.fromIterable(fs.getData().getList()).subscribe(bean -> {
                                        freeBookList.add(new FreeBookItemVM(context, bean));
                                    }
                            );
                        }
                    }else{
                        Observable.fromIterable(fs.getData().getList()).subscribe(bean -> {
                                    freeBookList.add(new FreeBookItemVM(context, bean));
                                }
                        );
                    }


                    freePageCount.set(freeBookList.size());
                }
                if (item instanceof LearnCate) {
                    LearnCate res = (LearnCate) item;
                    cates = res.getData();
                    for (LearnCate.DataBean bean : res.getData()) {
                        NavItemVM navItemVM = new NavItemVM(context, bean.getIcon(), bean.getName(), bean.getId(), res.getData());
                        navList.add(navItemVM);

                        navBeanList.add(bean);
                    }

                    nav1.set(res.getData().get(0));
                    nav2.set(res.getData().get(1));
                    nav3.set(res.getData().get(2));
                }
                if (item instanceof BookListResp) {
                    BookListResp res = (BookListResp) item;
                    Observable.fromIterable(res.getData().getList()).take(3).subscribe(bean -> {
                                recentBookList.add(new BookNewItemVm(context, bean));
                            }
                    );
                }
            }
            customBaseDialog.cancel();

        }, e -> {

            customBaseDialog.cancel();


        }, () -> {
        });
//
        obs.clear();
        obs.add(getRecentBook(2));
        Observable.merge(obs).buffer(obs.size()).subscribe(v -> {
            for (Object item : (List<Object>) v) {
                if (item instanceof BookListResp) {
                    BookListResp res = (BookListResp) item;
                    Observable.fromIterable(res.getData().getList()).take(3).subscribe(bean -> {
                                dayBookList.add(new BookNewItemVm(context, bean));
                            }
                    );
                }
            }
            customBaseDialog.cancel();

        }, e -> {

            customBaseDialog.cancel();


        }, () -> {
        });
        bannerAutoPlay();
    }

    public Observable<ExtraBean> getExtra() {
        HashMap<String, Object> params = new HashMap();
        params.put("id", 13);
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

//    public void getNavigator() {
//        HashMap<String, Object> params = new HashMap();
//        params.put("id", 12);
//        params.put("num", 4);
//
//        Observable<AdsResp> observable = RetrofitClient.getInstance().create(ApiService.class)
//                .ads(FormdataRequestUtil.getFormDataRequestParams(params))
//                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
//                .compose(RxUtils.schedulersTransformer()) //线程调度
//                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
//                .doOnSubscribe(disposable -> {
//                });
//
//        observable.subscribe(res -> {
//            if (res.getStatus() == -1) {
//                return;
//            }
////            navList.add(new CommonViewModel(context, "","-1",1,"全部"));
//            for (int i = 0; i < res.getData().size(); i++) {
//                AdsResp.DataBean bean = res.getData().get(i);
//                navList.add(new CommonViewModel(context, bean.getImg(), bean.getLink(), i, bean.getName(), bean.getId()));
//            }
//
////            SearchResultsResp.DataBean bean = new SearchResultsResp.DataBean();
////            bean.setName("splash_activity");
////            bean.setLink("http://www.baidu.com");
////            bean.setImg("https://pic.chinaz.com/2018/0416/18041617310389080.jpg");
//
//        }, e -> {
//            e.printStackTrace();
//        });
//    }

    Disposable d = null;


    public void bannerAutoPlay() {
        d = Observable.interval(2500, TimeUnit.MILLISECONDS)
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

    public Observable<BookListResp> getRecentBook(int type) {
        HashMap<String, Object> params = new HashMap();
        params.put("type", type);
        params.put("orderby", 0);

        Observable<BookListResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .learn(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

//        observable.subscribe(res -> {
//            Observable.fromIterable(res.getData().getList()).take(5).subscribe(bean -> {
//                        recentBookList.add(new BookNewItemVm(context, bean));
//                    }
//            );
//
//        }, e -> {
//            e.printStackTrace();
//        });
        return observable;
    }

    public Observable<FreeBooksResp> getFreeBooks() {
        HashMap<String, Object> params = new HashMap();
        params.put("type", 0);

        Observable<FreeBooksResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .freeList(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

//        observable.subscribe(res -> {
////            if(res.getStatus()==-1){
////                return ;
////            }
////            AdsResp.DataBean bean = new AdsResp.DataBean();
////            bean.setName("splash_activity");
////            bean.setLink("http://www.baidu.com");
////            bean.setImg("https://avatar.csdn.net/F/A/2/1_love__coder.jpg?1531211387");
////
////
////            freeBookList.add(new CommonViewModel(context, "https://csdnimg.cn/pubfooter/images/csdn_cs_qr.png", "http://m.taobao.com", 1));
////            freeBookList.add(new CommonViewModel(context, bean.getImg(), bean.getLink(), 2));
////            freeBookList.add(new CommonViewModel(context, bean.getImg(), bean.getLink(), 3));
////            freeBookList.add(new CommonViewModel(context, bean.getImg(), bean.getLink(), 4));
////            freeBookList.add(new CommonViewModel(context, bean.getImg(), "http://www.bing.com", 5));
////            freePageCount.set(freeBookList.size());
//
//        }, e -> {
//            e.printStackTrace();
//        });
        return observable;
    }

    public Observable<AdsResp> getAds() {
        HashMap<String, Object> params = new HashMap();
        params.put("id", 11);
        params.put("num", 5);

        Observable<AdsResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .ads(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

//        observable.subscribe(res -> {
////            if(res.getStatus()==-1){
////                return ;
////            }
//            AdsResp.DataBean bean = new AdsResp.DataBean();
//            bean.setName("splash_activity");
//            bean.setLink("http://www.baidu.com");
//            bean.setImg("https://avatar.csdn.net/F/A/2/1_love__coder.jpg?1531211387");
//
//
////            bannerList.clear();
//            bannerList.add(new CommonViewModel(context, bean.getImg(), bean.getLink(), 1));
//            bannerList.add(new CommonViewModel(context, bean.getImg(), bean.getLink(), 2));
//            bannerList.add(new CommonViewModel(context, bean.getImg(), bean.getLink(), 3));
//            bannerPageCount.set(bannerList.size());
//
//
//        }, e -> {
//            e.printStackTrace();
//        });
        return observable;
    }

    List cates = new ArrayList();

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
//        observable.subscribe(res -> {
//
////            if (res.getStatus() != 1) {
////                return;
////            }
//            cates=res.getData();
//            for (LearnCate.DataBean bean : res.getData()) {
//                NavItemVM navItemVM = new NavItemVM(context, bean.getIcon(),bean.getName(),bean.getId(),res.getData());
//                navList.add(navItemVM);
//            }
//
////            Observable.fromIterable(res.getData()).subscribe(bean -> {
////                        VideoPageItemVM pageItemViewModel = new VideoPageItemVM(context, bean);
////                        pageList.add(pageItemViewModel);
////                        pageItemViewModel.onCreate();
////                    },e->{},()->{
////                        isInitFinish.set(true);
////                    }
////
////            );
//        });
        return observable;
    }

    public BindingCommand moreBookClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            Bundle bundle = new Bundle();
            bundle.putString("id", "");
            bundle.putParcelableArrayList("categorys", (ArrayList<? extends Parcelable>) cates);
            startContainerActivity(BookListFragment.class.getCanonicalName(), bundle);
        }
    });
    public BindingCommand navClickCmd=new BindingCommand(()->{

        Bundle bundle=new Bundle();
        bundle.putString("id","");
        bundle.putParcelableArrayList("categorys",(ArrayList<? extends Parcelable>) navBeanList);
        startContainerActivity(BookListFragment.class.getCanonicalName(),bundle);

    });
    public BindingCommand nav1ClickCmd=new BindingCommand(()->{

        Bundle bundle=new Bundle();
        bundle.putString("id",nav1.get().getId());
        bundle.putParcelableArrayList("categorys",(ArrayList<? extends Parcelable>) navBeanList);
        startContainerActivity(BookListFragment.class.getCanonicalName(),bundle);

    });
    public BindingCommand nav2ClickCmd=new BindingCommand(()->{

        Bundle bundle=new Bundle();
        bundle.putString("id",nav2.get().getId());
        bundle.putParcelableArrayList("categorys",(ArrayList<? extends Parcelable>) navBeanList);
        startContainerActivity(BookListFragment.class.getCanonicalName(),bundle);

    });
    public BindingCommand nav3ClickCmd=new BindingCommand(()->{

        Bundle bundle=new Bundle();
        bundle.putString("id",nav3.get().getId());
        bundle.putParcelableArrayList("categorys",(ArrayList<? extends Parcelable>) navBeanList);
        startContainerActivity(BookListFragment.class.getCanonicalName(),bundle);

    });
    @Override
    public void onDestroy() {
        super.onDestroy();
        bannerList.clear();
        bannerList = null;
        customBaseDialog = null;
    }
}
