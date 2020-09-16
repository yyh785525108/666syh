package com.tchy.syh.my.feedback;

import android.app.Activity;
import android.content.Context;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.databinding.ObservableList;

import android.util.Log;

import com.tchy.syh.common.entity.CommonDataListResp;
import com.tchy.syh.my.entity.MissionResp;
import com.tchy.syh.userAccount.AccountApiService;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.StringUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class FeedBackVM extends BaseViewModel {

    public ObservableField<MissionResp.DataBean> bean=new ObservableField<>();
    public ObservableInt type=new ObservableInt(0);
    public ObservableField<String> desc=new ObservableField<>();
    public ObservableField<String> contact=new ObservableField<>();
    public ObservableList<URI> imgList=new ObservableArrayList<>();
    public FeedBackVM(Context context) {
        super(context);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //请求网络数据


    }


    public BindingCommand submitClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            List<MultipartBody.Part> parts=new ArrayList<>();

            for (int i = 0; i <imgList.size() ; i++) {
                URI url=imgList.get(i);
                MultipartBody.Part part =
                        MultipartBody.Part.createFormData("pic"+i,new Date().getTime()+"_"+i, RequestBody.create(MediaType.parse("multipart/form-data"), new File(url)));
                parts.add(part);
            }

            MultipartBody.Part[] partsArray=new MultipartBody.Part[0];
            if(StringUtils.isEmpty(desc.get())){
                ToastUtil.toastBottom("请输入反馈内容");

                return ;
            }
            if(StringUtils.isEmpty(contact.get())){
                ToastUtil.toastBottom("请输入联系方式");

                return ;
            }
            HashMap<String, Object> params = new HashMap();
            params.put("type",type.get() );
            params.put("content", desc.get());
            params.put("contact", contact.get());
            params.put("access_token", SPUtils.getInstance().getString("token"));
            Observable<CommonDataListResp> observable = RetrofitClient.getInstance().create(AccountApiService.class)
                    .feedBack(FormdataRequestUtil.getFormDataRequestParams(params), parts.toArray(partsArray) )
                    .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                    .compose(RxUtils.schedulersTransformer()) //线程调度
                    .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                    .doOnSubscribe(disposable -> {
                    });

            observable.subscribe(res -> {


                if(res.getStatus()!=1){
                    ToastUtil.toastBottom(res.getInfo());
                }
                ((Activity)context).finish();


            }, e -> {
                e.printStackTrace();
            });
        }});

    public BindingCommand<Integer> typeChanged=new BindingCommand(new BindingConsumer<Integer>() {
        @Override
        public void call(Integer o) {
            Log.d("sort", "call: "+o);
            type.set(o);
        }
    });
    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
