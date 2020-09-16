package com.yyh.read666.tab1;

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
import com.yyh.read666.tab2.YinpingFragment;
import com.yyh.read666.view.IndicatorLineUtil;
import com.yyh.read666.view.SmartFragmentStatePagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;

public class BooksActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    private Button backBtn;
    private HttpImplement httpImplement;
    private JSONArray data;


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

    }

    private void initData(){
        httpImplement=new HttpImplement();
        httpImplement.learn_items_cate_get("book", new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                   try {
                    JSONObject responseJson=new JSONObject(response);
                    int status=responseJson.getInt("status");
                    if (status==1){
                        data=responseJson.getJSONArray("data");
                        viewPager.setOffscreenPageLimit(data.length());//setOffscreenPageLimit  这个是缓存页面数量，默认缓存 1 页，也就是当前页的下一页
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for(int i=0;i<data.length();i++){
                                    System.out.println("ddddddddddddddddd:"+i);
                                    BookFragment bookFragment=new BookFragment();
                                    fragments.add(bookFragment);
                                    tabLayout.addTab(tabLayout.newTab());
                                }
                                tabLayout.setupWithViewPager(viewPager,false);
                                FmPagerAdapter pagerAdapter = new FmPagerAdapter(fragments,getSupportFragmentManager());
                                viewPager.setAdapter(pagerAdapter);
                                for(int i=0;i<data.length();i++){

                                    try {
                                        JSONObject book=data.getJSONObject(i);
                                        tabLayout.getTabAt(i).setText(book.getString("name"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                tabLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        IndicatorLineUtil.setIndicator(tabLayout, 0, 0);
                                    }
                                });

                                initFragmentData(data);


                            }
                        });

                    }else {
                        String info=responseJson.getString("info");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BooksActivity.this,info,Toast.LENGTH_SHORT).show();
                            }
                        });
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

    /**
     * 生成每个fragment的数据
     * @param data
     */
    private void initFragmentData(JSONArray data){
        for (int position=0;position<data.length();position++){
            try {
                BookFragment bookFragment=(BookFragment)fragments.get(position);
                JSONObject dataValue=data.getJSONObject(position);
                String id=dataValue.getString("id");
                bookFragment.refreshData(id);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
