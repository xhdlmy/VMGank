package com.bruce.gank.net.retrofit.api;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created time：2020/4/21
 * Author：Bruce
 * Function Description：
 */
public interface GankApi {

    // Banner
    @GET("banners")
    Observable<ResponseBody> getBanner();

    // 分类
    @GET("categories/{categories_type}")
    Observable<ResponseBody> getCategory(@Path("categories_type") String categoryType);

    // 分类数据
    @GET("category/{category}/type/{type}/page/{page}/count/{count}")
    Observable<ResponseBody> getClassifyData(@Path("category") String category, @Path("type") String type, @Path("page") int pager, @Path("count") int count);

    // 数据详情
    @GET("post/{post_id}")
    Observable<ResponseBody> getDataDetail(@Path("post_id") String post_id);

}
