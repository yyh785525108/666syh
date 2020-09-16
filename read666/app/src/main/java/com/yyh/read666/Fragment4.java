package com.yyh.read666;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yyh.read666.tab4.RiJingjinFragment;
import com.yyh.read666.view.IndicatorLineUtil;

import java.util.ArrayList;

public class Fragment4 extends Fragment implements View.OnClickListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] titles = new String[]{"热榜","最新","关注"};

    private LinearLayout wenziLay,jinjieLay;
    private View wenziView,jinjieView;

    private ImageView cicle_containImg;
    private TextView circle_tv;
    private ImageView tab1Img,tab2Img,tab3Img,tab4Img;
    private TextView tab1Tv,tab2Tv,tab3Tv,tab4Tv;

    private RiJingjinFragment riJingjinFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_4,container,false);
        initView(view);

        return view;
    }

    public void initView(View view){
        tabLayout = (TabLayout)view.findViewById(R.id.tablayout);
        viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        wenziLay=(LinearLayout)view.findViewById(R.id.wenziLay);
        jinjieLay=(LinearLayout)view.findViewById(R.id.jinjieLay);
        wenziView=(View) view.findViewById(R.id.wenziView);
        jinjieView=(View)view.findViewById(R.id.jinjieView);
        cicle_containImg=view.findViewById(R.id.cicle_containImg);
        circle_tv=view.findViewById(R.id.circle_tv);
        tab1Img=view.findViewById(R.id.tab1Img);
        tab2Img=view.findViewById(R.id.tab2Img);
        tab3Img=view.findViewById(R.id.tab3Img);
        tab4Img=view.findViewById(R.id.tab4Img);
        tab1Tv=view.findViewById(R.id.tab1Tv);
        tab2Tv=view.findViewById(R.id.tab2Tv);
        tab3Tv=view.findViewById(R.id.tab3Tv);
        tab4Tv=view.findViewById(R.id.tab4Tv);


        wenziLay.setOnClickListener(this);
        jinjieLay.setOnClickListener(this);

        tab1Img.setOnClickListener(this);
        tab2Img.setOnClickListener(this);
        tab3Img.setOnClickListener(this);
        tab4Img.setOnClickListener(this);

        for(int i=0;i<titles.length;i++){

            RiJingjinFragment riJingjinFragment=new RiJingjinFragment();
            fragments.add(riJingjinFragment);
            tabLayout.addTab(tabLayout.newTab());
        }



        tabLayout.setupWithViewPager(viewPager,false);
        FmPagerAdapter pagerAdapter = new FmPagerAdapter(fragments,getChildFragmentManager());

        viewPager.setAdapter(pagerAdapter);

        for(int i=0;i<titles.length;i++){
            tabLayout.getTabAt(i).setText(titles[i]);
        }
        tabLayout.post(new Runnable() {
            @Override
            public void run() {

                IndicatorLineUtil.setIndicator(tabLayout, 0, 0);
            }
        });
//        MyPageListener myPageListener=new MyPageListener();
//        viewPager.setOnPageChangeListener(myPageListener);
//        myPageListener.onPageSelected(0);
    }

    @Override
    public void onClick(View view) {
        if (view==wenziLay){
            wenziView.setVisibility(View.VISIBLE);
            jinjieView.setVisibility(View.GONE);
            cicle_containImg.setImageResource(R.drawable.publish_btn);
            circle_tv.setText("写日精进");
            tab1Tv.setText("日精进");
            tab2Tv.setText("目标墙");
            tab3Tv.setText("进圈子");
            tab4Tv.setText("我的首页");
            tab1Img.setImageResource(R.drawable.zuoye);
            tab2Img.setImageResource(R.drawable.wish);
            tab3Img.setImageResource(R.drawable.qz);
            tab4Img.setImageResource(R.drawable.icon_zye_nor);


        }else if (view==jinjieLay){
            wenziView.setVisibility(View.GONE);
            jinjieView.setVisibility(View.VISIBLE);
            cicle_containImg.setImageResource(R.drawable.baogao);
            circle_tv.setText("察觉报告");
            tab1Tv.setText("草稿");
            tab2Tv.setText("已发表");
            tab3Tv.setText("同修伙伴");
            tab4Tv.setText("我的主页");
            tab1Img.setImageResource(R.drawable.icon_cg_nor);
            tab2Img.setImageResource(R.drawable.icon_yfb_nor);
            tab3Img.setImageResource(R.drawable.icon_hb_nor);
            tab4Img.setImageResource(R.drawable.icon_zye_nor);
        }
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
