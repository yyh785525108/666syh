package com.tchy.syh.read.detail;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableList;

import com.tchy.syh.BR;
import com.tchy.syh.R;
import com.tchy.syh.book.book_detail.ApiService;
import com.tchy.syh.comment.CommentListItemVm;
import com.tchy.syh.comment.entity.CommentResp;
import com.tchy.syh.common.entity.CommonResp;
import com.tchy.syh.dialog.CommentDialog;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.tatarka.bindingcollectionadapter2.ItemBinding;


public class ReadDetailPageCommentVm extends BaseViewModel {
    public ObservableList<CommentListItemVm> commentList = new ObservableArrayList<>();
    private int pageNum = 1;
    public String id ;
    public String title;

    public ReadDetailPageCommentVm(Context context, String id,String title) {
        super(context);
        this.id=id;
        this.title=title;
    }

    public ItemBinding itemBinding = ItemBinding.of(BR.vm, R.layout.comment_list_item);
    public ObservableBoolean isRefreshFinish = new ObservableBoolean(true);
    public ObservableBoolean isLoadMoreFinish = new ObservableBoolean(true);

    Disposable d;
    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据
//        CommentResp.DataBean.ListBean b = new CommentResp.DataBean.ListBean();
//        b.setReply_num("1");
//        b.setAdd_time(new Date().getTime() + "");
//        b.setNickname("aaa");
//        b.setContents("666");
//        b.setLovenum("1");
//        b.setAvator("http://imsyh.7seme.com/image/1/2/201807/2_201807162116112188.jpg");
//
//        commentList.add(new CommentListItemVm(context, b));
        getConmments(true);
        Messenger.getDefault().register(context, "commentRefresh", new BindingAction() {
            @Override
            public void call() {
                getConmments(true);
            }
        });
        d = RxBus.getDefault().toObservable(CommentDialog.CommentReadBean.class).subscribe(v -> {

            commentDialogShow.set(false);
            writeCommentAPI(v.comment);
        });
    }

    public void writeCommentAPI(String v){
        HashMap<String, Object> params = new HashMap();

        params.put("id",id );
        params.put("content", v);
        params.put("access_token", SPUtils.getInstance().getString("token"));

        Observable<CommonResp> observable = RetrofitClient.getInstance().create(com.tchy.syh.read.ApiService.class)
                .add_news_comment(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(res -> {
            if (res.getStatus() == 1) {
                Messenger.getDefault().sendNoMsg("commentRefresh");
                ToastUtil.toastBottom("评论成功");
            }else{
                ToastUtil.toastBottom(res.getInfo());

            }

        },e->{
            ToastUtil.toastBottom("评论失败");
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
         if (d != null) {                                     d.dispose();                                 }
    }

    public BindingCommand backClick =new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ((Activity)context).finish();
        }
    });
    public BindingCommand commentWriteClick =new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            commentDialogShow.set(true);
        }
    });
    ObservableBoolean commentDialogShow=new ObservableBoolean(false);

    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            isRefreshFinish.set(false);
            pageNum = 1;
            getConmments(true);
        }
    });
    //上拉加载
    public BindingCommand onLoadMoreCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
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

        Observable<CommentResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .news_comment(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {

            if (res.getStatus() != 1) {
                ToastUtil.toastBottom("没有更多内容了");
                return;

            }

            if (isRefresh)
                commentList.clear();

            for (int i = 0; i <res.getData().getList().size() ; i++) {

                commentList.add(new CommentListItemVm(context, res.getData().getList().get(i),i+1,true,false,id));
            }



        }, e -> {

            e.printStackTrace();
        },()->{
            if (isRefresh)
                this.isRefreshFinish.set(true);
            else
                this.isLoadMoreFinish.set(true);

        });
    }

//    BindingRecyclerViewAdapter.ViewHolderFactory factory=new BindingRecyclerViewAdapter.ViewHolderFactory() {
//        @Override
//        public RecyclerView.ViewHolder createViewHolder(ViewDataBinding binding) {
//            RecyclerView.ViewHolder holder=
//            return null;
//        }
//    };
}
