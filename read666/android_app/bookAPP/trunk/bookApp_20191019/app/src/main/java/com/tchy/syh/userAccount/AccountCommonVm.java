package com.tchy.syh.userAccount;

import android.app.Activity;
import androidx.room.util.StringUtil;
import android.content.Context;
import androidx.databinding.ObservableField;
import android.os.Bundle;
import android.text.TextUtils;

import com.tchy.syh.R;
import com.tchy.syh.common.entity.CommonDataListResp;
import com.tchy.syh.userAccount.account_management.realcert.RealCertPictureFragment;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.StringUtils;

public class AccountCommonVm extends BaseViewModel {
    public ObservableField<String> mobile = new ObservableField<>();
    public ObservableField<String> verifyCode = new ObservableField<>();
    public ObservableField<String> email = new ObservableField<>();
    public ObservableField<String> old_pwd = new ObservableField<>();

    public ObservableField<String> password = new ObservableField<>();
    public ObservableField<String> confirmPassword = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>("");
    public ObservableField<String> id = new ObservableField<>("");
    public ObservableField<String> image1 = new ObservableField<>();
    public ObservableField<String> image2 = new ObservableField<>();
    public ObservableField<String> image3 = new ObservableField<>();

    public AccountCommonVm(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public BindingCommand forgetCheck = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (StringUtils.isEmpty(mobile.get())) {
                ToastUtil.toastBottom("请输入手机号");
                return;
            }
            if (StringUtils.isEmpty(verifyCode.get())) {
                ToastUtil.toastBottom("请输入验证码");
                return;
            }
            if (StringUtils.isEmpty(password.get())) {
                ToastUtil.toastBottom("请输入密码");
                return;
            }
            if (password.get().length()<8) {
                ToastUtil.toastBottom("请输入最少8位密码");
                return;
            }
            if (confirmPassword.get().equals(password.get())) {
                HashMap<String, Object> params = new HashMap();
                params.put("mobile", mobile.get());
                params.put("sms_code", verifyCode.get());
                params.put("password", password.get());

                Observable<CommonDataListResp> observable = RetrofitClient.getInstance().create(AccountApiService.class)
                        .reset_password(FormdataRequestUtil.getFormDataRequestParams(params))
                        .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                        .compose(RxUtils.schedulersTransformer()) //线程调度
                        .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                        .doOnSubscribe(disposable -> {
                        });

                observable.subscribe(res -> {
                    //进入DemoActivity页面
                    //关闭页面
                    if(res.getStatus()==1){
                        ((Activity) context).finish();
                        ToastUtil.toastBottom("重置密码成功");
                    }else{
                        ToastUtil.toastBottom(res.getInfo());
                    }

                }, e -> {
                    e.printStackTrace();
                });
            }else{
                ToastUtil.toastBottom("两次密码输入不一致");
            }
//            Bundle bundle = new Bundle();
//            bundle.putString("mobile",mobile.get());
//            bundle.putString("email", verifyCode.get());
//            bundle.putString("password", password.get());
//            startContainerActivity(ForgetSelectFragment.class.getCanonicalName(), bundle);
//            ((Activity) context).finish();
//            HashMap<String, Object> params = new HashMap();
//            params.put("code", verifyCode.get());
//            Observable<CommonResp> observable = RetrofitClient.getInstance().create(AccountApiService.class)
//                    .check(FormdataRequestUtil.getFormDataRequestParams(params))
//                    .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
//                    .compose(RxUtils.schedulersTransformer()) //线程调度
//                    .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
//                    .doOnSubscribe(disposable -> {
//                        showDialog();
//                    });
//
//            observable.subscribe(res -> {
//                //进入DemoActivity页面
//                //关闭页面
//                ((Activity) context).finish();
//            }, e -> {
//                e.printStackTrace();
//            });
        }
    });

    public BindingCommand modifyPwd = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            updatePwd();
        }
    });
    public BindingCommand modifyMobile = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            registerAPI();
        }
    });
    public BindingCommand cert = new BindingCommand(new BindingAction() {
        @Override
        public void call() {


            ((Activity) context).finish();
        }
    });
    public BindingCommand certNext = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if (TextUtils.isEmpty(id.get()) || TextUtils.isEmpty(name.get())) {
                ToastUtil.toastBottom("姓名或身份证号不能为空");
                return;
            }
            if (id.get() != null && id.get().length() != 18) {
                ToastUtil.toastBottom("请输入合法的二代身份证号码");
                return;
            }

            Bundle bundle = new Bundle();
            bundle.putString("id", id.get());
            bundle.putString("name", name.get());

            startContainerActivity(RealCertPictureFragment.class.getCanonicalName(), bundle);
            ((Activity) context).finish();
        }
    });

    private void verifyMobile() {

        if (StringUtils.isEmpty(mobile.get())) {
            ToastUtil.toastBottom("请输入手机号");
            return;
        }
        if (StringUtils.isEmpty(verifyCode.get())) {
            ToastUtil.toastBottom("请输入验证码");
            return;
        }
        HashMap<String, Object> params = new HashMap();
        params.put("mobile", mobile.get());
        params.put("code", verifyCode.get());
        params.put("access_token", SPUtils.getInstance().getString("token"));


        Observable<CommonDataListResp> observable = RetrofitClient.getInstance().create(AccountApiService.class)
                .register(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                ;

        observable.subscribe(res -> {
            if (res.getStatus() == 1) {
                Bundle bundle = new Bundle();
                bundle.putInt("icon", R.mipmap.bind_mobile);
                bundle.putString("msg", "绑定成功");
                bundle.putString("title", "绑定手机号");
                bundle.putString("btnText", "确定");
                startContainerActivity(ResultFragment.class.getCanonicalName(), bundle);
                ((Activity) context).finish();
            } else {
                ToastUtil.toastBottom(res.getInfo());
            }

        }, e -> {
            e.printStackTrace();
        });

    }

    private void registerAPI() {

        if (StringUtils.isEmpty(mobile.get())) {
            ToastUtil.toastBottom("请输入手机号");
            return;
        }
        if (StringUtils.isEmpty(verifyCode.get())) {
            ToastUtil.toastBottom("请输入验证码");
            return;
        }
        HashMap<String, Object> params = new HashMap();
        params.put("mobile", mobile.get());
        params.put("code", verifyCode.get());
        params.put("access_token", SPUtils.getInstance().getString("token"));


        Observable<CommonDataListResp> observable = RetrofitClient.getInstance().create(AccountApiService.class)
                .register(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                ;

        observable.subscribe(res -> {
            if (res.getStatus() == 1) {
                Bundle bundle = new Bundle();
                bundle.putInt("icon", R.mipmap.bind_mobile);
                bundle.putString("msg", "绑定成功");
                bundle.putString("title", "绑定手机号");
                bundle.putString("btnText", "确定");
                SPUtils.getInstance().put("mobile",mobile.get() );
                startContainerActivity(ResultFragment.class.getCanonicalName(), bundle);
                ((Activity) context).finish();
            } else {
                ToastUtil.toastBottom(res.getInfo());
            }

        }, e -> {
            e.printStackTrace();
        });

    }

    private void updatePwd() {
        if (StringUtils.isEmpty(password.get())) {
            ToastUtil.toastBottom("请输入新密码");
            return;
        }
        if (password.get().length() < 8) {
            ToastUtil.toastBottom("密码不能少于8位");
            return;
        }
        if (StringUtils.isEmpty(confirmPassword.get())) {
            ToastUtil.toastBottom("请输入确认密码");
            return;
        }
        if (password.get() != null && !password.get().equals(confirmPassword.get())) {
            ToastUtil.toastBottom("新密码和确认密码输入不一致");
            return;
        }
        HashMap<String, Object> params = new HashMap();
        params.put("old_password", old_pwd.get());
        params.put("new_password", password.get());
        params.put("access_token", SPUtils.getInstance().getString("token"));


        Observable<CommonDataListResp> observable = RetrofitClient.getInstance().create(AccountApiService.class)
                .modify_password(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer());// 网络错误的异常转换, 这里可以换成自己的ExceptionHandle

        observable.subscribe(res -> {
            if (res.getStatus() == 1) {

                Bundle bundle = new Bundle();
                bundle.putInt("icon", R.mipmap.bind_mobile);
                bundle.putString("msg", "设置成功");
                bundle.putString("title", "设置密码");
                bundle.putString("btnText", "确定");
                ToastUtil.toastBottom("修改成功");

                startContainerActivity(ResultFragment.class.getCanonicalName(), bundle);
                ((Activity) context).finish();
            } else {
                ToastUtil.toastBottom(res.getInfo());
            }

        }, e -> {
            e.printStackTrace();
        });

    }

    public BindingCommand nextClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

        }
    });

}
