package com.yyh.read666.tab1;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yyh.read666.R;
import com.yyh.read666.tab2.YinpingFragment;
import com.yyh.read666.view.IndicatorLineUtil;

import java.util.ArrayList;

public class VideoAudioReadActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] titles = new String[]{"视频","音频","文稿"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_audio_read);
        //方式一：这句代码必须写在setContentView()方法的后面
        getSupportActionBar().hide();
        initView();

    }

    public void initView(){
        tabLayout = (TabLayout)findViewById(R.id.tablayout);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        tabLayout.setSelectedTabIndicatorHeight(0);

        for(int i=0;i<titles.length;i++){

            YinpingFragment yinpingFragment=new YinpingFragment();
            fragments.add(yinpingFragment);
            tabLayout.addTab(tabLayout.newTab());
        }

        //监听一定要在setupWithViewPager方法之前添加,


        tabLayout.setupWithViewPager(viewPager,false);
//        FmPagerAdapter pagerAdapter = new FmPagerAdapter(fragments,getSupportFragmentManager());
//        viewPager.setAdapter(pagerAdapter);

        for(int i=0;i<titles.length;i++){
            tabLayout.getTabAt(i).setText(titles[i]);
        }
        tabLayout.post(new Runnable() {
            @Override
            public void run() {

                IndicatorLineUtil.setIndicator(tabLayout, 0, 0);
            }
        });

    }

    @Override
    public void onClick(View view) {

    }

    class  FmPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;

        public FmPagerAdapter(ArrayList<Fragment> fragments, FragmentManager fm) {
            super(fm);

            this.fragments=fragments;
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
