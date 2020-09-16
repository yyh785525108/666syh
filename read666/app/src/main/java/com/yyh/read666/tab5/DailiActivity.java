package com.yyh.read666.tab5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yyh.read666.R;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.HttpInterface;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.utils.SharedPreferencesUtil;
import com.yyh.read666.vo.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

public class DailiActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView statusTitleTv;
    private Button backBtn;

    private TextView leijiTv,ketiTv,jifenTv,fensiTv,shouquanmaTv;

    private HttpInterface httpInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_daili);
        //方式一：这句代码必须写在setContentView()方法的后面
        getSupportActionBar().hide();
        initView();
        initData();

    }
    private void initView(){
        leijiTv=findViewById(R.id.leijiTv);
        ketiTv=findViewById(R.id.ketiTv);
        jifenTv=findViewById(R.id.jifenTv);
        fensiTv=findViewById(R.id.fensiTv);
        shouquanmaTv=findViewById(R.id.shouquanmaTv);

        statusTitleTv=findViewById(R.id.statusTitleTv);
        statusTitleTv.setText("代理专区");
        backBtn=findViewById(R.id.back);

        backBtn.setOnClickListener(this);


    }

    private void initData(){
        httpInterface=new HttpImplement();
        User user= SharedPreferencesUtil.getLoginUser(this);
        String access_token="";
        if (user!=null){
            access_token = user.getAccess_token();
        }
        httpInterface.user_member(access_token, new OkHttpUtils.MyNetCall() {
            @Override
            public void success(Call call, String response) throws IOException {
                try {
                    JSONObject responseJson=new JSONObject(response);
                    int status=responseJson.getInt("status");
                    String info=responseJson.getString("info");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (status==1){
                                try {
                                    JSONObject data=responseJson.getJSONObject("data");
                                    int fensi=data.getInt("fensi");
                                    int order=data.getInt("order");
                                    double total_rmb=data.getDouble("total_rmb");
                                    double rmb=data.getDouble("rmb");
                                    int money=data.getInt("money");
                                    double total_shudou=data.getDouble("total_shudou");
                                    double shudou=data.getDouble("shudou");
                                    int sqm=data.getInt("sqm");

                                    leijiTv.setText(total_rmb+"");
                                    ketiTv.setText(rmb+"");
                                    jifenTv.setText(money+"");
                                    fensiTv.setText(fensi+"");
                                    shouquanmaTv.setText(sqm+"");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                Toast.makeText(DailiActivity.this,info,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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
        Intent intent;
        if (view==backBtn){
            finish();
        }
    }
}
