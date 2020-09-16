package com.yyh.read666;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yyh.read666.configs.Configs;
import com.yyh.read666.details.DetailControlActivity;
import com.yyh.read666.login.LoginIndexActivity;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.okhttp.UrlConstant;
import com.yyh.read666.tab2.YinpingDetailActivity;
import com.yyh.read666.tab5.SettingActivity;
import com.yyh.read666.utils.DateUtil;
import com.yyh.read666.utils.SharedPreferencesUtil;
import com.yyh.read666.vo.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView[] mTabs;

    private RelativeLayout[] mTabLay;
    public LinearLayout main_bottomLay;

    private Fragment1 fragment1;
//    private Fragment2 fragment2;
    private Fragment3 fragment3;

    //    private Fragment4 fragment4;
    private WebFragment rijingjinFragment;
    private WebFragment shangchengFragment;

    private Fragment5 fragment5;

    private int index;
    private int currentTabIndex;
    private Fragment[] fragments;

    private FragmentBackListener backListener;
    private boolean isInterception = false;

    private ImageView vipGuanggaoImg,closeGuanggaoImg;
    private RelativeLayout vipGuanggaoLay;


    public FragmentBackListener getBackListener() {
        return backListener;
    }

    /**
     * 设置Fragment中返回键监听
     * @param backListener
     */
    public void setBackListener(FragmentBackListener backListener) {
        this.backListener = backListener;
    }

    public boolean isInterception() {
        return isInterception;
    }

    /**
     * 区别Activity和Fragment返回键的监听事件
     * 可以自主在Fragment中设置监听事件
     * @param isInterception
     */
    public void setInterception(boolean isInterception) {
        this.isInterception = isInterception;
    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
////        super.onSaveInstanceState(outState);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        vipGuanggaoLay=findViewById(R.id.vipGuanggaoLay);
        vipGuanggaoImg=findViewById(R.id.vipGuanggaoImg);
        closeGuanggaoImg=findViewById(R.id.closeGuanggaoImg);

        main_bottomLay=findViewById(R.id.main_bottom);
        mTabs = new ImageView[5];
        mTabs[0] = (ImageView) findViewById(R.id.img_1);
        mTabs[1] = (ImageView) findViewById(R.id.img_2);
        mTabs[2] = (ImageView) findViewById(R.id.img_3);
        mTabs[3] = (ImageView) findViewById(R.id.img_4);
        mTabs[4] = (ImageView) findViewById(R.id.img_5);

        mTabLay = new RelativeLayout[5];
        mTabLay[0] = (RelativeLayout) findViewById(R.id.btn_1);
        mTabLay[1] = (RelativeLayout) findViewById(R.id.btn_2);
        mTabLay[2] = (RelativeLayout) findViewById(R.id.btn_3);
        mTabLay[3] = (RelativeLayout) findViewById(R.id.btn_4);
        mTabLay[4] = (RelativeLayout) findViewById(R.id.btn_5);

        mTabLay[0].setOnClickListener(this);
        mTabLay[1].setOnClickListener(this);
        mTabLay[2].setOnClickListener(this);
        mTabLay[3].setOnClickListener(this);
        mTabLay[4].setOnClickListener(this);

        fragment1 = new Fragment1();
        shangchengFragment = new WebFragment();
        fragment3 = new Fragment3();
        rijingjinFragment = new WebFragment();
        rijingjinFragment.url = UrlConstant.RIJINGJIN_1_URL;
        shangchengFragment.url = UrlConstant.SHANGCHENG;

        fragment5 = new Fragment5();

        // set first tab as selected
        mTabs[0].setSelected(true);

        fragments = new Fragment[]{fragment1, rijingjinFragment, fragment3, shangchengFragment, fragment5};
        // add and show first fragment

//        if (Configs.isFirst){
            Configs.isFirst=false;
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment1).add(R.id.fragment_container, rijingjinFragment)
                    .add(R.id.fragment_container, fragment3)
                    .add(R.id.fragment_container, shangchengFragment).add(R.id.fragment_container, fragment5).hide(rijingjinFragment).hide(fragment3).hide(shangchengFragment).hide(fragment5).show(fragment1)
                    .commit();
//        }
        closeGuanggaoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vipGuanggaoLay.setVisibility(View.GONE);
            }
        });
        vipGuanggaoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, WebActivity.class);
                intent.putExtra("url", UrlConstant.VIP_GUANGGAO);
                startActivity(intent);
            }
        });

       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               onClick(mTabs[0]);
               if (isTodayFirstLogin()){
                   User user= SharedPreferencesUtil.getLoginUser(MainActivity.this);
                   if (user!=null){
                       int level=Integer.parseInt(user.getLevel()) ;
                       String deadline=user.getDeadline();
                       System.out.println("level:"+level);
                       System.out.println("deadline:"+deadline);
                       if ((deadline!=null&&deadline.equals("0"))&&(level==0)){
                           vipGuanggaoLay.setVisibility(View.VISIBLE);
                       }
                   }else {
                       vipGuanggaoLay.setVisibility(View.GONE);
                   }



               }else {
                   vipGuanggaoLay.setVisibility(View.GONE);
               }
           }
       },1000);

       //chekctoken
        HttpImplement httpImplement=new HttpImplement();
        String token= SharedPreferencesUtil.getToken(MainActivity.this);
        if (token!=null&&!token.equals("")){
            httpImplement.check_token(token,token, new OkHttpUtils.MyNetCall() {
                @Override
                public void success(Call call, String response) throws IOException {
                    try {
                        JSONObject responseJson = new JSONObject(response);
                        int status = responseJson.getInt("status");
                        String info=responseJson.getString("info");
                        if (status != 1) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferencesUtil.clearLoginUser(MainActivity.this);
                                    Intent intent=new Intent(MainActivity.this, LoginIndexActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    }catch (JSONException e){

                    }
                }

                @Override
                public void failed(Call call, IOException e) {

                }
            });

        }



    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences(Configs.SHARE_NAME, Activity.MODE_PRIVATE);
        String thumb = sp.getString("thumb", null);
        if (thumb != null) {
            Glide.with(this).load(thumb).into((ImageView) findViewById(R.id.headImg));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                index = 0;
                tabClickEvent();
               setInterception(false);
                break;
            case R.id.btn_2:
                index = 1;
                tabClickEvent();
                setInterception(false);
                break;
            case R.id.btn_3:
//                index = 2;
////                tabClickEvent();
                SharedPreferences sp = getSharedPreferences(Configs.SHARE_NAME, Activity.MODE_PRIVATE);
                int type = sp.getInt("type", 0);
                if (type == 0) {
                    String id = sp.getString("id", "311");
                    //视频
                    Intent intent = new Intent(MainActivity.this, DetailControlActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("isVedio",sp.getInt("isVedio",0));
                    intent.putExtra("isFromTab3",true);//如果是这里点击的，就需要记忆音频播放
                    startActivity(intent);
                } else {
                    //音频
                    int position = sp.getInt("position", 0);
                    String data = sp.getString("data", "0");
                    String album=sp.getString("album","");
                    Intent intent = new Intent(MainActivity.this, YinpingDetailActivity.class);
                    intent.putExtra("position", position);//当前音频位置
                    intent.putExtra("data", data);
                    intent.putExtra("album",album);
                    intent.putExtra("mustJiyi",true);//是否需要记忆播放，只在这里进去的记录记忆播放
                    startActivity(intent);
                }
                setInterception(false);
                break;
            case R.id.btn_4:
                index = 3;
                tabClickEvent();
                rijingjinFragment.uploadUrl();
                setInterception(true);
                break;
            case R.id.btn_5:
                index = 4;
                tabClickEvent();
                setInterception(false);
                break;

        }
    }

    public void tabClickEvent() {
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTabIndex].setSelected(false);
        // set current tab as selected.
        mTabs[index].setSelected(true);
        currentTabIndex = index;


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if(isInterception){
                //处理Fragment中的返回键监听事件
                if (backListener != null) {
                    backListener.onBackForward();
                    return false;
                }
            }else {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 判断是否是当日第一次登陆
     */
    private boolean isTodayFirstLogin() {
        //取
        SharedPreferences preferences = getSharedPreferences("LastLoginTime", MODE_PRIVATE);
        String lastTime = preferences.getString("LoginTime", "2017-04-08");
        // Toast.makeText(MainActivity.this, "value="+date, Toast.LENGTH_SHORT).show();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        String todayTime = df.format(new Date());// 获取当前的日期

        SharedPreferences.Editor editor = getSharedPreferences("LastLoginTime", MODE_PRIVATE).edit();
        editor.putString("LoginTime", todayTime);
        editor.apply();

        if (lastTime.equals(todayTime)) { //如果两个时间段相等
//            Toast.makeText(this, "不是当日首次登陆", Toast.LENGTH_SHORT).show();
           return  false;
        } else {
//            Toast.makeText(this, "当日首次登陆送积分", Toast.LENGTH_SHORT).show();
          return  true;
        }
    }

}
