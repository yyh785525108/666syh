package com.tchy.syh.comment;

import com.tchy.syh.book.book_detail.entity.FreeBooksResp;
import com.tchy.syh.book.entity.AdsResp;
import com.tchy.syh.book.entity.BookListResp;
import com.tchy.syh.book.entity.ExtraBean;
import com.tchy.syh.book.entity.LearnCate;
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
    @POST("cli/add_news_comment")
    Observable<CommonResp> add_news_comment(@PartMap HashMap<String, RequestBody> params);

    @Multipart
    @POST("cli/add_audio_comment")
    Observable<CommonResp> add_audio_comment(@PartMap HashMap<String, RequestBody> params);

}
