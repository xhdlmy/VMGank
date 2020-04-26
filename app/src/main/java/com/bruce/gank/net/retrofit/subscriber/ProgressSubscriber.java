package com.bruce.gank.net.retrofit.subscriber;


import com.bruce.gank.net.retrofit.resp.GankResp;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 全局控制网络请求的 ProgressBar
 * 默认每次请求都需要加载框显示，并且回退可以取消请求
 */
public class ProgressSubscriber implements Observer<GankResp> {

    private static final String TAG = ProgressSubscriber.class.getSimpleName();

    private Disposable mDisposable;


    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(GankResp gankResp) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
