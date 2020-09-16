package com.tchy.syh.book;

import com.tchy.syh.book.book_detail.entity.FreeBooksResp;
import com.tchy.syh.book.entity.AdsResp;
import com.tchy.syh.book.entity.BookListResp;
import com.tchy.syh.book.entity.ExtraBean;
import com.tchy.syh.book.entity.LearnCate;
import com.tchy.syh.book.entity.VerionBean;
import com.tchy.syh.comment.entity.CommentLoveResp;
import com.tchy.syh.common.entity.CommonResp;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface ApiService {
    @Multipart
    @POST("cli/learn_cate")
    Observable<LearnCate> learn_cate(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/learn")
    Observable<BookListResp> learn(@PartMap HashMap<String, RequestBody> params);

    @Multipart
    @POST("cli/ads")
    Observable<AdsResp> ads(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/ads")
    Observable<ExtraBean> extrasList(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/free_list")
    Observable<FreeBooksResp> freeList(@PartMap HashMap<String, RequestBody> params);

    @Multipart
    @POST("cli/add_like")
    Observable<CommentLoveResp> addLike(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/member_collect")
    Observable<CommentLoveResp> member_collect(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/check_version")
    Observable<VerionBean> check_version(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/member_like")
    Observable<CommentLoveResp> member_like(@PartMap HashMap<String, RequestBody> params);


}
