package com.bruce.gank.net.retrofit.exception;

/**
 * Created by computer on 2018/4/28.
 * 框架中所有错误的定义
 */

public class ApiException extends Exception {

    public int code;
    public String message = "";

    public ApiException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

}
