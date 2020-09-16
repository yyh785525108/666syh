package com.tchy.syh.search;

import com.tchy.syh.book.entity.BookListResp;
import com.tchy.syh.search.entity.HistoryWordsResp;
import com.tchy.syh.search.entity.SearchResultsResp;
import com.tchy.syh.search.entity.HotWordsResp;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface ApiService {

    @Multipart
    @POST("cli/search")
    Observable<HotWordsResp> hot(@PartMap HashMap<String, RequestBody> params);

    @Multipart
    @POST("cli/search")
    Observable<HistoryWordsResp> history(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/search")
    Observable<BookListResp> search(@PartMap HashMap<String, RequestBody> params);

}
