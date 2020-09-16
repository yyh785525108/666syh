package com.tchy.syh.custom.edittext;

import android.content.Context;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import android.util.Log;

import com.tchy.syh.common.entity.CommonDataListResp;
import com.tchy.syh.userAccount.AccountApiService;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.StringUtils;


public class CustomEditTextVM extends BaseViewModel {
    public long defaultCounter = 60l;

    public ObservableBoolean isSupportClear = new ObservableBoolean(true);

    public ObservableBoolean canGetVerifyCode = new ObservableBoolean(true);
    public ObservableBoolean onFocus = new ObservableBoolean(false);

    Observable<Long> timer;
    //用户名的绑定
    public ObservableField<String> text = new ObservableField<>("");
    public ObservableField<String> param = new ObservableField<>("");

    public ObservableField<Long> counter = new ObservableField<>(defaultCounter);
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();
    public BindingCommand SubObserve;

    public class UIChangeObservable {
        //密码开关观察者
        public ObservableBoolean pSwitchObservable = new ObservableBoolean(false);
    }

    public CustomEditTextVM(Context context) {
        this(context, true);
    }

    public CustomEditTextVM(Context context, boolean isSupportClear) {
        super(context);
        this.isSupportClear.set(isSupportClear);
    }

    public BindingCommand clearUserNameOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            text.set("");
        }
    });
    public BindingCommand onSubBtnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (SubObserve != null)
                SubObserve.execute();
            else {


            }
        }
    });

    public BindingCommand passwordShowSwitchOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //让观察者的数据改变,在View层的监听则会被调用
            Log.d("sort", "call: " + uc.pSwitchObservable.get());
            uc.pSwitchObservable.set(!uc.pSwitchObservable.get());
        }
    });
    //用户名输入框焦点改变的回调事件
    public BindingCommand<Boolean> onEditFocusChangeCommand = new BindingCommand<>(new BindingConsumer<Boolean>() {
        @Override
        public void call(Boolean hasFocus) {
            onFocus.set(hasFocus);

        }
    });

    public BindingCommand getVerifyCodeOnClickCmd = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (SubObserve != null)
                SubObserve.execute();
            else {
                getVerifyCode();
            }

        }
    });


    private void getVerifyCode() {
        Log.d("sort", "getVerifyCode: ");
        if (!canGetVerifyCode.get()) {
            return;
        }

        HashMap<String, Object> params = new HashMap();
        params.put("mobile", param.get());
        String token=SPUtils.getInstance().getString("token");
        if(!StringUtils.isEmpty(token)){
            params.put("access_token", token);

        }
        Observable<CommonDataListResp> observable = RetrofitClient.getInstance().create(AccountApiService.class)
                .get_sms(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });

        observable.subscribe(res -> {
            //进入DemoActivity页面
            //关闭页面
            if (res.getStatus() == 1) {
                canGetVerifyCode.set(false);

                Observable.intervalRange(1, defaultCounter, 0, 1, TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(v -> {
//                    counter.set(defaultCounter - v);
                            if (defaultCounter == (long) v) {
                                canGetVerifyCode.set(true);
                                counter.set(-1l);
                            }
                            counter.set(defaultCounter - v);

                            Log.d("sort", "getVerifyCode: " + v);
                        });
                ToastUtil.toastBottom("验证码已发送");
            } else {
                ToastUtil.toastBottom(res.getInfo());
            }


        }, e -> {
            e.printStackTrace();
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
