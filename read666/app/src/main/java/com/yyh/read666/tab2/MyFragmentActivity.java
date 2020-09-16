package com.yyh.read666.tab2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.yyh.read666.Fragment1;
import com.yyh.read666.Fragment2;
import com.yyh.read666.Fragment3;
import com.yyh.read666.Fragment5;
import com.yyh.read666.FragmentBackListener;
import com.yyh.read666.R;
import com.yyh.read666.WebFragment;
import com.yyh.read666.configs.Configs;
import com.yyh.read666.details.DetailControlActivity;
import com.yyh.read666.okhttp.UrlConstant;

public class MyFragmentActivity extends AppCompatActivity implements View.OnClickListener {

    private Fragment fragment;
    private Button backBtn;
    private TextView statusTitleTv;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfragment);
        getSupportActionBar().hide();
        backBtn=findViewById(R.id.back);
        backBtn.setOnClickListener(this);
        statusTitleTv=findViewById(R.id.statusTitleTv);
        Intent intent=getIntent();
        int type=intent.getIntExtra("type",0);
        if (type==1){//音频
            fragment=new Fragment2();
            statusTitleTv.setText("音频专栏");
        }

            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).show(fragment)
                    .commit();


    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }}

}
