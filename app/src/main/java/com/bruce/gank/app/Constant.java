package com.bruce.gank.app;

import com.bruce.gank.R;

/**
 * Created time：2020/4/21
 * Author：Bruce
 * Function Description：
 */
public class Constant {

    public static final String CATEGORY_ARTICLE = GankApplication.getContext().getString(R.string.category_article);
    public static final String CATEGORY_GANHUO = GankApplication.getContext().getString(R.string.category_ganhuo);
    public static final String CATEGORY_GIRL = GankApplication.getContext().getString(R.string.category_girl);

    public static final String DATA_ALL = GankApplication.getContext().getString(R.string.info_all);
    public static final String DATA_ANDROID = GankApplication.getContext().getString(R.string.info_android);
    public static final String DATA_IOS = GankApplication.getContext().getString(R.string.info_ios);

    public static final int COUNT_10 = 10;

    /**
     * HTTP 协议相关
     */
    public static final String URL_BASE = "http://gank.io/api/v2/";
    // 超时时间
    public static final long CONNECT_TIMEOUT = 10000L;
    public static final long READ_TIMEOUT = 10000L;

}
