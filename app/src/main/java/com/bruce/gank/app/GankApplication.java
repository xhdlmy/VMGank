package com.bruce.gank.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * Created time：2020/4/21
 * Author：Bruce
 * Function Description：
 */
public class GankApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }



}
