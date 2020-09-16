package com.tchy.syh.listen.comment;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.tchy.syh.R;
import com.tchy.syh.comment.ApiService;
import com.tchy.syh.comment.entity.CommentLoveResp;
import com.tchy.syh.comment.entity.CommentResp;
import com.tchy.syh.common.entity.CommonResp;
import com.tchy.syh.dialog.CommentDialog;
import com.tchy.syh.listen.entity.CommentChangeUpdateEvent;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.NumberFormatUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.TimeFormatUtil;
import com.tchy.syh.utils.ToastUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;

public class AudioCommentReplyActivity extends RxAppCompatActivity {
    private CommentDialog commnetDialog;
    private CommentResp.DataBean.ListBean listBean;
    private int position;
    private TextView nickName;
    private TextView content;
    private TextView love;
    private TextView addTime;
    private TextView replyNum;
    private TextView no;
    private TextView replayNum2;
    private RecyclerView recyclerView;
    private List<CommentResp.DataBean.ListBean> replyDatas = new ArrayList<>();
    private CommentAdapter adapter;

    int pageNum = 0;
    private TwinklingRefreshLayout twinklingRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_audio_comment_replay_list);
        initView();
    }

    @SuppressLint("CheckResult")
    private void initView() {
        twinklingRefreshLayout = findViewById(R.id.twinklingRefreshLayout);
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                pageNum = 0;
                getList(true);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                pageNum++;
                getList(false);
            }
        });
        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommentAdapter(this, replyDatas);
        adapter.setOnCommentClickListener(new CommentAdapter.OnCommentClickListener() {
            @Override
            public void onZanClick(int position) {
                replyUpClick(replyDatas.get(position));
            }

            @Override
            public void onItemClick(int position) {

            }
        });
        recyclerView.setAdapter(adapter);
        listBean = getIntent().getParcelableExtra("comment");
        position = getIntent().getIntExtra("position", 0);
        Glide.with(this).load(listBean.getAvatar()).into((ImageView) findViewById(R.id.imageView2));
        nickName = findViewById(R.id.textView4);
        content = findViewById(R.id.textView6);
        replayNum2 = findViewById(R.id.commentnum);
        addTime = findViewById(R.id.textView9);
        replyNum = findViewById(R.id.textView10);
        no = findViewById(R.id.no);
        love = findViewById(R.id.textView7);
        love.setOnClickListener(view -> {
            upClick();
        });
        setInfo();
        getList(true);
        findViewById(R.id.et_comment).setOnClickListener(v -> {
            if (commnetDialog != null) {
                commnetDialog.dismiss();
                commnetDialog = null;
            }

            commnetDialog = new CommentDialog();

            Bundle bundle = new Bundle();
            bundle.putString("type", "reply");
            commnetDialog.setArguments(bundle);
            commnetDialog.show(getSupportFragmentManager(), "comment");
        });
        RxBus.getDefault().toObservable(CommentDialog.CommentReplyBean.class).subscribe(v -> {
            HashMap<String, Object> params = new HashMap();


            params.put("id", listBean.getLearn_id());
            params.put("pid", listBean.getId());
            params.put("content", v.comment);
            params.put("access_token", SPUtils.getInstance().getString("token"));
            io.reactivex.Observable<CommonResp> observable = null;

            observable = RetrofitClient.getInstance().create(ApiService.class)
                    .add_audio_comment(FormdataRequestUtil.getFormDataRequestParams(params))
                    .compose(RxUtils.bindToLifecycle(this)) //请求与View周期同步
                    .compose(RxUtils.schedulersTransformer()) //线程调度
                    .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                    .doOnSubscribe(disposable -> {
                    });


            observable.subscribe(res -> {
                if (res.getStatus() == 1) {
                    if (commnetDialog != null) {
                        commnetDialog.dismiss();
                    }
                    getList(true);
                    ToastUtil.toastBottom("评论成功");
                } else {
                    ToastUtil.toastBottom(res.getInfo());
                }

            }, e -> {
                e.printStackTrace();
            });

        });
    }

    private void setInfo() {
        nickName.setText(listBean.getNickname());
        content.setText(listBean.getContent());
        addTime.setText(TimeFormatUtil.formatLatest(listBean.getAdd_time()));
        List<CommentResp.DataBean.ListBean> comment_list = listBean.getComment_list();
        if (comment_list != null && comment_list.isEmpty()) {
            int size = comment_list.size();
            replyNum.setText(size + "");
        } else {
            replyNum.setText("0");
        }
        no.setText("#" + (position + 1));
        replayNum2.setText("相关回复共" + listBean.getReply_num() + "条");
        love.setText(NumberFormatUtil.format(listBean.getLovenum(), 10000, "w"));
        Drawable right = listBean.getIs_love() == 1 ?
                getResources().getDrawable(R.mipmap.up_selected) :
                getResources().getDrawable(R.mipmap.up);
        love.setCompoundDrawablesWithIntrinsicBounds(null, null, right, null);
    }


    private void upClick() {
        HashMap<String, Object> params = new HashMap();
        params.put("comment_id", listBean.getId());
        params.put("access_token", SPUtils.getInstance().getString("token"));
        Observable<CommentLoveResp> observable = RetrofitClient.getInstance().create(com.tchy.syh.book.book_detail.ApiService.class)
                .learn_commentlove(FormdataRequestUtil.getFormDataRequestParams(params))
                .delay(500, TimeUnit.MILLISECONDS)
                .compose(RxUtils.bindToLifecycle(this)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(v -> {
            if (v.getStatus() == 1) {
                listBean.setIs_love(v.getData().getIs_add());
                listBean.setLovenum(v.getData().getNum());
                setInfo();
                RxBus.getDefault().post(new CommentChangeUpdateEvent(position, listBean));
            }
            ToastUtil.toastBottom(v.getInfo());
        });
    }

    private void replyUpClick(CommentResp.DataBean.ListBean listBean) {
        HashMap<String, Object> params = new HashMap();
        params.put("comment_id", listBean.getId());
        params.put("access_token", SPUtils.getInstance().getString("token"));
        Observable<CommentLoveResp> observable = RetrofitClient.getInstance().create(com.tchy.syh.book.book_detail.ApiService.class)
                .learn_commentlove(FormdataRequestUtil.getFormDataRequestParams(params))
                .delay(500, TimeUnit.MILLISECONDS)
                .compose(RxUtils.bindToLifecycle(this)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(v -> {
            if (v.getStatus() == 1) {
                listBean.setIs_love(v.getData().getIs_add());
                listBean.setLovenum(v.getData().getNum());
                adapter.notifyDataSetChanged();
            }
            ToastUtil.toastBottom(v.getInfo());
        });
    }

    @SuppressLint("CheckResult")
    public void getList(boolean isRefresh) {
        HashMap<String, Object> params = new HashMap();

        params.put("id", listBean.getLearn_id());
        params.put("pid", listBean.getId());
        params.put("page", pageNum);
        params.put("access_token", SPUtils.getInstance().getString("token"));


        RetrofitClient.getInstance()
                .create(com.tchy.syh.book.book_detail.ApiService.class)
                .get_learn_comment(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(this)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .subscribe((Consumer<CommentResp>) commentResp -> {
                    if (isRefresh) {
                        twinklingRefreshLayout.finishRefreshing();
                    } else {
                        twinklingRefreshLayout.finishLoadmore();
                    }
                    if (commentResp.getStatus() == 1) {
                        List<CommentResp.DataBean.ListBean> list = commentResp.getData().getList();
                        if (isRefresh) {
                            replyDatas.clear();
                        } else {
                            if (list.size() <= 0) {
                                Toast.makeText(this, "没有更多了", Toast.LENGTH_SHORT).show();
                            }
                        }
                        replyDatas.addAll(list);
                        adapter.notifyDataSetChanged();
                        listBean.setComment_list(commentResp.getData().getList());
                        listBean.setReply_num(commentResp.getData().getList().size());
                        setInfo();
                        RxBus.getDefault().post(new CommentChangeUpdateEvent(position, listBean));
                    } else {
                        ToastUtil.toastBottom(commentResp.getInfo());
                    }
                }, throwable -> {
                    if (isRefresh) {
                        twinklingRefreshLayout.finishRefreshing();
                    } else {
                        twinklingRefreshLayout.finishLoadmore();
                    }
                });
    }
}
