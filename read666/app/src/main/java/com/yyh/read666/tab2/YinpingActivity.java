package com.yyh.read666.tab2;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonArray;
import com.yyh.read666.R;
import com.yyh.read666.details.DetailControlActivity;
import com.yyh.read666.details.GuankanzhongActivity;
import com.yyh.read666.details.GuankanzhongFragment;
import com.yyh.read666.music.MediaUtil;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.HttpInterface;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.tab4.RiJingjinFragment;
import com.yyh.read666.utils.DateUtil;
import com.yyh.read666.utils.SharedPreferencesUtil;
import com.yyh.read666.view.IndicatorLineUtil;
import com.yyh.read666.vo.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;

public class YinpingActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] titles = new String[]{"音频（234）", "简介"};

    YinpingFragment yinpingFragment;
    WebFragment webFragment;

    private TextView statusTitleTv;
    private Button backBtn;
    private ImageView userImg;
    private TextView nameTv, priceTv, gengxinTv, zhujiangrenTv;
    private Button dingyueBtn;





    JSONArray album ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_yinping2);
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


        yinpingFragment = new YinpingFragment();
        webFragment = new WebFragment();
        fragments.add(yinpingFragment);
        tabLayout.addTab(tabLayout.newTab());
        fragments.add(webFragment);
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
                IndicatorLineUtil.setIndicator(tabLayout, 40, 40);
            }
        });
//        initDuoshaojiLay();
//        initQbbfLay();
    }




    private void initData() {


            String id=getIntent().getStringExtra("id");


            HttpInterface httpInterface = new HttpImplement();
            httpInterface.learn_info(SharedPreferencesUtil.getToken(this), id, new OkHttpUtils.MyNetCall() {
                @Override
                public void success(Call call, String response) throws IOException {
                    try {
                        JSONObject responseJson = new JSONObject(response);
                        int status = responseJson.getInt("status");
                        if (status == 1) {
                            JSONObject data = responseJson.getJSONObject("data");

                            String author = data.getString("author");

                            String like_num=data.getString("like_num");
                            int is_album = data.getInt("is_album");
                            int is_collect = data.getInt("is_collect");
                            String title = data.getString("title");
                            String thumb = data.getString("thumb");
                            String price = data.getString("price");
                            String vipprice = data.getString("vipprice");
                            int update_num = data.getInt("update_num");
                            int update_time = data.getInt("update_time");



                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    statusTitleTv.setText(title);
                                    nameTv.setText(title);
                                    gengxinTv.setText("更新：" + DateUtil.timeStamp2Date(update_time + "", "MM-dd hh:ss") + " /第" + update_num + "集");

                                    Glide.with(YinpingActivity.this).load(thumb).into(userImg);

                                    int priceInt = (int) Float.parseFloat(price);
                                    int vippriceInt = (int) Float.parseFloat(vipprice);
                                    if (priceInt == 0) {
                                        priceTv.setText("价格:免费");
                                    } else {
                                        priceTv.setText("价格:" + price + "元");
                                    }
                                    if (vippriceInt == 0) {
                                        priceTv.setText(priceTv.getText().toString()+" VIP:免费");

                                    } else {
                                        priceTv.setText(priceTv.getText().toString()+" VIP:" + vipprice + "元");

                                    }


                                    zhujiangrenTv.setText("主讲：" + author);
                                    dingyueBtn.setText(is_collect == 1 ? "已订阅" : "+订阅");


                                if (is_album==1){
                                    try {
                                         album = data.getJSONArray("album");
                                        tabLayout.getTabAt(0).setText("音频（"+album.length()+")");
                                        yinpingFragment.refreshListView(album);
                                        yinpingFragment.data=data;

                                        JSONObject albumValue=album.getJSONObject(0);
                                        String id=albumValue.getString("id");

                                        initLearn_info(id);
                                        yinpingFragment.initDuoshaojiLay(album);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

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



    /**
     * 初始化音频内容
     */
    private void initLearn_info(String id){
        HttpInterface httpInterface=new HttpImplement();
        httpInterface.learn_info(SharedPreferencesUtil.getToken(this), id,"audio_info", new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                        JSONObject data = responseJson.getJSONObject("data");
                        String title = data.getString("title");
                        String id = data.getString("id");
                        String learn_id = data.getString("learn_id");
                        String comment_num = data.getString("comment_num");
                        String src = data.getString("src");
                        String content = data.getString("content");

                        int price=data.getInt("price");




                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                webFragment.refreshData(content);
//                                yinpingFragment.initMedia(src);
                            }
                        });


                    }else {
                        String info=responseJson.getString("info");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(YinpingActivity.this,info,Toast.LENGTH_SHORT).show();
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
    protected void onPause() {
        super.onPause();
        yinpingFragment.UIhandle.removeMessages(100);
    }


}
