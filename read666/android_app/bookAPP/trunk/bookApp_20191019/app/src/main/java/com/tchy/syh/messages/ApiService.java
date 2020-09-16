package com.tchy.syh.messages;

import com.tchy.syh.messages.entity.MessageResp;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface ApiService {

    @Multipart
    @POST("cli/historymsg")
    Observable<MessageResp> message(@PartMap HashMap<String, RequestBody> params);


}
