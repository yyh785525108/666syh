package com.yyh.read666;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.yyh.read666.details.DetailControlActivity;
import com.yyh.read666.details.ZuoyeActivity;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.HttpInterface;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.okhttp.UrlConstant;
import com.yyh.read666.tab1.BooksActivity;
import com.yyh.read666.tab1.CiTiaoActivity;
import com.yyh.read666.tab1.SousuoActivity;
import com.yyh.read666.tab1.VideoActivity;
import com.yyh.read666.tab1.XianshiMianfeiActivity;
import com.yyh.read666.tab2.MyFragmentActivity;
import com.yyh.read666.tab2.YinpingActivity;
import com.yyh.read666.tab2.YinpingDetailActivity;
import com.yyh.read666.tab2.YinpingFragment;
import com.yyh.read666.utils.DateUtil;
import com.yyh.read666.utils.DensityUtil;
import com.yyh.read666.utils.SharedPreferencesUtil;
import com.yyh.read666.vo.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;

public class Fragment1 extends Fragment implements View.OnClickListener {
    private Banner banner;
    private HttpInterface httpInterface;
    private RelativeLayout week_updateLay,mianfeiLay,jinqiLay,xinkeLay,shuyouTuijianLay;
    private LinearLayout videoLay, allLay,zhiboLay;
    private ImageView headImg;
    private LinearLayout sousuoLay;
    private RelativeLayout vipkaitongLay;
    private ImageView closeImg;

    private LinearLayout indexAllLay;

    private   JSONArray free;



    Handler handler = new Handler();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        initView(view);
        initData(view);

        return view;
    }

    private void initView(View view) {
        closeImg=view.findViewById(R.id.closeImg);
        vipkaitongLay=view.findViewById(R.id.vipkaitongLay);
        sousuoLay=view.findViewById(R.id.sousuoLay);
        headImg=view.findViewById(R.id.headImg);
        User user=SharedPreferencesUtil.getLoginUser(getActivity());
        if (user!=null){
            Glide.with(getActivity()).load(user.getAvatar()).into(headImg);
            try {
                int level=Integer.parseInt(user.getLevel()) ;
                if (level==0){
                    vipkaitongLay.setVisibility(View.VISIBLE);
                }else {
                    vipkaitongLay.setVisibility(View.GONE);
                }
            }catch (NumberFormatException e){
                headImg.setImageResource(R.drawable.ic_launcher);

            }

        }else {
            headImg.setImageResource(R.drawable.ic_launcher);
        }



        videoLay = view.findViewById(R.id.videoLay);
        allLay = view.findViewById(R.id.allLay);
        zhiboLay=view.findViewById(R.id.zhiboLay);
        indexAllLay=view.findViewById(R.id.indexAllLay);

        week_updateLay = view.findViewById(R.id.week_updateLay);
        mianfeiLay = view.findViewById(R.id.mianfeiLay);
        jinqiLay = view.findViewById(R.id.jinqiLay);
        xinkeLay = view.findViewById(R.id.xinkeLay);
        shuyouTuijianLay = view.findViewById(R.id.shuyouTuijianLay);

        mianfeiLay.setOnClickListener(this);
        jinqiLay.setOnClickListener(this);
        xinkeLay.setOnClickListener(this);
        shuyouTuijianLay.setOnClickListener(this);

        week_updateLay.setOnClickListener(this);
        videoLay.setOnClickListener(this);
        allLay.setOnClickListener(this);
        sousuoLay.setOnClickListener(this);
        zhiboLay.setOnClickListener(this);
        vipkaitongLay.setOnClickListener(this);
        closeImg.setOnClickListener(this);

    }

    private void initData(View view) {
        httpInterface = new HttpImplement();
        learn_rec_audio(view);
        rec_book(view);
        new_audio(view);
        home_book(view);//500
        comment_index(view);//500
        guanggao(view);
        cate_index(view);
        bottomGuanggao(view);
        jinriDaren(view);
        reShou(view);
    }


    /**
     * 今日热搜
     */
    private void reShou(View view) {
        RelativeLayout jinriresouLay=view.findViewById(R.id.jinriresouLay);
        jinriresouLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url",UrlConstant.BOOK_LIST);
                startActivity(intent);


            }
        });


        LinearLayout resouLay1 = view.findViewById(R.id.resouLay1);
        TextView resouTv1 = view.findViewById(R.id.resouTv1);

        LinearLayout resouLay2 = view.findViewById(R.id.resouLay2);
        TextView resouTv2 = view.findViewById(R.id.resouTv2);

        LinearLayout resouLay3 = view.findViewById(R.id.resouLay3);
        TextView resouTv3 = view.findViewById(R.id.resouTv3);

        LinearLayout resouLay4 = view.findViewById(R.id.resouLay4);
        TextView resouTv4 = view.findViewById(R.id.resouTv4);

        LinearLayout resouLay5 = view.findViewById(R.id.resouLay5);
        TextView resouTv5 = view.findViewById(R.id.resouTv5);

        LinearLayout resouLay6 = view.findViewById(R.id.resouLay6);
        TextView resouTv6 = view.findViewById(R.id.resouTv6);




        httpInterface.learn_search(SharedPreferencesUtil.getToken(getActivity()), "hot_search", new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
            Activity activity=getActivity();
            if (activity!=null){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            int status = responseJson.getInt("status");
                            if (status == 1) {
                                JSONArray data = responseJson.getJSONArray("data");
                                if (data.length()==0){
                                    resouLay1.setVisibility(View.GONE);
                                    resouLay2.setVisibility(View.GONE);
                                    resouLay3.setVisibility(View.GONE);
                                    resouLay4.setVisibility(View.GONE);
                                    resouLay5.setVisibility(View.GONE);
                                    resouLay6.setVisibility(View.GONE);
                                }else     if (data.length()==1){
                                    resouLay2.setVisibility(View.GONE);
                                    resouLay3.setVisibility(View.GONE);
                                    resouLay4.setVisibility(View.GONE);
                                    resouLay5.setVisibility(View.GONE);
                                    resouLay6.setVisibility(View.GONE);
                                }else     if (data.length()==2){
                                    resouLay3.setVisibility(View.GONE);
                                    resouLay4.setVisibility(View.GONE);
                                    resouLay5.setVisibility(View.GONE);
                                    resouLay6.setVisibility(View.GONE);
                                }else     if (data.length()==3){
                                    resouLay4.setVisibility(View.GONE);
                                    resouLay5.setVisibility(View.GONE);
                                    resouLay6.setVisibility(View.GONE);
                                }else     if (data.length()==4){

                                    resouLay5.setVisibility(View.GONE);
                                    resouLay6.setVisibility(View.GONE);
                                }else    if (data.length()==5){

                                    resouLay6.setVisibility(View.GONE);
                                }
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataValue = data.getJSONObject(i);
                                    String id = dataValue.getString("id");
                                    String title = dataValue.getString("title");
                                    int finalI = i;



                                    if (finalI == 0) {
                                        resouTv1.setText(title);
                                        resouLay1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                                intent.putExtra("id", id);
                                                startActivity(intent);
                                            }
                                        });
                                    }else   if (finalI == 1) {
                                        resouTv2.setText(title);
                                        resouLay2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                                intent.putExtra("id", id);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                    else   if (finalI == 2) {
                                        resouTv3.setText(title);
                                        resouLay3.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                                intent.putExtra("id", id);
                                                startActivity(intent);
                                            }
                                        });
                                    }else   if (finalI == 3) {
                                        resouTv4.setText(title);
                                        resouLay4.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                                intent.putExtra("id", id);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                    else   if (finalI == 4) {
                                        resouTv5.setText(title);
                                        resouLay5.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                                intent.putExtra("id", id);
                                                startActivity(intent);
                                            }
                                        });
                                    }else   if (finalI == 5) {
                                        resouTv6.setText(title);
                                        resouLay6.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                                intent.putExtra("id", id);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }



            }

            @Override
            public void failed(Call call, IOException e) {

            }
        });

    }

    /**
     * 首页词条列表
     * @param view
     */
    private void jinriDaren(View view){
        RelativeLayout jinriDarenLay=view.findViewById(R.id.jinriDarenLay);
        jinriDarenLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity=(MainActivity)getActivity();
                activity.onClick(activity.findViewById(R.id.btn_4));
            }
        });
        GridView jinriDarenGird=view.findViewById(R.id.jinriDarenGird);
//         item宽度
        int itemWidth = DensityUtil.dip2px(getActivity(), 100);
        // item之间的间隔
        int itemPaddingH = DensityUtil.dip2px(getActivity(), 15);
        int size = 5;
        // 计算GridView宽度
        int gridviewWidth =size*(itemWidth+itemPaddingH);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        jinriDarenGird.setLayoutParams(params);
        jinriDarenGird.setColumnWidth(itemWidth);
        jinriDarenGird.setHorizontalSpacing(itemPaddingH);
        jinriDarenGird.setStretchMode(GridView.NO_STRETCH);
        jinriDarenGird.setNumColumns(size);
        jinriDarenGird.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject value=(JSONObject)parent.getItemAtPosition(position);
                try {
                    String uid=value.getString("uid");
                    Intent intent=new Intent(getActivity(),WebActivity.class);
                    intent.putExtra("url",UrlConstant.ZHUYE_URL+"?uid="+uid);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        httpInterface.rjj_member_top(SharedPreferencesUtil.getToken(getActivity()), new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson=new JSONObject(response);
                    int status=responseJson.getInt("status");
                    if (status==1){
                        JSONArray data=responseJson.getJSONArray("data");
                        Activity activity=getActivity();
                        if (activity!=null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    DarenAdapter adapter=new DarenAdapter(activity,data);
                                    jinriDarenGird.setAdapter(adapter);
                                }
                            });

                        }

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


    /**
     * 首页词条列表
     * @param view
     */
    private void cate_index(View view){
        httpInterface.cate_index(new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson=new JSONObject(response);
                    int status=responseJson.getInt("status");
                    if (status==1){
                        JSONArray data=responseJson.getJSONArray("data");
                        Activity activity=getActivity();
                        if (activity!=null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                //动态添加该布局
                                    for (int i=0;i<data.length();i++){
                                        final  int position=i;
                                        View cateView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_index_cate_index, indexAllLay , false);
                                        LinearLayout cateBgLay=cateView.findViewById(R.id.cateBgLay);

                                        TextView nameTv=cateView.findViewById(R.id.nameTv);
                                        TextView enTv=cateView.findViewById(R.id.enTv);
                                        TextView name1Tv=cateView.findViewById(R.id.name1Tv);
                                        TextView name2Tv=cateView.findViewById(R.id.name2Tv);
                                        TextView name3Tv=cateView.findViewById(R.id.name3Tv);
                                        TextView name4Tv=cateView.findViewById(R.id.name4Tv);

                                        indexAllLay.addView(cateView,2+i);
                                        if (i%2==0){
                                            cateBgLay.setBackgroundResource(R.drawable.shape_corner_cate1);
                                        }else {
                                            cateBgLay.setBackgroundResource(R.drawable.shape_corner_cate2);

                                        }
                                        try {
                                            JSONObject dataValue = data.getJSONObject(i);
                                            String name=dataValue.getString("name");
                                            String en=dataValue.getString("en");
                                            String id=dataValue.getString("id");
                                            nameTv.setText(name);
                                            enTv.setText(en);
                                            JSONArray list=dataValue.getJSONArray("list");
                                            for (int j=0;j<list.length();j++){
                                                JSONObject listValue=list.getJSONObject(j);
                                                String tName=listValue.getString("name");
                                                String letter=listValue.getString("letter");
                                                String tId=listValue.getString("id");

                                                if (j==0){
                                                    String str=String.format(letter+"<font color=\"#999999\">%s", "("+tName+")");
                                                    name1Tv.setText(Html.fromHtml(str));
                                                    name1Tv.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent=new Intent(getActivity(), CiTiaoActivity.class);
                                                            intent.putExtra("type", position);
                                                            intent.putExtra("id", tId);

                                                            startActivity(intent);
                                                        }
                                                    });
                                                }else if (j==1){
                                                    String str=String.format(letter+"<font color=\"#999999\">%s", "("+tName+")");                                                name2Tv.setText(Html.fromHtml(str));
                                                    name2Tv.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent=new Intent(getActivity(), CiTiaoActivity.class);
                                                            intent.putExtra("type", position);
                                                            intent.putExtra("id", tId);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }else if (j==2){
                                                    String str=String.format(letter+"<font color=\"#999999\">%s", "("+tName+")");                                                name3Tv.setText(Html.fromHtml(str));
                                                    name3Tv.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent=new Intent(getActivity(), CiTiaoActivity.class);
                                                            intent.putExtra("type", position);
                                                            intent.putExtra("id", tId);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }else if (j==3){
                                                    String str=String.format(letter+"<font color=\"#999999\">%s", "("+tName+")");
                                                    name4Tv.setText(Html.fromHtml(str));

                                                    name4Tv.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent=new Intent(getActivity(), CiTiaoActivity.class);
                                                            intent.putExtra("type", position);
                                                            intent.putExtra("id", tId);
                                                            startActivity(intent);
                                                        }
                                                    });
                                                }
                                                int finalI = i;





                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }
                            });

                        }


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

    private void bottomGuanggao(View view){
        ImageView guanggao1=view.findViewById(R.id.guanggao1);
        ImageView guanggao2=view.findViewById(R.id.guanggao2);
        ImageView guanggao3=view.findViewById(R.id.guanggao3);
        ImageView guanggao4=view.findViewById(R.id.guanggao4);

        httpInterface.ads_items_get(SharedPreferencesUtil.getToken(getActivity()), "13","4",new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) {
                JSONObject responseJson = null;
                try {
                    responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                        JSONArray data = responseJson.getJSONArray("data");
                        Activity activity=getActivity();
                        if (activity!=null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i=0;i<data.length();i++){
                                        try {
                                            JSONObject value=data.getJSONObject(i);
                                            String tid=value.getString("id");
                                            String name=value.getString("name");
                                            String img=value.getString("img");
                                            String link=value.getString("link");
                                            JSONObject apppage=value.getJSONObject("apppage");
                                            String mode=apppage.getString("mode");

                                            if (i==0){
                                                Glide.with(getActivity()).load(img).into(guanggao1);
                                                guanggao1.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        if (mode.equals("video")){
                                                            JSONObject query= null;
                                                            try {
                                                                query = apppage.getJSONObject("query");
                                                                String id=query.getString("id");
                                                                Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                                                intent.putExtra("id", id);
                                                                startActivity(intent);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }else  if (mode.equals("book")){
                                                            JSONObject query= null;
                                                            try {
                                                                query = apppage.getJSONObject("query");
                                                                String id=query.getString("id");
                                                                Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                                                intent.putExtra("id", id);
                                                                startActivity(intent);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }else if (mode != null && mode.equals("audio")) {
                                                            try {
                                                                JSONObject query=apppage.getJSONObject("query");
                                                                String id=query.getString("id");
                                                                String vid = query.getString("vid");

                                                                HttpInterface httpInterface = new HttpImplement();
                                                                httpInterface.learn_info(SharedPreferencesUtil.getToken(getActivity()), id, new OkHttpUtils.MyNetCall() {
                                                                    @Override
                                                                    public void success(Call call, String response) throws IOException {
                                                                        try {
                                                                            JSONObject responseJson = new JSONObject(response);
                                                                            int position = 0;
                                                                            int status = responseJson.getInt("status");
                                                                            if (status == 1) {
                                                                                JSONObject data = responseJson.getJSONObject("data");

                                                                                JSONArray album = data.getJSONArray("album");
                                                                                for (int i = 0; i < album.length(); i++) {
                                                                                    String id = (album.getJSONObject(i)).getInt("id") + "";
                                                                                    if (id.equals(vid)) {
                                                                                        position = i;
                                                                                        break;
                                                                                    }

                                                                                }

                                                                                System.out.println("position:" + position);
                                                                                Intent intent = new Intent(getActivity(), YinpingDetailActivity.class);
                                                                                intent.putExtra("position", position);//当前音频位置
                                                                                intent.putExtra("data", data.toString());
                                                                                intent.putExtra("album", album.toString());
                                                                                startActivity(intent);

                                                                            }
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void failed(Call call, IOException e) {

                                                                    }
                                                                });


                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }else if (mode != null && mode.equals("album")){
                                                            JSONObject query= null;
                                                            try {
                                                                query = apppage.getJSONObject("query");
                                                                String id=query.getString("id");
                                                                Intent intent=new Intent(getActivity(),YinpingActivity.class);
                                                                intent.putExtra("id",id);
                                                                startActivity(intent);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }else {
                                                            Intent intent=new Intent(getActivity(),WebActivity.class);
                                                            intent.putExtra("url",link);
                                                            startActivity(intent);
                                                        }

                                                    }
                                                });
                                            }else   if (i==1){
                                                Glide.with(getActivity()).load(img).into(guanggao2);
                                                guanggao2.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        if (mode.equals("video")){
                                                            JSONObject query= null;
                                                            try {
                                                                query = apppage.getJSONObject("query");
                                                                String id=query.getString("id");
                                                                Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                                                intent.putExtra("id", id);
                                                                startActivity(intent);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }else  if (mode.equals("book")){
                                                            JSONObject query= null;
                                                            try {
                                                                query = apppage.getJSONObject("query");
                                                                String id=query.getString("id");
                                                                Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                                                intent.putExtra("id", id);
                                                                startActivity(intent);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }else if (mode != null && mode.equals("audio")) {
                                                            try {
                                                                JSONObject query=apppage.getJSONObject("query");
                                                                String id=query.getString("id");
                                                                String vid = query.getString("vid");

                                                                HttpInterface httpInterface = new HttpImplement();
                                                                httpInterface.learn_info(SharedPreferencesUtil.getToken(getActivity()), id, new OkHttpUtils.MyNetCall() {
                                                                    @Override
                                                                    public void success(Call call, String response) throws IOException {
                                                                        try {
                                                                            JSONObject responseJson = new JSONObject(response);
                                                                            int position = 0;
                                                                            int status = responseJson.getInt("status");
                                                                            if (status == 1) {
                                                                                JSONObject data = responseJson.getJSONObject("data");

                                                                                JSONArray album = data.getJSONArray("album");
                                                                                for (int i = 0; i < album.length(); i++) {
                                                                                    String id = (album.getJSONObject(i)).getInt("id") + "";
                                                                                    if (id.equals(vid)) {
                                                                                        position = i;
                                                                                        break;
                                                                                    }

                                                                                }

                                                                                System.out.println("position:" + position);
                                                                                Intent intent = new Intent(getActivity(), YinpingDetailActivity.class);
                                                                                intent.putExtra("position", position);//当前音频位置
                                                                                intent.putExtra("data", data.toString());
                                                                                intent.putExtra("album", album.toString());
                                                                                startActivity(intent);

                                                                            }
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void failed(Call call, IOException e) {

                                                                    }
                                                                });


                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }else if (mode != null && mode.equals("album")){
                                                            JSONObject query= null;
                                                            try {
                                                                query = apppage.getJSONObject("query");
                                                                String id=query.getString("id");
                                                                Intent intent=new Intent(getActivity(),YinpingActivity.class);
                                                                intent.putExtra("id",id);
                                                                startActivity(intent);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }else {
                                                            Intent intent=new Intent(getActivity(),WebActivity.class);
                                                            intent.putExtra("url",link);
                                                            startActivity(intent);
                                                        }

                                                    }
                                                });
                                            }else   if (i==2){
                                                Glide.with(getActivity()).load(img).into(guanggao3);
                                                guanggao3.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        if (mode.equals("video")){
                                                            JSONObject query= null;
                                                            try {
                                                                query = apppage.getJSONObject("query");
                                                                String id=query.getString("id");
                                                                Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                                                intent.putExtra("id", id);
                                                                startActivity(intent);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }else  if (mode.equals("book")){
                                                            JSONObject query= null;
                                                            try {
                                                                query = apppage.getJSONObject("query");
                                                                String id=query.getString("id");
                                                                Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                                                intent.putExtra("id", id);
                                                                startActivity(intent);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }else if (mode != null && mode.equals("audio")) {
                                                            try {
                                                                JSONObject query=apppage.getJSONObject("query");
                                                                String id=query.getString("id");
                                                                String vid = query.getString("vid");

                                                                HttpInterface httpInterface = new HttpImplement();
                                                                httpInterface.learn_info(SharedPreferencesUtil.getToken(getActivity()), id, new OkHttpUtils.MyNetCall() {
                                                                    @Override
                                                                    public void success(Call call, String response) throws IOException {
                                                                        try {
                                                                            JSONObject responseJson = new JSONObject(response);
                                                                            int position = 0;
                                                                            int status = responseJson.getInt("status");
                                                                            if (status == 1) {
                                                                                JSONObject data = responseJson.getJSONObject("data");

                                                                                JSONArray album = data.getJSONArray("album");
                                                                                for (int i = 0; i < album.length(); i++) {
                                                                                    String id = (album.getJSONObject(i)).getInt("id") + "";
                                                                                    if (id.equals(vid)) {
                                                                                        position = i;
                                                                                        break;
                                                                                    }

                                                                                }

                                                                                System.out.println("position:" + position);
                                                                                Intent intent = new Intent(getActivity(), YinpingDetailActivity.class);
                                                                                intent.putExtra("position", position);//当前音频位置
                                                                                intent.putExtra("data", data.toString());
                                                                                intent.putExtra("album", album.toString());
                                                                                startActivity(intent);

                                                                            }
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void failed(Call call, IOException e) {

                                                                    }
                                                                });


                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }else if (mode != null && mode.equals("album")){
                                                            JSONObject query= null;
                                                            try {
                                                                query = apppage.getJSONObject("query");
                                                                String id=query.getString("id");
                                                                Intent intent=new Intent(getActivity(),YinpingActivity.class);
                                                                intent.putExtra("id",id);
                                                                startActivity(intent);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }else {
                                                            Intent intent=new Intent(getActivity(),WebActivity.class);
                                                            intent.putExtra("url",link);
                                                            startActivity(intent);
                                                        }



                                                    }
                                                });
                                            }else   if (i==3){
                                                Glide.with(getActivity()).load(img).into(guanggao4);
                                                guanggao4.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {


                                                        if (mode.equals("video")){
                                                            JSONObject query= null;
                                                            try {
                                                                query = apppage.getJSONObject("query");
                                                                String id=query.getString("id");
                                                                Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                                                intent.putExtra("id", id);
                                                                startActivity(intent);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }else  if (mode.equals("book")){
                                                            JSONObject query= null;
                                                            try {
                                                                query = apppage.getJSONObject("query");
                                                                String id=query.getString("id");
                                                                Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                                                intent.putExtra("id", id);
                                                                startActivity(intent);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }else if (mode != null && mode.equals("audio")) {
                                                            try {
                                                                JSONObject query=apppage.getJSONObject("query");
                                                                String id=query.getString("id");
                                                                String vid = query.getString("vid");

                                                                HttpInterface httpInterface = new HttpImplement();
                                                                httpInterface.learn_info(SharedPreferencesUtil.getToken(getActivity()), id, new OkHttpUtils.MyNetCall() {
                                                                    @Override
                                                                    public void success(Call call, String response) throws IOException {
                                                                        try {
                                                                            JSONObject responseJson = new JSONObject(response);
                                                                            int position = 0;
                                                                            int status = responseJson.getInt("status");
                                                                            if (status == 1) {
                                                                                JSONObject data = responseJson.getJSONObject("data");

                                                                                JSONArray album = data.getJSONArray("album");
                                                                                for (int i = 0; i < album.length(); i++) {
                                                                                    String id = (album.getJSONObject(i)).getInt("id") + "";
                                                                                    if (id.equals(vid)) {
                                                                                        position = i;
                                                                                        break;
                                                                                    }

                                                                                }

                                                                                System.out.println("position:" + position);
                                                                                Intent intent = new Intent(getActivity(), YinpingDetailActivity.class);
                                                                                intent.putExtra("position", position);//当前音频位置
                                                                                intent.putExtra("data", data.toString());
                                                                                intent.putExtra("album", album.toString());
                                                                                startActivity(intent);

                                                                            }
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void failed(Call call, IOException e) {

                                                                    }
                                                                });


                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }else if (mode != null && mode.equals("album")){
                                                            JSONObject query= null;
                                                            try {
                                                                query = apppage.getJSONObject("query");
                                                                String id=query.getString("id");
                                                                Intent intent=new Intent(getActivity(),YinpingActivity.class);
                                                                intent.putExtra("id",id);
                                                                startActivity(intent);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }else {
                                                            Intent intent=new Intent(getActivity(),WebActivity.class);
                                                            intent.putExtra("url",link);
                                                            startActivity(intent);
                                                        }



                                                    }
                                                });
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }
                            });

                        }



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


    /**
     * 广告列表
     * @param view
     */
    private void guanggao(View view){
        httpInterface.ads_items_get(SharedPreferencesUtil.getToken(getActivity()), "11","3",new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) {
                JSONObject responseJson = null;
                JSONArray guanggaoData;

                try {
                    responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                         guanggaoData = responseJson.getJSONArray("data");
                        ArrayList images= new ArrayList<>();


                        for (int i=0;i<guanggaoData.length();i++){
                            String img=guanggaoData.getJSONObject(i).getString("img");
                            images.add(img);
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                banner = (Banner) view.findViewById(R.id.banner);
                                //设置banner样式
                                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
                                banner.setDelayTime(60 * 3);
                                //设置banner动画效果
                                banner.setImageLoader(new ImageLoader() {
                                    @Override
                                    public void displayImage(Context context, Object path, ImageView imageView) {
                                        Glide.with(context).load(path).into(imageView);
                                    }
                                });

                                banner.setImages(images);

                                banner.setBannerAnimation(Transformer.Default);
                                //设置自动轮播，默认为true
                                banner.isAutoPlay(true);
                                //设置轮播时间
                                banner.setDelayTime(1000 * 60 * 60 * 24);
                                //设置指示器位置（当banner模式中有指示器时）
                                banner.setIndicatorGravity(BannerConfig.CENTER);
                                banner.setOnBannerListener(new OnBannerListener() {
                                    @Override
                                    public void OnBannerClick(int position) {
                                        try {
                                            JSONObject value = guanggaoData.getJSONObject(position);
                                            String tid = value.getString("id");
                                            String name = value.getString("name");
                                            String img = value.getString("img");
                                            String link = value.getString("link");
                                            JSONObject apppage = value.getJSONObject("apppage");
                                            String mode = apppage.getString("mode");
                                            if (mode.equals("video")){
                                                JSONObject query= null;
                                                try {
                                                    query = apppage.getJSONObject("query");
                                                    String id=query.getString("id");
                                                    Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                                    intent.putExtra("id", id);
                                                    startActivity(intent);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }else  if (mode.equals("book")){
                                                JSONObject query= null;
                                                try {
                                                    query = apppage.getJSONObject("query");
                                                    String id=query.getString("id");
                                                    Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                                    intent.putExtra("id", id);
                                                    startActivity(intent);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }else if (mode != null && mode.equals("audio")) {
                                                try {
                                                    JSONObject query=apppage.getJSONObject("query");
                                                    String id=query.getString("id");
                                                    String vid = query.getString("vid");

                                                    HttpInterface httpInterface = new HttpImplement();
                                                    httpInterface.learn_info(SharedPreferencesUtil.getToken(getActivity()), id, new OkHttpUtils.MyNetCall() {
                                                        @Override
                                                        public void success(Call call, String response) throws IOException {
                                                            try {
                                                                JSONObject responseJson = new JSONObject(response);
                                                                int position = 0;
                                                                int status = responseJson.getInt("status");
                                                                if (status == 1) {
                                                                    JSONObject data = responseJson.getJSONObject("data");

                                                                    JSONArray album = data.getJSONArray("album");
                                                                    for (int i = 0; i < album.length(); i++) {
                                                                        String id = (album.getJSONObject(i)).getInt("id") + "";
                                                                        if (id.equals(vid)) {
                                                                            position = i;
                                                                            break;
                                                                        }

                                                                    }

                                                                    System.out.println("position:" + position);
                                                                    Intent intent = new Intent(getActivity(), YinpingDetailActivity.class);
                                                                    intent.putExtra("position", position);//当前音频位置
                                                                    intent.putExtra("data", data.toString());
                                                                    intent.putExtra("album", album.toString());
                                                                    startActivity(intent);

                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        @Override
                                                        public void failed(Call call, IOException e) {

                                                        }
                                                    });


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }else if (mode != null && mode.equals("album")){
                                                JSONObject query= null;
                                                try {
                                                    query = apppage.getJSONObject("query");
                                                    String id=query.getString("id");
                                                    Intent intent=new Intent(getActivity(),YinpingActivity.class);
                                                    intent.putExtra("id",id);
                                                    startActivity(intent);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }else {
                                                Intent intent=new Intent(getActivity(),WebActivity.class);
                                                intent.putExtra("url",link);
                                                startActivity(intent);
                                            }

                                        }catch (JSONException e){

                                        }
                                    }
                                });

                                //banner设置方法全部调用完毕时最后调用
                                banner.start();
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
    /**
     *
     * @param view
     */
    private void home_book(View view) {


        httpInterface.learn_home_book(SharedPreferencesUtil.getToken(getActivity()), new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                        JSONObject data = responseJson.getJSONObject("data");
                        JSONObject week = data.getJSONObject("week");//本周更新
                        dealWeek(view, week);

                        free = data.getJSONArray("free");//限时免费
                        dealFree(view, free);

                        JSONArray news = data.getJSONArray("new");//近期新书
                        dealNew(view,news);

                        JSONArray top5 = data.getJSONArray("top5"); //本周热门top5
                        dealTop5(view,top5);

                        JSONObject hot = data.getJSONObject("hot");//书友推荐
                        dealHot(view, hot);

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

    /**
     * 处理本周更新数据
     *
     * @param week
     */
    private void dealWeek(View view, JSONObject week) {

        //本周更新
        ImageView benzhouImg = view.findViewById(R.id.benzhouImg);
        TextView benzhouNameTv = view.findViewById(R.id.benzhouNameTv);
        TextView benzhouContentTv = view.findViewById(R.id.benzhouContentTv);
        TextView benzhouTimeTv = view.findViewById(R.id.benzhouTimeTv);
        TextView benzhouCountTv = view.findViewById(R.id.benzhouCountTv);

        LinearLayout benzhouLay=view.findViewById(R.id.benzhouLay);


        try {
            String id=week.getString("id");
            String title = week.getString("title");
            String  thumb = week.getString("thumb");
            String description = week.getString("description");
            String view_num = week.getString("view_num");
            String add_time = week.getString("add_time");
            Activity activity=getActivity();
            if (activity!=null){

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(getActivity()).load(thumb).into(benzhouImg);
                        benzhouNameTv.setText(title);
                        benzhouContentTv.setText(description);
                        benzhouTimeTv.setText(DateUtil.timeStamp2Date(add_time, "yyyy.MM.dd") + "上架");
                        benzhouCountTv.setText(view_num);
                        benzhouLay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                intent.putExtra("id", id);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    /**
     * 处理书友推荐数据
     *
     * @param hot
     */
    private void dealHot(View view, JSONObject hot) {
        //本周更新
        ImageView shuyouImg = view.findViewById(R.id.shuyouImg);
        TextView  shuyouNameTv = view.findViewById(R.id.shuyouNameTv);
        TextView shuyouContentTv = view.findViewById(R.id.shuyouContentTv);
        TextView  shuyouTimeTv = view.findViewById(R.id.shuyouTimeTv);
        TextView shuyouCountTv = view.findViewById(R.id.shuyouCountTv);

        LinearLayout shuyouLay=view.findViewById(R.id.shuyouLay);
        try {
            String id = hot.getString("id");
            String title = hot.getString("title");
            String thumb = hot.getString("thumb");
            String description = hot.getString("description");
            String view_num = hot.getString("view_num");
            String add_time = hot.getString("add_time");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Glide.with(getActivity()).load(thumb).into(shuyouImg);
                    shuyouNameTv.setText(title);
                    shuyouContentTv.setText(description);
                    shuyouTimeTv.setText(DateUtil.timeStamp2Date(add_time, "yyyy.MM.dd") + "上架");
                    shuyouCountTv.setText(view_num);
                    shuyouLay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        }
                    });

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    /**
     * 处理限时免费数据
     *
     * @param view
     * @param free
     */
    private void dealFree(View view, JSONArray free) {
        //限时免费
        ImageView xianshiImg1 = view.findViewById(R.id.xianshiImg1);
        TextView xianshiNameTv1 = view.findViewById(R.id.xianshiNameTv1);
        TextView xianshiCountTv1 = view.findViewById(R.id.xianshiCountTv1);

        ImageView xianshiImg2 = view.findViewById(R.id.xianshiImg2);
        TextView xianshiNameTv2 = view.findViewById(R.id.xianshiNameTv2);
        TextView xianshiCountTv2 = view.findViewById(R.id.xianshiCountTv2);

        ImageView xianshiImg3 = view.findViewById(R.id.xianshiImg3);
        TextView xianshiNameTv3 = view.findViewById(R.id.xianshiNameTv3);
        TextView xianshiCountTv3 = view.findViewById(R.id.xianshiCountTv3);

        LinearLayout xianshiLay1 = view.findViewById(R.id.xianshiLay1);
        LinearLayout xianshiLay2 = view.findViewById(R.id.xianshiLay2);
        LinearLayout xianshiLay3 = view.findViewById(R.id.xianshiLay3);

        for (int i = 0; i < free.length(); i++) {
            try {
                JSONObject freeValue = free.getJSONObject(i);
                String title = freeValue.getString("title");
                String id = freeValue.getString("id");
                String thumb = freeValue.getString("thumb");
                String description = freeValue.getString("description");
                String view_num = freeValue.getString("view_num");
                String add_time = freeValue.getString("add_time");
                int finalI = i;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalI == 0) {
                            xianshiCountTv1.setText(view_num);
                            xianshiNameTv1.setText(title);
                            Glide.with(getActivity()).load(thumb).into(xianshiImg1);
                            xianshiLay1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                }
                            });
                        } else if (finalI == 1) {
                            xianshiCountTv2.setText(view_num);
                            xianshiNameTv2.setText(title);
                            Glide.with(getActivity()).load(thumb).into(xianshiImg2);
                            xianshiLay2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                }
                            });
                        } else if (finalI == 2) {
                            xianshiCountTv3.setText(view_num);
                            xianshiNameTv3.setText(title);
                            Glide.with(getActivity()).load(thumb).into(xianshiImg3);
                            xianshiLay3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
    }

    /**
     * 处理近期新书
     *
     * @param view
     * @param news
     */
    private void dealNew(View view, JSONArray news) {
        //近期新书
        ImageView jinqiImg1 = view.findViewById(R.id.jinqiImg1);
        TextView jinqiNameTv1 = view.findViewById(R.id.jinqiNameTv1);
        TextView jinqiContentTv1 = view.findViewById(R.id.jinqiContentTv1);
        TextView jinqiTimeTv1 = view.findViewById(R.id.jinqiTimeTv1);
        TextView jinqiCountTv1= view.findViewById(R.id.jinqiCountTv1);

        ImageView jinqiImg2 = view.findViewById(R.id.jinqiImg2);
        TextView jinqiNameTv2 = view.findViewById(R.id.jinqiNameTv2);
        TextView jinqiContentTv2= view.findViewById(R.id.jinqiContentTv2);
        TextView jinqiTimeTv2 = view.findViewById(R.id.jinqiTimeTv2);
        TextView jinqiCountTv2= view.findViewById(R.id.jinqiCountTv2);

        ImageView jinqiImg3 = view.findViewById(R.id.jinqiImg3);
        TextView jinqiNameTv3 = view.findViewById(R.id.jinqiNameTv3);
        TextView jinqiContentTv3 = view.findViewById(R.id.jinqiContentTv3);
        TextView jinqiTimeTv3= view.findViewById(R.id.jinqiTimeTv3);
        TextView jinqiCountTv3= view.findViewById(R.id.jinqiCountTv3);


        LinearLayout jinqiLay1 = view.findViewById(R.id.jinqiLay1);
        LinearLayout jinqiLay2 = view.findViewById(R.id.jinqiLay2);
        LinearLayout jinqiLay3 = view.findViewById(R.id.jinqiLay3);

        for (int i = 0; i < news.length(); i++) {
            try {
                JSONObject newValue = news.getJSONObject(i);
                String id = newValue.getString("id");
                String title = newValue.getString("title");
                String thumb = newValue.getString("thumb");
                String description = newValue.getString("description");
                String view_num = newValue.getString("view_num");
                String add_time = newValue.getString("add_time");
                int finalI = i;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalI == 0) {
                            jinqiNameTv1.setText(title);
                            jinqiContentTv1.setText(description);
                            jinqiTimeTv1.setText(DateUtil.timeStamp2Date(add_time, "yyyy.MM.dd") + "上架");
                            jinqiCountTv1.setText(view_num);
                            Glide.with(getActivity()).load(thumb).into(jinqiImg1);
                            jinqiLay1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                }
                            });
                        } else if (finalI == 1) {
                            jinqiNameTv2.setText(title);
                            jinqiContentTv2.setText(description);
                            jinqiTimeTv2.setText(DateUtil.timeStamp2Date(add_time, "yyyy.MM.dd") + "上架");
                            jinqiCountTv2.setText(view_num);
                            Glide.with(getActivity()).load(thumb).into(jinqiImg2);
                            jinqiLay2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                }
                            });
                        } else if (finalI == 2) {
                            jinqiNameTv3.setText(title);
                            jinqiContentTv3.setText(description);
                            jinqiTimeTv3.setText(DateUtil.timeStamp2Date(add_time, "yyyy.MM.dd") + "上架");
                            jinqiCountTv3.setText(view_num);
                            Glide.with(getActivity()).load(thumb).into(jinqiImg3);
                            jinqiLay3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
    }

    /**
     * 处理本周热门
     * @param view
     * @param top5
     */
    private void dealTop5(View view, JSONArray top5) {
        //近期新书
        ImageView remenImg0 = view.findViewById(R.id.remenImg0);
        TextView remenNameTv0 = view.findViewById(R.id.remenNameTv0);
        TextView remenContentTv0 = view.findViewById(R.id.remenContentTv0);
        TextView remenTimeTv0 = view.findViewById(R.id.remenTimeTv0);
        TextView remenCountTv0= view.findViewById(R.id.remenCountTv0);

        ImageView remenImg1 = view.findViewById(R.id.remenImg1);
        TextView remenNameTv1 = view.findViewById(R.id.remenNameTv1);

        ImageView remenImg2 = view.findViewById(R.id.remenImg2);
        TextView remenNameTv2 = view.findViewById(R.id.remenNameTv2);

        ImageView remenImg3 = view.findViewById(R.id.remenImg3);
        TextView remenNameTv3 = view.findViewById(R.id.remenNameTv3);

        ImageView remenImg4 = view.findViewById(R.id.remenImg4);
        TextView remenNameTv4 = view.findViewById(R.id.remenNameTv4);


        LinearLayout benzhouRemenLay0 = view.findViewById(R.id.benzhouRemenLay0);
        LinearLayout benzhouRemenLay1 = view.findViewById(R.id.benzhouRemenLay1);
        LinearLayout benzhouRemenLay2 = view.findViewById(R.id.benzhouRemenLay2);
        LinearLayout benzhouRemenLay3 = view.findViewById(R.id.benzhouRemenLay3);
        LinearLayout benzhouRemenLay4 = view.findViewById(R.id.benzhouRemenLay4);

        for (int i = 0; i < top5.length(); i++) {
            try {
                JSONObject top5Value = top5.getJSONObject(i);
                String id = top5Value.getString("id");
                String title = top5Value.getString("title");
                String thumb = top5Value.getString("thumb");
                String description = top5Value.getString("description");
                String view_num = top5Value.getString("view_num");
                String add_time = top5Value.getString("add_time");
                int finalI = i;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalI == 0) {
                            remenNameTv0.setText(title);
                            remenContentTv0.setText(description);
                            remenTimeTv0.setText(DateUtil.timeStamp2Date(add_time, "yyyy.MM.dd") + "上架");
                            remenCountTv0.setText(view_num);
                            Glide.with(getActivity()).load(thumb).into(remenImg0);
                            benzhouRemenLay0.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                }
                            });
                        } else if (finalI == 1) {
                            remenNameTv1.setText(title);
                            Glide.with(getActivity()).load(thumb).into(remenImg1);
                            benzhouRemenLay1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                }
                            });
                        } else if (finalI == 2) {
                            remenNameTv2.setText(title);
                            Glide.with(getActivity()).load(thumb).into(remenImg2);
                            benzhouRemenLay2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                }
                            });
                        }else if (finalI == 3) {
                            remenNameTv3.setText(title);
                            Glide.with(getActivity()).load(thumb).into(remenImg3);
                            benzhouRemenLay3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                }
                            });
                        }else if (finalI == 4) {
                            remenNameTv4.setText(title);
                            Glide.with(getActivity()).load(thumb).into(remenImg4);
                            benzhouRemenLay4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
    }

    /**
     * 今日热评
     * @param view
     */
    private void comment_index(View view) {
        RelativeLayout repingLay=view.findViewById(R.id.repingLay);

        ImageView repingImg=view.findViewById(R.id.repingImg);
        TextView repingContentTv=view.findViewById(R.id.repingContentTv);
        TextView repingTitleTv=view.findViewById(R.id.repingTitleTv);
        TextView repingNickNameTv=view.findViewById(R.id.repingNickNameTv);

        TextView dateTv=view.findViewById(R.id.dayTv);
        TextView yearTv=view.findViewById(R.id.yearTv);
        Date date=new Date();
        SimpleDateFormat dateFm = new SimpleDateFormat("dd");
        dateTv.setText(dateFm.format(date));
        dateFm = new SimpleDateFormat("yyyy");
        String year=dateFm.format(date)+" "+DateUtil.getWeekOfDate(date);
        yearTv.setText(year);

        final String[] repingId = {""};
        httpInterface.learn_comment_index(new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                        JSONObject data = responseJson.getJSONObject("data");
                        String content=data.getString("content");
                        String avatar=data.getString("avatar");
                        String nickname=data.getString("nickname");
                        String title=data.getString("title");
                        repingId[0] =data.getString("learn_id");
                        Activity activity=getActivity();
                        if (activity!=null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Glide.with(getActivity()).load(avatar).into(repingImg);
                                    repingContentTv.setText(content);
                                    repingNickNameTv.setText(nickname);
                                    repingTitleTv.setText("—来自 "+title);
                                }
                            });
                        }



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Call call, IOException e) {

            }
        });

        repingLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(), DetailControlActivity.class);
//                intent.putExtra("id", repingId[0]);
//                startActivity(intent);
                Intent intent=new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", UrlConstant.SHUPING_LIST_URL);
                startActivity(intent);
            }
        });

    }


    /**
     * 首页每日一听
     */
    private void learn_rec_audio(View view) {
        TextView meiriyitingTv = view.findViewById(R.id.meiriyitingTv);
        RelativeLayout meiriyitingLay=view.findViewById(R.id.meiriyitingLay);

        final String[] id = new String[1];
        final String[] learn_id = new String[1];


        httpInterface.learn_rec_audio(SharedPreferencesUtil.getToken(getActivity()), new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) {
                JSONObject responseJson = null;
                try {
                    responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                        JSONArray data = responseJson.getJSONArray("data");
                        Activity activity=getActivity();
                        if (activity!=null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    meiriyitingLay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            HttpInterface httpInterface = new HttpImplement();
                                            httpInterface.learn_info(SharedPreferencesUtil.getToken(getActivity()), learn_id[0], new OkHttpUtils.MyNetCall() {
                                                @Override
                                                public void success(Call call, String response) throws IOException {
                                                    try {
                                                        JSONObject responseJson = new JSONObject(response);
                                                        int status = responseJson.getInt("status");
                                                        if (status == 1) {
                                                            JSONObject data = responseJson.getJSONObject("data");

                                                            int is_album = data.getInt("is_album");
                                                            JSONArray album=data.getJSONArray("album");
                                                            int position=-1;
                                                            for (int i=0;i<album.length();i++){
                                                                JSONObject albumValue=album.getJSONObject(i);
                                                                String tid=albumValue.getString("id");
                                                                if (id[0].equals(tid)){
                                                                    position=i;
                                                                    break;
                                                                }
                                                            }
                                                            if (position!=-1){
                                                                int finalPosition = position;
                                                                getActivity().runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {

                                                                        Intent intent=new Intent(getActivity(), YinpingDetailActivity.class);
                                                                        intent.putExtra("position", finalPosition);//当前音频位置
                                                                        intent.putExtra("data", data.toString());
                                                                        intent.putExtra("album", album.toString());
                                                                        startActivity(intent);


                                                                    }
                                                                });
                                                            }



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
                                    });
                                }
                            });


                            Runnable task = new Runnable() {
                                int position = 0;
                                public void run() {
                                    try {
                                        String title1 = data.getJSONObject(position).getString("title");

                                        position++;
                                        if (position == data.length()) {
                                            position = 0;
                                        }
                                        String title2 = data.getJSONObject(position).getString("title");

                                        id[0]= data.getJSONObject(position).getString("id");
                                        learn_id[0]= data.getJSONObject(position).getString("learn_id");

                                        //延迟3秒执行
                                        setTextAnimation(meiriyitingTv, title1, title2);
                                        handler.postDelayed(this, 4000);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            };
                            handler.post(task);//立即调用
                        }




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

    /**
     * 猜你喜欢
     */
    private void rec_book(View view) {

        TextView huanyipiTv = view.findViewById(R.id.huanyipiTv);
        huanyipiTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rec_book(view);
            }
        });
        ImageView xihuanImg1 = view.findViewById(R.id.xihuanImg1);
        TextView xihuanNameTv1 = view.findViewById(R.id.xihuanNameTv1);
        TextView xihuanCountTv1 = view.findViewById(R.id.xihuanCountTv1);

        ImageView xihuanImg2 = view.findViewById(R.id.xihuanImg2);
        TextView xihuanNameTv2 = view.findViewById(R.id.xihuanNameTv2);
        TextView xihuanCountTv2 = view.findViewById(R.id.xihuanCountTv2);

        ImageView xihuanImg3 = view.findViewById(R.id.xihuanImg3);
        TextView xihuanNameTv3 = view.findViewById(R.id.xihuanNameTv3);
        TextView xihuanCountTv3 = view.findViewById(R.id.xihuanCountTv3);


        LinearLayout cainiLay1 = view.findViewById(R.id.cainiLay1);
        LinearLayout cainiLay2 = view.findViewById(R.id.cainiLay2);
        LinearLayout cainiLay3 = view.findViewById(R.id.cainiLay3);



        httpInterface.learn_rec_book(SharedPreferencesUtil.getToken(getActivity()), "1", new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                        JSONArray data = responseJson.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject dataValue = data.getJSONObject(i);
                            String id = dataValue.getString("id");
                            String thumb = dataValue.getString("thumb");
                            String title = dataValue.getString("title");
                            String play_num = dataValue.getString("play_num");
                            int finalI = i;
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (finalI == 0) {
                                        xihuanCountTv1.setText(play_num);
                                        xihuanNameTv1.setText(title);
                                        Glide.with(getActivity()).load(thumb).into(xihuanImg1);
                                        cainiLay1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                                intent.putExtra("id", id);
                                                startActivity(intent);
                                            }
                                        });
                                    } else if (finalI == 1) {
                                        xihuanCountTv2.setText(play_num);
                                        xihuanNameTv2.setText(title);
                                        Glide.with(getActivity()).load(thumb).into(xihuanImg2);
                                        cainiLay2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                                intent.putExtra("id", id);
                                                startActivity(intent);
                                            }
                                        });
                                    } else if (finalI == 2) {
                                        xihuanCountTv3.setText(play_num);
                                        xihuanNameTv3.setText(title);
                                        Glide.with(getActivity()).load(thumb).into(xihuanImg3);
                                        cainiLay3.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent=new Intent(getActivity(), DetailControlActivity.class);
                                                intent.putExtra("id", id);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                }
                            });


                        }

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

    private void new_audio(View view) {
        ImageView tuijianImg = view.findViewById(R.id.tuijianImg);
        TextView tuijianNameTv = view.findViewById(R.id.tuijianNameTv);
        TextView tuijianContentTv = view.findViewById(R.id.tuijianContentTv);
        TextView tuijianPlay_numTv = view.findViewById(R.id.tuijianPlay_numTv);
        TextView tuijianCountTv = view.findViewById(R.id.tuijianCountTv);

        RelativeLayout xinkeLay1=view.findViewById(R.id.xinkeLay1);

        httpInterface.learn_new_audio(new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                        JSONObject data = responseJson.getJSONObject("data");
                        String id = data.getString("id");
                        String title = data.getString("title");
                        String thumb = data.getString("thumb");
                        String description = data.getString("description");
                        String play_num = data.getString("play_num");
                        String album_num = data.getString("album_num");
                        Activity activity=getActivity();
                        if (activity!=null){
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Glide.with(getActivity()).load(thumb).into(tuijianImg);
                                    tuijianNameTv.setText(title);
                                    tuijianContentTv.setText(description);
                                    tuijianPlay_numTv.setText(play_num);
                                    tuijianCountTv.setText(album_num + "集");
                                    xinkeLay1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent=new Intent(getActivity(), YinpingActivity.class);
                                            intent.putExtra("id", id);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            });

                        }



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

//    /**
//     * 猜你喜欢
//     */
//    private void rec_book(View view){
//        ImageView xianshiImg1=view.findViewById(R.id.xianshiImg1);
//        TextView xianshiNameTv1=view.findViewById(R.id.xianshiNameTv1);
//        TextView xianshiCountTv1=view.findViewById(R.id.xianshiCountTv1);
//
//        ImageView xianshiImg2=view.findViewById(R.id.xianshiImg2);
//        TextView xianshiNameTv2=view.findViewById(R.id.xianshiNameTv2);
//        TextView xianshiCountTv2=view.findViewById(R.id.xianshiCountTv2);
//
//        ImageView xianshiImg3=view.findViewById(R.id.xianshiImg3);
//        TextView xianshiNameTv3=view.findViewById(R.id.xianshiNameTv3);
//        TextView xianshiCountTv3=view.findViewById(R.id.xianshiCountTv3);
//
//
//
//    }

    @Override
    public void onClick(View view) {
        if (view == week_updateLay||view==jinqiLay||view==xinkeLay||view==shuyouTuijianLay) {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url",UrlConstant.BOOK_LIST);
            startActivity(intent);
        } else if (view == videoLay) {//直播
//            Intent intent = new Intent(getActivity(), VideoActivity.class);
//            startActivity(intent);
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url",UrlConstant.ZHIBO);
            startActivity(intent);
        } else if (view == allLay) {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url",UrlConstant.BOOK_LIST);
            startActivity(intent);
        }else if (view==zhiboLay){//音频
            Intent intent = new Intent(getActivity(), MyFragmentActivity.class);
            intent.putExtra("type",1);
            startActivity(intent);


        } else if (view==mianfeiLay){
            Intent intent=new Intent(getActivity(), XianshiMianfeiActivity.class);
            if (free==null){
                free=new JSONArray();
            }
            intent.putExtra("data",free.toString());

            startActivity(intent);
        }else if (view==sousuoLay){
            Intent intent=new Intent(getActivity(), SousuoActivity.class);

            startActivity(intent);
        }else if (view==vipkaitongLay){
            Intent intent=new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", UrlConstant.SHENGJI_URL);
            startActivity(intent);
        }else if (view==closeImg){
            vipkaitongLay.setVisibility(View.GONE);
        }
    }


    private void setTextAnimation(TextView textView, String title1, String title2) {
        ObjectAnimator mFadeOutObjectAnimator;
        //由于淡出，是向上移动，坐标为负，这里设定向上移动150个像素
        mFadeOutObjectAnimator = ObjectAnimator.ofFloat(textView, "translationY", 0, -30);
        //动画执行时间设定为500毫秒
        mFadeOutObjectAnimator.setDuration(500);
        mFadeOutObjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //当前动画时间点的动画值，在0到-150之间
                float value = (float) animation.getAnimatedValue();
                //由于不透明度取值为0-1，故而除以150，value/150取值在-1到0之间
                textView.setAlpha(1 + value / 30);
            }
        });
        mFadeOutObjectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                textView.setText(title1);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //动画执行完毕后修改TextView的值
                textView.setText(title2);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        ObjectAnimator mFadeInObjectAnimator;
        //由于是淡入，从底部向中心点移动，移动距离为150像素
        mFadeInObjectAnimator = ObjectAnimator.ofFloat(textView, "translationY", 30, 0);
        //动画执行时间设置为500毫秒
        mFadeInObjectAnimator.setDuration(500);
        //动画推迟100毫秒执行
        mFadeInObjectAnimator.setStartDelay(100);
        mFadeInObjectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //当前动画时间点的动画值
                float value = (float) animation.getAnimatedValue();
                //动画值在150-0之间变化，而alpha在0-1之间变化，故而alue/150
                textView.setAlpha(1 - value / 30);
            }
        });
        AnimatorSet mAnimatorSet = new AnimatorSet();
        //先淡出再淡入
        mAnimatorSet.play(mFadeOutObjectAnimator).before(mFadeInObjectAnimator);
        mAnimatorSet.start();
    }


    /**
     * 今日达人adapter
     */
    class DarenAdapter extends BaseAdapter {

        private JSONArray list;
        private Context ctx;
        private LayoutInflater mInflater;

        public DarenAdapter(Context context, JSONArray list) {
            ctx = context;
            this.list = list;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.length();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            try {
                return list.getJSONObject(position);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            DarenAdapter.ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_jinridaren, null);
                viewHolder = new  DarenAdapter.ViewHolder();
                viewHolder.headImg = convertView.findViewById(R.id.headImg);
                viewHolder.nameTv = convertView.findViewById(R.id.nameTv);
                viewHolder.timeTv = convertView.findViewById(R.id.timeTv);
                viewHolder.guanzhuBtn = convertView.findViewById(R.id.guanzhuBtn);
                viewHolder.vipImg = convertView.findViewById(R.id.vipImg);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (DarenAdapter.ViewHolder) convertView.getTag();
            }
            try {
                JSONObject data = list.getJSONObject(position);
                String id = data.getString("uid");

                String nickname = data.getString("nickname");
                String avatar = data.getString("avatar");
                String num = data.getString("num");
                String days = data.getString("days");
                int is_sub =Integer.parseInt( data.getString("is_sub"));
                int level=Integer.parseInt(data.getString("level"));

                if (level>0){
                    viewHolder.vipImg.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.vipImg.setVisibility(View.GONE);
                }
//                viewHolder.guanzhuBtn.setSelected(is_sub==1?true:false);
                viewHolder.guanzhuBtn.setSelected(is_sub==1?false:true);

                viewHolder.nameTv.setText(nickname);
                viewHolder.timeTv.setText("("+num+"/"+days+"天)");


                Glide.with(getActivity()).load(avatar).into(viewHolder.headImg);



            } catch (JSONException e) {
                e.printStackTrace();
            }


            return convertView;
        }

        class ViewHolder {
            public ImageView headImg;
            public TextView nameTv;
            public TextView  timeTv;
            public Button guanzhuBtn;
            public ImageView vipImg;



        }


    }

}
