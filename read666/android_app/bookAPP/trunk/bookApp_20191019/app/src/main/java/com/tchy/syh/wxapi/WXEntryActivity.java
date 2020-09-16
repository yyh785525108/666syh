package com.tchy.syh.wxapi;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.tchy.syh.app.AppApplication;
import com.tchy.syh.cons.WeixinCons;
import com.tchy.syh.userAccount.entity.WeChatNotify;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;
import com.tchy.syh.wxapi.entity.WXCallBackResp;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.trello.rxlifecycle2.components.RxActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WXEntryActivity extends RxActivity implements IWXAPIEventHandler {
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("sort", "onCreate: ");

        AppApplication.mWxApi.handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        Log.d("sort", "onReq: ");
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {

        if(resp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("提示");
        }
        switch (resp.errCode) {

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (RETURN_MSG_TYPE_SHARE == resp.getType()) ToastUtil.toastBottom("分享失败");
                else ToastUtil.toastBottom("登录失败");
                break;
            case BaseResp.ErrCode.ERR_OK:
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        //拿到了微信返回的code,立马再去请求access_token
                        String code = ((SendAuth.Resp) resp).code;
                        Log.d("sort", "onResp: " + code);
                        //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求


                        String urlStr = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WeixinCons.APP_ID + "&secret=" + WeixinCons.APP_SECRET +
                                "&code=" + code + "&grant_type=authorization_code";

                        OkHttpClient client = new OkHttpClient.Builder().build();
                        client.newCall(new Request.Builder().url(urlStr).get().build()).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                setResult(RESULT_CANCELED);
                                finish();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String resp = response.body().string();
                                try {
                                    JSONObject obj = new JSONObject(resp);

                                    String access_token = obj.getString("access_token");
                                    String openid = obj.getString("openid");
                                    String unionid = obj.getString("unionid");
                                    String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
                                    OkHttpClient client = new OkHttpClient.Builder().build();
                                    client.newCall(new Request.Builder().url(url).get().build()).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(Call call, IOException e) {
                                            setResult(RESULT_CANCELED);
                                            finish();
                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            JSONObject obj = null;
                                            String resp = response.body().string();
                                            try {
                                                obj = new JSONObject(resp);
                                                String nickname = obj.getString("nickname");
                                                String openid = obj.getString("openid");
                                                String unionid = obj.getString("unionid");

                                                SPUtils.getInstance().put("avatar",  obj.getString("headimgurl"));

                                                HashMap<String, Object> params = new HashMap();
                                                params.put("unionid", unionid);
                                                params.put("client_id", 3);
                                                params.put("openid", openid);
                                                params.put("nickname", nickname);
//                                                params.put("lat",0);
//                                                params.put("lng",0);
                                                Observable<WXCallBackResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                                                        .wxCallBack(FormdataRequestUtil.getFormDataRequestParams(params))
                                                        .compose(RxUtils.bindToLifecycle(WXEntryActivity.this)) //请求与View周期同步
                                                        .compose(RxUtils.schedulersTransformer()) //线程调度
                                                        .compose(RxUtils.exceptionTransformer()); // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                                                observable.subscribe(v -> {

                                                    Log.d("sort", "onResponse: "+new Gson().toJson(v).toString());
                                                    if (v.getStatus() == 1) {
                                                        SPUtils.getInstance().clear();

                                                        SPUtils.getInstance().put("mobile", v.getData().getMobile());
                                                        SPUtils.getInstance().put("nickname", v.getData().getNickname());
                                                        SPUtils.getInstance().put("avatar", v.getData().getAvatar());
                                                        SPUtils.getInstance().put("token", v.getData().getAccess_token());
                                                        SPUtils.getInstance().put("last_time", v.getData().getLast_time());
                                                        SPUtils.getInstance().put("uid", v.getData().getUid());
                                                        SPUtils.getInstance().put("password", v.getData().getPassword());

                                                        Messenger.getDefault().send("","refresh");
                                                        Messenger.getDefault().sendNoMsg("myRefresh");
                                                        WeChatNotify bean=new WeChatNotify(true,v.getInfo());
                                                        Messenger.getDefault().send(bean);
                                                        WXEntryActivity.this.setResult(RESULT_OK);
                                                        WXEntryActivity.this.finish();

//                                                        ToastUtils.showLong("授权成功");
                                                    }else{
                                                        setResult(RESULT_CANCELED);
                                                        finish();
                                                        ToastUtil.toastBottom("授权失败！");
                                                    }
                                                },e->{
                                                    setResult(RESULT_CANCELED);
                                                    finish();
                                                    ToastUtil.toastBottom("授权失败！！");
                                                });
                                            } catch (JSONException e) {
                                                setResult(RESULT_CANCELED);
                                                finish();
                                                ToastUtil.toastBottom("授权失败！！！");
                                                e.printStackTrace();
                                            }


                                        }
                                    });

                                } catch (JSONException e) {
                                    setResult(RESULT_CANCELED);
                                    finish();
                                    e.printStackTrace();
                                }

                            }
                        });


                        break;

                    case RETURN_MSG_TYPE_SHARE:
                        ToastUtil.toastBottom("分享成功");
                        finish();
                        break;
                }
                break;
        }
    }
}