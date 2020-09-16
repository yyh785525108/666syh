package com.yyh.read666.tab2;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.yyh.read666.R;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.HttpInterface;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.utils.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

public class PublishRijinjingActivity extends AppCompatActivity {

    private InputMethodManager imm;
    private EditText messageTextView;
    private Button confirmBtn;
    private RelativeLayout rlDlg;
    private int mLastDiff = 0;
    private TextView tvNumber;
    private int maxNumber = 5000;
    private Button back;
    private TextView statusTitleTv;

    private ImageView headImg;
    private TextView titleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏

        setContentView(R.layout.activity_pubulish_rijinjin);
        getSupportActionBar().hide();
        initView();

    }
    private void initView(){
        back=findViewById(R.id.back);
        statusTitleTv=findViewById(R.id.statusTitleTv);
        statusTitleTv.setText("发布日精进");
        headImg =findViewById(R.id.headImg);
        titleTv =findViewById(R.id.titleTv);
        String id=getIntent().getStringExtra("id");
        String vid=getIntent().getStringExtra("vid");
        String title=getIntent().getStringExtra("title");
        String thumb=getIntent().getStringExtra("thumb");

        Glide.with(this).load(thumb).into(headImg);
        titleTv.setText(title);
        JSONObject attach=new JSONObject();
        try {
            attach.put("type","audio");
            attach.put("id",Integer.parseInt(id));
            attach.put("vid",Integer.parseInt(vid));
        } catch (JSONException e) {
            e.printStackTrace();
        }



        messageTextView =findViewById(R.id.et_input_message);
        tvNumber = findViewById(R.id.tv_test);
        confirmBtn = findViewById(R.id.commitBtn);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=messageTextView.getText().toString();
                if (content.length()<20){
                    Toast.makeText(PublishRijinjingActivity.this,"请再分享多一点，至少20字",Toast.LENGTH_SHORT).show();
                    return;
                }
                HttpImplement httpImplement=new HttpImplement();
                httpImplement.rjj_blog(SharedPreferencesUtil.getToken(PublishRijinjingActivity.this),"publish" ,content, attach.toString(), new OkHttpUtils.MyNetCall() {
                    @Override
                    public void success(Call call, String response) throws IOException {
                        try {
                            JSONObject responseJson = new JSONObject(response);
                            int status = responseJson.getInt("status");
                            String info=responseJson.getString("info");
                            if (status == 1) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        Toast.makeText(PublishRijinjingActivity.this,info,Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(PublishRijinjingActivity.this,info,Toast.LENGTH_SHORT).show();

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


        messageTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvNumber.setText(editable.length() + "/" + maxNumber);
                if (editable.length() > maxNumber) {
                    /*dot_hong颜色值：#E73111,text_bottom_comment颜色值：#9B9B9B*/
                    tvNumber.setTextColor(getResources().getColor(R.color.dot_hong));
                } else {
                    tvNumber.setTextColor(getResources().getColor(R.color.text_bottom_comment));
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
