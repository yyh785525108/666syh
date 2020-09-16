package com.tchy.syh.userAccount.account_management.mod_info;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.ObservableField;
import androidx.core.app.ActivityCompat;

import com.tchy.syh.common.entity.CommonDataListResp;
import com.tchy.syh.userAccount.AccountApiService;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.ToastUtil;

import java.io.File;
import java.net.URI;
import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;
import me.goldze.mvvmhabit.utils.StringUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ModInfoVm extends BaseViewModel {
    public ObservableField<String> avatar = new ObservableField<>();
    public ObservableField<String> nickname = new ObservableField<>();
    public ObservableField<String> gender = new ObservableField<>();
    public ObservableField<String> birthday = new ObservableField<>();
    public ObservableField<String> birthdayVal = new ObservableField<>();

    public ModInfoVm(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        birthday.set(SPUtils.getInstance().getString("birthday"));
        gender.set(SPUtils.getInstance().getString("sex"));
        nickname.set(SPUtils.getInstance().getString("nickname"));
        avatar.set(SPUtils.getInstance().getString("avatar"));

    }

    public BindingCommand modAvatar = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            api(0);
        }
    });
    public BindingCommand modBirthday = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            api(3);
        }
    });
    public BindingCommand modGender = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            api(2);
        }
    });
    public BindingCommand submitClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            api(1);
        }
    });
    public BindingCommand nicknameClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            startContainerActivity(ModNickNameFragment.class.getCanonicalName());
        }
    });
    public URI picFrontUri;

    public void api(int type) {
        HashMap<String, Object> params = new HashMap();
        MultipartBody.Part bodyFront = null;
        switch (type) {
            case 0:
                params.put("avatar", avatar.get());

                if (picFrontUri != null) {
                    File fileFront = new File(picFrontUri);
                    bodyFront = MultipartBody.Part.createFormData("avatar", fileFront.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), fileFront));
                }
                break;
            case 1:
                if(StringUtils.isEmpty(nickname.get())){
                    ToastUtil.toastBottom("请输入昵称");
                    return;
                }
                params.put("nickname", nickname.get());
                break;
            case 2:
                int sex = 0;
                switch (gender.get()) {
                    case "保密":
                        sex = 0;
                        break;
                    case "男":
                        sex = 1;
                        break;
                    case "女":
                        sex = 2;
                        break;
                }
                params.put("sex", sex);
                break;
            default:
                params.put("birthday", birthdayVal.get());
        }
        params.put("access_token", SPUtils.getInstance().getString("token"));



        Observable<CommonDataListResp> observable = RetrofitClient.getInstance().create(AccountApiService.class)
                .modify_user(FormdataRequestUtil.getFormDataRequestParams(params), bodyFront)
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
//                    showDialog();
                });

        observable.subscribe(res -> {
            //进入DemoActivity页面
            //关闭页面
//            ((Activity) context).finish();
            if (res.getStatus() == 1) {
                ToastUtil.toastBottom("修改成功");
                SPUtils.getInstance().put("nickname", nickname.get());
                SPUtils.getInstance().put("sex", gender.get());
                SPUtils.getInstance().put("birthday", birthday.get());
                me.goldze.mvvmhabit.bus.Messenger.getDefault().sendNoMsg("myRefresh");
            } else {
                ToastUtil.toastBottom(res.getInfo());
            }

            if (type == 1) {
                ((Activity) context).finish();
            }
        }, e -> {
            e.printStackTrace();
        });
    }


}
