package com.tchy.syh.wxapi;






import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.tchy.syh.R;
import com.tchy.syh.cons.WeixinCons;
import com.tchy.syh.dialog.LoadingDialog;
import com.tchy.syh.my.entity.MyResp;
import com.tchy.syh.utils.FormdataRequestUtil;
import com.tchy.syh.utils.RetrofitClient;
import com.tchy.syh.utils.TimeFormatUtil;
import com.tchy.syh.utils.ToastUtil;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.trello.rxlifecycle2.components.RxActivity;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.HashMap;

import io.reactivex.Observable;
import me.goldze.mvvmhabit.bus.Messenger;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.SPUtils;

public class WXPayEntryActivity extends RxActivity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	LoadingDialog loadingDialog;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    	api = WXAPIFactory.createWXAPI(this, WeixinCons.APP_ID);
//    	loadingDialog =new LoadingDialog();
//		loadingDialog.show(getFragmentManager(),"loading" );
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
//		loadingDialog.dismiss();
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(resp.errCode==-1){
				finish();
				ToastUtil.toastBottom("支付失败，请联系客服人员");
			}else if(resp.errCode==-2){
				finish();
				ToastUtil.toastBottom("支付取消！");
			}else{
				Messenger.getDefault().sendNoMsg("paySuccess");
				user_info();
				ToastUtil.toastBottom("支付成功！");
			}


		}

	}
	public void user_info(){
		HashMap<String, Object> params = new HashMap();

		params.put("access_token", SPUtils.getInstance().getString("token"));
//        params.put("page", this.pageNum);
		Observable<MyResp> observable = RetrofitClient.getInstance().create(com.tchy.syh.my.ApiService.class)
				.user_info(FormdataRequestUtil.getFormDataRequestParams(params))
				.compose(RxUtils.bindToLifecycle(this)) //请求与View周期同步
				.compose(RxUtils.schedulersTransformer()) //线程调度
				.compose(RxUtils.exceptionTransformer()) // 网络错误的异常转换, 这里可以换成自己的ExceptionHandle
				.doOnSubscribe(disposable -> {
				});
		observable.subscribe(v -> {
			if(v.getStatus()==1){


				SPUtils.getInstance().put("nickname", v.getData().getNickname());
				SPUtils.getInstance().put("avatar", v.getData().getAvatar());
				SPUtils.getInstance().put("birthday", v.getData().getBirthday());
				SPUtils.getInstance().put("sex", v.getData().getSex());
				SPUtils.getInstance().put("password", v.getData().getPassword());
				SPUtils.getInstance().put("level", v.getData().getLevel());
				SPUtils.getInstance().put("level_name", v.getData().getLevel_name());
				SPUtils.getInstance().put("deadline", v.getData().getDeadline());
				SPUtils.getInstance().put("parent_name", v.getData().getParent_name());
				SPUtils.getInstance().put("parent_id", v.getData().getParent_id());
				SPUtils.getInstance().put("money", v.getData().getMoney()+"");
				SPUtils.getInstance().put("integral",  v.getData().getIntegral()+"");
				SPUtils.getInstance().put("mobile",  v.getData().getMobile());

				SPUtils.getInstance().put("is_new",  v.getData().getIs_new());

				SPUtils.getInstance().put("fensi", v.getData().getFensi());
				SPUtils.getInstance().put("birthday", v.getData().getBirthday());
				SPUtils.getInstance().put("is_qiandao", v.getData().getIs_qiandao());

			}else{
			}
			finish();

		},e -> {
			e.printStackTrace();
		});

	}
}