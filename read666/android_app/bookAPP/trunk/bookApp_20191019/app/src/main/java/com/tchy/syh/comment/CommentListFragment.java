package com.tchy.syh.comment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.tchy.syh.R;

import com.tchy.syh.BR;
import com.tchy.syh.comment.entity.CommentResp;
import com.tchy.syh.common.entity.CommonResp;
import com.tchy.syh.databinding.CommenReplyListBinding;
import com.tchy.syh.dialog.CommentDialog;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

import java.util.HashMap;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.bus.RxBus;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;

public class CommentListFragment extends BaseFragment<CommenReplyListBinding,CommentReplyVM> {
    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.commen_reply_list;
    }

    @Override
    public int initVariableId() {
        return BR.vm;
    }
    int type;
    @Override
    public CommentReplyVM initViewModel() {
        int  index=getArguments().getInt("index");
        String json=getArguments().getString("json");
        String title=getArguments().getString("title");
        type=getArguments().getInt("type");
        Gson gson=new Gson();
        CommentResp.DataBean.ListBean listBean= gson.fromJson(json,CommentResp.DataBean.ListBean.class );

        return new CommentReplyVM(getContext(),listBean,index,title,getArguments().getBoolean("isLearn"));
    }
    CommentDialog commnetDialog;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.etComment.setOnClickListener(v -> {
            if (commnetDialog != null) {
                commnetDialog.dismiss();
                commnetDialog = null;
            }

            commnetDialog = new CommentDialog();

            Bundle bundle=new Bundle();
            bundle.putString("type","reply" );
            commnetDialog.setArguments(bundle);
            commnetDialog.show(getActivity().getSupportFragmentManager(), "comment");
        });
       RxBus.getDefault().toObservable(CommentDialog.CommentReplyBean.class).subscribe(v -> {
            HashMap<String, Object> params = new HashMap();
            if (getActivity() == null) {
                return;
            }

            params.put("id",viewModel.entity.get().getLearn_id());
            params.put("pid",viewModel.entity.get().getId());
            params.put("content", v.comment);
            params.put("access_token", SPUtils.getInstance().getString("token"));
           io.reactivex.Observable<CommonResp> observable=null;
           if(viewModel.isLearn){
               observable = RetrofitClient.getInstance().create(ApiService.class)
                       .add_audio_comment(FormdataRequestUtil.getFormDataRequestParams(params))
                       .compose(RxUtils.bindToLifecycle(this)) //请求与View周期同步
                       .compose(RxUtils.schedulersTransformer()) //线程调度
                       .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                       .doOnSubscribe(disposable -> {
                       });
           }else{
               observable = RetrofitClient.getInstance().create(ApiService.class)
                       .add_news_comment(FormdataRequestUtil.getFormDataRequestParams(params))
                       .compose(RxUtils.bindToLifecycle(this)) //请求与View周期同步
                       .compose(RxUtils.schedulersTransformer()) //线程调度
                       .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                       .doOnSubscribe(disposable -> {
                       });
           }

            observable.subscribe(res -> {
                if (res.getStatus() == 1) {
                    if (commnetDialog != null) {
                        commnetDialog.dismiss();
                    }
                    viewModel.replyNum.set(viewModel.replyNum.get()+1);
                    Messenger.getDefault().sendNoMsg("commentRefresh");
                    ToastUtil.toastBottom("评论成功");
                } else {
                    ToastUtil.toastBottom(res.getInfo());
                }

            }, e -> {
                e.printStackTrace();
            });

        });
    }
}
