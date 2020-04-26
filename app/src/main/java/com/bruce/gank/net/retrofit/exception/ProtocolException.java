package com.bruce.gank.net.retrofit.exception;

/**
 * Created by computer on 2018/4/26.
 * 开发协议约定的错误
 */

public class ProtocolException extends Exception {

    private int mStateCode;
    private String mErrorMsg;

    public ProtocolException(String errorMsg) {
        mErrorMsg = errorMsg;
    }

    public ProtocolException(int stateCode, String errorMsg) {
        mStateCode = stateCode;
        mErrorMsg = errorMsg;
    }

    public int getStateCode() {
        return mStateCode;
    }

    public void setStateCode(int stateCode) {
        mStateCode = stateCode;
    }

    public String getErrorMsg() {
        return mErrorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        mErrorMsg = errorMsg;
    }
}
