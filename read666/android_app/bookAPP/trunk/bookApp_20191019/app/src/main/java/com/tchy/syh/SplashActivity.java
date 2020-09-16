package com.tchy.syh;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.taobao.sophix.SophixManager;
import com.tchy.syh.home.HomeActivity;

import me.goldze.mvvmhabit.utils.SPUtils;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);



        SophixManager.getInstance().queryAndLoadNewPatch();


        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isFirst=SPUtils.getInstance().getBoolean("isFirstStartup",true);
                Intent intent=new Intent();
                if(isFirst){
                    intent.setClass(SplashActivity.this, WelcomActivity.class);

                }else{

                    intent.setClass(SplashActivity.this, HomeActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 500);



    }
}
