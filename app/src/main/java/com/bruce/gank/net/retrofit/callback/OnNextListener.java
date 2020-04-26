package com.bruce.gank.net.retrofit.callback;


import com.bruce.gank.net.retrofit.exception.ApiException;

/**
 * 回调接口（主要处理成功结果 T）
 */
public abstract class OnNextListener<T> {

    /**
     * 成功后回调方法
     * @param t
     */
    public abstract void onNext(T t);

    /**
     * 失败后
     * @param e
     */
    public void onError(ApiException e) {

    }

    /**
     * 取消回调
     */
    public void onCancel() {

    }

}
