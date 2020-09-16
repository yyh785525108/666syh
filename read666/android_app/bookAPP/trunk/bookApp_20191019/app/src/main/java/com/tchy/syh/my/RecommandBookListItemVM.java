package com.tchy.syh.my;

import android.content.Context;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;

import com.tchy.syh.my.entity.RecommandListResp;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;


public class RecommandBookListItemVM extends BaseViewModel {
    public ObservableInt vote=new ObservableInt();


    public ObservableBoolean isVoted =new ObservableBoolean(false);
    public String name="";
    public RecommandListResp.DataBean bean=new RecommandListResp.DataBean();

    public BindingCommand voteClick  =new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            itemClick();
        }
    });

    public RecommandBookListItemVM (Context context, RecommandListResp.DataBean bean){
        super(context);
        this.bean=bean;
        this.isVoted.set(bean.getIs_vote()==0);
        this.vote.set(bean.getVote_num());
    }
    public void itemClick(){
        HashMap<String, Object> params = new HashMap();
        params.put("id",bean.getId());
        params.put("m","vote");
        params.put("access_token", SPUtils.getInstance().getString("token"));
//        params.put("page", this.pageNum);
        Observable<RecommandListResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .book_vote(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(v -> {
            ToastUtil.toastBottom(v.getInfo());
            if(v.getStatus()==1){
                isVoted.set(false);
                vote.set(bean.getVote_num()+1);
            }

        },e -> {
            e.printStackTrace();
        });

    }
}
