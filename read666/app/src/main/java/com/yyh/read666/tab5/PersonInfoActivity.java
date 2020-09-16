package com.yyh.read666.tab5;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class PersonInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nameEt,emailEt,zhiyeEt,qianmingEt;
    private TextView birthdayEt;

    private TextView statusTitleTv;
    private Button backBtn,commitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_person_info);
        //方式一：这句代码必须写在setContentView()方法的后面
        getSupportActionBar().hide();
        initView();


    }
    private void initView(){
        statusTitleTv=findViewById(R.id.statusTitleTv);
        backBtn=findViewById(R.id.back);
        commitBtn=findViewById(R.id.commitBtn);
        nameEt=findViewById(R.id.nameEt);
        emailEt=findViewById(R.id.emailEt);
        birthdayEt=findViewById(R.id.birthdayEt);
        zhiyeEt=findViewById(R.id.zhiyeEt);
        qianmingEt=findViewById(R.id.qianmingEt);

        statusTitleTv.setText("会员中心");
        birthdayEt.setOnClickListener(this);
        commitBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view==backBtn){
            finish();
        }else if (view==birthdayEt){
            showDatePickerDialog(this,Calendar.getInstance());
        }else if (view==commitBtn){
            String name=nameEt.getText().toString();
            String email=emailEt.getText().toString();
            String birthDay=birthdayEt.getText().toString();
            String zhiye=zhiyeEt.getText().toString();
            String qianming=qianmingEt.getText().toString();
            if (name.equals("")||email.equals("")||birthDay.equals("")||zhiye.equals("")||qianming.equals("")){
                Toast.makeText(this,"请先完善所有信息",Toast.LENGTH_SHORT).show();
                return;
            }else {
                HttpInterface httpInterface=new HttpImplement();
                httpInterface.user_member(SharedPreferencesUtil.getToken(this), "setting", name, email, birthDay, zhiye, qianming, new OkHttpUtils.MyNetCall() {
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
                                        Toast.makeText(PersonInfoActivity.this,info,Toast.LENGTH_SHORT).show();;
                                        finish();
                                    }else {
                                        Toast.makeText(PersonInfoActivity.this,info,Toast.LENGTH_SHORT).show();;
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

    public  void showDatePickerDialog(Activity activity, Calendar calendar) {
        // Calendar 需要这样来得到
        // Calendar calendar = Calendar.getInstance();
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity,
                // 绑定监听器(How the parent is notified that the date is set.)
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // 此处得到选择的时间，可以进行你想要的操作
                        birthdayEt.setText(year+"-"+monthOfYear+"-"+dayOfMonth);

                    }
                }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                ,calendar.get(Calendar.MONTH)
                ,calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
