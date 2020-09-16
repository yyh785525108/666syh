package com.yyh.read666.details;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.yyh.read666.R;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.utils.SharedPreferencesUtil;
import com.yyh.read666.view.IndicatorLineUtil;
import com.yyh.read666.vo.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;

public class GuankanzhongActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Button backBtn;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    private HttpImplement httpImplement;

    private String[] titles = new String[]{"进行中","已完成"};
    private String id;

    GuankanzhongFragment guankanzhongFragment1;
    GuankanzhongFragment guankanzhongFragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏

        setContentView(R.layout.activity_books);
        //方式一：这句代码必须写在setContentView()方法的后面
        getSupportActionBar().hide();
        initView();
        initData();

    }

    public void initView(){
        tabLayout = (TabLayout)findViewById(R.id.tablayout);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        backBtn=findViewById(R.id.back);
        backBtn.setOnClickListener(this);
         guankanzhongFragment1=new GuankanzhongFragment();
         guankanzhongFragment2=new GuankanzhongFragment();
        fragments.add(guankanzhongFragment1);
        tabLayout.addTab(tabLayout.newTab());
        fragments.add(guankanzhongFragment2);
        tabLayout.addTab(tabLayout.newTab());

        viewPager.setOffscreenPageLimit(1);//setOffscreenPageLimit  这个是缓存页面数量，默认缓存 1 页，也就是当前页的下一页

        tabLayout.setupWithViewPager(viewPager,false);
        FmPagerAdapter pagerAdapter = new FmPagerAdapter(fragments,getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        for(int i=0;i<titles.length;i++){
            tabLayout.getTabAt(i).setText(titles[i]);
        }
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                IndicatorLineUtil.setIndicator(tabLayout, 40, 40);
            }
        });


    }

    private void initData(){
        id=getIntent().getStringExtra("id");
        httpImplement=new HttpImplement();
        User user= SharedPreferencesUtil.getLoginUser(this);
        String access_token="";
        if (user!=null){
            access_token = user.getAccess_token();
        }
        httpImplement.learn(access_token,id,"online_member", new OkHttpUtils.MyNetCall() { //进行中
            @Override
            public void success(Call call, String response) throws IOException {
                   try {
                    JSONObject responseJson=new JSONObject(response);
                    int status=responseJson.getInt("status");
                    if (status==1){
                        JSONArray data=responseJson.getJSONArray("data");
                        System.out.println("data1:"+data.toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                guankanzhongFragment1.refreshListView(data);
                            }
                        });

                    }else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Call call, IOException e) {

            }
        });
        if (user==null){
            Toast.makeText(GuankanzhongActivity.this,"请先进行登录",Toast.LENGTH_SHORT).show();
        }else {
            httpImplement.learn(user.getAccess_token(),id,"wanbo_member", new OkHttpUtils.MyNetCall() { //进行中
                @Override
                public void success(Call call, String response) throws IOException {
                    try {
                        JSONObject responseJson=new JSONObject(response);
                        int status=responseJson.getInt("status");
                        if (status==1){
                            JSONArray data=responseJson.getJSONArray("data");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    System.out.println("data2:"+data.toString());
                                    guankanzhongFragment2.refreshListView(data);
                                }
                            });

                        }else {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failed(Call call, IOException e) {

                }
            });
        }


    }

    @Override
    public void onClick(View view) {
        if (view==backBtn){
            finish();
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
