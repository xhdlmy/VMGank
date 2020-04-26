package com.bruce.gank.net.retrofit.exception;


import androidx.annotation.NonNull;

import com.bruce.gank.util.LogUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * retry() 是 Observer 接收 onError() 事件后触发重订阅
 */
public class RetryWhenNetworkException implements Function<Observable<Throwable>, ObservableSource<?>> {

    private static final String TAG = RetryWhenNetworkException.class.getSimpleName();
    private int index;
    private int COUNT = 2;
    private long DELAY = 2000L;

    public RetryWhenNetworkException() {
    }

    @Override
    public ObservableSource<?> apply(@NonNull Observable<Throwable> throwableObservable) throws Exception {
        /**
         * map 函数参数一般是 Function 的 <I,O>，实现 Function 的 apply 方法对 I 类型进行处理后返回 O 类型数据
         * flatMap 函数参数一般是 Function 的 <I,O>，不过这里O为Observable类型
         */
        return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {

            @Override
            public ObservableSource<?> apply(@NonNull Throwable throwable) throws Exception {
                ApiException exception = null;
                if(throwable instanceof ApiException) exception = (ApiException) throwable;
                if(exception == null) exception = new ApiException(throwable, ERROR.UNKNOWN);
                switch (exception.code) {
                    case ERROR.HTTP_ERROR:
                        LogUtils.i(TAG, "e:" + exception.code + "" + exception.message);
//                    case ERROR.NETWORK_ERROR:
                        index++; // 记录重试次数 +1
                        if(index <= COUNT){
                            return Observable.timer(DELAY * index, TimeUnit.MILLISECONDS);
                        }else{
                            return Observable.error(exception);
                        }
                    default:
                        return Observable.error(exception);
                }
            }
        });
    }

}
