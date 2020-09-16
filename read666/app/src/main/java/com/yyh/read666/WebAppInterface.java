package com.yyh.read666;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;

import com.tchy.syh.wxapi.Util;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yyh.read666.configs.Configs;
import com.yyh.read666.login.LoginActivity;
import com.yyh.read666.login.LoginIndexActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WebAppInterface {
	private boolean isBind = false;

	private ProgressDialog mProgressDialog;
	private ArrayList<Uri> list;
	Context mContext;
	String videoId;

	private IWXAPI api;

	private static final int VIDEO_NUM = 100;
	private static final int GETZB=101;
	public Handler uHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			
		
			default:
				break;
			}
		};
	};

	/** Instantiate the interface and set the context */
	public WebAppInterface(Context c) {
		mContext = c;
		api = WXAPIFactory.createWXAPI(mContext, Configs.APP_ID4WX);
		IntentFilter filter = new IntentFilter("unbind");


	}

	@JavascriptInterface
	public void shareWebpage(String jsonStr) {
		System.out.println("------------shareWebpage:" + jsonStr);
		JSONObject json;
		try {
			json = new JSONObject(jsonStr);
			final String imgUrl = json.getString("imgUrl");
			final String sendLink = json.getString("sendLink");
			final String sTitle = json.getString("sTitle");
			final String sContent = json.getString("sContent");

			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setIcon(R.drawable.ic_launcher);
			 builder.setTitle("分享至");
			final String[] cities = { "微信朋友圈", "微信好友" };

			builder.setItems(cities, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						WXWebpageObject webpage = new WXWebpageObject();
						webpage.webpageUrl = sendLink;
						WXMediaMessage msg = new WXMediaMessage(webpage);
						msg.title = sTitle;
						msg.description = sContent;
						// Bitmap
						// thumb=ImageLoader.getInstance().loadImageSync(imgUrl);
						Bitmap thumb = BitmapFactory.decodeResource(
								mContext.getResources(), R.drawable.ic_launcher);
						msg.thumbData = Util.bmpToByteArray(thumb, true);

						SendMessageToWX.Req req = new SendMessageToWX.Req();
						req.transaction = buildTransaction("webpage");
						req.message = msg;
						// req.scene = isTimelineCb.isChecked() ?
						// SendMessageToWX.Req.WXSceneTimeline :
						// SendMessageToWX.Req.WXSceneSession;
						req.scene = SendMessageToWX.Req.WXSceneTimeline;

						api.sendReq(req);
						break;

					case 1:
						webpage = new WXWebpageObject();
						webpage.webpageUrl = sendLink;
						msg = new WXMediaMessage(webpage);
						msg.title = sTitle;
						msg.description = sContent;
						thumb = BitmapFactory.decodeResource(
								mContext.getResources(), R.drawable.ic_launcher);
						msg.thumbData = Util.bmpToByteArray(thumb, true);

						req = new SendMessageToWX.Req();
						req.transaction = buildTransaction("webpage");
						req.message = msg;
						req.scene = SendMessageToWX.Req.WXSceneSession;

						api.sendReq(req);
						break;
					}
				}
			});
			builder.show();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@JavascriptInterface
	public void login() {
		System.out.println("------------login----------");
//		Intent intent=new Intent(mContext, LoginIndexActivity.class);
//		mContext.startActivity(intent);

	}



	@JavascriptInterface
	public void pay2Weixin(String json) {
		System.out.println("json:" + json);
		try {
			JSONObject payJson=new JSONObject(json);
			PayReq payReq=new PayReq();
			payReq.appId=payJson.getString("appid");
			payReq.partnerId=payJson.getString("partnerid");
			payReq.prepayId=payJson.getString("prepayid");
			payReq.nonceStr=payJson.getString("noncestr");
			payReq.timeStamp=payJson.getString("timestamp");
			payReq.sign=payJson.getString("sign");
			payReq.packageValue="Sign=WXPay";
			api.sendReq(payReq);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	@JavascriptInterface
	public void pay2Alipay(String json) {
		System.out.println("json:" + json);
		try {
			JSONObject payJson=new JSONObject(json);
//			PayReq payReq=new PayReq();
//			payReq.appId=payJson.getString("appid");
//			payReq.partnerId=payJson.getString("partnerid");
//			payReq.prepayId=payJson.getString("prepayid");
//			payReq.nonceStr=payJson.getString("noncestr");
//			payReq.timeStamp=payJson.getString("timestamp");
//			payReq.sign=payJson.getString("sign");
//			payReq.packageValue="Sign=WXPay";
//			api.sendReq(payReq);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	@JavascriptInterface
	public void hideToolBar(boolean type) {
		System.out.println("type:" + type);
	}

	@JavascriptInterface
	public void locationHref(String mode,String query) {

		System.out.println("mode:" + mode);
		System.out.println("query:" + query);
		Intent intent = new Intent();
		intent.putExtra("mode",mode);
		intent.putExtra("query",query);

		intent.setAction("locationHref");
		mContext.sendBroadcast(intent);
	}


	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}



}
