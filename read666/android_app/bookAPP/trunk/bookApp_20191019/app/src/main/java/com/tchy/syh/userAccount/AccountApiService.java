package com.tchy.syh.userAccount;

import com.tchy.syh.common.entity.CommonDataListResp;
import com.tchy.syh.common.entity.CommonResp;
import com.tchy.syh.userAccount.entity.LoginResponseEntity;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface AccountApiService {
    @Multipart
    @POST("cli/login")
    Observable<LoginResponseEntity> login(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/mobile_login")
    Observable<LoginResponseEntity> mobile_login(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/check_mobile")
    Observable<CommonDataListResp> register(@PartMap HashMap<String, RequestBody> params);

    @Multipart
    @POST("cli/modify_password")
    Observable<CommonDataListResp> modify_password(@PartMap HashMap<String, RequestBody> params);

    @Multipart
    @POST("cli/reset_password")
    Observable<CommonDataListResp> reset_password(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/check")
    Observable<CommonResp> check(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/modify_user")
    Observable<CommonDataListResp> modify_user(@PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part file1);

    @Multipart
    @POST("cli/get_sms")
    Observable<CommonDataListResp> get_sms(@PartMap HashMap<String, RequestBody> params);

    @Multipart
    @POST("cli/member_idcard")
    Observable<CommonDataListResp> upload_idcard(@PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part file1, @Part MultipartBody.Part file2, @Part MultipartBody.Part file3);
    @Multipart
    @POST("cli/feedback")
    Observable<CommonDataListResp> feedBack(@PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part ...files);
}
