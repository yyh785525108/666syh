package com.yyh.read666.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import com.yyh.read666.MainActivity;
import com.yyh.read666.R;
import com.yyh.read666.configs.Configs;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.HttpInterface;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.utils.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

public class LoginActivity extends Activity {
	private Button loginBtn;
	private Button backBtn;
	// 隐藏或显示密码按钮
	private Button passCodeBtn;
	private EditText userNameEt, passWordEt;
	public ProgressDialog weixinDialog;
	private HttpInterface httpInterface;


	private static final int login = 0;
	WeiXinBrocast weiXinBrocast;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login_pass);

		backBtn=findViewById(R.id.back);
		loginBtn = (Button) findViewById(R.id.login);
		passCodeBtn = (Button) findViewById(R.id.login_request_code_btn);

		userNameEt = (EditText) findViewById(R.id.username_et);
		passWordEt = (EditText) findViewById(R.id.password_et);
		loginBtn.setOnClickListener(new BtnListener());
		backBtn.setOnClickListener(new BtnListener());
		passCodeBtn.setOnClickListener(new BtnListener());
		weiXinBrocast = new WeiXinBrocast();
		IntentFilter fil = new IntentFilter("weixin");
		IntentFilter fil2 = new IntentFilter("weixinDialog");

		registerReceiver(weiXinBrocast, fil);
		registerReceiver(weiXinBrocast, fil2);

	}

	class WeiXinBrocast extends BroadcastReceiver {


		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			System.out.println("----------------WeiXinBrocast-------------:"+intent.getAction());
			if (intent.getAction().equals("weixin")) {

				Intent it = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(it);

				finish();


			}
			if (intent.getAction().equals("weixinDialog")) {
				if (weixinDialog != null) {
					weixinDialog.hide();
				}
			}

		}
	}

	class BtnListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			switch (v.getId()) {

				case R.id.login:


					weixinDialog = ProgressDialog.show(LoginActivity.this, "请稍等",
							"正在登录");
					weixinDialog.show();
					final String username = userNameEt.getText().toString();
					final String password = passWordEt.getText().toString();

					HttpInterface httpInterface=new HttpImplement();
					httpInterface.login(username, password,  new OkHttpUtils.MyNetCall() {
						@Override
						public void success(Call call, final String response) throws IOException {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									weixinDialog.hide();
									try {
										JSONObject json=new JSONObject(response);
										int status=json.getInt("status");
										if (status==1) {
											JSONObject data=json.getJSONObject("data");


											SharedPreferencesUtil.saveLoginUser(LoginActivity.this,data);

											Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
											Intent intent=new Intent(LoginActivity.this, MainActivity.class);
											startActivity(intent);
											finish();

										}else{
											String info=json.getString("info");
											System.out.println("info:"+info);

											Toast.makeText(LoginActivity.this, info, Toast.LENGTH_SHORT).show();
										}

									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							});
						}

						@Override
						public void failed(Call call, IOException e) {

						}
					});


					break;


				case R.id.login_request_code_btn:
					if (passWordEt.getInputType()== InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD){
						passCodeBtn.setText("显示");
						passWordEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					}else {
						passCodeBtn.setText("隐藏");
						passWordEt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

					}
					break;
				case R.id.back:
						finish();
				break;



			}

		}


	}

	@Override
	protected void onDestroy() {
		if(weixinDialog != null) {
			weixinDialog.dismiss();
		}
		unregisterReceiver(weiXinBrocast);
		super.onDestroy();
	}
}