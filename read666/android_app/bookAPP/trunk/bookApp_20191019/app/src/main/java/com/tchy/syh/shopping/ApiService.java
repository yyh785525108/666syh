package com.tchy.syh.shopping;

import com.tchy.syh.book.book_detail.entity.FreeBooksResp;
import com.tchy.syh.book.entity.AdsResp;
import com.tchy.syh.book.entity.BookListResp;
import com.tchy.syh.book.entity.ExtraBean;
import com.tchy.syh.book.entity.ExtraBean1;
import com.tchy.syh.book.entity.ExtraBean2;
import com.tchy.syh.book.entity.ExtraBean3;
import com.tchy.syh.book.entity.ExtraBean4;
import com.tchy.syh.book.entity.LearnCate;
import com.tchy.syh.comment.entity.CommentLoveResp;
import com.tchy.syh.common.entity.CommonDataListResp;
import com.tchy.syh.common.entity.CommonResp;
import com.tchy.syh.shopping.entity.AddressResp;
import com.tchy.syh.shopping.entity.GoodsDetailResp;
import com.tchy.syh.shopping.entity.OrderSubmitResp;
import com.tchy.syh.shopping.entity.ShopBookResp;

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
    @POST("cli/book_list")
    Observable<ShopBookResp> book_list(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/area")
    Observable<ShopBookResp> area(@PartMap HashMap<String, RequestBody> params);

    @Multipart
    @POST("cli/book_info")
    Observable<GoodsDetailResp> book_info(@PartMap HashMap<String, RequestBody> params);


    @Multipart
    @POST("cli/address")
    Observable<AddressResp> address(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/address")
    Observable<CommonResp> addressMod(@PartMap HashMap<String, RequestBody> params);

    @Multipart
    @POST("cli/address")
    Observable<CommonDataListResp> addressModList(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/ads")
    Observable<AdsResp> ads(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/ads")
    Observable<ExtraBean> extrasList(@PartMap HashMap<String, RequestBody> params);

    @Multipart
    @POST("cli/book_buy")
    Observable<OrderSubmitResp> book_buy(@PartMap HashMap<String, RequestBody> params);


    @Multipart
    @POST("cli/ads")
    Observable<ExtraBean1> e1(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/ads")
    Observable<ExtraBean2> e2(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/ads")
    Observable<ExtraBean3> e3(@PartMap HashMap<String, RequestBody> params);
    @Multipart
    @POST("cli/ads")
    Observable<ExtraBean4> e4(@PartMap HashMap<String, RequestBody> params);





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
    @POST("cli/member_like")
    Observable<CommentLoveResp> member_like(@PartMap HashMap<String, RequestBody> params);


}
