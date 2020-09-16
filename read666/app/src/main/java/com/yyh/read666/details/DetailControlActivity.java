package com.yyh.read666.details;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yyh.read666.R;
import com.yyh.read666.WebActivity;
import com.yyh.read666.WebAppInterface;
import com.yyh.read666.configs.Configs;
import com.yyh.read666.db.BookSQLiteOpenHelper;
import com.yyh.read666.login.LoginIndexActivity;
import com.yyh.read666.music.MediaUtil;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.HttpInterface;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.okhttp.UrlConstant;
import com.yyh.read666.tab2.YinpingDetailActivity;
import com.yyh.read666.utils.BitmapUtil;
import com.yyh.read666.utils.HtmlUtil;
import com.yyh.read666.utils.MediaPlayerUtil;
import com.yyh.read666.utils.SharedPreferencesUtil;
import com.yyh.read666.view.HongbaoDialog;
import com.yyh.read666.view.dialog.InputTextMsgDialog;
import com.yyh.read666.vo.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import okhttp3.Call;

public class DetailControlActivity extends Activity implements View.OnClickListener {

    private Button backBtn;
    private         Button tiwenBtn;
    private RelativeLayout vedioHuiyuanLay;

    private TextView videoTv,audioTv,articleTv,wenGaoTv;
    private TextView bookTv,zuoyeTv;
    GridView gridView;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private String[] titles = new String[]{"视频","音频","文稿"};

    private  ImageView circlrImg;
    private TextView viptips;
    private RelativeLayout audioLay;
    private     JzvdStd jzvdStd;//视频
    private MediaPlayer mediaPlayer ;//音频

    private TextView startTimeTv,endTimeTv;
    private TextView totalCountTv,wanboTv;

    private LinearLayout videoDesLay;
    private WebView webView;
    private LinearLayout hongbaoLay;

    private LinearLayout is_albumLay;
    private SeekBar seekBar;
    private Button musicStartStopBtn;
    private TextView titleTv,dianzanTv,authorTv,descriptionTv,pingfenTv,album_countTv;
    private LinearLayout dianzanLay;
    private  ImageView dianZanImg;
    private String id;//上个页面传递过来的音视频id
    private HttpInterface httpInterface;

    public static final int UPDATE_MUSIC_UI=100;
    private RelativeLayout shujiLay;
    private  LinearLayout zuoyeLay;
    private  RelativeLayout jiehuoLay;
    private TextView zuoyeCountTv,zuoyeAllCountTv;

    private          RelativeLayout replyLay;
    private          TextView shuodianTv;
    private RelativeLayout shuodianTvLay;
    private ImageView shuodianImg;
    private Button zuoyeBtn;

    ;
    private TextView replyWentiTv;

    private  boolean hasYinping;//有些进来是没音频的

    private ImageView audioImg,videoImg;

    private ImageView bookImg,zuoyeImg,articleImg,wenGaoImg;
    private RelativeLayout xiezuoyeLay;
    private TextView countTv;

    private Button left15Btn,right15Btn;

    private int page=0;
    private RefreshLayout refreshLayout;
    private ShuyouquanAdapter shuyouquanAdapter;
    JSONArray allPinglunList;
    RecyclerView recyclerView;
    private Button shareBtn;

    String title,description,thumb;

    private int is_can_play;
    private boolean isPreparedSuccess=false;//是否准备完成

    private boolean isInVedioScreen=true;//是在音频还是视频页面

    private Dialog yinpingDialog;
    private Button kaitongBtn;
    private RelativeLayout beishuLay;
    private TextView beishuTv;
    private TextView beishuTv2;

    private GridViewAdapter gridViewAdapter;//集数
    private int selectPosition=0;

    private TextView goodNameTv,goodPriceTv;
    private ImageView goodImg;
    private Button buyBtn;
    String goodUrl="";



    private PayBrocast payBrocast;
    String audio_src = null;

    private static  final  int MESSAGE_ZHIFUBAO_RESULT=101;//支付宝支付结果

    private Handler UIhandle = new Handler(){

        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what==UPDATE_MUSIC_UI) {
                if (!isInVedioScreen){
                    //如果音乐在播放，视频不给播！！！
                    try {
                        Jzvd.CURRENT_JZVD.mediaInterface.pause();
                    }catch (Exception e){

                    }
                }

                BookSQLiteOpenHelper mySQLiteOpenHelper= BookSQLiteOpenHelper.getInstance(DetailControlActivity.this);
                User user=SharedPreferencesUtil.getLoginUser(DetailControlActivity.this);

                if (mediaPlayer!=null){
                    int position = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(position);
                    System.out.println("position:"+position+"  max:"+seekBar.getMax());
                    updateTime(startTimeTv,position);
                    //音频记忆
                    UIhandle.sendEmptyMessageDelayed(UPDATE_MUSIC_UI, 500);

                    mySQLiteOpenHelper.updateAudioTime2(id,position);
                    if (is_can_play==0){
                        //非会员只能听6分钟
                        if (position>=6*60*1000){
                            if (mediaPlayer!=null){
                                seekBar.setProgress(6*60*1000);
                                mediaPlayer.seekTo(seekBar.getProgress());
                                musicStartStopBtn.setSelected(false);
                                musicStartStopBtn.setBackgroundResource(R.drawable.audio_play_p);
                                mediaPlayer.pause();
                                circlrImg.clearAnimation();
                                if (!isInVedioScreen){

                                    if (yinpingDialog==null){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(DetailControlActivity.this);
                                        builder.setMessage("本内容为试听版本，请开通VIP后观看");// 为对话框设置内容
                                        builder.setNegativeButton("关闭",null);
                                        builder.setPositiveButton("开通VIP", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface arg0, int arg1) {
                                                weixin_zhifubaoPay();

                                            }
                                        });
                                        yinpingDialog=builder.create();
                                        yinpingDialog.show();// 使用show()方法显示对话框
                                    }
                                }
                            }
                        }
                    }
                }

                //视频记忆
                try {
                    long videoPosition=Jzvd.CURRENT_JZVD.mediaInterface.getCurrentPosition();
                    mySQLiteOpenHelper.updateVideoTime2(id,(int)videoPosition);
                }catch (NullPointerException e){

                }

                if (isInVedioScreen){
                    if (is_can_play==0){
                        //非会员只能看6分钟
                        try {
                            if (Jzvd.CURRENT_JZVD.mediaInterface.getCurrentPosition()>=6*60*1000){
                                Jzvd.CURRENT_JZVD.mediaInterface.seekTo(6*60*1000);
                                Jzvd.CURRENT_JZVD.mediaInterface.pause();
                                vedioHuiyuanLay.setVisibility(View.VISIBLE);
                            }else {
                                vedioHuiyuanLay.setVisibility(View.GONE);
                            }
                        }catch (Exception e){
                        }
                    }else {
                        vedioHuiyuanLay.setVisibility(View.GONE);
                    }
                }

            }else if(msg.what==MESSAGE_ZHIFUBAO_RESULT){
//                Result result = new Result((String) msg.obj);
//                Toast.makeText(DetailControlActivity.this, result.getResult(),
//                        Toast.LENGTH_LONG).show();
                //支付成功
                initLearn_info();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_video_audio_read2);

        if ( Configs.whereFrom==0){//如果是从音频界面过来的
            Configs.whereFrom=1;
            mediaPlayer= MediaUtil.getMediaPlayer();

            //发个广播，把音频里边的监听去掉
            if (Configs.Music_UIhandle!=null){
                Configs.Music_UIhandle.removeMessages(YinpingDetailActivity.UPDATE_MUSIC_UI);
                Configs.Music_UIhandle.removeMessages(YinpingDetailActivity.UPDATE_PROGRESS);
            }


            System.out.println("-------------------------------sendBroadcast-------------------------------------");
            if (MediaUtil.MEDIA_IS_INIT){//从没播放过

            }else {//播放过

                mediaPlayer.reset();

            }
        }


        refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
        recyclerView=findViewById(R.id.listView);
        allPinglunList =new JSONArray();
        shuyouquanAdapter=new ShuyouquanAdapter(DetailControlActivity.this, allPinglunList);
        //必须先设置LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailControlActivity.this));
        recyclerView.setAdapter(shuyouquanAdapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page=0;
                initShuyouQuan();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page=page+1;
                initShuyouQuan();
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });

        initView();
        initData();

        payBrocast = new PayBrocast();
        IntentFilter fil = new IntentFilter("WxPay");

        registerReceiver(payBrocast, fil);
    }

    class PayBrocast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            System.out.println("----------------WxPay-------------:"+intent.getAction());
            if (intent.getAction().equals("WxPay")) {
                String status=intent.getStringExtra("status");
                if (status!=null&&status.equals("1")){
                    //支付成功
                   initLearn_info();
                }



            }


        }
    }

    private void initView(){
        goodNameTv=findViewById(R.id.goodNameTv);
        goodPriceTv=findViewById(R.id.goodPriceTv);
        buyBtn=findViewById(R.id.buyBtn);
        goodImg=findViewById(R.id.goodImg);

        viptips=findViewById(R.id.viptips);
        beishuLay=findViewById(R.id.beishuLay);
        beishuTv=findViewById(R.id.beishuTv);
        beishuTv2=findViewById(R.id.beishuTv2);
        kaitongBtn=findViewById(R.id.kaitongBtn);
        shareBtn=findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(this);

        countTv=findViewById(R.id.countTv);
        audioImg=findViewById(R.id.audioImg);
        videoImg=findViewById(R.id.videoImg);
        bookImg=findViewById(R.id.bookImg);
        zuoyeImg=findViewById(R.id.zuoyeImg);
        articleImg=findViewById(R.id.articleImg);
        wenGaoImg=findViewById(R.id.wenGaoImg);

        xiezuoyeLay=findViewById(R.id.xiezuoyeLay);

        left15Btn=findViewById(R.id.left15Btn);
        right15Btn=findViewById(R.id.right15Btn);


        vedioHuiyuanLay=findViewById(R.id.vedioHuiyuanLay);
        vedioHuiyuanLay.setVisibility(View.GONE);
        tiwenBtn=findViewById(R.id.tiwenBtn);
        shuodianTv=findViewById(R.id.shuodianTv);
        shuodianTvLay=findViewById(R.id.shuodianTvLay);
        shuodianImg=findViewById(R.id.shuodianImg);
        zuoyeBtn=findViewById(R.id.zuoyeBtn);
        zuoyeBtn.setOnClickListener(this);
        shujiLay=findViewById(R.id.shujiLay);
        zuoyeLay=findViewById(R.id.zuoyeLay);
        jiehuoLay=findViewById(R.id.jiehuoLay);
        zuoyeCountTv=findViewById(R.id.zuoyeCountTv);
        zuoyeAllCountTv=findViewById(R.id.zuoyeAllCountTv);
        hongbaoLay=findViewById(R.id.hongbaoLay);

        bookTv=findViewById(R.id.bookTv);
        zuoyeTv=findViewById(R.id.zuoyeTv);
        circlrImg=findViewById(R.id.circlrImg);
        album_countTv=findViewById(R.id.album_countTv);
        is_albumLay=findViewById(R.id.is_albumLay);
        dianZanImg=findViewById(R.id.dianZanImg);
        webView=findViewById(R.id.webview);
        webView.addJavascriptInterface(new WebAppInterface(DetailControlActivity.this),
                "Android");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setDomStorageEnabled(true);// 打开本地缓存提供JS调用,至关重要
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(true);//support zoom
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        videoDesLay=findViewById(R.id.videoDesLay);
        startTimeTv=findViewById(R.id.startTime);
        endTimeTv=findViewById(R.id.endTime);
        seekBar=findViewById(R.id.seek);
        musicStartStopBtn=findViewById(R.id.musicStartStopBtn);
        titleTv = findViewById(R.id.titleTv);
        dianzanTv = findViewById(R.id.dianzanTv);
        authorTv = findViewById(R.id.authorTv);
        descriptionTv= findViewById(R.id.descriptionTv);
        pingfenTv= findViewById(R.id.pingfenTv);

        videoTv = findViewById(R.id.videoTv);
        audioTv =  findViewById(R.id.audioTv);
        articleTv =  findViewById(R.id.articleTv);
        wenGaoTv=  findViewById(R.id.wenGaoTv);
        jzvdStd = (JzvdStd) findViewById(R.id.jz_video);
        Jzvd.SAVE_PROGRESS=true;

        audioLay =findViewById(R.id.audioLay);
        gridView = (GridView) findViewById(R.id.grid);

        backBtn=findViewById(R.id.back);
        backBtn.setOnClickListener(this);

        videoTv.setOnClickListener(this);
        audioTv.setOnClickListener(this);
        articleTv.setOnClickListener(this);
        wenGaoTv.setOnClickListener(this);
        bookTv.setOnClickListener(this);
        zuoyeTv.setOnClickListener(this);
        hongbaoLay.setOnClickListener(this);
        musicStartStopBtn.setOnClickListener(this);
        xiezuoyeLay.setOnClickListener(this);
        left15Btn.setOnClickListener(this);
        right15Btn.setOnClickListener(this);
        kaitongBtn.setOnClickListener(this);
        beishuLay.setOnClickListener(this);
        beishuTv2.setOnClickListener(this);
        viptips.setOnClickListener(this);
        buyBtn.setOnClickListener(this);
        onClick(videoTv);
        onClick(bookTv);

//        setGridView();

        webView.setWebViewClient(new MyWebViewClient());

    }
    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//重置webview中img标签的图片大小
            HtmlUtil.imgReset(webView);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }




    private void initData() {
        mediaPlayer= MediaUtil.getMediaPlayer();
        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer=null;
        }

        httpInterface = new HttpImplement();
        id = getIntent().getStringExtra("id");



        initLearn_info();
        initXiangguanshuji();
        initDianzan();
        initShuyouQuan();
        initZuoyelist();
        initJiehuolist();


    }
    /**
     * 作业列表
     */
    private void initZuoyelist(){
        ListView listView=findViewById(R.id.zuoyeListView);

        HttpInterface httpInterface = new HttpImplement();


        httpInterface.learn_book_work(SharedPreferencesUtil.getToken(this), id,"","0", new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                        JSONObject data = responseJson.getJSONObject("data");
                        JSONArray list=data.getJSONArray("list");
                        String total_count=data.getString("total_count");
                        int count=data.getInt("count");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                zuoyeCountTv.setText(count+"");
                                zuoyeAllCountTv.setText("总计 "+total_count+" 条");

                                ZuoyeAdapter myAdapter=new ZuoyeAdapter(DetailControlActivity.this,list);
                                listView.setAdapter(myAdapter);
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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long tid) {
                Intent intent=new Intent(DetailControlActivity.this,WebActivity.class);
                JSONObject data=(JSONObject) parent.getItemAtPosition(position);
                try {
                    String id=data.getString("id");
                    intent.putExtra("url",UrlConstant.ZUOYE_DETAIL_URL+"?id="+id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });
    }

    /**
     * 解惑列表
     */
    private void initJiehuolist(){
        RelativeLayout tiwenLay=findViewById(R.id.tiwenLay);
        tiwenLay.setVisibility(View.GONE);
        tiwenBtn.setVisibility(View.GONE);
        Button tiwenCloseBtn=findViewById(R.id.tiwenCloseBtn);
        Button tiwenCommitBtn=findViewById(R.id.tiwenCommitBtn);
        EditText tiwenContentEt=findViewById(R.id.tiwenContentEt);
        tiwenContentEt.setText("11111111111111");

        ListView listView=findViewById(R.id.jiehuoListView);

        //回答问题
        replyLay=findViewById(R.id.replyLay);
        replyLay.setVisibility(View.GONE);
        Button replyCloseBtn=findViewById(R.id.replyCloseBtn);
        Button replyCommitBtn=findViewById(R.id.replyCommitBtn);
        EditText replyContentEt=findViewById(R.id.replyContentEt);
        replyWentiTv=findViewById(R.id.replyWentiTv);
        replyContentEt.setText("");

        replyCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyLay.setVisibility(View.GONE);
                tiwenBtn.setVisibility(View.VISIBLE);
            }
        });
        replyLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replyLay.setVisibility(View.GONE);
                tiwenBtn.setVisibility(View.VISIBLE);
            }
        });
        replyCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=replyContentEt.getText().toString();
                if (content==null||content.length()==0){
                    Toast.makeText(DetailControlActivity.this,"请先输入回答",Toast.LENGTH_SHORT).show();
                    return;
                }
                HttpInterface httpInterface = new HttpImplement();
                httpInterface.learn_book_ask(SharedPreferencesUtil.getToken(DetailControlActivity.this), "publish",id,(String) replyLay.getTag(),content, new OkHttpUtils.MyNetCall() {
                    @Override
                    public void success(Call call, String response) throws IOException {
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            int status = responseJson.getInt("status");
                            if (status == 1) {
                                String info=responseJson.getString("info");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideKeyboard();
                                        Toast.makeText(DetailControlActivity.this,info,Toast.LENGTH_SHORT).show();
                                        initJiehuolist();
                                    }

                                });
                            }else {
                                String info=responseJson.getString("info");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DetailControlActivity.this,info,Toast.LENGTH_SHORT).show();
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
        });


        tiwenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiwenLay.setVisibility(View.VISIBLE);
                tiwenBtn.setVisibility(View.GONE);
                tiwenContentEt.setFocusable(true);
                tiwenContentEt.setFocusableInTouchMode(true);
                tiwenContentEt.requestFocus();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);


            }
        });
        tiwenLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiwenLay.setVisibility(View.GONE);
                tiwenBtn.setVisibility(View.VISIBLE);
            }
        });
        tiwenCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tiwenLay.setVisibility(View.GONE);
                tiwenBtn.setVisibility(View.VISIBLE);

            }
        });
        tiwenCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=tiwenContentEt.getText().toString();
                if (content==null||content.length()==0){
                    Toast.makeText(DetailControlActivity.this,"请先输入提问",Toast.LENGTH_SHORT).show();
                    return;
                }
                HttpInterface httpInterface = new HttpImplement();
                httpInterface.learn_book_ask(SharedPreferencesUtil.getToken(DetailControlActivity.this), "publish",id,"",content, new OkHttpUtils.MyNetCall() {
                    @Override
                    public void success(Call call, String response) throws IOException {
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            int status = responseJson.getInt("status");
                            if (status == 1) {
                                String info=responseJson.getString("info");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideKeyboard();
                                        Toast.makeText(DetailControlActivity.this,info,Toast.LENGTH_SHORT).show();
                                        initJiehuolist();
                                    }

                                });
                            }else {
                                String info=responseJson.getString("info");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DetailControlActivity.this,info,Toast.LENGTH_SHORT).show();
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
        });


        HttpInterface httpInterface = new HttpImplement();
        httpInterface.learn_book_ask(SharedPreferencesUtil.getToken(this), id,"0", new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                        JSONObject data = responseJson.getJSONObject("data");
                        JSONArray list=data.getJSONArray("list");
                        String total_count=data.getString("total_count");
                        int count=data.getInt("count");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

//                                zuoyeCountTv.setText(count+"");
//                                zuoyeAllCountTv.setText("总计 "+total_count+" 条");
//
                                JiehuoAdapter myAdapter=new JiehuoAdapter(DetailControlActivity.this,list);
                                listView.setAdapter(myAdapter);
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
     * 书友圈
     */
    private void initShuyouQuan(){

        User user=SharedPreferencesUtil.getLoginUser(this);
        if (user!=null){
            Glide.with(this).load(user.getAvatar()).into(shuodianImg);

        }else {
            shuodianImg.setImageResource(R.drawable.ic_launcher);
        }
        shuodianImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetailControlActivity.this, WebActivity.class);
                intent.putExtra("url", UrlConstant.ZHUYE_URL+"?uid="+user.getUid());
                startActivity(intent);
            }
        });

        InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(this, R.style.dialog_center);
        inputTextMsgDialog.setmOnTextSendListener(new InputTextMsgDialog.OnTextSendListener() {
            @Override
            public void onTextSend(String msg) {
                //点击发送按钮后，回调此方法，msg为输入的值
                if (inputTextMsgDialog.getFlag()==0){
                    //评论
                    HttpInterface httpInterface = new HttpImplement();
                    httpInterface.learn_comment(SharedPreferencesUtil.getToken(DetailControlActivity.this), "add",id,"","",msg, new OkHttpUtils.MyNetCall() {
                        @Override
                        public void success(Call call, String response) throws IOException {
                            try {
                                JSONObject responseJson = new JSONObject(response);
                                int status = responseJson.getInt("status");
                                if (status == 1) {
                                    String info=responseJson.getString("info");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideKeyboard();
                                            Toast.makeText(DetailControlActivity.this,info,Toast.LENGTH_SHORT).show();
                                            initShuyouQuan();
                                        }

                                    });
                                }else {
                                    String info=responseJson.getString("info");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(DetailControlActivity.this,info,Toast.LENGTH_SHORT).show();
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

                }else if (inputTextMsgDialog.getFlag()==1){
                    String pid=(String)inputTextMsgDialog.getPinglunId();

                    HttpInterface httpInterface = new HttpImplement();
                    httpInterface.learn_comment(SharedPreferencesUtil.getToken(DetailControlActivity.this), "add",id,null,pid,msg, new OkHttpUtils.MyNetCall() {
                        @Override
                        public void success(Call call, String response) throws IOException {
                            try {
                                JSONObject responseJson = new JSONObject(response);
                                int status = responseJson.getInt("status");
                                if (status == 1) {
                                    String info=responseJson.getString("info");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            hideKeyboard();
                                            Toast.makeText(DetailControlActivity.this,info,Toast.LENGTH_SHORT).show();
                                            initShuyouQuan();
                                        }

                                    });
                                }else {
                                    String info=responseJson.getString("info");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(DetailControlActivity.this,info,Toast.LENGTH_SHORT).show();
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
            }
        });

        RelativeLayout shuyouReplayLay=findViewById(R.id.shuyouReplayLay);

        shuyouReplayLay.setVisibility(View.GONE);
        RelativeLayout shuodianLay=findViewById(R.id.shuodianLay);
        shuodianLay.setVisibility(View.GONE);
        shuodianTvLay.setVisibility(View.VISIBLE);
        hideKeyboard();
        Button shuodianCloseBtn=findViewById(R.id.shuodianCloseBtn);
        shuodianCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuodianLay.setVisibility(View.GONE);
                shuodianTvLay.setVisibility(View.VISIBLE);
                hideKeyboard();
            }
        });
        shuodianLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuodianLay.setVisibility(View.GONE);
                shuodianTvLay.setVisibility(View.VISIBLE);
                hideKeyboard();
            }
        });
        EditText shuodianContentEt=findViewById(R.id.shuodianContentEt);

        shuodianTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                shuodianTv.setVisibility(View.GONE);
//                shuodianLay.setVisibility(View.VISIBLE);
//                openKeyBoard(shuodianContentEt);
//                shuodianContentEt.setText("");
                inputTextMsgDialog.setFlag(0);//0为评论
                inputTextMsgDialog.setHint("说点什么~");
                inputTextMsgDialog.setText("");
                inputTextMsgDialog.show();
            }
        });
        Button shuodianCommitBtn=findViewById(R.id.shuodianCommitBtn);
        shuodianCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=shuodianContentEt.getText().toString();
                if (content==null||content.length()==0){
                    Toast.makeText(DetailControlActivity.this,"请先发表评论",Toast.LENGTH_SHORT).show();
                    return;
                }
                HttpInterface httpInterface = new HttpImplement();
                httpInterface.learn_comment(SharedPreferencesUtil.getToken(DetailControlActivity.this), "add",id,"","",content, new OkHttpUtils.MyNetCall() {
                    @Override
                    public void success(Call call, String response) throws IOException {
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            int status = responseJson.getInt("status");
                            if (status == 1) {
                                String info=responseJson.getString("info");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideKeyboard();
                                        Toast.makeText(DetailControlActivity.this,info,Toast.LENGTH_SHORT).show();
                                        initShuyouQuan();
                                    }

                                });
                            }else {
                                String info=responseJson.getString("info");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DetailControlActivity.this,info,Toast.LENGTH_SHORT).show();
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
        });



        TextView sreplyWentiTv=(TextView)findViewById(R.id.sreplyWentiTv);
        EditText sreplyContentEt=(EditText)findViewById(R.id.sreplyContentEt);
        Button sreplyCommitBtn=(Button)findViewById(R.id.sreplyCommitBtn);
        Button shuyouReplyCloseBtn=(Button)findViewById(R.id.shuyouReplyCloseBtn);
        shuyouReplyCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuyouReplayLay.setVisibility(View.GONE);

            }
        });
        sreplyCommitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=sreplyContentEt.getText().toString();
                if (content==null||content.length()==0){
                    Toast.makeText(DetailControlActivity.this,"请先输入评论",Toast.LENGTH_SHORT).show();
                    return;
                }
                String pid=(String) shuyouReplayLay.getTag();
                System.out.println("pid22222222:"+pid);
                HttpInterface httpInterface = new HttpImplement();
                httpInterface.learn_comment(SharedPreferencesUtil.getToken(DetailControlActivity.this), "add",id,null,pid,content, new OkHttpUtils.MyNetCall() {
                    @Override
                    public void success(Call call, String response) throws IOException {
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            int status = responseJson.getInt("status");
                            if (status == 1) {
                                String info=responseJson.getString("info");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideKeyboard();
                                        Toast.makeText(DetailControlActivity.this,info,Toast.LENGTH_SHORT).show();
                                        initShuyouQuan();
                                    }

                                });
                            }else {
                                String info=responseJson.getString("info");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(DetailControlActivity.this,info,Toast.LENGTH_SHORT).show();
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
        });



        HttpInterface httpInterface = new HttpImplement();
        httpInterface.items_comment_get(SharedPreferencesUtil.getToken(this), id,"","",page+"", new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                        JSONObject data = responseJson.getJSONObject("data");
                        String total_count=data.getString("total_count");
                        JSONArray list=data.getJSONArray("list");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ((TextView)findViewById(R.id.commentsTv)).setText(total_count);
                                countTv.setText(total_count+"条评论");
                                if (page==0){  //下拉刷新
                                    //清空数据再重新加载
                                    for (int i = 0; i < allPinglunList.length(); i++) {
                                        allPinglunList.remove(i);
                                        i--;
                                    }
                                    for (int i=0;i<list.length();i++){
                                        try {
                                            allPinglunList.put(list.getJSONObject(i));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    refreshLayout.finishRefresh();

                                }else {//上拉加载刷新
                                    for (int i=0;i<list.length();i++){
                                        try {
                                            allPinglunList.put(list.getJSONObject(i));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    refreshLayout.finishLoadMore();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 更新数据
                                        shuyouquanAdapter.notifyDataSetChanged();
                                    }
                                });

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

        shuyouquanAdapter.setOnItemClickListener(new ShuyouquanAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
//                shuyouReplayLay.setVisibility(View.VISIBLE);
//                sreplyContentEt.setText("");
                try {
                    JSONObject data= (JSONObject) allPinglunList.getJSONObject(pos);

                    String id = data.getString("id");
                    String content = data.getString("content");
                    String uid = data.getString("uid");
                    String nickname=data.getString("nickname");
//                    shuyouReplayLay.setTag(id);
//                    sreplyWentiTv.setText(content);

                    inputTextMsgDialog.setFlag(1);//0为回复别人的评论
                    inputTextMsgDialog.setPinglunId(id);
                    inputTextMsgDialog.setHint("回复@"+nickname);
                    inputTextMsgDialog.setText("");
                    inputTextMsgDialog.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long tid) {
//                shuyouReplayLay.setVisibility(View.VISIBLE);
//                sreplyContentEt.setText("");
//
//
//                JSONObject data= (JSONObject) parent.getItemAtPosition(position);
//                try {
//                    String id = data.getString("id");
//                    String content = data.getString("content");
//                    String uid = data.getString("uid");
//                    shuyouReplayLay.setTag(id);
//                    System.out.println("pid211111111111:"+uid);
//                    sreplyWentiTv.setText(content);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
    }

    /**
     * 初始化学习详情
     */
    private void initLearn_info(){
        LinearLayout shujiaLay=findViewById(R.id.shujiaLay);
        ImageView shujiaImg=findViewById(R.id.shujiaImg);
        TextView rebanTv=findViewById(R.id.rebanTv);
        rebanTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetailControlActivity.this,WebActivity.class);
                intent.putExtra("url",UrlConstant.BOOK_LIST+"#type=hot");
                startActivity(intent);
            }
        });

        shujiaLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpInterface.learn_shujia(SharedPreferencesUtil.getToken(DetailControlActivity.this), "add", id, new OkHttpUtils.MyNetCall() {
                    @Override
                    public void success(Call call, String response) throws IOException {
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            int status = responseJson.getInt("status");
                            String info=responseJson.getString("info");
                            if (status == 1) {
                                JSONObject data = responseJson.getJSONObject("data");
                                String type=data.getString("type");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (type!=null&&type.equals("add")){
                                            shujiaImg.setSelected(true);
                                        }else {
                                            shujiaImg.setSelected(false);
                                        }
                                        Toast.makeText(DetailControlActivity.this,info,Toast.LENGTH_SHORT).show();
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
        });
//        String testtoken="342aaeb38effc17370bc21abed99acd6";
//        httpInterface.learn_info(testtoken, id, new OkHttpUtils.MyNetCall() {

        httpInterface.learn_info(SharedPreferencesUtil.getToken(this), id, new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                        JSONObject data = responseJson.getJSONObject("data");
                        String  video_src = data.getString("video_src");
                        thumb = data.getString("thumb");
                        String poster = data.getString("poster");

                            JSONObject goods=data.getJSONObject("goods");
                            System.out.println("goods："+goods);
                            if (goods.has("thumb")){
                                String goodThumb=goods.getString("thumb");
                                String goodPrice=goods.getString("price");
                                goodUrl=goods.getString("url");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.goodLay).setVisibility(View.VISIBLE);
                                        goodNameTv.setText(title);
                                        goodPriceTv.setText("￥"+goodPrice+"元");
                                        Glide.with(DetailControlActivity.this).load(goodThumb).into(goodImg);
                                    }
                                });
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.goodLay).setVisibility(View.GONE);
                                    }
                                });
                            }







                        title = data.getString("title");
                        String like_num = data.getString("like_num");
                        String author = data.getString("author");
                        String play_num = data.getString("play_num");
                        description = data.getString("description");
                        String score = data.getString("score");

                        String content = data.getString("content");

                        int is_like=data.getInt("is_like");
                        int is_album=data.getInt("is_album");
                        int is_shujia=data.getInt("is_shujia");
                        int paiming=data.getInt("paiming");
                        int is_can_play=data.getInt("is_can_play");
                        System.out.println("is_can_play:"+is_can_play);
                        DetailControlActivity.this.is_can_play=is_can_play;

                        //存储一下
                        System.out.println("--------------------------------getSharedPreferences----------------------------------------------------------------");
                        SharedPreferences sp=getSharedPreferences(Configs.SHARE_NAME,Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putInt("type",0);//0视频,1音频
                        editor.putString("id",id);
                        editor.putString("thumb",thumb);
                        editor.commit();

                        SharedPreferences sp2=getSharedPreferences(Configs.SHARE_NAME, Activity.MODE_PRIVATE);

                        JSONObject online=data.getJSONObject("online");


                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    hasYinping=true;
                                    audio_src = data.getString("audio_src");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            isInVedioScreen=getIntent().getIntExtra("isVedio",0)==0?true:false;
                                            System.out.println("isInVedioScreen:"+isInVedioScreen);
                                            if (!isInVedioScreen){
                                                onClick(audioTv);
                                            }
                                        }
                                    });
                                    initMedia(audio_src);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    hasYinping=false;

                                }

                            }
                        }).start();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                rebanTv.setText("热榜 NO."+paiming+">");
                                titleTv.setText(title);
                                dianzanTv.setText(like_num);
                                authorTv.setText("热度"+play_num+" / "+author);
                                descriptionTv.setText(description);
                                pingfenTv.setText(score+"分");
                                Glide.with(DetailControlActivity.this).load(poster).into(jzvdStd.posterImageView);
                                jzvdStd.setUp(video_src
                                        , title);
                                Glide.with(DetailControlActivity.this).load(thumb).into(circlrImg);
                                //记忆播放
                                BookSQLiteOpenHelper sqlHelp= BookSQLiteOpenHelper.getInstance(DetailControlActivity.this);
                                int videoTime=sqlHelp.selectVideoTime(id); //视频进度
                                System.out.println("videoTime："+videoTime);
                                if (isInVedioScreen){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (videoTime!=-1){
                                                jzvdStd.startVideoAfterPreloading();

//                                                Jzvd.CURRENT_JZVD.mediaInterface.seekTo(videoTime);
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            Jzvd.CURRENT_JZVD.mediaInterface.seekTo(videoTime);
                                                        }catch (NullPointerException e){
                                                        }
                                                    }
                                                }, 4000);    //延时1s执行
                                            }

                                        }
                                    });
                                }
                                UIhandle.sendEmptyMessage(UPDATE_MUSIC_UI);

                                webView.loadDataWithBaseURL(null,content, "text/html",  "utf-8", null);
                                dianZanImg.setSelected(is_like==1?true:false);
                                shujiaImg.setSelected(is_shujia==1?true:false);

                                int album_count= 0;
                                try {
                                    album_count = data.getInt("album_count");
                                    album_countTv.setText("全"+album_count+"集");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                is_albumLay.setVisibility(is_album==1?View.VISIBLE:View.GONE);
                                if (is_album==1){
                                    try {
                                        JSONArray album = data.getJSONArray("album");
                                        setGridView(album.length());
                                        gridViewAdapter=new GridViewAdapter(DetailControlActivity.this,album);
                                        gridView.setAdapter(gridViewAdapter);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                dealOnline(online);



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
     * 初始化点赞
     */
    private void initDianzan(){
        dianZanImg=findViewById(R.id.dianZanImg);
        dianzanLay=findViewById(R.id.dianzanLay);
        dianzanTv=findViewById(R.id.dianzanTv);
        dianzanLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpInterface.learn(SharedPreferencesUtil.getToken(DetailControlActivity.this), id, "like", new OkHttpUtils.MyNetCall() {
                    @Override
                    public void success(Call call, String response) throws IOException {
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            int status = responseJson.getInt("status");
                            String info=responseJson.getString("info");
                            if (status == 1) {
                                JSONObject data = responseJson.getJSONObject("data");
                                int is_add=data.getInt("is_add");
                                int num=data.getInt("num");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dianzanTv.setText(num+"");
                                        dianZanImg.setSelected(is_add==1?true:false);
                                        Toast.makeText(DetailControlActivity.this,info,Toast.LENGTH_SHORT).show();
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
        });

    }
    /**
     * 初始化相关书籍
     */
    private void initXiangguanshuji(){
        httpInterface.learn(SharedPreferencesUtil.getToken(this), id, "related", new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                        JSONArray data = responseJson.getJSONArray("data");
                        dealXiangguanshuji(data);
                    }
                }catch (JSONException e){

                }
            }

            @Override
            public void failed(Call call, IOException e) {

            }
        });
    }
    private void dealXiangguanshuji(JSONArray data){

        ImageView xiangguanImg1 = findViewById(R.id.xiangguanImg1);
        TextView xiangguanNameTv1 = findViewById(R.id.xiangguanNameTv1);
        TextView xiangguanContentTv1 = findViewById(R.id.xiangguanContentTv1);


        ImageView xiangguanImg2 =findViewById(R.id.xiangguanImg2);
        TextView xiangguanNameTv2 = findViewById(R.id.xiangguanTv2);
        TextView xiangguanContentTv2 = findViewById(R.id.xiangguanContentTv2);


        ImageView xiangguanImg3 =findViewById(R.id.xiangguanImg3);
        TextView xiangguanNameTv3 =findViewById(R.id.xiangguanTv3);
        TextView xiangguanContentTv3 = findViewById(R.id.xiangguanContentTv3);


        ImageView xiangguanImg4 = findViewById(R.id.xiangguanImg4);
        TextView xiangguanNameTv4 = findViewById(R.id.xiangguanTv4);
        TextView xiangguanContentTv4 = findViewById(R.id.xiangguanContentTv4);


        LinearLayout xiangguanLay1 =findViewById(R.id.xiangguanLay1);
        LinearLayout xiangguanLay2 = findViewById(R.id.xiangguanLay2);
        LinearLayout xiangguanLay3 = findViewById(R.id.xiangguanLay3);
        LinearLayout xiangguanLay4 = findViewById(R.id.xiangguanLay4);


        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject top5Value = data.getJSONObject(i);
                String id = top5Value.getString("id");
                String title = top5Value.getString("title");
                String thumb = top5Value.getString("thumb");
                String description = top5Value.getString("description");
                String view_num = top5Value.getString("view_num");
                String add_time = top5Value.getString("add_time");
                int finalI = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalI == 0) {
                            xiangguanNameTv1.setText(title);
                            xiangguanContentTv1.setText(view_num+"点击");
                            Glide.with(DetailControlActivity.this).load(thumb).into(xiangguanImg1);
                            xiangguanLay1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(DetailControlActivity.this, DetailControlActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                }
                            });
                        } else if (finalI == 1) {
                            xiangguanNameTv2.setText(title);
                            xiangguanContentTv2.setText(view_num+"点击");
                            Glide.with(DetailControlActivity.this).load(thumb).into(xiangguanImg2);
                            xiangguanLay2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(DetailControlActivity.this, DetailControlActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                }
                            });
                        }else if (finalI == 2) {
                            xiangguanNameTv3.setText(title);
                            xiangguanContentTv3.setText(view_num+"点击");
                            Glide.with(DetailControlActivity.this).load(thumb).into(xiangguanImg3);
                            xiangguanLay3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(DetailControlActivity.this, DetailControlActivity.class);
                                    intent.putExtra("id", id);
                                    startActivity(intent);
                                }
                            });
                        }else if (finalI == 3) {
                            xiangguanNameTv4.setText(title);
                            xiangguanContentTv4.setText(view_num+"点击");
                            Glide.with(DetailControlActivity.this).load(thumb).into(xiangguanImg4);
                            xiangguanLay4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(DetailControlActivity.this, DetailControlActivity.class);
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
     * 初始化在线人数
     */
    private void dealOnline(JSONObject online){
        ImageView guankanImg1=findViewById(R.id.guankanImg1);
        ImageView guankanImg2=findViewById(R.id.guankanImg2);
        ImageView guankanImg3=findViewById(R.id.guankanImg3);
        ImageView guankanImg4=findViewById(R.id.guankanImg4);

        totalCountTv=findViewById(R.id.totalCountTv);
        wanboTv=findViewById(R.id.wanboTv);
        try {
            int total=online.getInt("total");
            int wanbo=online.getInt("wanbo");
            JSONArray list=online.getJSONArray("list");
//            totalCountTv.setText(wanbo+"人已看完");
//////            wanboTv.setText(total+"人正在同你一起观看");
            totalCountTv.setText(total+"人正在同你一起观看");
            wanboTv.setText(wanbo+"人已看完");
            if (list.length()==0){
                guankanImg1.setVisibility(View.GONE);
                guankanImg2.setVisibility(View.GONE);
                guankanImg3.setVisibility(View.GONE);
                guankanImg4.setVisibility(View.GONE);
            }else   if (list.length()==1){
                guankanImg1.setVisibility(View.VISIBLE);
                guankanImg2.setVisibility(View.GONE);
                guankanImg3.setVisibility(View.GONE);
                guankanImg4.setVisibility(View.GONE);
                JSONObject value=list.getJSONObject(0);
                String avatar=value.getString("avatar");
                Glide.with(DetailControlActivity.this).load(avatar).into(guankanImg1);


            }else   if (list.length()==2){
                guankanImg1.setVisibility(View.VISIBLE);
                guankanImg2.setVisibility(View.VISIBLE);
                guankanImg3.setVisibility(View.GONE);
                guankanImg4.setVisibility(View.GONE);
                JSONObject value=list.getJSONObject(0);
                String avatar=value.getString("avatar");
                Glide.with(DetailControlActivity.this).load(avatar).into(guankanImg1);

                value=list.getJSONObject(1);
                avatar=value.getString("avatar");
                Glide.with(DetailControlActivity.this).load(avatar).into(guankanImg2);

            }else   if (list.length()==3){
                guankanImg1.setVisibility(View.VISIBLE);
                guankanImg2.setVisibility(View.VISIBLE);
                guankanImg3.setVisibility(View.VISIBLE);
                guankanImg4.setVisibility(View.GONE);
                JSONObject value=list.getJSONObject(0);
                String avatar=value.getString("avatar");
                Glide.with(DetailControlActivity.this).load(avatar).into(guankanImg1);

                value=list.getJSONObject(1);
                avatar=value.getString("avatar");
                Glide.with(DetailControlActivity.this).load(avatar).into(guankanImg2);

                value=list.getJSONObject(2);
                avatar=value.getString("avatar");
                Glide.with(DetailControlActivity.this).load(avatar).into(guankanImg3);
            }else   if (list.length()==4){
                guankanImg1.setVisibility(View.VISIBLE);
                guankanImg2.setVisibility(View.VISIBLE);
                guankanImg3.setVisibility(View.VISIBLE);
                guankanImg4.setVisibility(View.VISIBLE);
                JSONObject value=list.getJSONObject(0);
                String avatar=value.getString("avatar");
                Glide.with(DetailControlActivity.this).load(avatar).into(guankanImg1);

                value=list.getJSONObject(1);
                avatar=value.getString("avatar");
                Glide.with(DetailControlActivity.this).load(avatar).into(guankanImg2);

                value=list.getJSONObject(2);
                avatar=value.getString("avatar");
                Glide.with(DetailControlActivity.this).load(avatar).into(guankanImg3);

                value=list.getJSONObject(3);
                avatar=value.getString("avatar");
                Glide.with(DetailControlActivity.this).load(avatar).into(guankanImg3);
            }


            findViewById(R.id.totolLay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(DetailControlActivity.this,GuankanzhongActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**设置GirdView参数，绑定数据*/
    private void setGridView(int size) {
//        int size =6;
        int length = 60;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length + 4) * density);
        int itemWidth = (int) (length * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(5); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectPosition=i;
                gridViewAdapter.notifyDataSetInvalidated();

                JSONObject data=(JSONObject) adapterView.getItemAtPosition(i);
                try {
                    int id=data.getInt("id");
                    httpInterface.learn_info(SharedPreferencesUtil.getToken(DetailControlActivity.this), id+"", "video_info", new OkHttpUtils.MyNetCall() {
                        @Override
                        public void success(Call call, String response) throws IOException {
                            try {
                                JSONObject responseJson = new JSONObject(response);
                                int status = responseJson.getInt("status");
                                if (status == 1) {
                                    JSONObject data=responseJson.getJSONObject("data");
                                    int id=data.getInt("id");
                                    int book_id=data.getInt("book_id");
                                    String title=data.getString("title");
                                    String description=data.getString("description");
                                    String src=data.getString("src");
                                    //点击了视频集数，切换视频
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            jzvdStd.setUp(src
                                                    , title);
                                        }
                                    });

                                }else{
                                    String info=responseJson.getString("info");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(DetailControlActivity.this,info,Toast.LENGTH_SHORT).show();
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * 初始化音频播放器
     * @param audioUrl 音频地址
     */
    private void initMedia(String audioUrl){
        System.out.println("!111111111111111111111111111111111111111111");
        mediaPlayer= MediaUtil.getMediaPlayer();
        if (MediaUtil.MEDIA_IS_INIT){//从没播放过
            System.out.println("!222222222222222222222222222");
        }else {//播放过
            mediaPlayer.reset();
            System.out.println("!3333333333333333333333333333");
        }
        try {
            System.out.println("!44444444444444444444444444:"+audioUrl);
            mediaPlayer.setDataSource(audioUrl);
//            mediaPlayer.prepare();
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isPreparedSuccess=true;
                    int duration = mediaPlayer.getDuration();
                    System.out.println("!5555555555555555555555555555555:duration:"+duration);
                    if (0 != duration) {
                        //更新 seekbar 长度
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                seekBar.setMax(duration);
                                int s = duration / 1000;
                                //设置文件时长，单位 "分:秒" 格式
                                String total = s / 60 + ":" + s % 60;
                                startTimeTv.setText("00:00");
                                endTimeTv.setText(total);
                            }
                        });


                    }
                }
            });


        } catch (Exception e) {
            System.out.println("!77777777777777777777777777");

            e.printStackTrace();
        }

        //记忆播放
        BookSQLiteOpenHelper sqlHelp= BookSQLiteOpenHelper.getInstance(this);
        int audioTime=sqlHelp.selectAudioTime(id);   //音频进度
        System.out.println("audioTime111："+audioTime);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (audioTime!=-1){
                    seekBar.setProgress(audioTime);
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                yinpingDialog=null;
                System.out.println("---------------------------------------onStopTrackingTouch------------------------:"+seekBar.getProgress());
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });



    }
    //更新时间
    private void updateTime(TextView textView,int millisecond){
        int s = millisecond / 1000;
        //设置文件时长，单位 "分:秒" 格式
//        String total = s / 60 + ":" + s % 60;
        String mStr = "";
        String sStr = "";
        int miniute=s/60;
        int second=s%60;
        if (miniute<10){
            mStr="0"+miniute;
        }else {
            mStr=miniute+"";
        }

        if (second<10){
            sStr="0"+second;
        }else {
            sStr=second+"";
        }

        textView.setText(mStr+":"+sStr);

    }



    /**GirdView 数据适配器*/
    public class GridViewAdapter extends BaseAdapter {
        Context context;
        JSONArray album;
        public GridViewAdapter(Context _context,JSONArray album) {
            this.context = _context;
            this.album=album;
            System.out.println("album.1111:"+album.toString());
        }

        @Override
        public int getCount() {
            return album.length();
        }

        @Override
        public Object getItem(int position) {
            try {
                return (Object) album.getJSONObject(position);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;

            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_jishu, null);
            TextView titleTv=convertView.findViewById(R.id.titleTv);
            titleTv.setText((position+1)+"");

            TextView vipTv=convertView.findViewById(R.id.vipTv);
            try {
                JSONObject albumData=album.getJSONObject(position);
                int is_vip=albumData.getInt("is_vip");
                if (is_vip==0){
                    vipTv.setVisibility(View.GONE);
                }else {
                    vipTv.setVisibility(View.VISIBLE);
                }
                if (selectPosition==position){
                    titleTv.setTextColor(0xff00b1ff);

                }else {
                    titleTv.setTextColor(0xff232122);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return convertView;
        }
    }




    @Override
    public void onClick(View view) {
        if (view==backBtn){
            finish();
        }else if (view==videoTv){
            isInVedioScreen=true;

            //存储一下
            SharedPreferences sp=getSharedPreferences(Configs.SHARE_NAME,Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putInt("type",0);//0视频,1音频
            editor.putInt("isVedio",0);//0视频里边的视频页面，1视频里边的音频页面
            editor.putString("id",id);
            editor.putString("thumb",thumb);
            editor.commit();


            audioImg.setVisibility(View.INVISIBLE);
            videoImg.setVisibility(View.VISIBLE);

            videoDesLay.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            videoTv.setSelected(true);
            audioTv.setSelected(false);
            articleTv.setSelected(false);
            jzvdStd.setVisibility(View.VISIBLE);
            audioLay.setVisibility(View.GONE);
            circlrImg.clearAnimation();

            if (mediaPlayer!=null){
                musicStartStopBtn.setSelected(false);
                musicStartStopBtn.setBackgroundResource(R.drawable.audio_play_p);
                if (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
            }
        }else if (view==audioTv){
            if (hasYinping){
                isInVedioScreen=false;
                vedioHuiyuanLay.setVisibility(View.GONE);
                //存储一下
                SharedPreferences sp=getSharedPreferences(Configs.SHARE_NAME,Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor=sp.edit();
                editor.putInt("type",0);//0视频,1音频
                editor.putInt("isVedio",1);//0视频里边的视频页面，1视频里边的音频页面
                editor.putString("id",id);
                editor.putString("thumb",thumb);
                editor.commit();

                audioImg.setVisibility(View.VISIBLE);
                videoImg.setVisibility(View.INVISIBLE);
                videoDesLay.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);

                videoTv.setSelected(false);
                audioTv.setSelected(true);
                articleTv.setSelected(false);
                jzvdStd.setVisibility(View.GONE);
                audioLay.setVisibility(View.VISIBLE);

                try {
//                    Jzvd.goOnPlayOnPause();
                    Jzvd.CURRENT_JZVD.mediaInterface.pause();
                }catch (Exception e){

                }

            }else {
                Toast.makeText(DetailControlActivity.this,"该栏目没有音频",Toast.LENGTH_SHORT).show();
            }


        }else if (view==articleTv){
//            videoDesLay.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);

            zuoyeTv.setSelected(false);
            bookTv.setSelected(false);
            articleTv.setSelected(true);
            wenGaoTv.setSelected(false);
            shuodianTvLay.setVisibility(View.GONE);
            zuoyeLay.setVisibility(View.GONE);
            shujiLay.setVisibility(View.GONE);
            jiehuoLay.setVisibility(View.VISIBLE);
            tiwenBtn.setVisibility(View.VISIBLE);
            zuoyeBtn.setVisibility(View.GONE);
//            jzvdStd.setVisibility(View.VISIBLE);
//            audioLay.setVisibility(View.GONE);
//            circlrImg.clearAnimation();
//
//            if (mediaPlayer!=null){
//                musicStartStopBtn.setSelected(false);
//
//                if (mediaPlayer.isPlaying()){
//                    mediaPlayer.pause();
//                }
//            }

            bookImg.setVisibility(View.INVISIBLE);
            zuoyeImg.setVisibility(View.INVISIBLE);
            articleImg.setVisibility(View.VISIBLE);
            wenGaoImg.setVisibility(View.INVISIBLE);

        }else if (view==musicStartStopBtn){
            yinpingDialog=null;
            Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
            LinearInterpolator lin = new LinearInterpolator();
            rotate.setInterpolator(lin);
            if (musicStartStopBtn.isSelected()){
                musicStartStopBtn.setSelected(false);
                musicStartStopBtn.setBackgroundResource(R.drawable.audio_play_p);
                mediaPlayer.pause();
                circlrImg.clearAnimation();
            }else {
                if (isPreparedSuccess){
                    musicStartStopBtn.setSelected(true);
                    musicStartStopBtn.setBackgroundResource(R.drawable.audio_zt_p);
                    mediaPlayer.start();
                    if (rotate != null) {
                        circlrImg.startAnimation(rotate);
                    }  else {
                        circlrImg.setAnimation(rotate);
                        circlrImg.startAnimation(rotate);
                    }
                }else {
                    Toast.makeText(DetailControlActivity.this,"正在加载中，请稍后",Toast.LENGTH_SHORT).show();
                }


            }
        }else if (view==bookTv){
            webView.setVisibility(View.GONE);

            zuoyeTv.setSelected(false);
            bookTv.setSelected(true);
            articleTv.setSelected(false);
            wenGaoTv.setSelected(false);
            tiwenBtn.setVisibility(View.GONE);
            shuodianTvLay.setVisibility(View.VISIBLE);

            zuoyeLay.setVisibility(View.GONE);
            shujiLay.setVisibility(View.VISIBLE);
            jiehuoLay.setVisibility(View.GONE);
            zuoyeBtn.setVisibility(View.GONE);

            bookImg.setVisibility(View.VISIBLE);
            zuoyeImg.setVisibility(View.INVISIBLE);
            articleImg.setVisibility(View.INVISIBLE);
            wenGaoImg.setVisibility(View.INVISIBLE);



        }else if (view==zuoyeTv){
            User user=SharedPreferencesUtil.getLoginUser(DetailControlActivity.this);

            if (user==null){
                Toast.makeText(getApplicationContext(),"请先进行登录",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(DetailControlActivity.this, LoginIndexActivity.class);
                startActivity(intent);
                finish();
                return;
            }

            webView.setVisibility(View.GONE);
            zuoyeTv.setSelected(true);
            bookTv.setSelected(false);
            articleTv.setSelected(false);
            wenGaoTv.setSelected(false);

            tiwenBtn.setVisibility(View.GONE);
            shuodianTvLay.setVisibility(View.GONE);

            zuoyeLay.setVisibility(View.VISIBLE);
            shujiLay.setVisibility(View.GONE);
            jiehuoLay.setVisibility(View.GONE);
            zuoyeBtn.setVisibility(View.VISIBLE);

            bookImg.setVisibility(View.INVISIBLE);
            zuoyeImg.setVisibility(View.VISIBLE);
            articleImg.setVisibility(View.INVISIBLE);
            wenGaoImg.setVisibility(View.INVISIBLE);

        }else if (view==wenGaoTv){
            webView.setVisibility(View.VISIBLE);
            tiwenBtn.setVisibility(View.GONE);
            zuoyeBtn.setVisibility(View.GONE);


            zuoyeTv.setSelected(false);
            bookTv.setSelected(false);
            articleTv.setSelected(false);
            wenGaoTv.setSelected(true);

            zuoyeLay.setVisibility(View.GONE);
            shujiLay.setVisibility(View.GONE);
            jiehuoLay.setVisibility(View.GONE);

            bookImg.setVisibility(View.INVISIBLE);
            zuoyeImg.setVisibility(View.INVISIBLE);
            articleImg.setVisibility(View.INVISIBLE);
            wenGaoImg.setVisibility(View.VISIBLE);
        }else if (view==zuoyeBtn){
            User user=SharedPreferencesUtil.getLoginUser(DetailControlActivity.this);

            if (user==null){
                Toast.makeText(getApplicationContext(),"请先进行登录",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(DetailControlActivity.this, LoginIndexActivity.class);
                startActivity(intent);
                finish();
                return;
            }
            Intent intent=new Intent(DetailControlActivity.this,ZuoyeActivity.class);
            intent.putExtra("id",id);
            startActivity(intent);
        }else if (view==hongbaoLay){
            User user=SharedPreferencesUtil.getLoginUser(DetailControlActivity.this);

            if (user==null){
                Toast.makeText(getApplicationContext(),"请先进行登录",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(DetailControlActivity.this, LoginIndexActivity.class);
                startActivity(intent);
                finish();
                return;
            }
            HongbaoDialog hongbaoDialog=new HongbaoDialog(this, new HongbaoDialog.OnDialogButtonClickListener() {
                @Override
                public void okButtonClick(float money) {
                    showDashangDialog(money);

                }


            });
            hongbaoDialog.show();
        }else if (view==xiezuoyeLay){
            User user=SharedPreferencesUtil.getLoginUser(DetailControlActivity.this);

            if (user==null){
                Toast.makeText(getApplicationContext(),"请先进行登录",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(DetailControlActivity.this, LoginIndexActivity.class);
                startActivity(intent);
                finish();
                return;
            }
            Intent intent=new Intent(DetailControlActivity.this,ZuoyeActivity.class);
            intent.putExtra("id",id);
            startActivity(intent);
        }else if (view==left15Btn){
            //快退
            if (seekBar.getProgress()<=15*1000){
                mediaPlayer.seekTo(0);
            }else {
                mediaPlayer.seekTo(seekBar.getProgress()-15*1000);
            }
        }else if (view==right15Btn){
            if (seekBar.getProgress()+15*1000>=seekBar.getMax()){
                mediaPlayer.seekTo(seekBar.getMax());
            }else {
                mediaPlayer.seekTo(seekBar.getProgress()+15*1000);
            }
        }else if (view==shareBtn){
            String webUrl= UrlConstant.LEARN_INFO+"?id="+id+"&sharefrom=android";
            String imgUrl=DetailControlActivity.this.thumb;;
            String sTitle=DetailControlActivity.this.title;
            String description=DetailControlActivity.this.description;

            System.out.println("imgUrl:"+imgUrl);
            showShareDialog(webUrl,imgUrl,sTitle,description);

//            Jzvd.CURRENT_JZVD.mediaInterface.pause();

        }else if (view==kaitongBtn){
            User user=SharedPreferencesUtil.getLoginUser(DetailControlActivity.this);

            if (user==null){
                Toast.makeText(getApplicationContext(),"请先进行登录",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(DetailControlActivity.this, LoginIndexActivity.class);
                startActivity(intent);
                finish();
                return;
            }
            weixin_zhifubaoPay();
        }else if (view==beishuLay||beishuTv2==view){
            showBeisuDialog();
        }else if (view==viptips){
            User user=SharedPreferencesUtil.getLoginUser(DetailControlActivity.this);

            if (user==null){
                Toast.makeText(getApplicationContext(),"请先进行登录",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(DetailControlActivity.this, LoginIndexActivity.class);
                startActivity(intent);
                finish();
                return;
            }
            Intent  intent=new Intent(DetailControlActivity.this, WebActivity.class);
            intent.putExtra("url", UrlConstant.SHENGJI_URL);
            startActivity(intent);
        }else if (view==buyBtn){
            Intent intent=new Intent(DetailControlActivity.this, WebActivity.class);
            intent.putExtra("url", goodUrl);
            startActivity(intent);
        }
    }



    /**
     * 打赏支付对话框
     */
    private void showDashangDialog(float money) {
        Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_weixin_zhifubao_pay, null);
        LinearLayout weixinZhifuLay=contentView.findViewById(R.id.weixinZhifuLay);
        LinearLayout zhifubaoZhifuLay=contentView.findViewById(R.id.zhifubaoZhifuLay);
        weixinZhifuLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpInterface.learn_shang(SharedPreferencesUtil.getToken(DetailControlActivity.this), id+"",null,money+"", "weixin", new OkHttpUtils.MyNetCall() {
                    @Override
                    public void success(Call call, String response) throws IOException {
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            int status = responseJson.getInt("status");
                            String info=responseJson.getString("info");
                            if (status == 1) {
                                JSONObject payJson = responseJson.getJSONObject("data");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            IWXAPI api = WXAPIFactory.createWXAPI(DetailControlActivity.this, Configs.APP_ID4WX);
                                            PayReq payReq=new PayReq();
                                            payReq.appId=payJson.getString("appid");
                                            payReq.partnerId=payJson.getString("partnerid");
                                            payReq.prepayId=payJson.getString("prepayid");
                                            payReq.nonceStr=payJson.getString("noncestr");
                                            payReq.timeStamp=payJson.getString("timestamp");
                                            payReq.sign=payJson.getString("sign");
                                            payReq.packageValue="Sign=WXPay";
                                            api.sendReq(payReq);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
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
                bottomDialog.dismiss();

            }
        });
        zhifubaoZhifuLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpInterface.learn_shang(SharedPreferencesUtil.getToken(DetailControlActivity.this), id+"",null,money+"", "alipay", new OkHttpUtils.MyNetCall() {
                    @Override
                    public void success(Call call, String response) throws IOException {
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            int status = responseJson.getInt("status");
                            String info=responseJson.getString("info");
                            if (status == 1) {
                                JSONObject payJson = responseJson.getJSONObject("data");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String param=payJson.getString("param");
                                            final String orderInfo = param;   // 订单信息

                                            Runnable payRunnable = new Runnable() {

                                                @Override
                                                public void run() {
                                                    PayTask alipay = new PayTask(DetailControlActivity.this);
                                                    Map<String,String> result = alipay.payV2(orderInfo,true);

                                                    Message msg = new Message();
                                                    msg.what = MESSAGE_ZHIFUBAO_RESULT;
                                                    msg.obj = result;
                                                    UIhandle.sendMessage(msg);
                                                }
                                            };
                                            // 必须异步调用
                                            Thread payThread = new Thread(payRunnable);
                                            payThread.start();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
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
                bottomDialog.dismiss();
            }
        });



        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }

    /**
     * 购买会员支付对话框
     */
    private void showBuyVipDialog(User user) {
        Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_weixin_zhifubao_pay, null);
        LinearLayout weixinZhifuLay=contentView.findViewById(R.id.weixinZhifuLay);
        LinearLayout zhifubaoZhifuLay=contentView.findViewById(R.id.zhifubaoZhifuLay);
        weixinZhifuLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpInterface.user_buyvip(user.getAccess_token(), "1","weixin", new OkHttpUtils.MyNetCall() {
                    @Override
                    public void success(Call call, String response) throws IOException {
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            int status = responseJson.getInt("status");
                            if (status == 1) {
                                JSONObject payJson = responseJson.getJSONObject("data");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            IWXAPI api = WXAPIFactory.createWXAPI(DetailControlActivity.this, Configs.APP_ID4WX);
                                            PayReq payReq=new PayReq();
                                            payReq.appId=payJson.getString("appid");
                                            payReq.partnerId=payJson.getString("partnerid");
                                            payReq.prepayId=payJson.getString("prepayid");
                                            payReq.nonceStr=payJson.getString("noncestr");
                                            payReq.timeStamp=payJson.getString("timestamp");
                                            payReq.sign=payJson.getString("sign");
                                            payReq.packageValue="Sign=WXPay";
                                            api.sendReq(payReq);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
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

                bottomDialog.dismiss();

            }
        });
        zhifubaoZhifuLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpInterface.user_buyvip(user.getAccess_token(), "1","alipay", new OkHttpUtils.MyNetCall() {
                    @Override
                    public void success(Call call, String response) throws IOException {
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            int status = responseJson.getInt("status");
                            if (status == 1) {
                                JSONObject payJson = responseJson.getJSONObject("data");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            String param=payJson.getString("param");
                                            final String orderInfo = param;   // 订单信息

                                            Runnable payRunnable = new Runnable() {

                                                @Override
                                                public void run() {
                                                    PayTask alipay = new PayTask(DetailControlActivity.this);
                                                    Map<String,String> result = alipay.payV2(orderInfo,true);

                                                    Message msg = new Message();
                                                    msg.what = MESSAGE_ZHIFUBAO_RESULT;
                                                    msg.obj = result;
                                                    UIhandle.sendMessage(msg);
                                                }
                                            };
                                            // 必须异步调用
                                            Thread payThread = new Thread(payRunnable);
                                            payThread.start();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
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

                bottomDialog.dismiss();
            }
        });



        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }


    /**
     * 分享对话框
     */
    private void showShareDialog(String webUrl,String imgUrl,String sTitle,String description) {
        Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_share, null);
        LinearLayout haoyouLay=contentView.findViewById(R.id.haoyouLay);
        LinearLayout pengyouquanLay=contentView.findViewById(R.id.pengyouquanLay);
        haoyouLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWebpage(webUrl,imgUrl,sTitle,description,0);
            }
        });
        pengyouquanLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWebpage(webUrl,imgUrl,sTitle,description,1);
                bottomDialog.dismiss();
            }
        });



        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }



    public void shareWebpage(String webUrl,String imgUrl,String sTitle,String description,int scene) {
        IWXAPI 	api = WXAPIFactory.createWXAPI(DetailControlActivity.this, Configs.APP_ID4WX);

        AlertDialog.Builder builder = new AlertDialog.Builder(DetailControlActivity.this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("分享至");
        final String[] cities = { "微信朋友圈", "微信好友" };
        RequestManager manager =Glide.with(DetailControlActivity.this);


        Glide.with(DetailControlActivity.this).load(imgUrl).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                Bitmap finalShareBitMap = BitmapUtil.DrawableToBitmap(resource);
                //初始化一个WXMusicObject，填写url
                WXWebpageObject webpage = new WXWebpageObject ();
                webpage.webpageUrl =webUrl;      //音频网页的 URL 地址

//用 WXMusicObject 对象初始化一个 WXMediaMessage 对象
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = sTitle;
                msg.description = description;

                //设置音乐缩略图
//                msg.thumbData = Util.bmpToByteArray(finalShareBitMap, true);
                msg.thumbData = BitmapUtil.bmpToByteArray(finalShareBitMap,32);

                //构造一个Req
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = msg;
                if (scene==0){
                    req.scene = SendMessageToWX.Req.WXSceneSession;;

                }else {
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;;

                }
                //  req.userOpenId = getOpenId();
                //调用api接口，发送数据到微信
                api.sendReq(req);
            }
        });







    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }


    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }



//    public class ShuyouquanAdapter extends  RecyclerView.Adapter<ShuyouquanAdapter.ViewHolder>{
//        private JSONArray list;
//        private Context ctx;
//        private LayoutInflater mInflater;
//        public interface ForecastAdapterOnClickHandler {
//            void onClick(String weatherForDay);
//        }
//
//        public ShuyouquanAdapter(Context context, JSONArray list) {
//            ctx = context;
//            this.list = list;
//            mInflater = LayoutInflater.from(context);
//        }
////
////        private ShuyouquanAdapter.OnMyItemClickListener listener;
////        public void setOnMyItemClickListener(ShuyouquanAdapter.OnMyItemClickListener listener){
////            this.listener = listener;
////
////        }
//        public   interface OnMyItemClickListener{
//            public void onItemClick(View v,int pos);
//        }
//        private OnMyItemClickListener mOnItemClickListener;
//
//        public void setOnItemClickListener(OnMyItemClickListener mOnItemClickListener) {
//            this.mOnItemClickListener = mOnItemClickListener;
//        }
//
//
//        @NonNull
//        @Override
//        public ShuyouquanAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yinping_pinglun,parent,false);
//            ShuyouquanAdapter.ViewHolder viewHolder = new ShuyouquanAdapter.ViewHolder(convertView);
//
////            convertView.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    shuyouReplayLay.setVisibility(View.VISIBLE);
////                    sreplyContentEt.setText("");
////
////
////                    JSONObject data= (JSONObject) parent.getItemAtPosition(position);
////                    try {
////                        String id = data.getString("id");
////                        String content = data.getString("content");
////                        String uid = data.getString("uid");
////                        shuyouReplayLay.setTag(id);
////                        System.out.println("pid211111111111:"+uid);
////                        sreplyWentiTv.setText(content);
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////                }
////            });
//
//            return viewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ShuyouquanAdapter.ViewHolder viewHolder, int position) {
//            if(mOnItemClickListener != null){
//                //为ItemView设置监听器
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int position = viewHolder.getLayoutPosition(); // 1
//                        mOnItemClickListener.onItemClick(viewHolder.itemView,position); // 2
//                    }
//                });
//            }
//
////            if (listener!=null) {
////                viewHolder.yinpingLay.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        listener.myClick(v,position);
////                    }
////                });
////
////
////                // set LongClick
////                viewHolder.yinpingLay.setOnLongClickListener(new View.OnLongClickListener() {
////                    @Override
////                    public boolean onLongClick(View v) {
////                        listener.mLongClick(v,position);
////                        return true;
////                    }
////                });
////            }
//            try {
//                JSONObject data = list.getJSONObject(position);
//                String id = data.getString("id");
//                String content = data.getString("content");
//                String uid = data.getString("uid");
//                int reply_num = data.getInt("reply_num");
//                int lovenum = data.getInt("lovenum");
//                String type = data.getString("type");
//                String nickname = data.getString("nickname");
//                String avatar = data.getString("avatar");
//                String time = data.getString("time");
//                String learn_rank = data.getString("learn_rank");
//                int is_love = data.getInt("is_love");
//                int level=Integer.parseInt(data.getString("level"));
//
//                JiehuoReplyAdapter myAdapter;
//                JSONArray reply;
//                if (reply_num!=0){
//                    reply=data.getJSONArray("reply");
//                    myAdapter=new JiehuoReplyAdapter(DetailControlActivity.this,reply);
//                    viewHolder.replyListView.setAdapter(myAdapter);
//                }else {
//                    reply=new JSONArray();
//                    myAdapter=new JiehuoReplyAdapter(DetailControlActivity.this,reply);
//                    viewHolder.replyListView.setAdapter(myAdapter);
//                }
//
//                if (level>0){
//                    viewHolder.vipImg.setVisibility(View.VISIBLE);
//                }else {
//                    viewHolder.vipImg.setVisibility(View.GONE);
//                }
//                if (type!=null&&type.equals("shang")){
//                    viewHolder.contentTv.setTextColor(Color.RED);
//                }else {
//                    viewHolder.contentTv.setTextColor(0xff444444);
//                }
//                viewHolder.nameTv.setText(nickname);
//                viewHolder.dengjiTv.setText(learn_rank);
//                viewHolder.contentTv.setText(content);
//                viewHolder.dianZanTv.setText(lovenum+"");
//                viewHolder.dianZanImg.setSelected(is_love==0?false:true);
//                viewHolder.dianZanTv.setTextColor(is_love==0?0xFF838383:0xFFff6868);
//                viewHolder.timeTv.setText(time);
//                Glide.with(DetailControlActivity.this).load(avatar).into(viewHolder.headImg);
//
//                ViewHolder finalViewHolder = viewHolder;
//                viewHolder.headImg.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent=new Intent(DetailControlActivity.this,WebActivity.class);
//                        intent.putExtra("url",UrlConstant.ZHUYE_URL+"?uid="+uid);
//                        startActivity(intent);
//                    }
//                });
//                viewHolder.dianZanImg.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        httpInterface.learn_comment(SharedPreferencesUtil.getToken(DetailControlActivity.this), "love", id, new OkHttpUtils.MyNetCall() {
//                            @Override
//                            public void success(Call call, String response) throws IOException {
//                                try {
//                                    JSONObject responseJson = new JSONObject(response);
//                                    int status = responseJson.getInt("status");
//                                    String info=responseJson.getString("info");
//                                    if (status == 1) {
//                                        JSONObject data = responseJson.getJSONObject("data");
//                                        int is_add=data.getInt("is_add");
//                                        int num=data.getInt("num");
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                finalViewHolder.dianZanTv.setText(num+"");
//                                                finalViewHolder.dianZanImg.setSelected(is_add==1?true:false);
//                                                finalViewHolder.dianZanTv.setTextColor(is_add==0?0xFF838383:0xFFff6868);
//                                                Toast.makeText(DetailControlActivity.this,info,Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    }
//                                }catch (JSONException e){
//
//                                }
//                            }
//
//                            @Override
//                            public void failed(Call call, IOException e) {
//
//                            }
//                        });
//                    }
//                });
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//
//        @Override
//        public int getItemCount() {
//            return list.length();
//        }
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            public ImageView headImg;
//            public TextView nameTv;
//            public TextView dengjiTv;
//            public TextView contentTv, timeTv;
//            private ImageView dianZanImg;
//            private TextView dianZanTv;
//            private ImageView vipImg;
//            private ListView replyListView;
//            public ViewHolder (View view)
//            {
//                super(view);
//                headImg = view.findViewById(R.id.headImg);
//                nameTv = view.findViewById(R.id.nameTv);
//                dengjiTv = view.findViewById(R.id.dengjiTv);
//                contentTv = view.findViewById(R.id.contentTv);
//                timeTv = view.findViewById(R.id.timeTv);
//                dianZanImg=view.findViewById(R.id.dianZanImg);
//                dianZanTv=view.findViewById(R.id.dianzanTv);
//                vipImg=view.findViewById(R.id.vipImg);
//                replyListView=view.findViewById(R.id.replyListView);
//            }
//        }
//
//    }


    /**
     * 书友圈
     */
//    public class ShuyouquanAdapter extends BaseAdapter {
//
//        private JSONArray list;
//        private Context ctx;
//        private LayoutInflater mInflater;
//
//        public ShuyouquanAdapter(Context context, JSONArray list) {
//            ctx = context;
//            this.list = list;
//            mInflater = LayoutInflater.from(context);
//        }
//
//        @Override
//        public int getCount() {
//            // TODO Auto-generated method stub
//            return list.length();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            // TODO Auto-generated method stub
//            try {
//                return list.getJSONObject(position);
//            } catch (JSONException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        @Override
//        public long getItemId(int position) {
//            // TODO Auto-generated method stub
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            // TODO Auto-generated method stub
//            ShuyouquanAdapter.ViewHolder viewHolder = null;
//            if (convertView == null) {
//                convertView = mInflater.inflate(R.layout.item_yinping_pinglun, null);
//                viewHolder = new ShuyouquanAdapter.ViewHolder();
//                viewHolder.headImg = convertView.findViewById(R.id.headImg);
//                viewHolder.vipImg = convertView.findViewById(R.id.vipImg);
//
//
//                viewHolder.nameTv = convertView.findViewById(R.id.nameTv);
//                viewHolder.dengjiTv = convertView.findViewById(R.id.dengjiTv);
//                viewHolder.contentTv = convertView.findViewById(R.id.contentTv);
//                viewHolder.timeTv = convertView.findViewById(R.id.timeTv);
//                viewHolder.dianZanImg = convertView.findViewById(R.id.dianZanImg);
//                viewHolder.dianZanTv = convertView.findViewById(R.id.dianzanTv);
//                viewHolder.replyListView = convertView.findViewById(R.id.replyListView);
//
//
//                viewHolder.replyListView.setFocusable(false);
//                viewHolder.replyListView.setClickable(false);
//                viewHolder.replyListView.setPressed(false);
//                viewHolder.replyListView.setEnabled(false);
//
//                convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (ShuyouquanAdapter.ViewHolder) convertView.getTag();
//            }
//            try {
//                JSONObject data = list.getJSONObject(position);
//                String id = data.getString("id");
//                String content = data.getString("content");
//                String uid = data.getString("uid");
//                int reply_num = data.getInt("reply_num");
//                int lovenum = data.getInt("lovenum");
//                String type = data.getString("type");
//                String nickname = data.getString("nickname");
//                String avatar = data.getString("avatar");
//                String time = data.getString("time");
//                String learn_rank = data.getString("learn_rank");
//                int is_love = data.getInt("is_love");
//                int level=Integer.parseInt(data.getString("level"));
//
//                JiehuoReplyAdapter myAdapter;
//                JSONArray reply;
//                if (reply_num!=0){
//                    reply=data.getJSONArray("reply");
//                    myAdapter=new JiehuoReplyAdapter(DetailControlActivity.this,reply);
//                    viewHolder.replyListView.setAdapter(myAdapter);
//                }else {
//                    reply=new JSONArray();
//                    myAdapter=new JiehuoReplyAdapter(DetailControlActivity.this,reply);
//                    viewHolder.replyListView.setAdapter(myAdapter);
//                }
//
//                if (level>0){
//                    viewHolder.vipImg.setVisibility(View.VISIBLE);
//                }else {
//                    viewHolder.vipImg.setVisibility(View.GONE);
//                }
//                if (type!=null&&type.equals("shang")){
//                    viewHolder.contentTv.setTextColor(Color.RED);
//                }else {
//                    viewHolder.contentTv.setTextColor(0xff444444);
//                }
//                viewHolder.nameTv.setText(nickname);
//                viewHolder.dengjiTv.setText(learn_rank);
//                viewHolder.contentTv.setText(content);
//                viewHolder.dianZanTv.setText(lovenum+"");
//                viewHolder.dianZanImg.setSelected(is_love==0?false:true);
//                viewHolder.dianZanTv.setTextColor(is_love==0?0xFF838383:0xFFf6868);
//                viewHolder.timeTv.setText(time);
//                Glide.with(DetailControlActivity.this).load(avatar).into(viewHolder.headImg);
//
//                ViewHolder finalViewHolder = viewHolder;
//                viewHolder.headImg.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent=new Intent(DetailControlActivity.this,WebActivity.class);
//                        intent.putExtra("url",UrlConstant.ZHUYE_URL+"?uid="+uid);
//                        startActivity(intent);
//                    }
//                });
//                viewHolder.dianZanImg.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        httpInterface.learn_comment(SharedPreferencesUtil.getToken(DetailControlActivity.this), "love", id, new OkHttpUtils.MyNetCall() {
//                            @Override
//                            public void success(Call call, String response) throws IOException {
//                                try {
//                                    JSONObject responseJson = new JSONObject(response);
//                                    int status = responseJson.getInt("status");
//                                    String info=responseJson.getString("info");
//                                    if (status == 1) {
//                                        JSONObject data = responseJson.getJSONObject("data");
//                                        int is_add=data.getInt("is_add");
//                                        int num=data.getInt("num");
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                finalViewHolder.dianZanTv.setText(num+"");
//                                                finalViewHolder.dianZanImg.setSelected(is_add==1?true:false);
//                                                finalViewHolder.dianZanTv.setTextColor(is_add==0?0xFF838383:0xFFff6868);
//                                                Toast.makeText(DetailControlActivity.this,info,Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    }
//                                }catch (JSONException e){
//
//                                }
//                            }
//
//                            @Override
//                            public void failed(Call call, IOException e) {
//
//                            }
//                        });
//                    }
//                });
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return convertView;
//        }
//
//        class ViewHolder {
//            public ImageView headImg;
//            public TextView nameTv;
//            public TextView dengjiTv;
//            public TextView contentTv, timeTv;
//            private ImageView dianZanImg;
//            private TextView dianZanTv;
//            private ImageView vipImg;
//            private ListView replyListView;
//        }
//
//
//    }
    /**
     * 作业
     */
    public class ZuoyeAdapter extends BaseAdapter {

        private JSONArray list;
        private Context ctx;
        private LayoutInflater mInflater;

        public ZuoyeAdapter(Context context, JSONArray list) {
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
            ZuoyeAdapter.ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_yinping_zuoye, null);
                viewHolder = new ZuoyeAdapter.ViewHolder();
                viewHolder.headImg = convertView.findViewById(R.id.headImg);
                viewHolder.nameTv = convertView.findViewById(R.id.nameTv);
                viewHolder.timeTv = convertView.findViewById(R.id.timeTv);
                viewHolder.title1Tv = convertView.findViewById(R.id.content1Tv);
                viewHolder.title2Tv = convertView.findViewById(R.id.content2Tv);
                viewHolder.title3Tv = convertView.findViewById(R.id.content3Tv);
                viewHolder.zan_numTv = convertView.findViewById(R.id.dianzanCountTv);
                viewHolder.view_numTv = convertView.findViewById(R.id.viewsTv);
                viewHolder.cmt_numTv=convertView.findViewById(R.id.cmt_num);
                viewHolder.viewsLay=convertView.findViewById(R.id.viewsLay);
                viewHolder.contentLay=convertView.findViewById(R.id.contentLay);
                viewHolder.dianzanLay=convertView.findViewById(R.id.dianzan2Lay);
                viewHolder.dianZanImg=convertView.findViewById(R.id.dianZanImg);
                viewHolder.vipImg=convertView.findViewById(R.id.vipImg);



                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ZuoyeAdapter.ViewHolder) convertView.getTag();
            }
            try {
                JSONObject data = list.getJSONObject(position);
                String id = data.getString("id");
                String uid = data.getString("uid");

                String nickname = data.getString("nickname");
                String avatar = data.getString("avatar");
                String time = data.getString("time");
                String title1 = data.getString("title1");
                String title2 = data.getString("note");
                String title3 = data.getString("resolve");
                int zan_num = data.getInt("zan_num");
                int cmt_num = data.getInt("cmt_num");
                int view_num = data.getInt("view_num");
                int is_zan = data.getInt("is_zan");
                int level=Integer.parseInt(data.getString("level"));

                if (level>0){
                    viewHolder.vipImg.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.vipImg.setVisibility(View.GONE);
                }

                viewHolder.nameTv.setText(nickname);
                viewHolder.timeTv.setText(time);
                viewHolder.title1Tv.setText("作业："+title1);
                viewHolder.title2Tv.setText("笔记："+title2);
                viewHolder.title3Tv.setText("决定："+title3);

                viewHolder.zan_numTv.setText(zan_num+"");
                viewHolder.view_numTv.setText(view_num+"");
                viewHolder.cmt_numTv.setText(cmt_num+"");
                viewHolder.dianZanImg.setSelected(is_zan==0?false:true);

                Glide.with(DetailControlActivity.this).load(avatar).into(viewHolder.headImg);
                viewHolder.headImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(DetailControlActivity.this,WebActivity.class);
                        intent.putExtra("url",UrlConstant.ZHUYE_URL+"?uid="+uid);
                        startActivity(intent);
                    }
                });
                viewHolder.viewsLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(DetailControlActivity.this, WebActivity.class);
                        intent.putExtra("url", UrlConstant.ZUOYE_DETAIL_URL+"?id="+id);
                        startActivity(intent);
                    }
                });
                viewHolder.contentLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(DetailControlActivity.this, WebActivity.class);
                        intent.putExtra("url", UrlConstant.ZUOYE_DETAIL_URL+"?id="+id);
                        startActivity(intent);
                    }
                });
                ViewHolder finalViewHolder = viewHolder;
                viewHolder.dianzanLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        httpInterface.learn_book_work(SharedPreferencesUtil.getToken(DetailControlActivity.this), "zan", id, new OkHttpUtils.MyNetCall() {
                            @Override
                            public void success(Call call, String response) throws IOException {
                                try {
                                    JSONObject responseJson = new JSONObject(response);
                                    int status = responseJson.getInt("status");
                                    String info=responseJson.getString("info");
                                    if (status == 0) {

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                int zan_num=Integer.parseInt( finalViewHolder.zan_numTv.getText().toString());
                                                if (info!=null&&info.equals("点赞成功")){
                                                    finalViewHolder.zan_numTv.setText(zan_num+1+"");
                                                    finalViewHolder.dianZanImg.setSelected(true);
                                                }else {
                                                    finalViewHolder.zan_numTv.setText(zan_num-1+"");
                                                    finalViewHolder.dianZanImg.setSelected(false);
                                                }

                                                Toast.makeText(DetailControlActivity.this,info,Toast.LENGTH_SHORT).show();
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
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return convertView;
        }

        class ViewHolder {
            public ImageView headImg;
            public TextView nameTv;
            public TextView  timeTv;
            private TextView title1Tv,title2Tv,title3Tv;
            public TextView  zan_numTv,view_numTv,cmt_numTv;
            private LinearLayout viewsLay,contentLay,dianzanLay;
            private ImageView dianZanImg;
            private ImageView vipImg;

        }


    }



    /**
     * 解惑
     */
    public class JiehuoAdapter extends BaseAdapter {

        private JSONArray list;
        private Context ctx;
        private LayoutInflater mInflater;


        public JiehuoAdapter(Context context, JSONArray list) {
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
            JiehuoAdapter.ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_jiehuo, null);
                viewHolder = new JiehuoAdapter.ViewHolder();
                viewHolder.headImg = convertView.findViewById(R.id.headImg);
                viewHolder.nameTv = convertView.findViewById(R.id.nameTv);
                viewHolder.timeTv = convertView.findViewById(R.id.timeTv);
                viewHolder.askTv = convertView.findViewById(R.id.askTv);
                viewHolder.replyListView = convertView.findViewById(R.id.replyListView);
                viewHolder.replyNumTv = convertView.findViewById(R.id.replyNumTv);
                viewHolder.replyAllTv = convertView.findViewById(R.id.replyAllTv);
                viewHolder.bianjiLay=convertView.findViewById(R.id.bianjiLay);

                viewHolder.dianzanTv = convertView.findViewById(R.id.dianzanTv);
                viewHolder.dianzanLay = convertView.findViewById(R.id.dianzanLay);
                viewHolder.dianZanImg = convertView.findViewById(R.id.dianZanImg);


                convertView.setTag(viewHolder);
            } else {
                viewHolder = (JiehuoAdapter.ViewHolder) convertView.getTag();
            }
            try {
                JSONObject data = list.getJSONObject(position);
                String id = data.getString("id");
                String uid = data.getString("uid");

                String content = data.getString("content");
                String add_time = data.getString("add_time");
                String zan_num = data.getString("zan_num");
                String avatar = data.getString("avatar");
                String nickname = data.getString("nickname");
                String level = data.getString("level");
                String time = data.getString("time");
                int is_love = data.getInt("is_love");
                int reply_num = data.getInt("reply_num");
                JiehuoReplyAdapter myAdapter;
                JSONArray reply;
                if (reply_num!=0){
                    reply=data.getJSONArray("reply");
                    myAdapter=new JiehuoReplyAdapter(DetailControlActivity.this,reply);
                    viewHolder.replyListView.setAdapter(myAdapter);
                }else {
                    reply=new JSONArray();
                    myAdapter=new JiehuoReplyAdapter(DetailControlActivity.this,reply);
                    viewHolder.replyListView.setAdapter(myAdapter);
                }


                if (reply_num>1){
                    viewHolder.replyAllTv.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.replyAllTv.setVisibility(View.GONE);
                }
                viewHolder.headImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(DetailControlActivity.this,WebActivity.class);
                        intent.putExtra("url",UrlConstant.ZHUYE_URL+"?uid="+uid);
                        startActivity(intent);
                    }
                });

                ViewHolder finalViewHolder = viewHolder;
                viewHolder.replyAllTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finalViewHolder.replyAllTv.setText("");
                        HttpInterface httpInterface = new HttpImplement();
                        httpInterface.learn_book_ask(SharedPreferencesUtil.getToken(DetailControlActivity.this), "answer",id,"1", new OkHttpUtils.MyNetCall() {
                            @Override
                            public void success(Call call, String response) throws IOException {
                                try {
                                    JSONObject responseJson = new JSONObject(response);
                                    int status = responseJson.getInt("status");
                                    if (status == 1) {
                                        JSONArray data = responseJson.getJSONArray("data");
                                        for (int i=0;i<data.length();i++){
                                            JSONObject value=data.getJSONObject(i);
                                            reply.put(value);
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                myAdapter.notifyDataSetChanged();
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
                });


                viewHolder.replyNumTv.setText(reply_num+"人进行了回答");
                viewHolder.nameTv.setText(nickname);
                viewHolder.timeTv.setText(time);
                viewHolder.timeTv.setText("提了一个问题 "+time);
                viewHolder.askTv.setText(content);

                viewHolder.dianzanTv.setText(zan_num+"");
                viewHolder.dianZanImg.setSelected(is_love==0?false:true);
                viewHolder.dianzanTv.setTextColor(is_love==0?0xFF838383:0xFFf6868);

                viewHolder.dianZanImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        httpInterface.learn_book_askDianzan(SharedPreferencesUtil.getToken(DetailControlActivity.this), "zan", id, new OkHttpUtils.MyNetCall() {
                            @Override
                            public void success(Call call, String response) throws IOException {
                                try {
                                    JSONObject responseJson = new JSONObject(response);
                                    int status = responseJson.getInt("status");
                                    String info=responseJson.getString("info");
                                    if (status == 1) {
                                        JSONObject data = responseJson.getJSONObject("data");
                                        int num=data.getInt("zan_num");
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                finalViewHolder.dianzanTv.setText(num+"");
                                                if (info.contains("点赞成功")) {
                                                    finalViewHolder.dianZanImg.setSelected(true);
                                                    finalViewHolder.dianzanTv.setTextColor(0xFFff6868);

                                                }else {
                                                    finalViewHolder.dianZanImg.setSelected(true);
                                                    finalViewHolder.dianzanTv.setTextColor(0xFF838383);
                                                }

                                                Toast.makeText(DetailControlActivity.this,info,Toast.LENGTH_SHORT).show();
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
                });


                Glide.with(DetailControlActivity.this).load(avatar).into(viewHolder.headImg);
                viewHolder.bianjiLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //回答问题
                        tiwenBtn.setVisibility(View.GONE);

                        replyLay.setVisibility(View.VISIBLE);
                        replyLay.setTag(id);


                        replyWentiTv.setText("问题:"+content);
                    }
                });



            } catch (JSONException e) {
                e.printStackTrace();
            }


            return convertView;
        }

        class ViewHolder {
            public ImageView headImg;
            public TextView nameTv;
            public TextView  timeTv;
            public TextView  askTv;
            private TextView replyNumTv;
            private TextView replyAllTv;
            private LinearLayout bianjiLay;

            private TextView dianzanTv;
            private ListView replyListView;


            private  LinearLayout dianzanLay;
            private ImageView dianZanImg;

        }


    }

    /**
     * 微信支付
     */
    public void weixin_zhifubaoPay(){
        // 支付
        User user=SharedPreferencesUtil.getLoginUser(DetailControlActivity.this);
        if (user==null){
            Toast.makeText(getApplicationContext(),"请先进行登录",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(DetailControlActivity.this, LoginIndexActivity.class);
            startActivity(intent);
            finish();
         return;
        }
        showBuyVipDialog( user);
    }

    /**
     * 弹起软键盘
     * @param editText
     */
    public void openKeyBoard(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager imm =
                (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
        editText.setSelection(editText.getText().length());
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                InputMethodManager imm =
//                        (InputMethodManager)
//                                getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(editText, 0);
//                editText.setSelection(editText.getText().length());
//            }
//        }, 20);

    }
    /**
     * 收起软键盘
     */
    public  void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UIhandle.removeMessages(UPDATE_MUSIC_UI);
        unregisterReceiver(payBrocast);
    }

    /**
     * 倍速
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showBeisuDialog() {

        Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_beisu, null);
        RelativeLayout b05Lay=contentView.findViewById(R.id.b05Lay);
        RelativeLayout b075Lay=contentView.findViewById(R.id.b075Lay);
        RelativeLayout b10Lay=contentView.findViewById(R.id.b10Lay);
        RelativeLayout b125Lay=contentView.findViewById(R.id.b125Lay);
        RelativeLayout b15Lay=contentView.findViewById(R.id.b15Lay);
        RelativeLayout b20Lay=contentView.findViewById(R.id.b20Lay);

        ImageView b05Img=contentView.findViewById(R.id.b05Img);
        ImageView b075Img=contentView.findViewById(R.id.b075Img);
        ImageView b10Img=contentView.findViewById(R.id.b10Img);
        ImageView b125Img=contentView.findViewById(R.id.b125Img);
        ImageView b15Img=contentView.findViewById(R.id.b15Img);
        ImageView b20Img=contentView.findViewById(R.id.b20Img);

        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                float speed=  mediaPlayer.getPlaybackParams().getSpeed();
                if (speed==0.5f){
                    b05Img.setSelected(true);
                    b075Img.setSelected(false);
                    b10Img.setSelected(false);
                    b125Img.setSelected(false);
                    b15Img.setSelected(false);
                    b20Img.setSelected(false);
                }else  if (speed==0.75f){
                    b05Img.setSelected(false);
                    b075Img.setSelected(true);
                    b10Img.setSelected(false);
                    b125Img.setSelected(false);
                    b15Img.setSelected(false);
                    b20Img.setSelected(false);
                }else  if (speed==1.0f){
                    b05Img.setSelected(false);
                    b075Img.setSelected(false);
                    b10Img.setSelected(true);
                    b125Img.setSelected(false);
                    b15Img.setSelected(false);
                    b20Img.setSelected(false);
                }else  if (speed==1.25f){
                    b05Img.setSelected(false);
                    b075Img.setSelected(false);
                    b10Img.setSelected(false);
                    b125Img.setSelected(true);
                    b15Img.setSelected(false);
                    b20Img.setSelected(false);
                }else  if (speed==1.5f){
                    b05Img.setSelected(false);
                    b075Img.setSelected(false);
                    b10Img.setSelected(false);
                    b125Img.setSelected(false);
                    b15Img.setSelected(true);
                    b20Img.setSelected(false);
                }else  if (speed==2.0f){
                    b05Img.setSelected(false);
                    b075Img.setSelected(false);
                    b10Img.setSelected(false);
                    b125Img.setSelected(false);
                    b15Img.setSelected(false);
                    b20Img.setSelected(true);
                }
            }

        }catch (Exception e){
            b05Img.setSelected(false);
            b075Img.setSelected(false);
            b10Img.setSelected(true);
            b125Img.setSelected(false);
            b15Img.setSelected(false);
            b20Img.setSelected(false);
        }

        b05Lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float speed=0.5f;
                beishuTv.setText("0.5");
                MediaPlayerUtil.setPlaySpeed(mediaPlayer,speed);
                b05Img.setSelected(true);
                b075Img.setSelected(false);
                b10Img.setSelected(false);
                b125Img.setSelected(false);
                b15Img.setSelected(false);
                b20Img.setSelected(false);
                bottomDialog.dismiss();
            }
        });
        b075Lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float speed=0.75f;
                beishuTv.setText("0.75");
                MediaPlayerUtil.setPlaySpeed(mediaPlayer,speed);
                b05Img.setSelected(false);
                b075Img.setSelected(true);
                b10Img.setSelected(false);
                b125Img.setSelected(false);
                b15Img.setSelected(false);
                b20Img.setSelected(false);
                bottomDialog.dismiss();
            }
        });
        b10Lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float speed=1f;
                beishuTv.setText("1.0");
                MediaPlayerUtil.setPlaySpeed(mediaPlayer,speed);
                b05Img.setSelected(false);
                b075Img.setSelected(false);
                b10Img.setSelected(true);
                b125Img.setSelected(false);
                b15Img.setSelected(false);
                b20Img.setSelected(false);
                bottomDialog.dismiss();
            }
        });
        b125Lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float speed=1.25f;
                beishuTv.setText("1.25");
                MediaPlayerUtil.setPlaySpeed(mediaPlayer,speed);
                b05Img.setSelected(false);
                b075Img.setSelected(false);
                b10Img.setSelected(false);
                b125Img.setSelected(true);
                b15Img.setSelected(false);
                b20Img.setSelected(false);
                bottomDialog.dismiss();
            }
        });
        b15Lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float speed=1.5f;
                beishuTv.setText("1.5");
                MediaPlayerUtil.setPlaySpeed(mediaPlayer,speed);
                b05Img.setSelected(false);
                b075Img.setSelected(false);
                b10Img.setSelected(false);
                b125Img.setSelected(false);
                b15Img.setSelected(true);
                b20Img.setSelected(false);
                bottomDialog.dismiss();
            }
        });
        b20Lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float speed=2.0f;
                beishuTv.setText("2.0");
                MediaPlayerUtil.setPlaySpeed(mediaPlayer,speed);
                b05Img.setSelected(false);
                b075Img.setSelected(false);
                b10Img.setSelected(false);
                b125Img.setSelected(false);
                b15Img.setSelected(false);
                b20Img.setSelected(true);
                bottomDialog.dismiss();
            }
        });


        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }


}