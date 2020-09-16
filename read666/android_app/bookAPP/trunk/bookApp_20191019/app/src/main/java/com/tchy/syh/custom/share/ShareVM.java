package com.tchy.syh.custom.share;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tchy.syh.app.AppApplication;
import com.tchy.syh.book.book_detail.ApiService;
import com.tchy.syh.common.entity.CommonResp;
import com.tchy.syh.userAccount.userLogin.LoginActivity;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.StringUtils;


public class ShareVM extends BaseViewModel {
    String id;
    public ObservableField<String > shareImgUrl =new ObservableField<>();
    public  ShareVM(Context context) {
        super(context);
        this.id=id;
    }

    public BindingCommand qqClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtil.toastBottom("暂不支持，请选择其他方式");
        }
    });
    public BindingCommand kongjianClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtil.toastBottom("暂不支持，请选择其他方式");
        }
    });
    public BindingCommand weixinClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            if(StringUtils.isEmpty(shareImgUrl.get())){
                return;
            }
            share(0);
        }
    });
    public BindingCommand pengyouquanClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            if(StringUtils.isEmpty(shareImgUrl.get())){
                return;
            }
            share(1);
        }
    });
    public BindingCommand weiboClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            ToastUtil.toastBottom("暂不支持，请选择其他方式");
        }
    });public BindingCommand localSave=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            share(9);

        }
    });
    public void setBitmap(Bitmap bitmap){
        this.bitmap=bitmap;
    }
    public static byte[] bitmap2Bytes(Bitmap bitmap, Bitmap.CompressFormat format) {
        if (bitmap == null) {
            return null;
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(format, 50, baos);
            return baos.toByteArray();
        }
    }

    Bitmap bitmap;
    public void share(int type){
        Glide.with(context).asBitmap().load(shareImgUrl.get()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                bitmap=resource;

                if(type==9){

                    if(saveBitmap(bitmap)!=null){

                        ToastUtil.toastBottom("保存成功");
                    }
                    return ;
                }
                WXImageObject imageObject = new WXImageObject(bitmap);


                WXMediaMessage msg = new WXMediaMessage();  //这个对象是用来包裹发送信息的对象
                msg.mediaObject = imageObject;

                Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, 150, 240, true);
                bitmap.recycle();
                msg.thumbData = bitmap2Bytes(thumbBitmap, Bitmap.CompressFormat.JPEG);


                SendMessageToWX.Req req = new SendMessageToWX.Req();    //创建一个请求对象
                req.message = msg;  //把msg放入请求对象中
                if(type==0){
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                }else{
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                }
                req.transaction = id;  //这个tag要唯一,用于在回调中分辨是哪个分享请求
                boolean b = AppApplication.mWxApi.sendReq(req);   //如果调用成功微信,会返回true
            }
        });

    }

    public void getImage (){
        if(!StringUtils.isEmpty(shareImgUrl.get())){
            return;
        }
        HashMap<String, Object> params = new HashMap();
        params.put("access_token", SPUtils.getInstance().getString("token"));
        Observable<CommonResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .haibao(FormdataRequestUtil.getFormDataRequestParams(params))
                .delay(500, TimeUnit.MILLISECONDS )
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {
            if (res.getStatus() != 1) {

                if(res.getStatus()==10005||res.getStatus()==10002){
                    ToastUtil.toastBottom("请先登录");
                    startActivity(LoginActivity.class);
                }
                return;
            }

            shareImgUrl.set(res.getData().getImg());

        }, e -> {
            e.printStackTrace();
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(bitmap!=null){
            bitmap.recycle();
            bitmap=null;
        }

    }
    public URI saveBitmap(Bitmap bitmap) {
        String path = Environment
                .getExternalStorageDirectory().getPath();

        File file = new File(path, System.currentTimeMillis() + ".jpg");


        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            System.out.println("___________保存的__sd___下_______________________");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaStore.Images.Media.insertImage(context.getContentResolver(),
                bitmap, file.getName(), null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        if(Build.VERSION.SDK_INT>=24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            uri = FileProvider.getUriForFile(context, "com.tchy.syh.share", file);
        }else{

            uri = Uri.fromFile(file);
        }

        intent.setData(uri);
        context.sendBroadcast(intent);
        return file.toURI();
    }
}
