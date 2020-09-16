package com.tchy.syh.wxapi;




import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yyh.read666.R;
import com.yyh.read666.configs.Configs;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

		 api = WXAPIFactory.createWXAPI(this, Configs.APP_ID4WX, true);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d("TAG", "onPayFinish, errCode = " + resp.errCode);
		System.out.println("onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {

			Intent intent = new Intent();
			intent.setAction("WxPay");
			if (resp.errCode==0){
				//支付成功
				intent.putExtra("status","1");
			}else if (resp.errCode==-2){
				//取消支付
				intent.putExtra("status","0");

			}else {
				//失败
				intent.putExtra("status","-1");
			}
			sendBroadcast(intent);
			finish();
		}
	}
}