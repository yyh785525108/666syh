package com.yyh.read666.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.igexin.sdk.GetuiPushException;
import com.igexin.sdk.IUserLoggerInterface;
import com.igexin.sdk.PushManager;
import com.tencent.bugly.Bugly;
import com.yyh.read666.MainActivity;
import com.yyh.read666.R;
import com.yyh.read666.WebActivity;
import com.yyh.read666.okhttp.UrlConstant;
import com.yyh.read666.tab5.GuanyuActivity;
import com.yyh.read666.utils.SharedPreferencesUtil;
import com.yyh.read666.welcome.EZActivity;


public class WelcomeActivity extends Activity {
    private ImageView welcomeImg = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        setContentView(R.layout.activity_welcome);
        //方式一：这句代码必须写在setContentView()方法的后面
        welcomeImg = (ImageView) this.findViewById(R.id.welcome_img);
        AlphaAnimation anima = new AlphaAnimation(1.0f, 1.0f);
        anima.setDuration(100);// 设置动画显示时间
        welcomeImg.startAnimation(anima);
        anima.setAnimationListener(new AnimationImpl());

        com.igexin.sdk.PushManager.getInstance().initialize(this);

        Bugly.init(getApplicationContext(), "e9294e00dc", false);





    }

    private class AnimationImpl implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            SharedPreferences sp=getSharedPreferences("welcome",MODE_PRIVATE);
             boolean isFirst=  sp.getBoolean("isFrist",true);
            if (isFirst){//是否第一次登陆

                startDialog();
            }else {
                String access_token= SharedPreferencesUtil.getToken(WelcomeActivity.this);
                System.out.println("access_token:"+access_token);
                if (access_token!=null&&!access_token.equals("")){
                    Intent intent=new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent=new Intent(WelcomeActivity.this,LoginIndexActivity.class);
                    startActivity(intent);
                    finish();
                }
            }





        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.widget_user_dialog);
            window.setGravity(Gravity.CENTER);

            TextView tvContent = window.findViewById(R.id.tv_content);
            TextView tvCancel = window.findViewById(R.id.tv_cancel);
            TextView tvAgree = window.findViewById(R.id.tv_agree);
            String str = "    感谢您对本公司的支持!本公司非常重视您的个人信息和隐私保护。" +
                    "为了更好地保障您的个人权益，在您使用我们的产品前，" +
                    "请务必审慎阅读《隐私政策》和《用户协议》内的所有条款，" +
                    "您点击“同意”的行为即表示您已阅读完毕并同意以上协议的全部内容。" +
                    "如您同意以上协议内容，请点击“同意”，开始使用我们的产品和服务!";

            SpannableStringBuilder ssb = new SpannableStringBuilder();
            ssb.append(str);
            final int start = str.indexOf("《");//第一个出现的位置
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
//                    Toast.makeText(SplashScreenActivity.this, "《隐私政策》", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(WelcomeActivity.this, WebActivity.class);
                    intent.putExtra("url", UrlConstant.YINSI);
                    startActivity(intent);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.blue));
                    ds.setUnderlineText(false);
                }
            }, start, start + 6, 0);

            int end = str.lastIndexOf("《");
            ssb.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View widget) {
                    Intent intent=new Intent(WelcomeActivity.this, WebActivity.class);
                    intent.putExtra("url", UrlConstant.GUANYU);
                    startActivity(intent);
                }

                @Override
                public void updateDrawState(@NonNull TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.blue));
                    ds.setUnderlineText(false);
                }
            }, end, end + 6, 0);

            tvContent.setMovementMethod(LinkMovementMethod.getInstance());
            tvContent.setText(ssb, TextView.BufferType.SPANNABLE);


            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.cancel();
                    finish();
                }
            });

            tvAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sp=getSharedPreferences("welcome",MODE_PRIVATE);
                    SharedPreferences.Editor et=sp.edit();
                    et.putBoolean("isFrist",false);
                    et.commit();
                    Intent intent=new Intent(WelcomeActivity.this, EZActivity.class);
                    startActivity(intent);
                    finish();
                    alertDialog.cancel();
                }
            });
        }

    }

}
