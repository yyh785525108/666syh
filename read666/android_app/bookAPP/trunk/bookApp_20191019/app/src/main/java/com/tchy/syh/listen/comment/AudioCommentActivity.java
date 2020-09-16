package com.tchy.syh.listen.comment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.tchy.syh.R;
import com.tchy.syh.app.AppApplication;
import com.tchy.syh.book.book_detail.ApiService;
import com.tchy.syh.comment.entity.CommentLoveResp;
import com.tchy.syh.comment.entity.CommentResp;
import com.tchy.syh.common.entity.CommonPayResp;
import com.tchy.syh.common.entity.CommonResp;
import com.tchy.syh.cons.HttpCons;
import com.tchy.syh.cons.WeixinCons;

import com.tchy.syh.dialog.CommentDialog;
import com.tchy.syh.dialog.DashangDialog;
import com.tchy.syh.dialog.PayDialog;
import com.tchy.syh.listen.entity.CommentChangeUpdateEvent;
import com.tchy.syh.utils.FormUtil;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;


import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;


public class AudioCommentActivity extends RxAppCompatActivity {
    List<CommentResp.DataBean.ListBean> datas = new ArrayList<>();
    Disposable d;
    CommentDialog commnetDialog;
    private String title;
    private String cover;
    private String sub_title;
    private String book_id;
    private String audio_id;
    private PayDialog payDialog;
    private DashangDialog dashangDialog;
    private Disposable d1;
    private Disposable d2;
    private Disposable d3;
    private CommentAdapter adapter;
    private RecyclerView rv;
    private TwinklingRefreshLayout twinklingRefreshLayout;
    private CommentAdapter.OnCommentClickListener listener = new CommentAdapter.OnCommentClickListener() {
        @Override
        public void onZanClick(int position) {
            upClick(datas.get(position));
        }

        @Override
        public void onItemClick(int position) {
            Intent intent = new Intent(AudioCommentActivity.this,AudioCommentReplyActivity.class);
            CommentResp.DataBean.ListBean listBean = datas.get(position);
            intent.putExtra("comment",listBean);
            intent.putExtra("position",position);
            startActivity(intent);
        }
    };

    private void upClick(CommentResp.DataBean.ListBean listBean) {
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_comment_frag);

        initParam();
        initView();

    }


    public void initParam() {
        //获取列表传入的实体
        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            title = mBundle.getString("title");
            cover = mBundle.getString("cover");
            sub_title = mBundle.getString("sub_title");
            book_id = mBundle.getString("book_id");
            audio_id = mBundle.getString("audio_id");
        }
    }


    public void initView() {
        twinklingRefreshLayout = findViewById(R.id.twinklingRefreshLayout);
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                getConmments(true);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                pageNum++;
                getConmments(false);
            }
        });
        d3=RxBus.getDefault().toObservable(CommentChangeUpdateEvent.class).subscribe(commentChangeUpdateEvent -> {
            datas.remove(commentChangeUpdateEvent.position);
            datas.add(commentChangeUpdateEvent.position,commentChangeUpdateEvent.listBean);
            adapter.notifyDataSetChanged();
        });

        initList();

        Glide.with(this)
                .load(cover)
                .into((ImageView) findViewById(R.id.cover));
        ((TextView) findViewById(R.id.main_title)).setText(title);
        ((TextView) findViewById(R.id.sub_title)).setText(sub_title);
        findViewById(R.id.iv_back).setOnClickListener(view12 -> {
            finish();
        });
        findViewById(R.id.et_comment).setOnClickListener(view1 -> {
            if (commnetDialog != null) {
                commnetDialog.dismiss();
                commnetDialog = null;
            }
            commnetDialog = new CommentDialog();
            Bundle bundle = new Bundle();
            bundle.putString("type", "audio");
            commnetDialog.setArguments(bundle);
            commnetDialog.show(getSupportFragmentManager(), "comment");
        });
        d = RxBus.getDefault().toObservable(CommentDialog.CommentAudioBean.class).subscribe(v -> {
            HashMap<String, Object> params = new HashMap();

            params.put("id", book_id);
            params.put("content", v.comment);
            params.put("access_token", SPUtils.getInstance().getString("token"));
            io.reactivex.Observable<CommonResp> observable = RetrofitClient.getInstance().create(ApiService.class)
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
                    ToastUtil.toastBottom("评论成功");
                } else {
                    ToastUtil.toastBottom(res.getInfo());
                }
            }, e -> {
                e.printStackTrace();
            });

        });

        findViewById(R.id.dashang).setOnClickListener(v -> {
            if (dashangDialog != null) {
                dashangDialog.dismiss();
                dashangDialog = null;
            }

            dashangDialog = new DashangDialog();
            dashangDialog.show(getSupportFragmentManager(), "dashang");
        });

        d1 = RxBus.getDefault().toObservable(DashangDialog.DashangBean.class).subscribe(v -> {
            if (payDialog != null) {
                payDialog.dismiss();
                payDialog = null;
            }
            payDialog = new PayDialog();
            Bundle bundle = new Bundle();
            bundle.putDouble("price", v.money);
            bundle.putString("id", book_id);
            payDialog.setArguments(bundle);
            payDialog.show(getSupportFragmentManager(), "pay");

        });
        d2 = RxBus.getDefault().toObservable(PayDialog.PayBean.class).subscribe(v -> {
            if (v.type == 0) {
                dashang(v.money);
            } else {
                ToastUtil.toastBottom("暂不支持，请选择其他支付方式");
            }
        });
    }

    private void initList() {
        rv = findViewById(R.id.rv);
        adapter = new CommentAdapter(this, datas);
        adapter.setOnCommentClickListener(listener);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        getConmments(false);
    }

    @SuppressLint("CheckResult")
    public void dashang(double currMoney) {
        HashMap<String, Object> params = new HashMap();

        params.put("id", book_id);
        DecimalFormat format = new DecimalFormat("0.00");
        params.put("money", format.format(currMoney));
        params.put("access_token", SPUtils.getInstance().getString("token"));
        io.reactivex.Observable<CommonPayResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .shang(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(this)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(v -> {
            if (v.getStatus() == 1) {
                PayReq request = new PayReq();
                request.appId = v.getData().getAppid();
                request.partnerId = WeixinCons.PARTNER_ID;
                request.prepayId = v.getData().getPrepayid();
                request.packageValue = "Sign=WXPay";
                request.nonceStr = v.getData().getNoncestr();
                request.timeStamp = v.getData().getTimestamp();
                request.sign = v.getData().getSign();
                AppApplication.mWxApi.sendReq(request);
            } else {

            }


        }, e -> {
            e.printStackTrace();
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
         if (d != null) {                                     d.dispose();                                 }

        if (d1 != null) {
            d1.dispose();
            d1 = null;
        }
        if (d2 != null) {
            d2.dispose();
            d2 = null;
        }
        if (d3 != null) {
            d3.dispose();
            d3 = null;
        }
    }


    int pageNum = 0;

    @SuppressLint("CheckResult")
    public void getConmments(boolean isRefresh) {
        if (isRefresh){
            pageNum=0;
        }
        HashMap<String, String> para = new HashMap<>();
        para.put("appkey", HttpCons.APP_KEY);
        para.put("access_token", SPUtils.getInstance().getString("token"));
        para.put("id", book_id);
        if (!TextUtils.isEmpty(audio_id)) {
            para.put("albumid", audio_id);
        }
        para.put("page", "" + pageNum);

        String sig = FormUtil.genSig(para);
        para.put("sig", sig);
        Observable<CommentResp> observable = RetrofitClient.getInstance().create(com.tchy.syh.listen.ApiService.class)
                .get_learn_comment(para)
                .compose(RxUtils.bindToLifecycle(AudioCommentActivity.this)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {
            if (res.getStatus() != 1) {
                return;
            }
            if (isRefresh) {
                twinklingRefreshLayout.finishRefreshing();
                datas.clear();
                datas.addAll(res.getData().getList());
                adapter.notifyDataSetChanged();
            } else {
                twinklingRefreshLayout.finishLoadmore();

                int size = res.getData().getList().size();
                if (size <= 0) {
                    Toast.makeText(this, "没有更多了", Toast.LENGTH_SHORT).show();
                    return;
                }
                datas.addAll(res.getData().getList());
                adapter.notifyDataSetChanged();
            }
        }, e -> {

            if (isRefresh) {

                twinklingRefreshLayout.finishRefreshing();
            } else
                twinklingRefreshLayout.finishLoadmore();

            e.printStackTrace();
        });
    }
}
