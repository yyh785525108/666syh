package com.yyh.read666.tab1;

import android.app.Activity;
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

import com.google.android.material.tabs.TabLayout;
import com.yyh.read666.R;
import com.yyh.read666.view.IndicatorLineUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 词条
 */
public class CiTiaoDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] titles = new String[]{"必读干货", "作业讨论", "课程学习"};


    private TextView tipsTv, descriptionTv;
    private Button backBtn;
    private Button guanzhuBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_citiao_detail);
        //方式一：这句代码必须写在setContentView()方法的后面
        getSupportActionBar().hide();
        initView();
        initData();

    }

    public void initView() {
        tipsTv = findViewById(R.id.tipsTv);
        backBtn = findViewById(R.id.back);
        descriptionTv = findViewById(R.id.descriptionTv);
        guanzhuBtn = findViewById(R.id.guanzhuBtn);

        backBtn.setOnClickListener(this);
        guanzhuBtn.setOnClickListener(this);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);


    }

    private void initData() {
        try {
            JSONObject citiaoItemData = new JSONObject(getIntent().getStringExtra("citiaoItemData"));

            String id = citiaoItemData.getString("id");
            String title = citiaoItemData.getString("title");
            String description = citiaoItemData.getString("description");
            String sub_num = citiaoItemData.getString("sub_num");
            String num = citiaoItemData.getString("num");
            int is_sub = citiaoItemData.getInt("is_sub");
            tipsTv.setText(title);
            descriptionTv.setText(sub_num + "人关注  " + num + "条内容");
            guanzhuBtn.setSelected(is_sub == 0 ? false : true);


            GanhuoFragment ganhuoFragment = new GanhuoFragment();
            ganhuoFragment.id = id;
            fragments.add(ganhuoFragment);
            tabLayout.addTab(tabLayout.newTab());

            ZuoyeFragment zuoyeFragment = new ZuoyeFragment();
            zuoyeFragment.id = id;
            fragments.add(zuoyeFragment);
            tabLayout.addTab(tabLayout.newTab());

            KechengXuexiFragment kechengXuexiFragment = new KechengXuexiFragment();
            kechengXuexiFragment.id = id;
            fragments.add(kechengXuexiFragment);
            tabLayout.addTab(tabLayout.newTab());

            viewPager.setOffscreenPageLimit(2);//setOffscreenPageLimit  这个是缓存页面数量，默认缓存 1 页，也就是当前页的下一页

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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ;

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
