package com.yyh.read666;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yyh.read666.configs.Configs;
import com.yyh.read666.details.DetailControlActivity;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.HttpInterface;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.okhttp.TestEncrypt;
import com.yyh.read666.okhttp.UrlConstant;
import com.yyh.read666.tab2.YinpingActivity;
import com.yyh.read666.tab2.YinpingDetailActivity;
import com.yyh.read666.utils.BitmapUtil;
import com.yyh.read666.utils.HtmlUtil;
import com.yyh.read666.utils.SharedPreferencesUtil;
import com.yyh.read666.vo.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

@SuppressLint("JavascriptInterface")

public class WebActivity extends Activity {
    private WebView webView;
    private Button backBtn;

    private TextView webTitle;

    private ProgressBar bar;
    private String url;
    private Button shareBtn;

    private  WebReceiver webReceiver;
    private boolean mIsRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web);
        webTitle=(TextView)findViewById(R.id.titleTv);
        webView = (WebView) findViewById(R.id.webView);
        bar = (ProgressBar) findViewById(R.id.bar);
        bar.setMax(100);
        bar.setVisibility(View.GONE);
        webView.addJavascriptInterface(new WebAppInterface(this),
                "Android");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(PluginState.ON);
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setSupportZoom(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(false);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(true);//support zoom
        webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
        shareBtn=findViewById(R.id.shareBtn);

        backBtn=(Button)findViewById(R.id.back);
        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });


        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                super.onProgressChanged(view, newProgress);

            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                // TODO Auto-generated method stub
                super.onReceivedTitle(view, title);
                webTitle.setText(title);
//                Toast.makeText(WebActivity.this,webView.getUrl(),Toast.LENGTH_LONG).show();
                if (webView.getUrl().startsWith(UrlConstant.RIJINGJIN_CONTENT_URL+"?id=")) {
                    shareBtn.setVisibility(View.VISIBLE);
                } else {
                    shareBtn.setVisibility(View.GONE);
                }
            }

        });
        uploadUrl();
        //实例化IntentFilter对象
        IntentFilter filter = new IntentFilter();
        filter.addAction("WxPay");
        filter.addAction("locationHref");

        webReceiver = new WebReceiver();
        //注册广播接收
        registerReceiver(webReceiver, filter);
        shareBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String sTitle="日精进分享";
                String description="";
                showShareDialog(webView.getUrl(),sTitle,description);
            }
        });

    }
    class WebReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //通过土司验证接收到广播
            if (intent != null && intent.getAction().equals("WxPay")) {
                System.out.println("----------------------WebReceiver--------------");
                String status = intent.getStringExtra("status");
                webView.loadUrl("javascript:app_pay_callback('" + status + "' )");
            } else if (intent != null && intent.getAction().equals("locationHref")) {
                System.out.println("----------------------locationHref2--------------");
                String mode = intent.getStringExtra("mode");
                String query = intent.getStringExtra("query");
                if (mode != null && (mode.equals("book")||mode.equals("video"))) {
                    try {
                        JSONObject queryJson = new JSONObject(query);
                        String id = queryJson.getString("id");
                        intent = new Intent(WebActivity.this, DetailControlActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (mode != null && mode.equals("audio")) {
                    try {
                        JSONObject queryJson = new JSONObject(query);
                        String id = queryJson.getString("id");
                        String vid = queryJson.getString("vid");

                        HttpInterface httpInterface = new HttpImplement();
                        httpInterface.learn_info(SharedPreferencesUtil.getToken(WebActivity.this), id, new OkHttpUtils.MyNetCall() {
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
                                        Intent intent = new Intent(WebActivity.this, YinpingDetailActivity.class);
                                        intent.putExtra("position", position);//当前音频位置
                                        intent.putExtra("data", data.toString());
                                        intent.putExtra("album",album.toString());

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
                    //音频列表
                     intent=new Intent(WebActivity.this, YinpingActivity.class);
                    JSONObject queryJson = null;
                    try {
                        queryJson = new JSONObject(query);
                        String id = queryJson.getString("id");
                        intent.putExtra("id",id);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

        }
    }


    public void uploadUrl(){
        url=getIntent().getStringExtra("url");
        User user = SharedPreferencesUtil.getLoginUser(WebActivity.this);
        String access_token = "";
        if (user!=null){
            access_token = user.getAccess_token();
        }
        Map<String, String> extraHeaders;
        extraHeaders = new HashMap<String, String>();
        extraHeaders.put("Authorization",
                "appname=syhapp&version=1.0&device_type=Android&app_auth=" + access_token);
        webView.loadUrl(url, extraHeaders);
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //拦截url，跳转到新的activity页面打开url
            System.out.println("url22:"+url);
            if (!(url.startsWith("http") || url.startsWith("https"))) {
                if (url.startsWith("tel:")){
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse(url);
                    intent.setData(data);
                    startActivity(intent);
                    return true;
                }

                return true;
            }

            /**
             * 推荐采用的新的二合一接口(payInterceptorWithUrl),只需调用一次
             */
            final PayTask task = new PayTask(WebActivity.this);
            boolean isIntercepted = task.payInterceptorWithUrl(url, true, new H5PayCallback() {
                @Override
                public void onPayResult(final H5PayResultModel result) {
                    // 支付结果返回
                    final String url = result.getReturnUrl();
                    if (url!=null&&!url.equals("")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                view.loadUrl(url);
                            }
                        });
                    }
                }
            });

            /**
             * 判断是否成功拦截
             * 若成功拦截，则无需继续加载该URL；否则继续加载
             */
            if (!isIntercepted) {
                Intent intent =new Intent(WebActivity.this, WebActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
            return true;


        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mIsRedirect = true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mIsRedirect = false;
            //重置webview中img标签的图片大小
//            HtmlUtil.imgReset(webView);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(webReceiver);
    }

    /**
     * 分享对话框
     */
    private void showShareDialog(String webUrl,String sTitle,String description) {
        Dialog bottomDialog = new Dialog(WebActivity.this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(WebActivity.this).inflate(R.layout.dialog_share, null);
        LinearLayout haoyouLay=contentView.findViewById(R.id.haoyouLay);
        LinearLayout pengyouquanLay=contentView.findViewById(R.id.pengyouquanLay);
        haoyouLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWebpage(webUrl,sTitle,description,0);
            }
        });
        pengyouquanLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWebpage(webUrl,sTitle,description,1);
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
    public void shareWebpage(String webUrl,String sTitle,String description,int scene) {
        IWXAPI api = WXAPIFactory.createWXAPI(WebActivity.this, Configs.APP_ID4WX);






        AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("分享至");
        final String[] cities = { "微信朋友圈", "微信好友" };
        RequestManager manager = Glide.with(WebActivity.this);


        //初始化一个WXWebpageObject，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl =webUrl;

//用 WXMusicObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage(webpage);


        Bitmap thumbBmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        msg.title = sTitle;
        msg.description = description;

        //设置音乐缩略图
        msg.thumbData = BitmapUtil.bmpToByteArray(thumbBmp,32);
//                    msg.thumbData = Util.bmpToByteArray(finalShareBitMap, true);

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


    };
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

}
