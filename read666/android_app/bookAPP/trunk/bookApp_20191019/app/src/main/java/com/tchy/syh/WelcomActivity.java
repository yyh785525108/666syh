package com.tchy.syh;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.tchy.syh.common.CommonImageFragment;
import com.tchy.syh.home.HomeActivity;

import java.util.ArrayList;
import java.util.List;


public class WelcomActivity extends AppCompatActivity {
    List<Fragment> list =new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);

        CommonImageFragment f1=CommonImageFragment.getInstance(R.mipmap.guide1,false);
        CommonImageFragment f2=CommonImageFragment.getInstance(R.mipmap.guide2,false);
        CommonImageFragment f3=CommonImageFragment.getInstance(R.mipmap.guide3,true);
        list.add(f1);
        list.add(f2);
        list.add(f3);
        ViewPager vp=findViewById(R.id.vp);
        final TextView skip=findViewById(R.id.skip);

        skip.setOnClickListener(v->{
            Intent intent =new Intent();
            intent.setClass(this, HomeActivity.class);
            startActivity(intent);
            this.finish();
        });
        vp.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==2){
                    skip.setText("进入应用");
                    skip.setVisibility(View.GONE);
                }else{
                    skip.setText("跳过");
                    skip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    public class FragmentAdapter extends FragmentStatePagerAdapter {

        public FragmentAdapter(FragmentManager manager){
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
