package com.tchy.syh.listen;


import com.tchy.syh.comment.entity.CommentResp;
import com.tchy.syh.listen.entity.AudioCommentEntity;
import com.tchy.syh.listen.entity.AudioDetailEntity;
import com.tchy.syh.listen.entity.CollectResult;
import com.tchy.syh.listen.entity.PayInfo;
import com.tchy.syh.listen.entity.ResAudioList;
import com.tchy.syh.listen.entity.ResListen;
import com.tchy.syh.listen.entity.ResponseWrapper;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface ApiService {
    @FormUrlEncoded
    @POST("cli/learn")
    Observable<ResponseWrapper<ResListen>> listListenData(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("cli/learn_album")
    Observable<ResponseWrapper<ResAudioList>> listAudioData(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("cli/get_learn_comment")
    Observable<CommentResp> get_learn_comment(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("cli/audio_comment")
    Observable<CommentResp> audio_comment(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("cli/member_collect")
    Observable<ResponseWrapper<CollectResult>> member_collect(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("cli/member_like")
    Observable<ResponseWrapper<CollectResult>> member_like(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("cli/learn_buy")
    Observable<ResponseWrapper<PayInfo>> learn_buy(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("cli/history_list")
    Observable<ResponseWrapper> history_list(@FieldMap Map<String, String> params);


    @Multipart
    @POST("cli/learn_info")
    Observable<ResponseWrapper<AudioDetailEntity>> learn_info(@PartMap HashMap<String, RequestBody> params);


}
