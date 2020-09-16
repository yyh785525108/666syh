package com.yyh.read666.tab5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yyh.read666.R;
import com.yyh.read666.login.LoginActivity;
import com.yyh.read666.login.LoginIndexActivity;
import com.yyh.read666.utils.SharedPreferencesUtil;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout xinxiLay,phoneLay,passLay,exitLay;
    private TextView statusTitleTv;
    private Button backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);
        //方式一：这句代码必须写在setContentView()方法的后面
        getSupportActionBar().hide();
        initView();


    }
    private void initView(){
        xinxiLay=findViewById(R.id.xinxiLay);
        phoneLay=findViewById(R.id.phoneLay);
        passLay=findViewById(R.id.passLay);
        exitLay=findViewById(R.id.exitLay);
        statusTitleTv=findViewById(R.id.statusTitleTv);
        statusTitleTv.setText("设置");
        backBtn=findViewById(R.id.back);

        backBtn.setOnClickListener(this);
        xinxiLay.setOnClickListener(this);
        phoneLay.setOnClickListener(this);
        passLay.setOnClickListener(this);
        exitLay.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if (view==backBtn){
            finish();
        }else if (view==xinxiLay){
            intent=new Intent(SettingActivity.this,PersonInfoActivity.class);
            startActivity(intent);
        }else if (view==phoneLay){
            Toast.makeText(this,"程序猿小哥正在加紧完善哦",Toast.LENGTH_SHORT).show();
        }else if (view==passLay){
            intent=new Intent(SettingActivity.this,PassSettingActivity.class);
            startActivity(intent);
        }else if (view==exitLay){
            SharedPreferencesUtil.clearLoginUser(SettingActivity.this);
            intent=new Intent(SettingActivity.this, LoginIndexActivity.class);
            startActivity(intent);
        }
    }
}
