package com.yyh.read666.tab5;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.bugly.beta.Beta;
import com.yyh.read666.R;
import com.yyh.read666.WebActivity;
import com.yyh.read666.login.LoginIndexActivity;
import com.yyh.read666.okhttp.UrlConstant;
import com.yyh.read666.utils.SharedPreferencesUtil;

public class GuanyuActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout lianxiLay,xieyiLay,versionLay,yinsiLay;

    private Button backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guanyu);
        //方式一：这句代码必须写在setContentView()方法的后面
        getSupportActionBar().hide();
        initView();


    }
    private void initView(){
        lianxiLay=findViewById(R.id.lianxiLay);
        xieyiLay=findViewById(R.id.xieyiLay);
        versionLay=findViewById(R.id.versionLay);
        yinsiLay=findViewById(R.id.yinsiLay);

        backBtn=findViewById(R.id.back);

        backBtn.setOnClickListener(this);
        xieyiLay.setOnClickListener(this);
        versionLay.setOnClickListener(this);
        lianxiLay.setOnClickListener(this);
        yinsiLay.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if (view==backBtn){
            finish();
        }else  if (view==xieyiLay){
            intent=new Intent(GuanyuActivity.this, WebActivity.class);
            intent.putExtra("url", UrlConstant.GUANYU);
            startActivity(intent);
        }else  if (view==versionLay){
//            Toast.makeText(GuanyuActivity.this,"当前版本已是最新版本",Toast.LENGTH_SHORT).show();
            Beta.checkUpgrade(true,true);
        }else  if (view==lianxiLay){
            intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + "4006711319");
            intent.setData(data);
            startActivity(intent);
        }else if (view==yinsiLay){
            intent=new Intent(GuanyuActivity.this, WebActivity.class);
            intent.putExtra("url", UrlConstant.YINSI);
            startActivity(intent);
        }
    }
}
