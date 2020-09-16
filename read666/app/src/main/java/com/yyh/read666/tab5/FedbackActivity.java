package com.yyh.read666.tab5;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yyh.read666.R;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.HttpInterface;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.utils.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;

public class FedbackActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText contentEt,phoneEt;

    private TextView statusTitleTv;
    private Button backBtn,commitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_yijianfankui);
        //方式一：这句代码必须写在setContentView()方法的后面
        getSupportActionBar().hide();
        initView();


    }
    private void initView(){
        statusTitleTv=findViewById(R.id.statusTitleTv);
        backBtn=findViewById(R.id.back);
        commitBtn=findViewById(R.id.commitBtn);
        contentEt=findViewById(R.id.contentEt);
        phoneEt=findViewById(R.id.phoneEt);


        statusTitleTv.setText("意见反馈");
        commitBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view==backBtn){
            finish();
        }else if (view==commitBtn){
            String content=contentEt.getText().toString();
            String phone=phoneEt.getText().toString();

            if (content.equals("")){
                Toast.makeText(this,"请输入反馈信息",Toast.LENGTH_SHORT).show();
                return;
            }else {
                HttpInterface httpInterface=new HttpImplement();
                httpInterface.user_feedback(SharedPreferencesUtil.getToken(this), content, phone, new OkHttpUtils.MyNetCall() {
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
                                        Toast.makeText(FedbackActivity.this,info,Toast.LENGTH_SHORT).show();;
                                        finish();
                                    }else {
                                        Toast.makeText(FedbackActivity.this,info,Toast.LENGTH_SHORT).show();;
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
        }
    }


}
