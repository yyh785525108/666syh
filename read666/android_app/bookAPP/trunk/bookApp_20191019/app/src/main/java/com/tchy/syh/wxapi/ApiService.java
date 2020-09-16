package com.tchy.syh.wxapi;

import com.tchy.syh.wxapi.entity.WXCallBackResp;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface ApiService {


    @Multipart
    @POST("cli/wx_callback")
    Observable<WXCallBackResp> wxCallBack(@PartMap HashMap<String, RequestBody> params);

}
