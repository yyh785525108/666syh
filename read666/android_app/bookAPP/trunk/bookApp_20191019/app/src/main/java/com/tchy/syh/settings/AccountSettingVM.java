package com.tchy.syh.settings;

import android.content.Context;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import android.os.Bundle;
import android.text.TextUtils;

import com.tchy.syh.R;
import com.tchy.syh.my.ApiService;
import com.tchy.syh.my.entity.MyResp;
import com.tchy.syh.userAccount.ResultFragment;
import com.tchy.syh.userAccount.account_management.modify_mobile.ModMobileFragment;
import com.tchy.syh.userAccount.account_management.pwd.ModPwdFragment;
import com.tchy.syh.userAccount.account_management.realcert.RealCertFragment;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;

import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;

public class AccountSettingVM extends BaseViewModel {

    public ObservableField<MyResp.DataBean> bean=new ObservableField<>();
    public AccountSettingVM(Context context){
        super(context);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        user_info();

    }

    public ObservableField<String> mobile=new ObservableField<>();
    public ObservableInt mobileState=new ObservableInt();
    public ObservableField<String> pwd=new ObservableField<>();
    public ObservableInt pwdState=new ObservableInt();
    public ObservableField<String> real=new ObservableField<>();
    public ObservableField<String>  realState=new ObservableField<>();
    public BindingCommand mobileClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle =new Bundle();
            bundle.putInt("state",mobileState.get() );
            startContainerActivity(ModMobileFragment.class.getCanonicalName(),bundle);
        }
    });
    public  BindingCommand pwdClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            Bundle bundle =new Bundle();
            bundle.putInt("state",pwdState.get() );
            startContainerActivity(ModPwdFragment.class.getCanonicalName(),bundle);

        }
    });
    public BindingCommand realClick=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
           if(realState.get()!=null&&realState.get().equals("0")){
                Bundle bundle=new Bundle();
                bundle.putInt("icon", R.mipmap.group2 );
                bundle.putString("title", "实名认证");
                bundle.putString("msg", "认证已提交,请耐心等候审核结果！");
                bundle.putString("btnText", "确定");
                startContainerActivity(ResultFragment.class.getCanonicalName(),bundle);
            }else {
                Bundle bundle =new Bundle();
                bundle.putString("state",realState.get() );
                startContainerActivity(RealCertFragment.class.getCanonicalName(),bundle);
            }


        }
    });

    public void user_info(){
        HashMap<String, Object> params = new HashMap();

        params.put("access_token", SPUtils.getInstance().getString("token"));
//        params.put("page", this.pageNum);
        Observable<MyResp> observable = RetrofitClient.getInstance().create(ApiService.class)
                .user_info(FormdataRequestUtil.getFormDataRequestParams(params))
                .compose(RxUtils.bindToLifecycle(context)) //请求与View周期同步
                .compose(RxUtils.schedulersTransformer()) //线程调度
                .compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
                .doOnSubscribe(disposable -> {
                });
        observable.subscribe(v -> {
            if(v.getStatus()==1){

                bean.set(v.getData());
                if(TextUtils.isEmpty(v.getData().getMobile())){
                    mobileState.set(0);
                    mobile.set("请设置");
                }else{
                    mobileState.set(1);
                    String s=v.getData().getMobile();
                    StringBuilder mobileBuilder=new StringBuilder();
                    if(!TextUtils.isEmpty(s)){
                        int eLen=s.length();
                        if(eLen>=11){
                            String startStr=s.substring(0,3);
                            mobileBuilder.append(startStr);
                            String endStr=s.substring(eLen-2);


                            int placeHolderLength =eLen-startStr.length()-endStr.length();
                            for(int i=0;i<placeHolderLength;i++){
                                mobileBuilder.append("*");
                            }
                            mobileBuilder.append(endStr);
                        }
                    }
                    mobile.set(mobileBuilder.toString());
                }
                pwdState.set(v.getData().getSet_pwd());
                realState.set(v.getData().getRenzheng());
                if(v.getData().getSet_pwd()==0){
                    pwd.set("请设置");
                }else{
                    String s="";
                    pwd.set("********");
                }
                if(v.getData().getRenzheng()==null||v.getData().getRenzheng().equals("-1")){
                    real.set("请设置");

                }else if(v.getData().getRenzheng().equals("0")){
                    real.set("已提交");
                }else if(v.getData().getRenzheng().equals("1")){
                    real.set("已认证");
                }else{
                    real.set("认证失败,请重新提交");
                }
                SPUtils.getInstance().put("nickname", v.getData().getNickname());
                SPUtils.getInstance().put("avatar", v.getData().getAvatar());
                SPUtils.getInstance().put("birthday", v.getData().getBirthday());
                SPUtils.getInstance().put("sex", v.getData().getSex());

                SPUtils.getInstance().put("password", v.getData().getPassword());
                SPUtils.getInstance().put("level", v.getData().getLevel());
                SPUtils.getInstance().put("level_name", v.getData().getLevel_name());
                SPUtils.getInstance().put("deadline", v.getData().getDeadline());
                SPUtils.getInstance().put("parent_name", v.getData().getParent_name());
                SPUtils.getInstance().put("parent_id", v.getData().getParent_id());
                SPUtils.getInstance().put("money", v.getData().getMoney()+"");
                SPUtils.getInstance().put("fensi", v.getData().getFensi());
                SPUtils.getInstance().put("birthday", v.getData().getBirthday());
                SPUtils.getInstance().put("is_qiandao", v.getData().getIs_qiandao());
                SPUtils.getInstance().put("mobile",  v.getData().getMobile());
                SPUtils.getInstance().put("is_new",  v.getData().getIs_new());
            }else{

            }


        },e -> {
            e.printStackTrace();
        });

    }
}
