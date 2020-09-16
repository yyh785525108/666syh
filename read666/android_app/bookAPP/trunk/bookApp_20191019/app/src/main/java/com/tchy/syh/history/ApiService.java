package com.tchy.syh.history;

import com.tchy.syh.book.book_detail.entity.BookDetailResp;
import com.tchy.syh.common.entity.CommonDataListResp;
import com.tchy.syh.history.entity.HistoryResp;
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
    @POST("cli/history_list")
    Observable<HistoryResp> history_list(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/history_list")
    Observable<CommonDataListResp> mHistory_list(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/learn_info")
    Observable<ResponseWrapper<AudioDetailEntity>> learn_info(@PartMap HashMap<String, RequestBody> params);
}
