package com.tchy.syh.daily_advance.daily_list;

import android.content.Context;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import android.os.Bundle;
import android.util.Log;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.tchy.syh.book.book_detail.BookVideoPlayerActivity;
import com.tchy.syh.comment.entity.CommentLoveResp;
import com.tchy.syh.daily_advance.daily_home.DailyCommentActivity;
import com.tchy.syh.daily_advance.entity.DailyAdvanceListResp;
import com.tchy.syh.daily_advance.entity.DailyAttentionResp;
import com.tchy.syh.history.ApiService;
import com.tchy.syh.listen.detail.AudioDetailFragment;
import com.tchy.syh.listen.entity.AudioDetailEntity;
import com.tchy.syh.listen.entity.ResponseWrapper;
import com.tchy.syh.read.detail.ReadDetailFragment;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.NumberFormatUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.StringUtils;

public class DailyListItemVm extends BaseViewModel{
    public DailyAdvanceListResp.DataBean.ListBean entity;
    public ObservableInt isAttention=new ObservableInt();
    public ObservableInt imageNum = new ObservableInt();
    public ObservableField<String> nickname=new ObservableField<>();
    public ObservableField<String> recommend=new ObservableField<>();
    public ObservableField<String> comment=new ObservableField<>();
    public ObservableField<String> browse=new ObservableField<>();
    public ObservableInt isUp=new ObservableInt();
    public NineGridViewClickAdapter adapter ;
    final String IMG_URL = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1549879555024&di=6c201a99f1633ebf2f688e6c9" +
            "c7972d2&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Ffront%2F214%2Fw580h434%2F20180920%2F-cZ4-hhuhism4329679.jpg";

    public DailyListItemVm(Context context, DailyAdvanceListResp.DataBean.ListBean entity) {
        super(context);
        this.entity = entity;

        if(StringUtils.isEmpty(entity.getLearn_rank())){
            nickname.set(entity.getNickname());
        }else{
            nickname.set(entity.getNickname()+"("+entity.getLearn_rank()+"天)");
        }
        recommend.set("推荐("+ NumberFormatUtil.format(entity.getLovenum(),10000,"万")+")");
        comment.set("评论("+ NumberFormatUtil.format(entity.getLovenum(),10000,"万")+")");
        browse.set("浏览("+ NumberFormatUtil.format(entity.getLovenum(),10000,"万")+")");
        isAttention.set(entity.getIs_love());//是否关注 需改动
        isUp.set(entity.getIs_love());  //是否推荐
        List<ImageInfo> imageInfo = getImageInfo(entity.getAvatar(),entity.getImage_num());
        adapter = new NineGridViewClickAdapter(context, imageInfo);
    }

    private  List<ImageInfo> getImageInfo(String avatar,int image_num) {
        List<ImageInfo> imageInfo = new ArrayList<>();
        for (int i = 0; i <image_num ; i++) {
            ImageInfo info  = new ImageInfo();
            info.setBigImageUrl(IMG_URL);
            info.setThumbnailUrl(IMG_URL);
            imageInfo.add(info);
        }

        return imageInfo;
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
    public BindingCommand upClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            HashMap<String, Object> params = new HashMap();
            params.put("comment_id",entity.getId() );
            params.put("access_token", SPUtils.getInstance().getString("token"));
            Observable<CommentLoveResp> observable;

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
//
//            observable.subscribe(v->{
//                if(v.getStatus()==1){
//
//                    recommend.set("推荐("+ NumberFormatUtil.format(entity.getLovenum(),10000,"万")+")");
////                    Messenger.getDefault().sendNoMsg("commentRefresh");
//                }
//
//                ToastUtil.toastBottom(v.getInfo());
//            });
            recommend.set("推荐("+ NumberFormatUtil.format(entity.getLovenum(),10000,"万")+")");
            isUp.set(1);
        }
    });

    public BindingCommand onCommentClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle mBundle = new Bundle();
            mBundle.putString("id",entity.getId());
            mBundle.putParcelable("bean",entity);

            startActivity(DailyCommentActivity.class,mBundle);
        }
    });
    public BindingCommand itemAudioClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //todo 跳转至音频详情
            Log.d("sort", "call: e"+entity.getId());
            requestAudioDetail(entity.getId());
//
        }
    });
    private void requestAudioDetail(String id) {
        HashMap<String, Object> params = new HashMap();
        params.put("id", id);
        params.put("access_token", SPUtils.getInstance().getString("token"));
        Observable<ResponseWrapper<AudioDetailEntity>> observable = RetrofitClient.getInstance().create(ApiService.class)
                .learn_info(FormdataRequestUtil.getFormDataRequestParams(params))
                .delay(500, TimeUnit.MILLISECONDS)
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {
            if (res.isSuccess()) {
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("detail", res.getData());
                startContainerActivity(AudioDetailFragment.class.getCanonicalName(), mBundle);
            } else {
                if (res.getStatus() == 1005) {
                    ToastUtil.toastBottom("未登录或登录状态失效");
                }
            }

        }, e -> {
            e.printStackTrace();
        });
    }
}
