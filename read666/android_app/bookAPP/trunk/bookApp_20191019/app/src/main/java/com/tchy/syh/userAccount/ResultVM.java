package com.tchy.syh.userAccount;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tchy.syh.userAccount.userLogin.LoginActivity;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.SPUtils;

public class ResultVM extends BaseViewModel{
    public int iconid;
    public String msg="";
    public String title="";
    public String btnText="";
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public ResultVM(Context context,int iconid, String msg, String title, String btnText){
        super(context);
        this.iconid=iconid;
        this.msg=msg;
        this.title=title;
        this.btnText=btnText;

    }
    public BindingCommand click=new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            if(title=="设置密码"){
                Intent intent1=new Intent();
                intent1.putExtra("isExit", true);
                intent1.setClass(context,LoginActivity.class );
                context.startActivity(intent1);
                SPUtils.getInstance().clear();
                SPUtils.getInstance().put("isFirstStartup", false);

            }else{
                ((Activity)context).finish();

            }
        }
    });
}
