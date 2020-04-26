package com.bruce.gank.net.retrofit.exception;

import android.content.Context;
import android.net.ParseException;

import com.bruce.gank.R;
import com.bruce.gank.app.GankApplication;
import com.bruce.gank.util.LogUtils;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import retrofit2.HttpException;


/**
 * Created by computer on 2018/4/28.
 * 网络请求框架中的错误分类
 */

public class ExceptionCategory {

    private static final String TAG = ExceptionCategory.class.getSimpleName();

    private static Context sContext;

    // 对应 HTTP 的错误传输状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;

    public static ApiException handleException(Throwable e) {
        sContext = GankApplication.getContext();
        ApiException exception;
        // 情况一：OkHttp 请求传输错误  HttpException 来自 Retrofit2
        if(e instanceof SocketTimeoutException) {
            SocketTimeoutException socketTimeoutException = (SocketTimeoutException) e;
        }
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            exception = new ApiException(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                default:
                    LogUtils.i(TAG, ((HttpException) e).message());
                    exception.message = sContext.getString(R.string.error_http);
                    break;
            }
            return exception;
        }
        // 情况二：后端约定协议错误
        else if (e instanceof ProtocolException) {
            ProtocolException protocolException = (ProtocolException) e;
            exception = new ApiException(e, protocolException.getStateCode());
            exception.message = protocolException.getErrorMsg();
            return exception;
        }
        // 情况三：json 解析错误
        else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            exception = new ApiException(e, ERROR.PARSE_ERROR);
            exception.message = sContext.getString(R.string.error_json);
            return exception;
        }
        // 情况四：请求之前检测到无可用网络
        else if (e instanceof NoNetException) {
            exception = new ApiException(e, ERROR.NO_NET_ERROR);
            exception.message = sContext.getString(R.string.error_no_net);
            return exception;
        }
        // 情况五：网络连接错误
        else if (e instanceof ConnectException) {
            exception = new ApiException(e, ERROR.NETWORK_ERROR);
            exception.message = sContext.getString(R.string.error_connect);  
            return exception;
        }
        // 情况六：网络Socket错误
        else if (e instanceof SocketTimeoutException || e instanceof TimeoutException) {
            exception = new ApiException(e, ERROR.NETWORK_ERROR);
            exception.message = sContext.getString(R.string.error_socket_timeout);
            return exception;
        }
        // 情况七：未知错误
        else {
            exception = new ApiException(e, ERROR.UNKNOWN);
            exception.message = sContext.getString(R.string.error_unknow);
            return exception;
        }
    }

}

