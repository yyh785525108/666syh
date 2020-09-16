package com.yyh.read666.tab5;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

import okhttp3.Call;

public class PassSettingActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText oldPassEt,newPassEt,newPassCorfirmEt;

    private TextView statusTitleTv;
    private Button backBtn,commitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pass_setting);
        //方式一：这句代码必须写在setContentView()方法的后面
        getSupportActionBar().hide();
        initView();


    }
    private void initView(){
        statusTitleTv=findViewById(R.id.statusTitleTv);
        backBtn=findViewById(R.id.back);
        commitBtn=findViewById(R.id.commitBtn);
        oldPassEt=findViewById(R.id.oldPassEt);
        newPassCorfirmEt=findViewById(R.id.newPassCorfirmEt);
        newPassEt=findViewById(R.id.newPassEt);

        statusTitleTv.setText("修改密码");

        backBtn.setOnClickListener(this);
        commitBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view==backBtn){
            finish();
//            findViewById(R.id.shuodianLay).setVisibility(View.VISIBLE);
//            openKeyBoard((EditText)findViewById(R.id.shuodianContentEt));

        }else if (view==commitBtn){
            String oldPass=oldPassEt.getText().toString();
            String newPass=newPassEt.getText().toString();
            String newPassCorfirm=newPassCorfirmEt.getText().toString();

            if (oldPass.equals("")||newPass.equals("")||newPassCorfirm.equals("")){
                Toast.makeText(this,"请先完善所有信息",Toast.LENGTH_SHORT).show();
                return;
            }else if (!newPass.endsWith(newPassCorfirm)){
                Toast.makeText(this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                return;
            }else {
                HttpInterface httpInterface=new HttpImplement();
                httpInterface.user_member(SharedPreferencesUtil.getToken(this), "password", oldPass, newPass, newPassCorfirm,  new OkHttpUtils.MyNetCall() {
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
                                        Toast.makeText(PassSettingActivity.this,info,Toast.LENGTH_SHORT).show();;
                                        finish();
                                    }else {
                                        Toast.makeText(PassSettingActivity.this,info,Toast.LENGTH_SHORT).show();;
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
}
