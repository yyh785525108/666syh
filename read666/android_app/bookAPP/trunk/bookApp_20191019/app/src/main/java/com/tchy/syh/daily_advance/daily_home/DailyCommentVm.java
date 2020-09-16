package com.tchy.syh.daily_advance.daily_home;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.app.AppApplication;
import com.tchy.syh.book.book_detail.ApiService;
import com.tchy.syh.book.book_detail.entity.BookDetailResp;
import com.tchy.syh.book.book_detail.video.CommentCountBean;
import com.tchy.syh.comment.CommentListItemVm;
import com.tchy.syh.comment.entity.CommentResp;
import com.tchy.syh.common.CommonBottomBarVM;
import com.tchy.syh.common.entity.CommonDataListResp;
import com.tchy.syh.daily_advance.daily_list.DailyCommentListItemVm;
import com.tchy.syh.daily_advance.entity.DailyAdvanceListResp;
import com.tchy.syh.daily_advance.entity.DailyAttentionResp;
import com.tchy.syh.userAccount.userLogin.LoginActivity;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableFilter;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.utils.ConvertUtils;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.StringUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class DailyCommentVm extends BaseViewModel {
    //    public BookListResp.DataBean.ListBean entity;
    public static final int BOOK_DETAIL_UPDATE = 0x000001;
    public DailyAdvanceListResp.DataBean.ListBean entity;
    public ObservableField<String> avatar=new ObservableField<>();
    public ObservableField<String> nickname=new ObservableField<>();
    public ObservableInt isAttention=new ObservableInt();

    public ObservableInt num=new ObservableInt();
    public ObservableInt isUp=new ObservableInt();
    public ObservableList<DailyCommentListItemVm> commentList = new ObservableArrayList<>();
    private int pageNum = 0;
    public ObservableField<CommonBottomBarVM> bottomVM = new ObservableField<>();
    public ItemBinding itemBinding = ItemBinding.of(BR.vm, R.layout.daily_comment_list_item);
    public ObservableBoolean isLoadMoreFinish = new ObservableBoolean(true);
    public String id ="";
    public ObservableField<String> title=new ObservableField<>();
    public DailyCommentVm(Context context, DailyAdvanceListResp.DataBean.ListBean entity, String id) {
        super(context);
        title.set("详情评论");
        this.entity = entity;
        if(StringUtils.isEmpty(entity.getLearn_rank())){
            nickname.set(entity.getNickname());
        }else{
            nickname.set(entity.getNickname()+"("+entity.getLearn_rank()+")");
        }
        this.id = id;
        isAttention.set(entity.getIs_love());
        avatar.set(entity.getAvatar());
        num.set(entity.getLovenum());
        isUp.set(entity.getIs_love());

    }

    @Override
    public void onCreate() {
        super.onCreate();
        getDetail();
        getConmments(false);
        Messenger.getDefault().register(context, "refresh", String.class, new BindingConsumer<String>() {
            @Override
            public void call(String s) {
                getDetail();

            }
        });
    }
    public ObservableField<BookDetailResp.DataBean> dataBean = new ObservableField<>();
    public ObservableInt pagerIndex = new ObservableInt();
    public BindingCommand typeChanged = new BindingCommand(new BindingConsumer<Integer>() {
        @Override
        public void call(Integer o) {
            pagerIndex.set(o);
        }
    });

    public BindingCommand backClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ((Activity)context).finish();
        }
    });
    public void getDetail() {
        HashMap<String, Object> params = new HashMap();
        params.put("id", id);
        params.put("access_token", SPUtils.getInstance().getString("token"));
        Observable<BookDetailResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .learn_info(FormdataRequestUtil.getFormDataRequestParams(params))
                .delay(500, TimeUnit.MILLISECONDS)
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {
            if (res.getStatus() != 1) {

                if (res.getStatus() == 10005 || res.getStatus() == 10002) {
                    ToastUtil.toastBottom("请先登录");
                    startActivity(LoginActivity.class);
                }
                return;
            }
            res.getData().setId(id);
            dataBean.set(res.getData());
            bottomVM.set(new CommonBottomBarVM(context, res.getData(), false));
            Messenger.getDefault().send(res.getData(), BOOK_DETAIL_UPDATE);

        }, e -> {
            e.printStackTrace();
        });

    }
    //上拉加载
    public BindingCommand onRVLoadMoreCommand = new BindingCommand(new BindingConsumer<Integer>() {
        @Override
        public void call(Integer o) {
            isLoadMoreFinish.set(false);
            pageNum++;
            getConmments(false);
        }
    });
    public void getConmments(boolean isRefresh) {

        HashMap<String, Object> params = new HashMap();

        params.put("id", id);
        params.put("page", this.pageNum);
        params.put("access_token", SPUtils.getInstance().getString("token"));

//        Observable<CommentResp> observable = RetrofitClient.getInstance().create(ApiService.class)
//                .get_learn_comment(FormdataRequestUtil.getFormDataRequestParams(params))
//                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
//                .compose(RxUtils.schedulersTransformer()) //线程调度
//                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
//                .doOnSubscribe(disposable -> {
//                });
//
//        observable.subscribe(res -> {
//
//            if (res.getStatus() != 1) {
//                ToastUtil.toastBottom("没有更多内容了");
//                return;
//
//            }
//            if (Integer.valueOf(res.getData().getTotal_count()) > 0 && res.getData().getCount() == 0) {
//                ToastUtil.toastBottom("没有更多内容了");
//                return;
//            }
//            CommentCountBean bean = new CommentCountBean();
//            bean.setId(id);
//            bean.setCount(res.getData().getTotal_count());
//            Messenger.getDefault().send(bean, "comment_count");
//            if (isRefresh)
//                commentList.clear();
//            for (int i = 0; i < res.getData().getList().size(); i++) {
//
//                commentList.add(new CommentListItemVm(context, res.getData().getList().get(i), i + 1, true, true, id));
//            }
//
//
//        }, e -> {
//
//            e.printStackTrace();
//        }, () -> {
//            this.isLoadMoreFinish.set(true);
//        });
        if (isRefresh)
            commentList.clear();

        for (int i = 0; i < 1; i++) {
            CommentResp.DataBean.ListBean listBean = new CommentResp.DataBean.ListBean();
            listBean.setAdd_time(""+new Date().getTime());
            listBean.setComment_list(null);
            listBean.setContent("放三卡房卡叫房东昂法当当当当当的");
            listBean.setIs_love(1);
            listBean.setNickname("大大" + i);
            listBean.setLearn_rank((i + 100)+"/365"  );
            commentList.add(new DailyCommentListItemVm(context, listBean, i + 1, false, true, id));
        }
        this.isLoadMoreFinish.set(true);
    }
    public BindingCommand upAttention = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            HashMap<String, Object> params = new HashMap();
            params.put("comment_id",entity.getId() );
            params.put("access_token", SPUtils.getInstance().getString("token"));
            Observable<DailyAttentionResp> observable;

//            if(isLearn){
//                observable= RetrofitClient.getInstance().create(com.tchy.syh.book.book_detail.ApiService.class)
//                        .learn_commentlove(FormdataRequestUtil.getFormDataRequestParams(params))
//                        .delay(500, TimeUnit.MILLISECONDS )
//                        .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
//                        .compose(RxUtils.schedulersTransformer()) //线程调度
//                        .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
//                        .doOnSubscribe(disposable -> {
//                        });
//            }else{
//                observable= RetrofitClient.getInstance().create(com.tchy.syh.book.book_detail.ApiService.class)
//                        .news_commentlove(FormdataRequestUtil.getFormDataRequestParams(params))
//                        .delay(500, TimeUnit.MILLISECONDS )
//                        .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
//                        .compose(RxUtils.schedulersTransformer()) //线程调度
//                        .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
//                        .doOnSubscribe(disposable -> {
//                        });
//            }

//            observable.subscribe(v->{
//                    if(v.getStatus()==1){
//
//                    num.set(v.getData().getNum());
//                    isUp.set(v.getData().getIs_add());
//                    Messenger.getDefault().sendNoMsg("commentRefresh");
//                    }
//
//                ToastUtil.toastBottom(v.getInfo());
//            });
            isAttention.set(1);
        }
    });
}
