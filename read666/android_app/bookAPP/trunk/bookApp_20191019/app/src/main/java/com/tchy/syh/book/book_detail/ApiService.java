package com.tchy.syh.book.book_detail;

import com.tchy.syh.book.book_detail.entity.BookDetailResp;
import com.tchy.syh.book.book_detail.entity.CollectResp;
import com.tchy.syh.comment.entity.CommentLoveResp;
import com.tchy.syh.comment.entity.CommentResp;
import com.tchy.syh.common.entity.CommonDataListResp;
import com.tchy.syh.common.entity.CommonPayResp;
import com.tchy.syh.common.entity.CommonResp;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface ApiService {
    @Multipart
    @POST("cli/haibao")
    Observable<CommonResp> haibao(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/task")
    Observable<CommonDataListResp> task(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/learn_info")
    Observable<BookDetailResp> learn_info(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/get_learn_comment")
    Observable<CommentResp> get_learn_comment(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/get_news_comment")
    Observable<CommentResp> get_news_comment(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/learn_time")
    Observable<CommonResp> learn_time(@PartMap HashMap<String, RequestBody> params);

    @Multipart
    @POST("cli/add_like")
    Observable<CollectResp> add_like(@PartMap HashMap<String, RequestBody> params);

    @Multipart
    @POST("cli/news_comment")
    Observable<CommentResp> news_comment(@PartMap HashMap<String, RequestBody> params);

    @Multipart
    @POST("cli/learn_commentlove")
    Observable<CommentLoveResp> learn_commentlove(@PartMap HashMap<String, RequestBody> params);

    @Multipart
    @POST("cli/news_commentlove")
    Observable<CommentLoveResp> news_commentlove(@PartMap HashMap<String, RequestBody> params);

    @Multipart
    @POST("cli/buyvip")
    Observable<CommonPayResp> buyvip(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/add_audio_comment")
    Observable<CommonResp> add_audio_comment(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/shang")
    Observable<CommonPayResp> shang(@PartMap HashMap<String, RequestBody> params);
}
