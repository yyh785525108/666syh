package com.tchy.syh.userAccount.register;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebViewFragment;

import com.tchy.syh.userAccount.AccountApiService;
import com.tchy.syh.userAccount.entity.LoginResponseEntity;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;


public class RegisterVM extends BaseViewModel {
    //用户名的绑定
    public ObservableField<String> userName = new ObservableField<>("");
    //密码的绑定
    public ObservableField<String> password = new ObservableField<>("");

    public ObservableField<String> verifyCode = new ObservableField<>("");
    public ObservableBoolean isAgree = new ObservableBoolean(true);
    public RegisterVM(Context context) {
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



    public BindingCommand register = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            register();
        }
    });

    public BindingCommand gotoProtocal = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startContainerActivity(WebViewFragment.class.getCanonicalName());
        }
    });




    /**
     * 网络模拟一个登陆操作
     **/
    private void register() {


        if (TextUtils.isEmpty(userName.get())) {
            ToastUtil.toastBottom("请输入账号！");
            return;
        }
        if (TextUtils.isEmpty(verifyCode.get())) {
            ToastUtil.toastBottom("请输入验证码！");
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtil.toastBottom("请输入密码！");
            return;
        }
        if (!isAgree.get()) {
            ToastUtil.toastBottom("阅读并同意协议后才能注册！");
            return;
        }
//        showDialog();
        registerAPI();
    }
    private void registerAPI()  {
        HashMap<String,Object> params=new HashMap();
        params.put("mobile",userName.get());
        params.put("password",password.get());
        params.put("code",verifyCode.get());


        Observable<LoginResponseEntity> observable = RetrofitClient.getInstance().create(AccountApiService.class)
                .register(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {showDialog();});

        observable.subscribe(res->{
            //进入DemoActivity页面
            //关闭页面
            ((Activity) context).finish();
            Log.d("sort", "requestNetWork: "+res.getData().getAccess_token());
        },e->{
            e.printStackTrace();
        });

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.gotoEmitter=null;
    }
}
