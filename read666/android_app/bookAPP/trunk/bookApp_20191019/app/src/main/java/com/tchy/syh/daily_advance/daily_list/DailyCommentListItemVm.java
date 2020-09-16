package com.tchy.syh.daily_advance.daily_list;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.google.gson.Gson;
import com.tchy.syh.book.book_detail.ApiService;
import com.tchy.syh.comment.CommentListFragment;
import com.tchy.syh.comment.entity.CommentLoveResp;
import com.tchy.syh.comment.entity.CommentResp;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.StringUtils;

public class DailyCommentListItemVm extends BaseViewModel{
    public ObservableField<CommentResp.DataBean.ListBean> entity=new ObservableField<>();
    public ObservableField<String> avatar=new ObservableField<>();
    public ObservableField<String> nickname=new ObservableField<>();
    public ObservableInt num=new ObservableInt();
    public ObservableInt isUp=new ObservableInt();
    public int index=0;
    public boolean canClickItem=false;
    public boolean isLearn;
    String id;
    public DailyCommentListItemVm(Context context, CommentResp.DataBean.ListBean entity, int i, boolean canClickItem, boolean isLearn, String id) {
        super(context);
        entity.setContent(Html.fromHtml(entity.getContent()).toString());
        this.entity .set(entity);
        if(StringUtils.isEmpty(entity.getLearn_rank())){
            nickname.set(entity.getNickname());
        }else{
            nickname.set(entity.getNickname()+"("+entity.getLearn_rank()+")");
        }

        avatar.set(entity.getAvatar());
        num.set(entity.getLovenum());
        isUp.set(entity.getIs_love());
        this.index=i;
        this.canClickItem=canClickItem;
        this.isLearn=isLearn;
        this.id=id;

    }

    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(canClickItem){
                Gson gson=new Gson();
                entity.get().setLearn_id(id);
                String json=gson.toJson(entity.get());
                Bundle bundle=new Bundle();
                bundle.putString("json",json );
                bundle.putInt("index",index );
                bundle.putBoolean("isLearn",isLearn );
                startContainerActivity(CommentListFragment.class.getCanonicalName(),bundle);
            }
        }
    });
    public BindingCommand upClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            HashMap<String, Object> params = new HashMap();
            params.put("comment_id",entity.get().getId() );
            params.put("access_token", SPUtils.getInstance().getString("token"));
            Observable<CommentLoveResp> observable;

            if(isLearn){
                observable= RetrofitClient.getInstance().create(ApiService.class)
                        .learn_commentlove(FormdataRequestUtil.getFormDataRequestParams(params))
                        .delay(500, TimeUnit.MILLISECONDS )
                        .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                        .compose(RxUtils.schedulersTransformer()) //线程调度
                        .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                        .doOnSubscribe(disposable -> {
                        });
            }else{
                observable= RetrofitClient.getInstance().create(ApiService.class)
                        .news_commentlove(FormdataRequestUtil.getFormDataRequestParams(params))
                        .delay(500, TimeUnit.MILLISECONDS )
                        .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                        .compose(RxUtils.schedulersTransformer()) //线程调度
                        .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                        .doOnSubscribe(disposable -> {
                        });
            }

            observable.subscribe(v->{
                if(v.getStatus()==1){
                    num.set(v.getData().getNum());
                    isUp.set(v.getData().getIs_add());
//                    Messenger.getDefault().sendNoMsg("commentRefresh");
                }
                ToastUtil.toastBottom(v.getInfo());
            });
        }
    });
    public void getListAPI(){
        HashMap<String, Object> params = new HashMap();
        params.put("comment_id",entity.get().getId() );
        params.put("access_token", SPUtils.getInstance().getString("token"));
        Observable<CommentLoveResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .get_learn_comment(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(v->{
            if(v.getStatus()==1){
                CommentResp.DataBean.ListBean bean=entity.get();
                bean .setLovenum(v.getData().getNum());
                entity.set(bean);
                num.set(entity.get().getLovenum());
            }

            ToastUtil.toastBottom(v.getInfo());
        });
    }
}
