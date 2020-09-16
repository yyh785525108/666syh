package com.yyh.read666.tab2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
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

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tchy.syh.wxapi.Util;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yyh.read666.MainActivity;
import com.yyh.read666.R;
import com.yyh.read666.WebActivity;
import com.yyh.read666.configs.Configs;
import com.yyh.read666.db.AudioSQLiteOpenHelper;
import com.yyh.read666.details.DetailControlActivity;
import com.yyh.read666.login.LoginActivity;
import com.yyh.read666.login.LoginIndexActivity;
import com.yyh.read666.music.MediaUtil;
import com.yyh.read666.music.XMPlayerReceiver;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.HttpInterface;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.okhttp.UrlConstant;
import com.yyh.read666.utils.BitmapUtil;
import com.yyh.read666.utils.DateUtil;
import com.yyh.read666.utils.HtmlUtil;
import com.yyh.read666.utils.MediaPlayerUtil;
import com.yyh.read666.utils.SharedPreferencesUtil;
import com.yyh.read666.view.HongbaoDialog;
import com.yyh.read666.view.dialog.InputTextMsgDialog;
import com.yyh.read666.view.dialog.PayMsgDialog;
import com.yyh.read666.vo.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;

public class YinpingDetailActivity extends AppCompatActivity implements View.OnClickListener  {

    private Button backBtn;

    private HttpInterface httpInterface;
    public JSONObject data;//上个页面传递下来的
    private int position;//当前音频位置
    private JSONArray album;

    private TextView titleTv;
    private ImageView circlrImg;
    private SeekBar seekBar;
    private TextView   startTimeTv,endTimeTv;
    private Button playBtn,nextBtn,prevBtn;
    private RelativeLayout audioFengmianLay;
    private ImageView userImg;
    private TextView userNameTv,dingyueTv;
    private Button dingyueBtn;
    private ImageView dianzanImg;
    private TextView dianzanTv;
    private LinearLayout kuaijin15Lay,kuaitui15Lay;
    private LinearLayout dashangLay;
    private RelativeLayout beishuLay;

//    private ScrollView scrollView;
    private LinearLayout listLay;
    private TextView shuodianTv;
    private RelativeLayout shuodianTvLay;
    private ImageView shuodianImg;

    private MediaPlayer mediaPlayer ;//音频
    private boolean isPlaying;//是否正在播放，切换下一首需要用到

    public static final int UPDATE_MUSIC_UI=100;

    public static final int UPDATE_PROGRESS=101;

    private int subId;//音频子id
    private String yinpingId;//音频id

    private Button left15Btn,right15Btn;

    private Button shareBtn;

    private String src;
    private String yinpingTile,subTile;
    String thumb ;

    private ImageView guanggaoImg;

    private ImageView backImg;

    private TextView beishuTv;
    private LinearLayout bookLay,wengaoLay;
    private TextView bookTv,wenGaoTv;
    private ImageView bookImg,wenGaoImg;
    private  String dataStr,albumStr;

    private RelativeLayout jingcaiShupingLay;
    private RelativeLayout circleLay;
    private boolean judgeCanLoadMore = true;
    private LinearLayout pubulishLay;

//    ListView listView;
    RecyclerView recyclerView;
    private WebView webView;

    private int page=0;
    private RefreshLayout refreshLayout;
    private PinglunAdapter pinglunAdapter;
    JSONArray allPinglunList;

    private boolean need2ResetMedia=true;//如果是上次播放的，并且打开过app的，进来就是false
    private PayMsgDialog payDialog;

    private boolean hasQuanxian;//当前音频是否有权限

    private PayBrocast payBrocast;

    private boolean mustJiyi;
    private static  final  int MESSAGE_ZHIFUBAO_RESULT=102;//支付宝支付结果


    private Handler UIhandle = new Handler(){

        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if(msg.what==UPDATE_MUSIC_UI) {
                System.out.println("-------------------------------------111111111111111-----:"+UIhandle);
                int position = mediaPlayer.getCurrentPosition();
                seekBar.setProgress(position);
                updateTime(startTimeTv,position);
                if (position>=(seekBar.getMax()-500)){//防止跳不过去
                    //尽头了，播下一集
                    System.out.println("------------------------------------------------------------------------------------------222222222222222222222222");

                    onClick(nextBtn);
                }else {
                    UIhandle.sendEmptyMessageDelayed(UPDATE_MUSIC_UI, 500);
                }


            }else if (msg.what==UPDATE_PROGRESS){
                //音频记忆
                int position = mediaPlayer.getCurrentPosition();
                AudioSQLiteOpenHelper mySQLiteOpenHelper= AudioSQLiteOpenHelper.getInstance(YinpingDetailActivity.this);
                mySQLiteOpenHelper.updateAudioTime2(yinpingId,subId+"",position);
                UIhandle.sendEmptyMessageDelayed(UPDATE_PROGRESS, 5*1000);
            }else if(msg.what==MESSAGE_ZHIFUBAO_RESULT){
//                Result result = new Result((String) msg.obj);
//                Toast.makeText(DetailControlActivity.this, result.getResult(),
//                        Toast.LENGTH_LONG).show();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_audio_detail2);
        //方式一：这句代码必须写在setContentView()方法的后面
        getSupportActionBar().hide();


        if ( Configs.whereFrom==1){//如果是从视频界面过来的
            Configs.whereFrom=0;
            mediaPlayer= MediaUtil.getMediaPlayer();
            if (MediaUtil.MEDIA_IS_INIT){//从没播放过

            }else {//播放过
                mediaPlayer.reset();

            }
        }
        //发个广播，把音频里边的监听去掉
        if (Configs.Music_UIhandle!=null){
            Configs.Music_UIhandle.removeMessages(YinpingDetailActivity.UPDATE_MUSIC_UI);
            Configs.Music_UIhandle.removeMessages(YinpingDetailActivity.UPDATE_PROGRESS);
        }
        Configs.Music_UIhandle=UIhandle;


        mustJiyi=getIntent().getBooleanExtra("mustJiyi",false);
        System.out.println("mustJiyi111111111111111111:"+mustJiyi);
        initView();
        dataStr=getIntent().getStringExtra("data");
        albumStr=getIntent().getStringExtra("album");
        position=getIntent().getIntExtra("position",0);

        initData(dataStr,position,albumStr);
         payDialog = new PayMsgDialog(this, R.style.dialog_center);
        payDialog.setmOnTextSendListener(new PayMsgDialog.OnTextSendListener() {
            @Override
            public void onTextSend(String msg) {
                System.out.println("msg:"+msg);
                weixinPay(yinpingId,msg);

            }
        });
        payBrocast = new PayBrocast();
        IntentFilter fil = new IntentFilter("WxPay");
        registerReceiver(payBrocast, fil);
    }

    class PayBrocast extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            System.out.println("----------------onReceive-------------:"+intent.getAction());
            if (intent.getAction().equals("WxPay")) {
                String status=intent.getStringExtra("status");
                if (status!=null&&status.equals("1")){
                    //支付成功
                    initLearn_info(subId+"");//刷新权限
                }
            }


        }
    }
    public void initView() {
        pubulishLay=findViewById(R.id.pubulishLay);
        circleLay=findViewById(R.id.circleLay);
        recyclerView=findViewById(R.id.listView);
        shuodianTv=findViewById(R.id.shuodianTv);
        shuodianTvLay=findViewById(R.id.shuodianTvLay);
        shuodianImg=findViewById(R.id.shuodianImg);
        jingcaiShupingLay=findViewById(R.id.jingcaiShupingLay);
        jingcaiShupingLay.setVisibility(View.GONE);
        bookLay=findViewById(R.id.bookLay);
        wengaoLay=findViewById(R.id.wengaoLay);

        bookTv=findViewById(R.id.bookTv);
        wenGaoTv=findViewById(R.id.wenGaoTv);
        bookImg=findViewById(R.id.bookImg);
        wenGaoImg=findViewById(R.id.wenGaoImg);


        beishuTv=findViewById(R.id.beishuTv);
        backImg=findViewById(R.id.backImg);
        guanggaoImg=findViewById(R.id.guanggaoImg);
        left15Btn=findViewById(R.id.left15Btn);
        right15Btn=findViewById(R.id.right15Btn);
        shareBtn=findViewById(R.id.shareBtn);

        listLay=findViewById(R.id.listLay);
//        scrollView=findViewById(R.id.scrollView);
//        scrollView.smoothScrollTo(0, 0);
        kuaijin15Lay=findViewById(R.id.kuaijin15Lay);
          kuaitui15Lay=findViewById(R.id.kuaitui15Lay);
        beishuLay=findViewById(R.id.beishuLay);


        dianzanImg=findViewById(R.id.dianzanImg);
        dianzanTv=findViewById(R.id.dianzanTv);
        userImg=findViewById(R.id.userImg);
        userNameTv=findViewById(R.id.userNameTv);
        dingyueTv=findViewById(R.id.dingyueTv);
        startTimeTv = findViewById(R.id.startTime);
        endTimeTv = findViewById(R.id.endTime);
        backBtn = findViewById(R.id.back);
        circlrImg =findViewById(R.id.circlrImg);
        titleTv=findViewById(R.id.titleTv);
        seekBar=findViewById(R.id.seek);
        audioFengmianLay=findViewById(R.id.audioFengmianLay);
        dingyueBtn=findViewById(R.id.dingyueBtn);
        playBtn=findViewById(R.id.play);
        nextBtn=findViewById(R.id.next);
        prevBtn=findViewById(R.id.prev);
        dashangLay=findViewById(R.id.dashangLay);
       webView=findViewById(R.id.webview);

        backBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        prevBtn.setOnClickListener(this);
        dingyueBtn.setOnClickListener(this);
        kuaijin15Lay.setOnClickListener(this);
        kuaitui15Lay.setOnClickListener(this);
        beishuLay.setOnClickListener(this);
        dashangLay.setOnClickListener(this);
        listLay.setOnClickListener(this);
        left15Btn.setOnClickListener(this);
        right15Btn.setOnClickListener(this);
        pubulishLay.setOnClickListener(this);
        refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);

        shareBtn.setOnClickListener(this);

        bookLay.setOnClickListener(this);
        wengaoLay.setOnClickListener(this);
        onClick(bookLay);

        allPinglunList =new JSONArray();
         pinglunAdapter =new PinglunAdapter(YinpingDetailActivity.this, allPinglunList);
        //必须先设置LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(YinpingDetailActivity.this));
        recyclerView.setAdapter(pinglunAdapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page=0;
                initShuyouQuan(subId +"");
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page=page+1;
                initShuyouQuan(subId +"");
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });

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

    private void initData(String dataStr,int position,String albumStr) {
        //直接先获取最近播放的记录，看是不是一致的。再判断midea的status

        SharedPreferences sp=getSharedPreferences(Configs.SHARE_NAME, Activity.MODE_PRIVATE);
        String oldData=sp.getString("data","");
        int oldPosition=sp.getInt("position",0);

        System.out.println("oldData:"+oldData);
        System.out.println("dataStr:"+dataStr);

        System.out.println("oldPosition:"+oldPosition);
        System.out.println("position:"+position);

        System.out.println("MediaUtil.media_Status:"+MediaUtil.MEDIA_IS_INIT);

        if (oldData.equals(dataStr)&&oldPosition==position&&MediaUtil.MEDIA_IS_INIT){
            //那就证明是继续播放，等会不需要重新初始化播放器
            mustJiyi=false;
            hasQuanxian=true;
            need2ResetMedia=false;
            mediaPlayer=MediaUtil.getMediaPlayer();

            initProgress();
            if (mediaPlayer.isPlaying()){
                isPlaying=false;
            }else {
                isPlaying=true;
            }
            onClick(playBtn);
        }


        try {
            data=new JSONObject(dataStr);
            System.out.println("data:"+data);

            yinpingId=data.getString("id");
            String author = data.getString("author");
            int is_album = data.getInt("is_album");
            int is_collect = data.getInt("is_collect");
            yinpingTile= data.getString("title");
            thumb = data.getString("thumb");
            String price = data.getString("price");
            String vipprice = data.getString("vipprice");
            int update_num = data.getInt("update_num");
            int update_time = data.getInt("update_time");
            String like_num=data.getString("like_num");
            String collect_num=data.getString("collect_num");

//            String content=data.getString("content");
//            //存储一下
//             sp=getSharedPreferences(Configs.SHARE_NAME, Activity.MODE_PRIVATE);
//            SharedPreferences.Editor editor=sp.edit();
//            editor.putInt("type",1);//0视频,1音频
//            editor.putString("data",data.toString());
//            editor.putString("thumb",thumb);
//            editor.putString("album",albumStr);
//            editor.putInt("position",position);
//
//            editor.commit();
            dianzanTv.setText(like_num);
//            statusTitleTv.setText("title");
            Glide.with(this).load(thumb).into(this.circlrImg);

            Glide.with(this).load(thumb).into(this.userImg);





            userNameTv.setText(yinpingTile);
            dingyueTv.setText(collect_num+"人订阅");
            if (is_collect==1){
                dingyueBtn.setText("已订阅");
            }else {
                dingyueBtn.setText("+订阅");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        httpInterface = new HttpImplement();
        System.out.println("position:"+position);

        try {
            album=new JSONArray(albumStr);
            subId =(album.getJSONObject(position)).getInt("id");
            initLearn_info(subId +"");
            initShuyouQuan(subId +"");
            initLearn_audio_tuijian(subId +"");
            initDianzan();
            guanggao();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * 初始化点赞
     */
    private void initDianzan(){
        ImageView dianZanImg=findViewById(R.id.dianZanImg);
        LinearLayout dianzanLay=findViewById(R.id.dianzanLay);
        dianzanTv=findViewById(R.id.dianzanTv);
        dianzanLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpInterface.learn(SharedPreferencesUtil.getToken(YinpingDetailActivity.this), yinpingId, "like", new OkHttpUtils.MyNetCall() {
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
                                        Toast.makeText(YinpingDetailActivity.this,info,Toast.LENGTH_SHORT).show();
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
     * 初始化指定推荐
     */
    private void initLearn_audio_tuijian(String id){
        LinearLayout benzhouLay=findViewById(R.id.benzhouLay);

        ImageView benzhouImg=findViewById(R.id.benzhouImg);
        TextView benzhouNameTv=findViewById(R.id.benzhouNameTv);
        TextView benzhouContentTv=findViewById(R.id.benzhouContentTv);
        TextView benzhouTimeTv=findViewById(R.id.benzhouTimeTv);
        TextView benzhouCountTv=findViewById(R.id.benzhouCountTv);
        LinearLayout zhidingTuijieLay=findViewById(R.id.zhidingTuijieLay);

        httpInterface.learn_audio_tuijian(SharedPreferencesUtil.getToken(this), id, new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                        JSONArray dataArray=responseJson.getJSONArray("data");
                        if (dataArray.length()>0){
                            JSONArray array = responseJson.getJSONArray("data");
                            if (data.length()!=0){
                                JSONObject data=array.getJSONObject(0);
                                String title = data.getString("title");
                                String id = data.getString("id");
                                String thumb = data.getString("thumb");
                                String keywords = data.getString("keywords");
                                String add_time = data.getString("add_time");
                                String play_num = data.getString("play_num");

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        zhidingTuijieLay.setVisibility(View.VISIBLE);
                                        Glide.with(YinpingDetailActivity.this).load(thumb).into(benzhouImg);
                                        benzhouNameTv.setText(title);
                                        benzhouContentTv.setText(keywords);
                                        benzhouCountTv.setText(play_num);
                                        benzhouTimeTv.setText(add_time);
                                        benzhouLay.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent=new Intent(YinpingDetailActivity.this,DetailControlActivity.class);
                                                intent.putExtra("id",id);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                            }


                        }else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    zhidingTuijieLay.setVisibility(View.GONE);
                                }
                            });
                        }



                    }else {
                        String info=responseJson.getString("info");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(YinpingDetailActivity.this,info,Toast.LENGTH_SHORT).show();
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
    private void initShuyouQuan(String albumId){
        User user=SharedPreferencesUtil.getLoginUser(this);
        if (user==null){
            shuodianImg.setImageResource(R.drawable.ic_launcher);
        }else {
            Glide.with(this).load(user.getAvatar()).into(shuodianImg);
        }

        shuodianImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user==null){

                }else {
                    Intent intent=new Intent(YinpingDetailActivity.this, WebActivity.class);
                    intent.putExtra("url", UrlConstant.ZHUYE_URL+"?uid="+user.getUid());
                    startActivity(intent);
                }

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
                    httpInterface.learn_comment(SharedPreferencesUtil.getToken(YinpingDetailActivity.this), "add",yinpingId+"" ,albumId,"",msg, new OkHttpUtils.MyNetCall() {
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
                                            Toast.makeText(YinpingDetailActivity.this,info,Toast.LENGTH_SHORT).show();
                                            initShuyouQuan(albumId);
                                        }

                                    });
                                }else {
                                    String info=responseJson.getString("info");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(YinpingDetailActivity.this,info,Toast.LENGTH_SHORT).show();
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
                    httpInterface.learn_comment(SharedPreferencesUtil.getToken(YinpingDetailActivity.this), "add",yinpingId+"" ,albumId,pid,msg, new OkHttpUtils.MyNetCall() {
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
                                            Toast.makeText(YinpingDetailActivity.this,info,Toast.LENGTH_SHORT).show();
                                            initShuyouQuan(albumId);
                                        }

                                    });
                                }else {
                                    String info=responseJson.getString("info");
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(YinpingDetailActivity.this,info,Toast.LENGTH_SHORT).show();
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
        Button shuodianCloseBtn=findViewById(R.id.shuodianCloseBtn);
        shuodianCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuodianLay.setVisibility(View.GONE);
            }
        });
        shuodianLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuodianLay.setVisibility(View.GONE);
            }
        });
        EditText shuodianContentEt=findViewById(R.id.shuodianContentEt);

        shuodianTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                shuodianLay.setVisibility(View.VISIBLE);
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
                    Toast.makeText(YinpingDetailActivity.this,"请先发表评论",Toast.LENGTH_SHORT).show();
                    return;
                }
                HttpInterface httpInterface = new HttpImplement();
                httpInterface.learn_comment(SharedPreferencesUtil.getToken(YinpingDetailActivity.this), "add",yinpingId+"" ,albumId,"",content, new OkHttpUtils.MyNetCall() {
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
                                        Toast.makeText(YinpingDetailActivity.this,info,Toast.LENGTH_SHORT).show();
                                        initShuyouQuan(subId +"");
                                    }

                                });
                            }else {
                                String info=responseJson.getString("info");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(YinpingDetailActivity.this,info,Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(YinpingDetailActivity.this,"请先输入评论",Toast.LENGTH_SHORT).show();
                    return;
                }
                String pid=(String) shuyouReplayLay.getTag();
                System.out.println("pid22222222:"+pid);
                HttpInterface httpInterface = new HttpImplement();
                httpInterface.learn_comment(SharedPreferencesUtil.getToken(YinpingDetailActivity.this), "add",yinpingId+"" ,albumId,pid,content, new OkHttpUtils.MyNetCall() {
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
                                        Toast.makeText(YinpingDetailActivity.this,info,Toast.LENGTH_SHORT).show();
                                        initShuyouQuan(subId +"");
                                    }

                                });
                            }else {
                                String info=responseJson.getString("info");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(YinpingDetailActivity.this,info,Toast.LENGTH_SHORT).show();
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






        (  (TextView)findViewById(R.id.shuyouTv)).setText("精彩评论");
        HttpInterface httpInterface = new HttpImplement();
        httpInterface.items_comment_get(SharedPreferencesUtil.getToken(this),yinpingId+"" ,albumId,"",page+"", new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                        JSONObject data = responseJson.getJSONObject("data");
                        String total_count=data.getString("total_count");
                        String count=data.getString("count");
                        JSONArray list=data.getJSONArray("list");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bookTv.setText("评论("+total_count+")");
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
                                        pinglunAdapter.notifyDataSetChanged();
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

        pinglunAdapter.setOnMyItemClickListener(new PinglunAdapter.OnMyItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
//                shuyouReplayLay.setVisibility(View.VISIBLE);
//                sreplyContentEt.setText("");
                try {
                    JSONObject data= (JSONObject) allPinglunList.getJSONObject(pos);

                    String id = data.getString("id");
                    String content = data.getString("content");
                    String uid = data.getString("uid");
                    String nickname = data.getString("nickname");

 //                  shuyouReplayLay.setTag(id);
//                    System.out.println("pid211111111111:"+uid);
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

            @Override
            public void mLongClick(View v, int pos) {

            }
        }) ;

    }
    /**
     * 初始化音频内容
     */
    private void initLearn_info(String id){
        httpInterface.learn_info(SharedPreferencesUtil.getToken(this), id,"audio_info", new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                        JSONObject data = responseJson.getJSONObject("data");
                        String title = data.getString("title");
                        subTile=title;
                        String id = data.getString("id");
                        String learn_id = data.getString("learn_id");
                        String comment_num = data.getString("comment_num");
                         src = data.getString("src");
                        String content = data.getString("content");
                        String priceStr=data.getString("price");
                        float price=Float.parseFloat(priceStr);



                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                titleTv.setText(title);
                                System.out.println("price:"+priceStr);
                                if ((int)price==0){
                                    hasQuanxian=true;
                                    if (need2ResetMedia){

                                        initMedia(src,id);
                                    }
                                }else {
                                    hasQuanxian=false;
                                    payDialog.setImgAndPrice(thumb,priceStr);
                                    payDialog.show();
                                }


                               webView.loadDataWithBaseURL(null,content, "text/html",  "utf-8", null);

                            }
                        });


                    }else {
                        String info=responseJson.getString("info");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(YinpingDetailActivity.this,info,Toast.LENGTH_SHORT).show();
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
//            if (mediaPlayer!=null){
//                mediaPlayer.reset();;
//            }
            finish();
        }else if (view==playBtn){
//            sendNotification();
            if (hasQuanxian){
                if (isPlaying){
                    isPlaying=false;
                    mediaPlayer.pause();
                    playBtn.setBackgroundResource(R.drawable.play);
                    circleLay.clearAnimation();
                }else {
                    try {
                        data = new JSONObject(dataStr);
                        System.out.println("data:" + data);
                        thumb = data.getString("thumb");
                        albumStr=getIntent().getStringExtra("album");
                        album=new JSONArray(albumStr);


                        //存储一下
                        SharedPreferences sp=getSharedPreferences(Configs.SHARE_NAME, Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sp.edit();
                        editor.putInt("type",1);//0视频,1音频
                        editor.putString("data",data.toString());
                        editor.putString("thumb",thumb);
                        editor.putString("album",albumStr);
                        editor.putInt("position",position);

                        editor.commit();
                    }catch (JSONException e){

                    }


                    isPlaying=true;
                    Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
                    LinearInterpolator lin = new LinearInterpolator();
                    rotate.setInterpolator(lin);
                    mediaPlayer.start();
                    playBtn.setBackgroundResource(R.drawable.pause);
                    circleLay.startAnimation(rotate);
                }
            }else {
                payDialog.show();
            }


        }else if (view==nextBtn){
            System.out.println("---------------------------nextBtn-------------------------------");
            System.out.println("---------------------------nextBtn11-------------------------------:"+position);
            need2ResetMedia=true;
            isPlaying=false;
            position=position+1;
            if (position>=album.length()){
                position=0;
            }
            System.out.println("---------------------------nextBtn22-------------------------------:"+position);

            SharedPreferences sp=getSharedPreferences(Configs.SHARE_NAME, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putInt("position",position);
            editor.commit();

            int id= 0;
            try {
                id = (album.getJSONObject(position)).getInt("id");
                subId =id;
                initLearn_info(subId +"");
                try {
                    initShuyouQuan(subId +"");
                }catch (IllegalArgumentException e){

                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (view==prevBtn){
            need2ResetMedia=true;
            isPlaying=false;
            position=position-1;
            if (position<0){
                position=album.length()-1;
            }
            SharedPreferences sp=getSharedPreferences(Configs.SHARE_NAME, Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putInt("position",position);
            editor.commit();

            int id= 0;
            try {
                id = (album.getJSONObject(position)).getInt("id");
                subId =id;
                initLearn_info(subId +"");
                initShuyouQuan(subId +"");
                initLearn_audio_tuijian(subId +"");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if (view==dingyueBtn){
            User user=SharedPreferencesUtil.getLoginUser(YinpingDetailActivity.this);

            if (user==null){
                Toast.makeText(getApplicationContext(),"请先进行登录",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(YinpingDetailActivity.this, LoginIndexActivity.class);
                startActivity(intent);
                finish();
                return;
            }
            dingyue(dingyueBtn);
        }else if (view==kuaitui15Lay){
            //快退
            if (hasQuanxian){
                if (seekBar.getProgress()<=15*1000){
                    mediaPlayer.seekTo(0);
                }else {
                    mediaPlayer.seekTo(seekBar.getProgress()-15*1000);
                }
            }else {
                payDialog.show();
            }


        }else if (view==kuaijin15Lay){
            //快进
            if (hasQuanxian){
                if (seekBar.getProgress()+15*1000>=seekBar.getMax()){
                    mediaPlayer.seekTo(seekBar.getMax());
                }else {
                    mediaPlayer.seekTo(seekBar.getProgress()+15*1000);
                }
            }else {
                payDialog.show();
            }

        }else if (view==beishuLay){
            if (hasQuanxian){
                showBeisuDialog();
            }else {
                payDialog.show();
            }

        }else if (view==dashangLay){
            User user=SharedPreferencesUtil.getLoginUser(YinpingDetailActivity.this);

            if (user==null){
                Toast.makeText(getApplicationContext(),"请先进行登录",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(YinpingDetailActivity.this, LoginIndexActivity.class);
                startActivity(intent);
                finish();
                return;
            }
            HongbaoDialog hongbaoDialog=new HongbaoDialog(this, new HongbaoDialog.OnDialogButtonClickListener() {
                @Override
                public void okButtonClick(float money) {
                    showZhifuDialog(money);
                }


            });
            hongbaoDialog.show();
        }else if (view==listLay){
            showYinpingList();
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
            String musicUrl= UrlConstant.YINPING_DETAIL_URL+"?id="+yinpingId+"#vid="+subId+"&sharefrom=android";
            String imgUrl=thumb;
            String musicDataUrl=src;
            String sTitle=yinpingTile;
            String description=subTile;
            showShareDialog(musicUrl,imgUrl,musicDataUrl,sTitle,description);
        }else if (view==bookLay){
            bookTv.setSelected(true);
            wenGaoTv.setSelected(false);
            bookImg.setVisibility(View.VISIBLE);
            wenGaoImg.setVisibility(View.INVISIBLE);
            shuodianTvLay.setVisibility(View.VISIBLE);

            findViewById(R.id.pinglunLay).setVisibility(View.VISIBLE);
            findViewById(R.id.webview).setVisibility(View.GONE);
        }else if (view==wengaoLay){
            bookTv.setSelected(false);
            wenGaoTv.setSelected(true);
            bookImg.setVisibility(View.INVISIBLE);
            wenGaoImg.setVisibility(View.VISIBLE);
            shuodianTvLay.setVisibility(View.GONE);

            findViewById(R.id.pinglunLay).setVisibility(View.GONE);
            findViewById(R.id.webview).setVisibility(View.VISIBLE);
        }else if (view==pubulishLay){
            User user=SharedPreferencesUtil.getLoginUser(YinpingDetailActivity.this);

            if (user==null){
                Toast.makeText(getApplicationContext(),"请先进行登录",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(YinpingDetailActivity.this, LoginIndexActivity.class);
                startActivity(intent);
                finish();
                return;
            }
            Intent intent=new Intent(YinpingDetailActivity.this,PublishRijinjingActivity.class);
            intent.putExtra("id",yinpingId);
            intent.putExtra("vid",subId+"");
            intent.putExtra("title",subTile);
            intent.putExtra("thumb",thumb);
            startActivity(intent);
        }

    }

    /**
     * 订阅或取消订阅
     */
    private void dingyue(Button dingyueBtn){
        String id= null;
        try {
            id = data.getInt("id")+"";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpInterface.learn(SharedPreferencesUtil.getToken(YinpingDetailActivity.this), id, "collect", new OkHttpUtils.MyNetCall() {
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
                                dingyueTv.setText(num+"人订阅");
                                dingyueBtn.setText(is_add==1?"已订阅":"+订阅");
                                Toast.makeText(YinpingDetailActivity.this,info,Toast.LENGTH_SHORT).show();
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

    /**
     * 初始化音频播放器
     * @param audioUrl 音频地址
     */
    private void initMedia(String audioUrl,String id){

        beishuTv.setText("1.0");
        mediaPlayer= MediaUtil.getMediaPlayer();
        if (MediaUtil.MEDIA_IS_INIT){//播放过
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            initProgress();
            seekBar.setProgress(0);
            mediaPlayer.seekTo(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("mustJiyi:"+mustJiyi);
//        initJiyi();
        if (mustJiyi){
            mustJiyi=false;
            initJiyi();
        }else {
            onClick(playBtn);
        }


    }

    /**
     * 设置进度
     */
    private void initProgress(){
        int duration = mediaPlayer.getDuration();
        if (0 != duration) {
            //更新 seekbar 长度
            seekBar.setMax(duration);

            int s = duration / 1000;
            //设置文件时长，单位 "分:秒" 格式
            String total = ((s / 60 <10?("0"+s/60):s/60)+ ":" + (s %60 <10?("0"+s%60):s%60));
            startTimeTv.setText("00:00");
            endTimeTv.setText(total);

        }
        if (isPlaying){
            //初始化时，判断下是否需要播放
            mediaPlayer.start();
        }
        if (!UIhandle.hasMessages(UPDATE_MUSIC_UI)){
            UIhandle.sendEmptyMessage(UPDATE_MUSIC_UI);
        }
        if (!UIhandle.hasMessages(UPDATE_PROGRESS)){
            UIhandle.sendEmptyMessage(UPDATE_PROGRESS);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (hasQuanxian){
                    mediaPlayer.seekTo(seekBar.getProgress());
                }else {
                    payDialog.show();
                }

            }
        });
    }

    /**
     * 记忆播放
     */
    private void initJiyi(){
        //获取记忆播放
        User user=SharedPreferencesUtil.getLoginUser(YinpingDetailActivity.this);
        String accessToken="";
        if (user!=null){
            accessToken= user.getAccess_token();
        }
        //记忆播放
        AudioSQLiteOpenHelper sqlHelp= AudioSQLiteOpenHelper.getInstance(this);
        int audioTime=sqlHelp.selectAudioTime(yinpingId,subId+"");   //音频进度
        System.out.println("audioTime："+audioTime);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (audioTime != -1) {
                    seekBar.setProgress(audioTime);
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
                onClick(playBtn);

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

        textView.setText(mStr+" : "+sStr);

    }



    private void sendNotification(){
        String channelId = "notification";
        String channelName = "channelName";

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent notificationIntent = new Intent(this, YinpingDetailActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        builder.setContentTitle("通知标题")//设置通知栏标题
                .setContentIntent(pendingIntent) //设置通知栏点击意图
                .setContentText("通知内容")
                .setNumber(10)
                .setTicker("通知内容") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setSmallIcon(R.mipmap.ic_launcher)//设置通知小ICON
                .setChannelId(channelId)
                .setDefaults(Notification.DEFAULT_ALL);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        if (notificationManager != null) {
            notificationManager.notify(11, notification);
        }



//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.mipmap.ic_launcher)//这玩意在通知栏上显示一个logo
//                .setCategory(CATEGORY_MESSAGE)
//                .setOngoing(true);
//        //点击通知栏跳转的activity
//        Intent intent = new Intent(this, YinpingDetailActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setAutoCancel(false);//点击不让消失
//        builder.setSound(null);//关了通知默认提示音
//        builder.setPriority(NotificationCompat.PRIORITY_MAX);//咱们通知很重要
//        builder.setVibrate(null);//关了车震
//        builder.setContentIntent(pendingIntent);//整个点击跳转activity安排上
//        builder.setOnlyAlertOnce(false);
//        builder.setChannelId(channelId);////必须添加（Android 8.0） 【唯一标识】
//        RemoteViews remoteViews = initNotifyView(null,"ttttttttt");
//        builder.setContent(remoteViews);//把自定义view放上
//        builder.setCustomBigContentView(remoteViews);//把自定义view放上
//        Notification notification = builder.build();
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        if (notificationManager != null) {
//            notificationManager.notify(100, notification);
//        }
//
//    System.out.println("-----------------nofity------------------");



    }

    private RemoteViews initNotifyView(Bitmap bitmap,String title) {
        String packageName = this.getPackageName();
        RemoteViews remoteView = new RemoteViews(packageName, R.layout.customnotice);
//        remoteView.setImageViewBitmap(R.id.widget_title, R.drawable.ic_launcher);
        remoteView.setImageViewResource(R.id.widget_title, R.drawable.ic_launcher);

        remoteView.setTextViewText(R.id.widget_title, title);

        Intent prv = new Intent(this,XMPlayerReceiver.class);//播放上一首
        prv.setAction(XMPlayerReceiver.PLAY_PRE);
        PendingIntent intent_prev = PendingIntent.getBroadcast(this, 1, prv,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.widget_prev, intent_prev);


        Intent next = new Intent(this,XMPlayerReceiver.class);//播放下一首
        next.setAction(XMPlayerReceiver.PLAY_NEXT);
        PendingIntent intent_next = PendingIntent.getBroadcast(this, 2, next,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.widget_next, intent_next);


        Intent startpause = new Intent(this, XMPlayerReceiver.class);//暂停
        startpause.setAction(XMPlayerReceiver.PLAY_PAUSE);
        PendingIntent intent_pause = PendingIntent.getBroadcast(this, 3, startpause,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.widget_play, intent_pause);

        Intent startplay = new Intent(this,XMPlayerReceiver.class);//播放
        startplay.setAction(XMPlayerReceiver.PLAY_PLAY);
        PendingIntent intent_play = PendingIntent.getBroadcast(this, 4, startplay,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteView.setOnClickPendingIntent(R.id.widget_play, intent_play);
        return remoteView;
    }



//    public class MyAdapter extends BaseAdapter {
//
//        private JSONArray list;
//        private Context ctx;
//        private LayoutInflater mInflater;
//
//        public MyAdapter(Context context, JSONArray list) {
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
//            MyAdapter.ViewHolder viewHolder = null;
//            if (convertView == null) {
//                convertView = mInflater.inflate(R.layout.item_yinping_pinglun, null);
//                viewHolder = new MyAdapter.ViewHolder();
//                viewHolder.headImg = convertView.findViewById(R.id.headImg);
//                viewHolder.nameTv = convertView.findViewById(R.id.nameTv);
//                viewHolder.dengjiTv = convertView.findViewById(R.id.dengjiTv);
//                viewHolder.contentTv = convertView.findViewById(R.id.contentTv);
//                viewHolder.timeTv = convertView.findViewById(R.id.timeTv);
//                viewHolder.dianZanImg = convertView.findViewById(R.id.dianZanImg);
//                viewHolder.dianZanTv = convertView.findViewById(R.id.dianzanTv);
//                viewHolder.vipImg = convertView.findViewById(R.id.vipImg);
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
//                viewHolder = (MyAdapter.ViewHolder) convertView.getTag();
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
//
//
//                viewHolder.nameTv.setText(nickname);
//                viewHolder.dengjiTv.setText(learn_rank);
//                viewHolder.contentTv.setText(content);
//                viewHolder.timeTv.setText(time);
//                viewHolder.dianZanTv.setText(lovenum+"");
//                viewHolder.dianZanImg.setSelected(is_love==0?false:true);
//                Glide.with(YinpingDetailActivity.this).load(avatar).into(viewHolder.headImg);
//                JiehuoReplyAdapter myAdapter;
//                JSONArray reply;
//                if (reply_num!=0){
//                    reply=data.getJSONArray("reply");
//                    myAdapter=new JiehuoReplyAdapter(YinpingDetailActivity.this,reply);
//                    viewHolder.replyListView.setAdapter(myAdapter);
//                }else {
//                    reply=new JSONArray();
//                    myAdapter=new JiehuoReplyAdapter(YinpingDetailActivity.this,reply);
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
//                viewHolder.headImg.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent=new Intent(YinpingDetailActivity.this,WebActivity.class);
//                        intent.putExtra("url",UrlConstant.ZHUYE_URL+"?uid="+uid);
//                        startActivity(intent);
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
//
//        }
//    }



//    public    class PinglunAdapter extends  RecyclerView.Adapter<PinglunAdapter.ViewHolder>{
//        private JSONArray list;
//        private Context ctx;
//        private LayoutInflater mInflater;
//        public PinglunAdapter(Context context, JSONArray list) {
//            ctx = context;
//            this.list = list;
//            mInflater = LayoutInflater.from(context);
//        }
//
//        private YinpingFragment.MyRecyclerAdapter.OnMyItemClickListener listener;
////        public void setOnMyItemClickListener(YinpingFragment.MyRecyclerAdapter.OnMyItemClickListener listener){
////            this.listener = listener;
////
////        }
////
////        public   interface OnMyItemClickListener{
////            void myClick(View v,int pos);
////            void mLongClick(View v,int pos);
////        }
//
//        @NonNull
//        @Override
//        public PinglunAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yinping_pinglun,parent,false);
//            PinglunAdapter.ViewHolder viewHolder = new PinglunAdapter.ViewHolder(convertView);
//
//            return viewHolder;
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull PinglunAdapter.ViewHolder viewHolder, int position) {
//            if (listener!=null) {
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
//
//
//                viewHolder.nameTv.setText(nickname);
//                viewHolder.dengjiTv.setText(learn_rank);
//                viewHolder.contentTv.setText(content);
//                viewHolder.timeTv.setText(time);
//                viewHolder.dianZanTv.setText(lovenum+"");
//                viewHolder.dianZanImg.setSelected(is_love==0?false:true);
//                Glide.with(YinpingDetailActivity.this).load(avatar).into(viewHolder.headImg);
//                JiehuoReplyAdapter myAdapter;
//                JSONArray reply;
//                if (reply_num!=0){
//                    reply=data.getJSONArray("reply");
//                    myAdapter=new JiehuoReplyAdapter(YinpingDetailActivity.this,reply);
//                    viewHolder.replyListView.setAdapter(myAdapter);
//                }else {
//                    reply=new JSONArray();
//                    myAdapter=new JiehuoReplyAdapter(YinpingDetailActivity.this,reply);
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
//                viewHolder.headImg.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent=new Intent(YinpingDetailActivity.this,WebActivity.class);
//                        intent.putExtra("url",UrlConstant.ZHUYE_URL+"?uid="+uid);
//                        startActivity(intent);
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
//        class ViewHolder extends RecyclerView.ViewHolder {
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







    public void shareWebpage(String musicUrl,String imgUrl,String musicDataUrl,String sTitle,String description,int scene) {
        IWXAPI 	api = WXAPIFactory.createWXAPI(YinpingDetailActivity.this, Configs.APP_ID4WX);






        AlertDialog.Builder builder = new AlertDialog.Builder(YinpingDetailActivity.this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("分享至");
        final String[] cities = { "微信朋友圈", "微信好友" };
        RequestManager manager =Glide.with(YinpingDetailActivity.this);





            Glide.with(YinpingDetailActivity.this).load(imgUrl).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    Bitmap finalShareBitMap = BitmapUtil.DrawableToBitmap(resource);
                    //初始化一个WXMusicObject，填写url
                    WXMusicObject music = new WXMusicObject();
                    music.musicUrl=musicUrl;      //音频网页的 URL 地址
                    music.musicDataUrl=musicDataUrl;//音频数据的 URL 地址

//用 WXMusicObject 对象初始化一个 WXMediaMessage 对象
                    WXMediaMessage msg = new WXMediaMessage();
                    msg.mediaObject = music;
                    msg.title = sTitle;
                    msg.description = description;

                    //设置音乐缩略图
                    msg.thumbData = BitmapUtil.bmpToByteArray(finalShareBitMap,32);
//                    msg.thumbData = Util.bmpToByteArray(finalShareBitMap, true);

                    //构造一个Req
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("music");
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


    /**
     * 广告列表
     * @param
     */
    private void guanggao(){
        httpInterface.ads_items_get(SharedPreferencesUtil.getToken(YinpingDetailActivity.this), "19","1",new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) {
                JSONObject responseJson = null;
                try {
                    responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                        JSONArray data = responseJson.getJSONArray("data");


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    JSONObject value=data.getJSONObject(0);
                                    String img=value.getString("img");
                                    String id=value.getString("id");
                                    String name=value.getString("name");
                                    String link=value.getString("link");
                                    JSONObject apppage=value.getJSONObject("apppage");
                                    String mode=apppage.getString("mode");
                                    Glide.with(YinpingDetailActivity.this).load(img).into(guanggaoImg);
                                    guanggaoImg.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            if (mode.equals("video")){
                                                JSONObject query= null;
                                                try {
                                                    query = apppage.getJSONObject("query");
                                                    String id=query.getString("id");
                                                    Intent intent=new Intent(YinpingDetailActivity.this, DetailControlActivity.class);
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
                                                    Intent intent=new Intent(YinpingDetailActivity.this, DetailControlActivity.class);
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
                                                    httpInterface.learn_info(SharedPreferencesUtil.getToken(YinpingDetailActivity.this), id, new OkHttpUtils.MyNetCall() {
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
                                                                    Intent intent = new Intent(YinpingDetailActivity.this, YinpingDetailActivity.class);
                                                                    intent.putExtra("position", position);//当前音频位置
                                                                    intent.putExtra("data", data.toString());
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
                                                    Intent intent=new Intent(YinpingDetailActivity.this,YinpingActivity.class);
                                                    intent.putExtra("id",id);
                                                    startActivity(intent);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }else {
                                                Intent intent=new Intent(YinpingDetailActivity.this,WebActivity.class);
                                                intent.putExtra("url",link);
                                                startActivity(intent);
                                            }

                                        }
                                    });
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
    }


    public class JiehuoReplyAdapter extends BaseAdapter {

        private JSONArray list;
        private Context ctx;
        private LayoutInflater mInflater;

        public JiehuoReplyAdapter(Context context, JSONArray list) {
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
            JiehuoReplyAdapter.ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_jiehuo_reply, null);
                viewHolder = new JiehuoReplyAdapter.ViewHolder();
                viewHolder.headImg = convertView.findViewById(R.id.headImg);
                viewHolder.nameTv = convertView.findViewById(R.id.nameTv);
                viewHolder.contentTv = convertView.findViewById(R.id.contentTv);
                viewHolder.timeTv = convertView.findViewById(R.id.timeTv);
                viewHolder.vipImg = convertView.findViewById(R.id.vipImg);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (JiehuoReplyAdapter.ViewHolder) convertView.getTag();
            }
            try {
                JSONObject data = list.getJSONObject(position);
                String id = data.getString("id");
                String uid = data.getString("uid");

                String content = data.getString("content");
                String avatar = data.getString("avatar");
                String nickname = data.getString("nickname");
                int level=Integer.parseInt(data.getString("level"));
                String time = data.getString("time");

                viewHolder.nameTv.setText(nickname);
                viewHolder.contentTv.setText(content);
                viewHolder.timeTv.setText(time);
                if (level>0){
                    viewHolder.vipImg.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.vipImg.setVisibility(View.GONE);
                }

                Glide.with(YinpingDetailActivity.this).load(avatar).into(viewHolder.headImg);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return convertView;
        }

        class ViewHolder {
            public ImageView headImg;
            public TextView nameTv;
            public TextView  contentTv,timeTv;
            public ImageView vipImg;


        }


    }
    /**
     * 音频对话框列表
     */
    private void showYinpingList() {
        Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_yinping_list, null);
        RecyclerView recyclerView=contentView.findViewById(R.id.recyclerView);
       MyRecyclerAdapter adapter=new MyRecyclerAdapter(YinpingDetailActivity.this,album);
        //必须先设置LayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(YinpingDetailActivity.this));
        recyclerView.setAdapter(adapter);
        adapter.setOnMyItemClickListener(new MyRecyclerAdapter.OnMyItemClickListener() {
            @Override
            public void myClick(View v, int pos) {
                position=pos;
                initData(dataStr,pos,albumStr);
                bottomDialog.dismiss();
            }

            @Override
            public void mLongClick(View v, int pos) {

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
    public static class MyRecyclerAdapter extends  RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder>{
        private JSONArray list;
        private Context ctx;
        private LayoutInflater mInflater;
        public MyRecyclerAdapter(Context context, JSONArray list) {
            ctx = context;
            this.list = list;
            mInflater = LayoutInflater.from(context);
        }

        private MyRecyclerAdapter.OnMyItemClickListener listener;
        public void setOnMyItemClickListener(MyRecyclerAdapter.OnMyItemClickListener listener){
            this.listener = listener;

        }

        public   interface OnMyItemClickListener{
            void myClick(View v,int pos);
            void mLongClick(View v,int pos);
        }

        @NonNull
        @Override
        public MyRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yinping2,parent,false);
            MyRecyclerAdapter.ViewHolder viewHolder = new MyRecyclerAdapter.ViewHolder(convertView);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyRecyclerAdapter.ViewHolder viewHolder, int position) {
            if (listener!=null) {
                viewHolder.yinpingLay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.myClick(v,position);
                    }
                });


                // set LongClick
                viewHolder.yinpingLay.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        listener.mLongClick(v,position);
                        return true;
                    }
                });
            }
            try {
                JSONObject data=list.getJSONObject(position);
                int id=data.getInt("id");
                String title=data.getString("title");
                String duration=data.getString("duration");
                String play_num=data.getString("play_num");
                String time=data.getString("time");
                JSONArray tags=data.getJSONArray("tags");



                viewHolder.nameTv.setText(title);
                viewHolder.countTv.setText(play_num);
                viewHolder.timeTv.setText(duration);
                viewHolder.dateTv.setText(DateUtil.timeStamp2Date(time,"yyyy/MM/dd"));
                if (tags!=null&&tags.length()!=0){
                    viewHolder.tagTv.setText("置顶");
                    viewHolder.tagTv.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.tagTv.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        @Override
        public int getItemCount() {
            return list.length();
        }
        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView nameTv;
            public TextView countTv;
            public TextView dateTv;
            public TextView timeTv;
            public LinearLayout yinpingLay;
            public TextView tagTv;
            public ViewHolder (View view)
            {
                super(view);
                nameTv = view.findViewById(R.id.nameTv);
                countTv = view.findViewById(R.id.countTv);
                dateTv = view.findViewById(R.id.dateTv);
                timeTv = view.findViewById(R.id.timeTv);
                yinpingLay = view.findViewById(R.id.yinpingLay);
                tagTv=view.findViewById(R.id.tagTv);
            }
        }

    }

    /**
     * 支付对话框
     */
    private void showZhifuDialog(float money) {
        Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_weixin_zhifubao_pay, null);
        LinearLayout weixinZhifuLay=contentView.findViewById(R.id.weixinZhifuLay);
        LinearLayout zhifubaoZhifuLay=contentView.findViewById(R.id.zhifubaoZhifuLay);
        weixinZhifuLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                httpInterface.learn_shang(SharedPreferencesUtil.getToken(YinpingDetailActivity.this), yinpingId +"",subId+"",money+"", "weixin", new OkHttpUtils.MyNetCall() {
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
                                            IWXAPI api = WXAPIFactory.createWXAPI(YinpingDetailActivity.this, Configs.APP_ID4WX);
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
                httpInterface.learn_shang(SharedPreferencesUtil.getToken(YinpingDetailActivity.this), yinpingId +"",subId+"",money+"", "alipay", new OkHttpUtils.MyNetCall() {
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
                                                    PayTask alipay = new PayTask(YinpingDetailActivity.this);
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
     * 分享对话框
     */
    private void showShareDialog(String musicUrl,String imgUrl,String musicDataUrl,String sTitle,String description) {
        Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_share, null);
        LinearLayout haoyouLay=contentView.findViewById(R.id.haoyouLay);
        LinearLayout pengyouquanLay=contentView.findViewById(R.id.pengyouquanLay);
        haoyouLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWebpage(musicUrl,imgUrl,musicDataUrl,sTitle,description,0);
            }
        });
        pengyouquanLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWebpage(musicUrl,imgUrl,musicDataUrl,sTitle,description,1);
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
//        UIhandle.removeMessages(UPDATE_MUSIC_UI);
//        UIhandle.removeMessages(UPDATE_PROGRESS);
        System.out.println("---------------------------------onDestroy------------------------------------------");
        unregisterReceiver(payBrocast);

    }


    /**
     * 微信支付
     */
    public void weixinPay(String id,String mobile){
        // 支付
        User user=SharedPreferencesUtil.getLoginUser(YinpingDetailActivity.this);
        httpInterface.learn_buy(user.getAccess_token(), id,mobile,"weixin" ,new OkHttpUtils.MyNetCall() {
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
                                    IWXAPI api = WXAPIFactory.createWXAPI(YinpingDetailActivity.this, Configs.APP_ID4WX);
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


                    }else {
                        String info=responseJson.getString("info");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(YinpingDetailActivity.this,info,Toast.LENGTH_SHORT).show();
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
