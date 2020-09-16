package com.tchy.syh.orders;

import com.tchy.syh.common.entity.CommonDataListResp;
import com.tchy.syh.common.entity.CommonNoDataResp;
import com.tchy.syh.my.entity.MissionResp;
import com.tchy.syh.my.entity.MyResp;
import com.tchy.syh.my.entity.SpreadListResp;
import com.tchy.syh.my.entity.StudyListResp;
import com.tchy.syh.orders.entity.OrderResp;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface ApiService {

    @Multipart
    @POST("cli/order_list")
    Observable<OrderResp> order_list(@PartMap HashMap<String, RequestBody> params);



}
