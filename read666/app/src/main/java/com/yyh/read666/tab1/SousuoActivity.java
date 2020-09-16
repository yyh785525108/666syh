package com.yyh.read666.tab1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tchy.syh.wxapi.Util;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yyh.read666.R;
import com.yyh.read666.WebActivity;
import com.yyh.read666.configs.Configs;
import com.yyh.read666.details.DetailControlActivity;
import com.yyh.read666.music.MediaUtil;
import com.yyh.read666.music.XMPlayerReceiver;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.HttpInterface;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.okhttp.UrlConstant;
import com.yyh.read666.tab2.YinpingActivity;
import com.yyh.read666.tab2.YinpingDetailActivity;
import com.yyh.read666.utils.BitmapUtil;
import com.yyh.read666.utils.DateUtil;
import com.yyh.read666.utils.MediaPlayerUtil;
import com.yyh.read666.utils.SharedPreferencesUtil;
import com.yyh.read666.view.HongbaoDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

public class SousuoActivity extends AppCompatActivity implements View.OnClickListener {

    private Button backBtn;
    private LinearLayout otherLay;
    private ListView souResultListView;
    private EditText souEt;


    private HttpInterface httpInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sousuo);
        //方式一：这句代码必须写在setContentView()方法的后面
        getSupportActionBar().hide();
        initView();
        initData();


    }

    public void initView() {
        otherLay=findViewById(R.id.otherLay);
        souResultListView=findViewById(R.id.souResultListView);
        souResultListView.setVisibility(View.GONE);
        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(this);
        souEt=findViewById(R.id.souEt);
        souEt.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    otherLay.setVisibility(View.GONE);
                    souResultListView.setVisibility(View.VISIBLE);
                    initSouData(s.toString());
                } else {
                    otherLay.setVisibility(View.VISIBLE);
                    souResultListView.setVisibility(View.GONE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        souResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long tid) {
                JSONObject value= (JSONObject) parent.getItemAtPosition(position);
                try {
                    String id=value.getString("id");
                    Intent intent=new Intent(SousuoActivity.this,DetailControlActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void initData() {
            reShou();
            initHistorySou();
    }
    /**
     * 今日热搜
     */
    private void reShou() {
        RelativeLayout jinriresouLay=findViewById(R.id.jinriresouLay);
        jinriresouLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SousuoActivity.this, BooksActivity.class);
                startActivity(intent);
            }
        });


        LinearLayout resouLay1 =findViewById(R.id.resouLay1);
        TextView resouTv1 = findViewById(R.id.resouTv1);

        LinearLayout resouLay2 = findViewById(R.id.resouLay2);
        TextView resouTv2 = findViewById(R.id.resouTv2);

        LinearLayout resouLay3 =findViewById(R.id.resouLay3);
        TextView resouTv3 =findViewById(R.id.resouTv3);

        LinearLayout resouLay4 = findViewById(R.id.resouLay4);
        TextView resouTv4 = findViewById(R.id.resouTv4);

        LinearLayout resouLay5 = findViewById(R.id.resouLay5);
        TextView resouTv5 = findViewById(R.id.resouTv5);

        LinearLayout resouLay6 = findViewById(R.id.resouLay6);
        TextView resouTv6 = findViewById(R.id.resouTv6);



        httpInterface=new HttpImplement();
        httpInterface.learn_search(SharedPreferencesUtil.getToken(SousuoActivity.this), "hot_search", new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                    runOnUiThread(new Runnable() {
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
                                                Intent intent=new Intent(SousuoActivity.this, DetailControlActivity.class);
                                                intent.putExtra("id", id);
                                                startActivity(intent);
                                            }
                                        });
                                    }else   if (finalI == 1) {
                                        resouTv2.setText(title);
                                        resouLay2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent=new Intent(SousuoActivity.this, DetailControlActivity.class);
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
                                                Intent intent=new Intent(SousuoActivity.this, DetailControlActivity.class);
                                                intent.putExtra("id", id);
                                                startActivity(intent);
                                            }
                                        });
                                    }else   if (finalI == 3) {
                                        resouTv4.setText(title);
                                        resouLay4.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent=new Intent(SousuoActivity.this, DetailControlActivity.class);
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
                                                Intent intent=new Intent(SousuoActivity.this, DetailControlActivity.class);
                                                intent.putExtra("id", id);
                                                startActivity(intent);
                                            }
                                        });
                                    }else   if (finalI == 5) {
                                        resouTv6.setText(title);
                                        resouLay6.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent=new Intent(SousuoActivity.this, DetailControlActivity.class);
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

            @Override
            public void failed(Call call, IOException e) {

            }
        });

    }

    private void initHistorySou(){
        ListView listView=findViewById(R.id.listView);
        httpInterface=new HttpImplement();
        httpInterface.learn_search(SharedPreferencesUtil.getToken(SousuoActivity.this), "history", new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            int status = responseJson.getInt("status");
                            if (status == 1) {
                                JSONArray data = responseJson.getJSONArray("data");
                                String arrayData[]=new String[data.length()];

                                for (int i=0;i<data.length();i++){
                                    String value=data.getString(i);
                                    arrayData[i]=value;
                                }
                                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(SousuoActivity.this,android.R.layout.simple_list_item_1,arrayData);
                                listView.setAdapter(arrayAdapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void failed(Call call, IOException e) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String result = parent.getItemAtPosition(position).toString();//获取选择项的值
                souEt.setText(result);
            }
        });
    }

    private void initSouData(String str){
        httpInterface=new HttpImplement();
        httpInterface.learn_search(SharedPreferencesUtil.getToken(SousuoActivity.this), null, str, "0", new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    String info=responseJson.getString("info");
                    if (status == 1) {
                        JSONObject data = responseJson.getJSONObject("data");
                        JSONArray list=data.getJSONArray("list");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MyAdapter adapter=new MyAdapter(SousuoActivity.this,list);
                                souResultListView.setAdapter(adapter);
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


    @Override
    public void onClick(View view) {
        if (view == backBtn) {
//            if (mediaPlayer!=null){
//                mediaPlayer.reset();;
//            }
            finish();
        }
    }


    public class MyAdapter extends BaseAdapter {

        private JSONArray list;
        private Context ctx;
        private LayoutInflater mInflater;

        public MyAdapter(Context context, JSONArray list) {
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
            System.out.println("getView-------------------");
            MyAdapter.ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_sousuo, null);
                viewHolder = new  MyAdapter.ViewHolder();
                viewHolder.title = convertView.findViewById(R.id.title);
                viewHolder.description = convertView.findViewById(R.id.description);



                convertView.setTag(viewHolder);
            } else {
                viewHolder = (MyAdapter.ViewHolder) convertView.getTag();
            }
            try {
                JSONObject data = list.getJSONObject(position);
                String title = data.getString("title");
                String description = data.getString("description");

                viewHolder.title.setText(title);
                viewHolder.description.setText(description);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return convertView;
        }

        class ViewHolder {
            public TextView title;
            public TextView description;

        }


    }
}
