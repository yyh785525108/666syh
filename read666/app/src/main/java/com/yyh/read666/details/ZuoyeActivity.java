package com.yyh.read666.details;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.yyh.read666.R;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.HttpInterface;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.utils.SharedPreferencesUtil;
import com.yyh.read666.vo.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

public class ZuoyeActivity extends Activity implements View.OnClickListener {
    private EditText bijiEt,juedingEt,kunhuoEt;
    private ImageView tongbuImg;
    private Button fabiaoBtn,back;
    private TextView statusTitleTv;
    private EditText daanEt1,daanEt2,daanEt3;
    private TextView ask1Tv,ask2Tv,ask3Tv;
    private String id;
    private ImageView headImg;
    private LinearLayout tongbuLay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_zuoye_write);
        initView();
        initData();

    }
    private void initView(){
        headImg=findViewById(R.id.headImg);
        tongbuLay=findViewById(R.id.tongbuLay);
        daanEt1=findViewById(R.id.daan1Et);
        daanEt2=findViewById(R.id.daan2Et);
        daanEt3=findViewById(R.id.daan3Et);
        ask1Tv=findViewById(R.id.ask1Tv);
        ask2Tv=findViewById(R.id.ask2Tv);
        ask3Tv=findViewById(R.id.ask3Tv);
        bijiEt=findViewById(R.id.bijiEt);
        juedingEt=findViewById(R.id.juedingEt);
        tongbuImg=findViewById(R.id.tongbuImg);
        fabiaoBtn=findViewById(R.id.fabiaoBtn);
        back=findViewById(R.id.back);
        statusTitleTv=findViewById(R.id.statusTitleTv);
        statusTitleTv.setText("作业");

        back.setOnClickListener(this);
        fabiaoBtn.setOnClickListener(this);
        tongbuImg.setOnClickListener(this);
        tongbuLay.setOnClickListener(this);
        User user=SharedPreferencesUtil.getLoginUser(ZuoyeActivity.this);
        if (user!=null){
            Glide.with(ZuoyeActivity.this).load(user.getAvatar()).into(headImg);
        }else {
            headImg.setImageResource(R.drawable.ic_launcher);
        }

    }

    private void  initData(){
         id=getIntent().getStringExtra("id");
        HttpInterface httpInterface=new HttpImplement();
        httpInterface.learn_book_work(SharedPreferencesUtil.getToken(this), "info", id, new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson = new JSONObject(response);
                    int status = responseJson.getInt("status");
                    if (status == 1) {
                        JSONObject data = responseJson.getJSONObject("data");
                        String book_id=data.getString("book_id");
                        String title1=data.getString("title1");
                        String title2=data.getString("title2");
                        String title3=data.getString("title3");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ask1Tv.setText(title1);
                                if (title2.equals("")){
                                    ask2Tv.setVisibility(View.GONE);
                                    daanEt2.setVisibility(View.GONE);
                                }else {
                                    ask2Tv.setText(title2);
                                }
                                if (title3.equals("")){
                                    ask3Tv.setVisibility(View.GONE);
                                    daanEt3.setVisibility(View.GONE);
                                }else {
                                    ask3Tv.setText(title3);
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
    public void onClick(View v) {
        if (v==back){
            finish();
        }else if (v==fabiaoBtn){
            String daan1=daanEt1.getText().toString();
            String daan2=daanEt2.getText().toString();
            String daan3=daanEt3.getText().toString();

            String biji=bijiEt.getText().toString();
            String jueding=juedingEt.getText().toString();
            if (daan1.equals("")){
                Toast.makeText(ZuoyeActivity.this,"请输入你的答案",Toast.LENGTH_SHORT).show();
                return;
            }
            if (biji.equals("")){
                Toast.makeText(ZuoyeActivity.this,"请输入你的笔记",Toast.LENGTH_SHORT).show();
                return;
            }
            if (jueding.equals("")){
                Toast.makeText(ZuoyeActivity.this,"请输入你的决定",Toast.LENGTH_SHORT).show();
                return;
            }
//            if (kunhuo.equals("")){
//                Toast.makeText(ZuoyeActivity.this,"请输入你的困惑",Toast.LENGTH_SHORT).show();
//                return;
//            }
            String is_sync=(tongbuImg.isSelected()?1:0)+"";

            HttpInterface httpInterface=new HttpImplement();
            httpInterface.learn_book_work(SharedPreferencesUtil.getToken(this), "publish", id,daan1,daan2,daan3,biji,jueding,is_sync, new OkHttpUtils.MyNetCall() {
                @Override
                public void success(Call call, String response) throws IOException {
                    try {
                        JSONObject responseJson = new JSONObject(response);
                        int status = responseJson.getInt("status");
                        if (status == 0) {
                            String info=responseJson.getString("info");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ZuoyeActivity.this,info,Toast.LENGTH_SHORT).show();
                                    finish();
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

        }else if (v==tongbuImg||v==tongbuLay){
            tongbuImg.setSelected(!tongbuImg.isSelected());
        }

    }
}
