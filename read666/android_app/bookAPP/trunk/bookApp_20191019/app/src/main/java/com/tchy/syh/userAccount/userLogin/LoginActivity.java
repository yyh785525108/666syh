package com.tchy.syh.userAccount.userLogin;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;

import com.tchy.syh.R;
import com.tchy.syh.base.BCBaseActivity;
import com.tchy.syh.home.HomeActivity;
import com.tchy.syh.userAccount.entity.WeChatNotify;
import com.tchy.syh.utils.ToastUtil;

import java.util.Date;

import me.goldze.mvvmhabit.bus.Messenger;

/**
 * 一个MVVM模式的登陆界面
 */
public class LoginActivity extends BCBaseActivity {
    Fragment account ;
    Fragment mobile ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login_activity);
        account = new LoginAccountFragment();
        mobile = new LoginMobileFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, account, "account");
        transaction.add(R.id.fragment_container, mobile, "mobile");
        transaction.hide(mobile);
        transaction.show(account);
        transaction.commit();
        overridePendingTransition(R.anim.right_enter,R.anim.left_leave );
        Messenger.getDefault().register(this,WeChatNotify.class,(v)->{
            if(v.isSuccess){
                if(getIntent().getBooleanExtra("isExit",false )){
                    Intent intent=new Intent(this, HomeActivity.class);

                    ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(this, R.anim.right_enter,R.anim.left_leave );

                    this.startActivity(intent,options.toBundle());
                    this.finishAffinity();

                }else{
                    ToastUtil.toastBottom("授权登录成功");
//                    Messenger.getDefault().send("","refresh" );
                    Intent intent =new Intent();
                    this.finish();

                }

            }

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Messenger.getDefault().unregister(this);
    }

    public void switchFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Log.d("sort", "onCreate: "+mobile.isHidden());
        Log.d("sort", "onCreate: "+account.isHidden());
        if(mobile.isHidden()){
            transaction.setCustomAnimations(R.anim.right_enter,R.anim.left_leave);
            transaction.show(mobile);
            transaction.hide(account);
        } else if(account.isHidden()){
            transaction.setCustomAnimations(R.anim.left_enter,R.anim.right_leave);

            transaction.show(account);
            transaction.hide(mobile);
        }
        transaction.commit();
    }
    public long firstTime=0;
    @Override
    public void onBackPressed() {
        long currTime=new Date().getTime();
        if(currTime-firstTime<=2000){
            this.finishAffinity();
        }else{
            ToastUtil.toastBottom("再次按下返回键退出应用");
            firstTime=new Date().getTime();
        }



    }


}
