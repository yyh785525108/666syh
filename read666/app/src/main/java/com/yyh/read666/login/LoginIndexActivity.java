package com.yyh.read666.login;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

public class LoginIndexActivity extends Activity {

	private Button weixinLogin;
	private Button phoneLogin;
	private Button suibianBtn;
	public ProgressDialog weixinDialog;
	private IWXAPI api;
	private HttpInterface httpInterface;


	private static final int login = 0;
	WeiXinBrocast weiXinBrocast;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome1);

		api = WXAPIFactory.createWXAPI(this, Configs.APP_ID4WX, true);
		// 将该app注册到微信
		boolean isregister = api.registerApp(Configs.APP_ID4WX);


		weixinLogin = findViewById(R.id.weixinLogin);
		phoneLogin= findViewById(R.id.phoneLogin);
		suibianBtn=findViewById(R.id.suibianBtn);
		weixinLogin.setOnClickListener(new BtnListener());
		phoneLogin.setOnClickListener(new BtnListener());
		suibianBtn.setOnClickListener(new BtnListener());

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

				Intent it = new Intent(LoginIndexActivity.this, MainActivity.class);
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

				case R.id.weixinLogin:
					weixinDialog = ProgressDialog.show(LoginIndexActivity.this, "请稍等",
							"正在启动微信");
					weixinDialog.show();
					getToken();
					break;
				case R.id.phoneLogin:
					Intent intent=new Intent(LoginIndexActivity.this,LoginActivity.class);
					startActivity(intent);

					break;
				case R.id.suibianBtn:
					 intent=new Intent(LoginIndexActivity.this,MainActivity.class);
					startActivity(intent);
					finish();
					break;

			}

		}

		/**
		 * 微信授权登录
		 */
		public void getToken() {
			System.out.println("-------getToken---------");
			final SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "none";
			api.sendReq(req);
			// finish();
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