package com.tchy.syh.comment;

import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;

import com.tchy.syh.R;
import com.tchy.syh.BR;
import com.tchy.syh.book.book_detail.ApiService;
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
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

public class CommentReplyVM extends BaseViewModel {
    public ObservableField<CommentResp.DataBean.ListBean> entity=new ObservableField<>();
    public int index=0;
    public ObservableList<CommentListItemVm> observableList=new ObservableArrayList<>();
    private int pageNum = 0;
    public ObservableInt num=new ObservableInt();
    public ObservableInt replyNum=new ObservableInt();
    public ObservableInt isUp=new ObservableInt();
    public ObservableInt upNum=new ObservableInt();
    public ObservableField<String> title=new ObservableField<String>() ;
    public ObservableBoolean isRefreshFinish = new ObservableBoolean(true);
    public ObservableBoolean isLoadMoreFinish = new ObservableBoolean(true);
    public ItemBinding itemBinding = ItemBinding.of(BR.vm, R.layout.comment_list_item);
    public boolean isLearn;
    public CommentReplyVM(Context context,CommentResp.DataBean.ListBean listBean,int index,String title,boolean isLearn){
        super(context);
        this.index=index;
        this.entity.set(listBean);
        this.isUp.set(listBean.getIs_love());
        this.upNum.set(listBean.getLovenum());
        this.title.set(title);
        this.num.set(entity.get().getReply_num());
        this.isLearn=isLearn;
        this.replyNum.set(this.entity.get().getReply_num());

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Messenger.getDefault().register(context, "commentRefresh", new BindingAction() {
            @Override
            public void call() {
                getList(true);
            }
        });
        getList(true);
    }

    public BindingCommand upClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            HashMap<String, Object> params = new HashMap();
            params.put("comment_id",entity.get().getId() );
            params.put("access_token", SPUtils.getInstance().getString("token"));
            Observable<CommentLoveResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                    .learn_commentlove(FormdataRequestUtil.getFormDataRequestParams(params))
                    .delay(500, TimeUnit.MILLISECONDS )
                    .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                    .compose(RxUtils.schedulersTransformer()) //线程调度
                    .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                    .doOnSubscribe(disposable -> {
                    });
            observable.subscribe(v->{
                if(v.getStatus()==1){
                    isUp.set(v.getData().getIs_add());
                    upNum.set(v.getData().getNum());
                    Messenger.getDefault().sendNoMsg("commentRefresh");

                }

                ToastUtil.toastBottom(v.getInfo());
            });
        }
    });
    public void getList(boolean isRefresh) {
        HashMap<String, Object> params = new HashMap();

        params.put("id", entity.get().getLearn_id());
        params.put("pid", entity.get().getId());
        params.put("page", this.pageNum);
        params.put("access_token",SPUtils.getInstance().getString("token"));
        Observable<CommentResp> observable=null;

        if(isLearn){
            observable = RetrofitClient.getInstance().create(ApiService.class)
                    .get_learn_comment(FormdataRequestUtil.getFormDataRequestParams(params))
                    .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                    .compose(RxUtils.schedulersTransformer()) //线程调度
                    .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                    .doOnSubscribe(disposable -> {
                    });
        }else{
            observable = RetrofitClient.getInstance().create(ApiService.class)
                    .news_comment(FormdataRequestUtil.getFormDataRequestParams(params))
                    .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                    .compose(RxUtils.schedulersTransformer()) //线程调度
                    .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                    .doOnSubscribe(disposable -> {
                    });
        }


        observable.subscribe(res -> {

            if (res.getStatus() != 1) {
                return;

            }
            if (isRefresh)
                observableList.clear();

            this.num.set(Integer.valueOf(res.getData().getTotal_count()));

            for (int i = 0; i < res.getData().getList().size(); i++) {
                observableList.add(new CommentListItemVm(context,  res.getData().getList().get(i),i+1,false,isLearn,this.entity.get().getLearn_id()));
            }



        }, e -> {

            e.printStackTrace();
        },()->{ if (isRefresh) {
            this.isRefreshFinish.set(true);
        } else
            this.isLoadMoreFinish.set(true);});
    }

    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isRefreshFinish.set(false);
            pageNum = 0;
            //重新请求
            getList(true);

        }
    });
    //上拉加载
    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isLoadMoreFinish.set(false);
            pageNum++;
            getList(false);
        }
    });


}
