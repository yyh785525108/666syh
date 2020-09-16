package com.tchy.syh.my;

import com.tchy.syh.common.entity.CommonDataListResp;
import com.tchy.syh.common.entity.CommonNoDataResp;
import com.tchy.syh.common.entity.CommonPayResp;
import com.tchy.syh.my.entity.BonusResp;
import com.tchy.syh.my.entity.BuyVIPResp;
import com.tchy.syh.my.entity.MyResp;
import com.tchy.syh.my.entity.MissionResp;
import com.tchy.syh.my.entity.RecommandListResp;
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
    @POST("cli/buyvip")
    Observable<BuyVIPResp> buyvip(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/fans_top")
    Observable<SpreadListResp> fans_top(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/order_list")
    Observable<OrderResp> order_list(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/paihan_list")
    Observable<StudyListResp> paihan_list(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/user_info")
    Observable<MyResp> user_info(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/task")
    Observable<MissionResp> task(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/task")
    Observable<CommonDataListResp> task1(@PartMap HashMap<String, RequestBody> params);

    @Multipart
    @POST("cli/integral_log")
    Observable<BonusResp> integral_log(@PartMap HashMap<String, RequestBody> params);


    @Multipart
    @POST("cli/qiandao")
    Observable<CommonNoDataResp> qiandao(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/book_vote")
    Observable<RecommandListResp> book_vote(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/goods_list")
    Observable<RecommandListResp> goods_list(@PartMap HashMap<String, RequestBody> params);
   
}
