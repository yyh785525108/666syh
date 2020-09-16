package com.tchy.syh.book.book_detail.video.desp;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;
import android.util.Log;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.book.ApiService;
import com.tchy.syh.book.book_detail.BookDetailVm;
import com.tchy.syh.book.book_detail.entity.BookDetailResp;
import com.tchy.syh.book.book_list.BookNewItemVm;
import com.tchy.syh.book.entity.BookListResp;
import com.tchy.syh.comment.entity.CommentLoveResp;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;
import me.tatarka.bindingcollectionadapter2.OnItemBind;


public class BookDetailVideoPageDespVm extends BaseViewModel {
    String id ;
    public ObservableField<BookDetailResp.DataBean> dataBean = new ObservableField<>();
    public ObservableInt collectNum=new ObservableInt();
    public ObservableInt isCollect=new ObservableInt();
    public ObservableInt upNum=new ObservableInt();
    public ObservableInt isUp=new ObservableInt();
    public ObservableList bookList=new ObservableArrayList();
    public BookDetailVideoPageDespVm(Context context,String id ){
        super(context);
        this.id=id;

    }
    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Messenger.getDefault().unregister(context);

    }

    public final ItemBinding onItemBind = ItemBinding.of(new OnItemBind<Object>() {
        @Override
        public void onItemBind(ItemBinding itemBinding, int position, Object item) {
            itemBinding.set(BR.vm, position == 0 ? R.layout.book_detail_video_desp_header : R.layout.book_new_item);
        }
    });
    @Override
    public void registerRxBus() {
        super.registerRxBus();
        Messenger.getDefault().register(context, BookDetailVm.BOOK_DETAIL_UPDATE, BookDetailResp.DataBean.class, new BindingConsumer< BookDetailResp.DataBean>() {
            @Override
            public void call(final  BookDetailResp.DataBean bean) {
                //删除选择对话框
                dataBean.set(bean);

                collectNum.set(bean.getCollect_num());
                isCollect.set(bean.getIs_collect());

                upNum.set(bean.getLike_num());
                isUp.set(bean.getIs_zan());

                getBooks(true);
                Log.d("sort", "call: "+bean.getTitle());

            }

        });
        Messenger.getDefault().register(context,"collectRefresh", BookDetailResp.DataBean.class, new BindingConsumer< BookDetailResp.DataBean>() {
            @Override
            public void call(final  BookDetailResp.DataBean bean) {

                collectNum.set(bean.getCollect_num());
                isCollect.set(bean.getIs_collect());


            }

        });

        Messenger.getDefault().register(context,"upRefresh", BookDetailResp.DataBean.class, new BindingConsumer< BookDetailResp.DataBean>() {
            @Override
            public void call(final  BookDetailResp.DataBean bean) {

                upNum.set(bean.getLike_num());
                isUp.set(bean.getIs_zan());
            }

        });


    }
    public void getBooks(boolean isRefresh) {

        HashMap<String, Object> params = new HashMap();

        params.put("type", 1);
        params.put("orderby", 0);

        params.put("fid", dataBean.get().getFid());

        params.put("page", 0);
        Observable<BookListResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .learn(FormdataRequestUtil.getFormDataRequestParams(params))
                .delay(500, TimeUnit.MILLISECONDS)
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {

            if (res.getStatus() != 1) {
                return;

            }

            bookList.clear();
            bookList.add(this);
            Observable.fromIterable(res.getData().getList()).take(5).subscribe(bean -> {
                bookList.add(new BookNewItemVm(context, bean));
                    }
            );



        }, e -> {

            e.printStackTrace();
        },()->{
        });
    }

   public  BindingCommand collectClick=new BindingCommand(()->{
        addCollect();
    });

    public  BindingCommand upClick=new BindingCommand(()->{
        addUp();
    });
    public void addCollect(){
        HashMap<String, Object> params = new HashMap();

        params.put("id", dataBean.get().getId());
        params.put("type", "learn");
        params.put("access_token", SPUtils.getInstance().getString("token"));


        Observable<CommentLoveResp> observable = RetrofitClient.getInstance().create(com.tchy.syh.book.ApiService.class)
                .member_collect(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {

            if (res.getStatus() != 1) {
                ToastUtil.toastBottom(res.getInfo());
                return;

            }
            collectNum.set(res.getData().getNum());
            isCollect.set(res.getData().getIs_add());

            notifyCollectStateSync();
//            upNum.set(bean.getLike_num());
//            isUp.set(bean.getIs_zan());

            ToastUtil.toastBottom(res.getData().getIs_add()==1?"收藏成功":"取消收藏");

        }, e -> {

            e.printStackTrace();
        });
    }
    public void notifyCollectStateSync(){
        BookDetailResp.DataBean bean =new BookDetailResp.DataBean();
        bean.setIs_collect(isCollect.get());
        bean.setCollect_num(collectNum.get());
        Messenger.getDefault().send(bean, "collectRefreshInDesp");
    }
    public void notifyUpStateSync(){
        BookDetailResp.DataBean bean =new BookDetailResp.DataBean();
        bean.setIs_zan(isUp.get());
        bean.setLike_num(upNum.get());
        Messenger.getDefault().send(bean, "upRefreshInDesp");
    }
    public void addUp(){
        HashMap<String, Object> params = new HashMap();

        params.put("id", dataBean.get().getId());
        params.put("type", "learn");
        params.put("access_token", SPUtils.getInstance().getString("token"));


        Observable<CommentLoveResp> observable = RetrofitClient.getInstance().create(com.tchy.syh.book.ApiService.class)
                .member_like(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {

            if (res.getStatus() != 1) {
                ToastUtil.toastBottom(res.getInfo());
                return;

            }
            upNum.set(res.getData().getNum());
            isUp.set(res.getData().getIs_add());
            notifyUpStateSync();
//            upNum.set(bean.getLike_num());
//            isUp.set(bean.getIs_zan());

            ToastUtil.toastBottom(res.getData().getIs_add()==1?"点赞成功":"取消点赞");

        }, e -> {

            e.printStackTrace();
        });
    }
}
