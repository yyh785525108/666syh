package com.yyh.read666.tab4;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yyh.read666.R;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.tab2.YinpingActivity;
import com.yyh.read666.utils.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

public class NoticeDetailActivity extends AppCompatActivity implements View.OnClickListener  {
    private Button backBtn;
    private TextView statusTitleTv;
    private TextView titleTv,description;
    private ImageView thumb;
    private WebView webView;




    private HttpImplement httpImplement;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏

        setContentView(R.layout.activiti_notice_detail);
        //方式一：这句代码必须写在setContentView()方法的后面
        getSupportActionBar().hide();
        initView();
        refreshData();

    }

    public void initView(){
        statusTitleTv=findViewById(R.id.statusTitleTv);
        backBtn=findViewById(R.id.back);
        backBtn.setOnClickListener(this);

        titleTv=findViewById(R.id.titleTv);
        description=findViewById(R.id.description);
        thumb=findViewById(R.id.thumb);
        webView=findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(false);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        //设置Web视图
        webView.setWebViewClient(new webViewClient ());


    }
    //Web视图
    private class webViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    /**
     * 初始化(活动)列表
     */
    private void refreshData(){
        String id=getIntent().getStringExtra("id");
        String m="info";
        httpImplement=new HttpImplement();
        httpImplement.notice_notice(SharedPreferencesUtil.getToken(this), m,id, new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson=new JSONObject(response);
                    int status=responseJson.getInt("status");
                    if (status==1){
                        JSONObject data=responseJson.getJSONObject("data");
                        String title=data.getString("title");
                        String id=data.getString("id");
                        String description=data.getString("description");
                        String author=data.getString("author");
                        String content=data.getString("content");
                        String thumb=data.getString("thumb");
                        String link=data.getString("link");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                statusTitleTv.setText(title);
                                titleTv.setText(title);
                                NoticeDetailActivity.this.description.setText(description);
                                Glide.with(NoticeDetailActivity.this).load(thumb).into(NoticeDetailActivity.this.thumb);
                                webView.loadDataWithBaseURL(null,content, "text/html",  "utf-8", null);
                            }
                        });

                    }else {
                        String info=responseJson.getString("info");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(NoticeDetailActivity.this,info,Toast.LENGTH_SHORT).show();
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

}
