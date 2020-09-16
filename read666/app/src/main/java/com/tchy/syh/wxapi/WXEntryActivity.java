package com.tchy.syh.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yyh.read666.configs.Configs;
import com.yyh.read666.login.LoginActivity;
import com.yyh.read666.okhttp.HttpImplement;
import com.yyh.read666.okhttp.HttpInterface;
import com.yyh.read666.okhttp.OkHttpUtils;
import com.yyh.read666.utils.SharedPreferencesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "WXEntryActivity";
	private static final int GET_TOKEN=100;//获取微信token
	private static final int GET_INFO=101;//获取个人信息

	private static final int LOGIN=102;//获取个人信息后进行登陆

	private HttpInterface httpInterface;


//	private Handler uHandler=new Handler(){
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//				case GET_TOKEN:
//					String response=(String) msg.obj;
//					try {
//						JSONObject json=new JSONObject(response);
//						final String 	access_token=json.getString("access_token");
//						final String 	expires_in=json.getString("expires_in");
//						final String 	refresh_token=json.getString("refresh_token");
//						final String 	openid=json.getString("openid");
//						final String 	scope=json.getString("scope");
//						System.out.println("access_token:"+access_token+" expires_in:"+expires_in+" refresh_token"+refresh_token+" openid:"+openid+" scope:"+scope);
//
//						SharedPreferences sp=getSharedPreferences("user", Context.MODE_PRIVATE);
//						Editor et=sp.edit();
//						et.putString("access_token", access_token);
//						et.commit();
//
//						//获取用户个人信息（UnionID机制）
//						new Thread(new Runnable() {
//							@Override
//							public void run() {
//								// TODO Auto-generated method stub
//								String url="https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid;
//								String response= HttpUtils.getByUrl(url);
//								Message mes=new Message();
//								mes.what=GET_INFO;
//								mes.obj=response;
//								uHandler.sendMessage(mes);
//
//							}
//						}).start();
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//
//
//					break;
//
//				case GET_INFO:
//
//					response=(String) msg.obj;
//					try {
//						JSONObject json=new JSONObject(response);
//
//							final String 	openid=json.getString("openid");
//							final String 	nickname=json.getString("nickname");
//							final int 	sex=json.getInt("sex");
//							final String 	province=json.getString("province");
//							final String 	city=json.getString("city");
//							final String 	country=json.getString("country");
//							final String 	headimgurl=json.getString("headimgurl");
//							final String 	unionid=json.getString("unionid");
//							System.out.println("openid:"+openid+" nickname:"+nickname+" sex"+sex+" province:"+province+" city:"+city+" country:"+country+" headimgurl:"+headimgurl+" unionid:"+unionid);
//
//						com.alivc.live.pusher.demo.okhttp.HttpInterface httpInterface=new HttpImplement();
//						httpInterface.wx_callback(unionid, openid, nickname, headimgurl, 0 + "", new OkHttpUtils.MyNetCall() {
//							@Override
//							public void success(Call call, String response) throws IOException {
//								try {
//									JSONObject json=new JSONObject(response);
//									int status=json.getInt("status");
//									if (status==1){
//										JSONObject data=json.getJSONObject("data");
//										//这个是系统本身平台的
//										String access_token=data.getString("access_token");
//										int uid=Integer.parseInt(data.getString("uid"));
//										String nickname=data.getString("nickname");
//										String mobile=data.getString("mobile");
//										String avatar=data.getString("avatar");
//										String last_time=data.getString("last_time");
//
//										SharedPreferences sp=getSharedPreferences(Configs.SHARE_NAME, Context.MODE_PRIVATE);
//										Editor et=sp.edit();
//										et.putString("access_token", access_token);
//										et.putInt("uid", uid);
//										et.putString("nickname", nickname);
//										et.putString("mobile", mobile);
//										et.putString("avatar", avatar);
//										et.putString("last_time", last_time);
//
//										et.commit();
//										Intent intent= new Intent();
//										intent.setAction("weixin");
//										sendBroadcast(intent);
//									}else {
//										final String 	info=json.getString("info");
//										Toast.makeText(WXEntryActivity.this,info,Toast.LENGTH_SHORT).show();
//										Intent intent= new Intent();
//										intent.setAction("weixinDialog");
//										sendBroadcast(intent);
//									}
//
//
//								} catch (JSONException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//							}
//
//							@Override
//							public void failed(Call call, IOException e) {
//
//							}
//						});
//
//
//
//
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//					break;
//				case LOGIN:
//
//					response=(String) msg.obj;
//
//					System.out.println("response:"+response);
//					try {
//						JSONObject json=new JSONObject(response);
//						int status=json.getInt("status");
//						if (status==1){
//							JSONObject data=json.getJSONObject("data");
//							//这个是系统本身平台的
//							String access_token=data.getString("access_token");
//							int uid=Integer.parseInt(data.getString("uid"));
//							String nickname=data.getString("nickname");
//							String mobile=data.getString("mobile");
//							String avatar=data.getString("avatar");
//							String last_time=data.getString("last_time");
//
//							SharedPreferences sp=getSharedPreferences(Configs.SHARE_NAME, Context.MODE_PRIVATE);
//							Editor et=sp.edit();
//							et.putString("access_token", access_token);
//							et.putInt("uid", uid);
//							et.putString("nickname", nickname);
//							et.putString("mobile", mobile);
//							et.putString("avatar", avatar);
//							et.putString("last_time", last_time);
//
//							et.commit();
//							Intent intent= new Intent();
//							intent.setAction("weixin");
//							sendBroadcast(intent);
//						}else {
//							final String 	info=json.getString("info");
//							Toast.makeText(WXEntryActivity.this,info,Toast.LENGTH_SHORT).show();
//							Intent intent= new Intent();
//							intent.setAction("weixinDialog");
//							sendBroadcast(intent);
//						}
//
//
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//
//					break;
//
//
//			}
//		};
//	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IWXAPI api = WXAPIFactory.createWXAPI(this, Configs.APP_ID4WX, true);
		api.handleIntent(getIntent(), this);

//		httpInterface = new HttpService();

	}

	@Override
	public void onReq(BaseReq req) {
		// Log.e(TAG, "onReq...");
		System.out.println("onReq.....................................");
		Intent intent= new Intent();
		intent.setAction("weixinDialog");
		sendBroadcast(intent);
	}

	@Override
	public void onResp(BaseResp resp) {
		// Log.e(TAG, "onResp...");
		System.out.println("resp...");
		System.out.println("resp.errCode==="+resp.errCode);

		switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:// 用户同意,只有这种情况的时候code是有效的
				System.out.println("BaseResp.ErrCode.ERR_OK");
				try {
					String tCode2 = ((SendAuth.Resp) resp).code;
				} catch (Exception e) {
					System.out.println();
					finish();
				}
				final String tCode = ((SendAuth.Resp) resp).code;

				System.out.println("---------code-----------:"+tCode);

				String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ Configs.APP_ID4WX+"&secret="+Configs.APPSECRECT4WX+"&code="+tCode+"&grant_type=authorization_code";

				OkHttpUtils.getInstance().getDataAsyn(url, new OkHttpUtils.MyNetCall() {
					@Override
					public void success(Call call, String response) throws IOException {
						try {
							JSONObject json=new JSONObject(response);
							final String access_token=json.getString("access_token");
							final String expires_in=json.getString("expires_in");
							final String refresh_token=json.getString("refresh_token");
							final String openid=json.getString("openid");
							final String scope=json.getString("scope");
							System.out.println("access_token:"+access_token+" expires_in:"+expires_in+" refresh_token"+refresh_token+" openid:"+openid+" scope:"+scope);

							SharedPreferences sp=getSharedPreferences("user", Context.MODE_PRIVATE);
							Editor et=sp.edit();
							et.putString("access_token", access_token);
							et.commit();

							String url="https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid;
							OkHttpUtils.getInstance().getDataAsyn(url, new OkHttpUtils.MyNetCall() {
								@Override
								public void success(Call call, String response) throws IOException {
									try {
										JSONObject json=new JSONObject(response);

										final String openid=json.getString("openid");
										final String nickname=json.getString("nickname");
										final int 	sex=json.getInt("sex");
										final String province=json.getString("province");
										final String city=json.getString("city");
										final String country=json.getString("country");
										final String headimgurl=json.getString("headimgurl");
										final String unionid=json.getString("unionid");
										System.out.println("openid:"+openid+" nickname:"+nickname+" sex"+sex+" province:"+province+" city:"+city+" country:"+country+" headimgurl:"+headimgurl+" unionid:"+unionid);

										HttpInterface httpInterface=new HttpImplement();
										httpInterface.wx_callback(unionid, openid,headimgurl  , nickname,  new OkHttpUtils.MyNetCall() {
											@Override
											public void success(Call call, String response) throws IOException {
												try {
													JSONObject json=new JSONObject(response);
													int status=json.getInt("status");
													if (status==1){
														JSONObject data=json.getJSONObject("data");

														SharedPreferencesUtil.saveLoginUser(WXEntryActivity.this,data);

														Intent intent= new Intent();
														intent.setAction("weixin");
														sendBroadcast(intent);
													}else {
														final String info=json.getString("info");
														runOnUiThread(new Runnable() {
															@Override
															public void run() {
																Toast.makeText(WXEntryActivity.this,info, Toast.LENGTH_SHORT).show();
																Intent intent= new Intent();
																intent.setAction("weixinDialog");
																sendBroadcast(intent);
															}
														});

													}


												} catch (JSONException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											}

											@Override
											public void failed(Call call, IOException e) {

											}
										});




									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}

								@Override
								public void failed(Call call, IOException e) {

								}
							});


						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void failed(Call call, IOException e) {

					}
				});





				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:// 用户拒绝授权
				Intent intent= new Intent();
				intent.setAction("weixinDialog");
				sendBroadcast(intent);
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:// 用户取消
				intent= new Intent();
				intent.setAction("weixinDialog");
				sendBroadcast(intent);
				break;

			default:// 发送返回
				intent= new Intent();
				intent.setAction("weixinDialog");
				sendBroadcast(intent);
				break;
		}
		finish();
	}
}