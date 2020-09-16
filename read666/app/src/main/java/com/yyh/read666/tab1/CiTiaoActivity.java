package com.yyh.read666.tab1;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.yyh.read666.R;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.HttpInterface;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.tab2.WebFragment;
import com.yyh.read666.tab2.YinpingFragment;
import com.yyh.read666.utils.DateUtil;
import com.yyh.read666.utils.SharedPreferencesUtil;
import com.yyh.read666.view.IndicatorLineUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;

/**
 * 词条
 */
public class CiTiaoActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] titles = new String[]{"人生智慧", "财商智慧"};

    CiTiaoFragment ciTiaoFragment1,ciTiaoFragment2;

    private TextView statusTitleTv;
    private Button backBtn;
    private ImageView userImg;
    private TextView nameTv, priceTv, gengxinTv, zhujiangrenTv;
    private Button dingyueBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_citiao);
        //方式一：这句代码必须写在setContentView()方法的后面
        getSupportActionBar().hide();
        initView();
        initData();

    }

    public void initView() {
        statusTitleTv = findViewById(R.id.statusTitleTv);
        backBtn = findViewById(R.id.back);
        userImg = findViewById(R.id.userImg);
        nameTv = findViewById(R.id.nameTv);
        priceTv = findViewById(R.id.priceTv);
        gengxinTv = findViewById(R.id.gengxinTv);
        zhujiangrenTv = findViewById(R.id.zhujiangrenTv);
        dingyueBtn = findViewById(R.id.dingyueBtn);
        backBtn.setOnClickListener(this);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);



    }

    private void initData() {
        int type=getIntent().getIntExtra("type",0);
        String id=getIntent().getStringExtra("id");


        ciTiaoFragment1 = new CiTiaoFragment();
        ciTiaoFragment1.type=0;
        if (type==0){
            ciTiaoFragment1.id=id;
        }
        fragments.add(ciTiaoFragment1);
        tabLayout.addTab(tabLayout.newTab());

        ciTiaoFragment2 = new CiTiaoFragment();
        ciTiaoFragment2.type=1;
        if (type==1){
            ciTiaoFragment2.id=id;
        }
        fragments.add(ciTiaoFragment2);
        tabLayout.addTab(tabLayout.newTab());

        viewPager.setOffscreenPageLimit(1);//setOffscreenPageLimit  这个是缓存页面数量，默认缓存 1 页，也就是当前页的下一页

        tabLayout.setupWithViewPager(viewPager, false);
        FmPagerAdapter pagerAdapter = new FmPagerAdapter(fragments, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        for (int i = 0; i < titles.length; i++) {
            tabLayout.getTabAt(i).setText(titles[i]);
        }
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                IndicatorLineUtil.setIndicator(tabLayout, 20, 20);
            }
        });
        if (type==0){
            viewPager.setCurrentItem(0);
        }else {
            viewPager.setCurrentItem(1);
        }

    }


    @Override
    public void onClick(View view) {
        if (view == backBtn) {
            finish();
        }
    }

    class FmPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;

        public FmPagerAdapter(ArrayList<Fragment> fragments, FragmentManager fm) {
            super(fm);

            this.fragments = fragments;
        }


        @Override
        public int getCount() {

            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

    }
}
