package com.bruce.gank.net.okhttp;

import java.util.Collections;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * 自定义 CookieStore 策略
 */
public class CookieJarImpl implements CookieJar {

    public static final String TAG = "CookieJar";

    private CookieStore cookieStore;

    public CookieJarImpl(CookieStore cookieStore)
    {
        if (cookieStore != null) this.cookieStore = cookieStore;
    }

    /**
     * OKHttp源码:
     * 1、从 response 中拿到响应头中 "Set-Cookie" 字段内容
     * 2、解析该内容，构建 Cookie 类,得到 List<Cookie>
     */
    @Override
    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies)
    {
        cookieStore.add(url, cookies);
    }

    /**
     *     HttpEngineer 中 networkRequest
     * 2、由 BridgeInterceptor 的 interceptor 方法，将 Cookie 添加到 RequestBuild 请求的构建中
     * @return 将返回尚未过期的已匹配的 cookie
     */
    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url) {
        boolean isNeedCookie = CookieHelper.isNeedCookie();
        if(isNeedCookie){
            return cookieStore.get(url);
        }else{
            return Collections.emptyList();
        }
     }

}
