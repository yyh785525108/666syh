package com.yyh.read666.welcome;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.yyh.read666.R;
import com.yyh.read666.login.LoginActivity;
import com.yyh.read666.login.LoginIndexActivity;
import com.yyh.read666.login.WelcomeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  wsl
 * on 2019/6/18 12:52
 */
public class EZActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewpager;
    private Button btnSubmit;
    private ImageView ivPoint1, ivPoint2, ivPoint3, ivPoint4;
    private List<ImageView> guidePointList;

    private Animation animation01 = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        //以上设置一定要在 super.onCreate(savedInstanceState) 方法之前设置
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ez_layout);
        //方式一：这句代码必须写在setContentView()方法的后面
        getSupportActionBar().hide();
        initView();
        initData();
    }




    protected void initView() {
        guidePointList = new ArrayList<>();
        viewpager = findViewById(R.id.viewpager);
        btnSubmit = findViewById(R.id.btnSubmit);
        ivPoint1 = findViewById(R.id.ivPoint1);
        ivPoint2 = findViewById(R.id.ivPoint2);
        ivPoint3 = findViewById(R.id.ivPoint3);
        ivPoint4 = findViewById(R.id.ivPoint4);
        btnSubmit.setOnClickListener(this);

        addGuidePointToList();
        animation01 = AnimationUtils.loadAnimation(this, R.anim.animation_guide_alpha_0_1);
    }

    /**
     * 添加引导点 到 list
     */
    private void addGuidePointToList() {
        guidePointList.add(ivPoint1);
        guidePointList.add(ivPoint2);
        guidePointList.add(ivPoint3);
//        guidePointList.add(ivPoint4);
    }


    protected void initData() {
        final List<Integer> list = new ArrayList<>();
        list.add(R.drawable.zhiyin1);
        list.add(R.drawable.zhiyin2);
        list.add(R.drawable.zhiyin3);
        final GuideAdapter guideAdapter = new GuideAdapter(EZActivity.this, list);
        viewpager.setAdapter(guideAdapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == list.size() - 1) {
                    btnSubmit.setVisibility(View.VISIBLE);
                    if (animation01 != null) {
                        btnSubmit.setAnimation(animation01);
                        animation01.start();
                    }
                } else {
                    btnSubmit.setVisibility(View.GONE);
                }

                setGuidePoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        viewpager.setPageTransformer(false, new DepthPageTransformer());


    }

    /**
     * 设置引导点  的状态 （选中、未选中）
     *
     * @param position 当前滑动到的位置
     */
    private void setGuidePoint(int position) {
        for (int i = 0; i < guidePointList.size(); i++) {
            if (position == i) {
                guidePointList.get(i).setImageResource(R.drawable.shape_guide_point_big);
            } else {
                guidePointList.get(i).setImageResource(R.drawable.shape_guide_point_small);
            }
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        if (v==btnSubmit){


            Intent intent=new Intent(EZActivity.this, LoginIndexActivity.class);
            startActivity(intent);
            finish();
        }
    }

}
