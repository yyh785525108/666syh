package com.tchy.syh.collect;

import com.tchy.syh.book.book_detail.entity.BookDetailResp;
import com.tchy.syh.book.entity.BookListResp;
import com.tchy.syh.book.entity.LearnCate;
import com.tchy.syh.collect.entity.CollectResp;
import com.tchy.syh.collect.entity.ReadListResp;
import com.tchy.syh.listen.entity.AudioDetailEntity;
import com.tchy.syh.listen.entity.ResponseWrapper;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface ApiService {


    @Multipart
    @POST("cli/member_collect")
    Observable<CollectResp> member_collect(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/learn_info")
    Observable<ResponseWrapper<AudioDetailEntity>>  learn_info(@PartMap HashMap<String, RequestBody> params);
}
