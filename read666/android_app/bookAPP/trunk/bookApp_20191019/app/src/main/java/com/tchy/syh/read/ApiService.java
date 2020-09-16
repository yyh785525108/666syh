package com.tchy.syh.read;

import com.tchy.syh.book.entity.LearnCate;
import com.tchy.syh.comment.entity.CommentLoveResp;
import com.tchy.syh.common.entity.CommonResp;
import com.tchy.syh.read.entity.ReadDetailResp;
import com.tchy.syh.read.entity.ReadListResp;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface ApiService {

    @Multipart
    @POST("cli/news_list")
    Observable<ReadListResp> news_list(@PartMap HashMap<String, RequestBody> params);

    @Multipart
    @POST("cli/learn_cate")
    Observable<LearnCate> learn_cate(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/news_info")
    Observable<ReadDetailResp> news_info(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/add_news_comment")
    Observable<CommonResp> add_news_comment(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/member_collect")
    Observable<CommentLoveResp> member_collect(@PartMap HashMap<String, RequestBody> params);

    @Multipart
    @POST("cli/member_like")
    Observable<CommentLoveResp> member_like(@PartMap HashMap<String, RequestBody> params);

    @Multipart
    @POST("cli/news_commentlove")
    Observable<CommentLoveResp> news_commentlove(@PartMap HashMap<String, RequestBody> params);
}
