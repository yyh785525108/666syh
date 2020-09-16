package com.tchy.syh.userAccount.userLogin;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.ObservableField;

import android.util.Log;

import com.tchy.syh.app.AppApplication;
import com.tchy.syh.userAccount.AccountApiService;
import com.tchy.syh.userAccount.entity.LoginResponseEntity;
import com.tchy.syh.userAccount.forget.ForgetFragment;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.StringUtils;


public class LoginViewModel extends BaseViewModel {
    //用户名的绑定
    public ObservableField<String> userName = new ObservableField<>("");
    //密码的绑定
    public ObservableField<String> password = new ObservableField<>("");

    public ObservableField<String> verifyCode = new ObservableField<>("");


    public LoginViewModel(Context context) {
        super(context);
    }

    ObservableEmitter<String> gotoEmitter;
    ObservableOnSubscribe observableOnSubscribe = new ObservableOnSubscribe<String>() {
        @Override
        public void subscribe(ObservableEmitter<String> emitter) throws Exception {
            gotoEmitter=emitter;
        }
    };
    public Observable uiControl=Observable.create(observableOnSubscribe);
    public BindingCommand onSubBtnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //让观察者的数据改变,在View层的监听则会被调用
            Log.d("sort", "call: hhhhjhajsdhajkhdjkahsjkdhasj");

        }
    });


    public BindingCommand regClickCmd = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });
    public BindingCommand loginMobileOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            mobile_login();
        }
    });
    public BindingCommand loginAccountOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            login();
        }
    });
    public BindingCommand gotoMobileClickCmd = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //goto mobile login
            gotoEmitter.onNext("");
//            startContainerActivity(ForgetFragment.class.getCanonicalName());
        }
    });

    public BindingCommand gotoAccountClickCmd = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //goto account login
            Log.d("sort", "call: ");
        }
    });

    public BindingCommand gotoForgetPwdClickCmd = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //goto forget pwd
            startContainerActivity(ForgetFragment.class.getCanonicalName());
        }
    });
    public BindingCommand gotoWeixinLoginClickCmd = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //goto weixin login
            wxLogin();
        }
    });
    public void wxLogin() {
        if (!AppApplication.mWxApi.isWXAppInstalled()) {
            ToastUtil.toastBottom("您还未安装微信客户端");
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        AppApplication.mWxApi.sendReq(req);
    }
    /**
     * 网络模拟一个登陆操作
     **/
    private void login() {


//        if (TextUtils.isEmpty(userName.get())) {
//            ToastUtils.showShort("请输入账号！");
//            return;
//        }
//        if (TextUtils.isEmpty(password.get())) {
//            ToastUtils.showShort("请输入密码！");
//            return;
//        }
        loginApi();

    }

    private void loginApi()  {
        if(StringUtils.isEmpty(userName.get())){
            ToastUtil.toastBottom("请输入用户名");
            return ;
        }
        if(StringUtils.isEmpty(password.get())){
            ToastUtil.toastBottom("请输入密码");
            return ;
        }
        if(password.get()!=null&&password.get().length()<8){
            ToastUtil.toastBottom("请输入8-18位密码");
            return ;
        }
        HashMap<String,Object> params=new HashMap();

        params.put("username",userName.get());
        params.put("password",password.get());
        params.put("client_id","3");


        Observable<LoginResponseEntity> observable = RetrofitClient.getInstance().create(AccountApiService.class)
                .login(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {});

        observable.subscribe(res->{
            if(res.getStatus()==1){
                SPUtils.getInstance().put("mobile", res.getData().getMobile());
                SPUtils.getInstance().put("nickname", res.getData().getNickname());
                SPUtils.getInstance().put("avatar", res.getData().getAvatar());
                SPUtils.getInstance().put("token", res.getData().getAccess_token());
                SPUtils.getInstance().put("uid", res.getData().getUid());
                ((Activity) context).finish();

                me.goldze.mvvmhabit.bus.Messenger.getDefault().send("","refresh" );
                ToastUtil.toastBottom("登录成功");
            }else{
                ToastUtil.toastBottom(res.getInfo());
            }
            //关闭页面

        },e->{
            dismissDialog();
            e.printStackTrace();
        });

    }
    public void mobile_login()  {
        HashMap<String,Object> params=new HashMap();
        params.put("mobile",userName.get());
        params.put("code",password.get());

        Observable<LoginResponseEntity> observable = RetrofitClient.getInstance().create(AccountApiService.class)
                .mobile_login(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {showDialog();});

        observable.subscribe(res->{
            dismissDialog();
            //进入DemoActivity页面
            //关闭页面
            ((Activity) context).finish();
            Log.d("sort", "requestNetWork: "+res.getData().getAccess_token());
        },e->{
            dismissDialog();
            e.printStackTrace();
        });

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.gotoEmitter=null;
    }
}
