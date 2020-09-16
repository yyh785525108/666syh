package com.tchy.syh.common;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.tchy.syh.book.book_detail.entity.BookDetailResp;
import com.tchy.syh.comment.entity.CommentLoveResp;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;

public class CommonBottomBarVM extends BaseViewModel {
    public BookDetailResp.DataBean bean;
   public boolean hasComment;
    public ObservableInt collectNum=new ObservableInt();
    public ObservableInt isCollect=new ObservableInt();
    public ObservableInt upNum=new ObservableInt();
    public ObservableInt isUp=new ObservableInt();
    public CommonBottomBarVM(Context context,BookDetailResp.DataBean bean,boolean hasComment){
       super(context);
       this.bean=bean;
        collectNum.set(bean.getCollect_num());
        isCollect.set(bean.getIs_collect());
        upNum.set(bean.getLike_num());
        isUp.set(bean.getIs_zan());
       this.hasComment= hasComment;
       onCreate();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Messenger.getDefault().register(context,"collectRefreshInDesp", BookDetailResp.DataBean.class, new BindingConsumer< BookDetailResp.DataBean>() {
            @Override
            public void call(final  BookDetailResp.DataBean bean) {

                collectNum.set(bean.getCollect_num());
                isCollect.set(bean.getIs_collect());


            }

        });

        Messenger.getDefault().register(context,"upRefreshInDesp", BookDetailResp.DataBean.class, new BindingConsumer< BookDetailResp.DataBean>() {
            @Override
            public void call(final  BookDetailResp.DataBean bean) {

                upNum.set(bean.getLike_num());
                isUp.set(bean.getIs_zan());
            }

        });

    }

    public BindingCommand collectClick=new BindingCommand(()->{
        addCollect();

    });
    public BindingCommand upClick=new BindingCommand(()->{
        addUp();

    });
    public BindingCommand commentClick=new BindingCommand(()->{


    });
    public BindingCommand backClick=new BindingCommand(()->{

        ((Activity)context).finish();
    });
    public void notifyCollectStateSync(){
        BookDetailResp.DataBean bean =new BookDetailResp.DataBean();
        bean.setIs_collect(isCollect.get());
        bean.setCollect_num(collectNum.get());

        Messenger.getDefault().send(bean, "collectRefresh");
    }
    public void notifyUpStateSync(){
        BookDetailResp.DataBean bean =new BookDetailResp.DataBean();
        bean.setIs_zan(isUp.get());
        bean.setLike_num(upNum.get());
        Messenger.getDefault().send(bean, "upRefresh");
    }
    public void addCollect(){
        HashMap<String, Object> params = new HashMap();

        params.put("id", bean.getId());
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
    public void addUp(){
        HashMap<String, Object> params = new HashMap();

        params.put("id", bean.getId());
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
