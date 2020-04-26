/*
 * Copyright (C) 2015 Drakeet <drakeet.me@gmail.com>
 *
 * This file is part of Meizhi
 *
 * Meizhi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Meizhi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Meizhi.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.bruce.gank.net.retrofit;

import com.bruce.gank.app.Constant;
import com.bruce.gank.net.okhttp.CookieJarImpl;
import com.bruce.gank.net.okhttp.OkClient;
import com.bruce.gank.net.okhttp.PersistentCookieStore;
import com.bruce.gank.net.retrofit.api.GankApi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 创建 Retrofit 实例
 */
public class RxRetrofit {

    private static Retrofit getRetrofit() {
        OkHttpClient client = OkClient.getInstance().getOkHttpClient();
        return getRetrofit(client);
    }

    private static Retrofit getRetrofit(OkHttpClient client) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(Constant.URL_BASE)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return builder.build();
    }

    public static CookieJarImpl getCookieJar(){
        return OkClient.getInstance().getCookieJar();
    }

    public static PersistentCookieStore getCookieStore(){
        return OkClient.getInstance().getCookieStore();
    }

    // 获取各种 Retrofit 的 Api
    public static GankApi getService() {
        return getRetrofit().create(GankApi.class);
    }

}
