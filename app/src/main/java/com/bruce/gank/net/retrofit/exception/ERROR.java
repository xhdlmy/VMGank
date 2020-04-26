package com.bruce.gank.net.retrofit.exception;

/**
 * 跟后端约定没有重复的就 OK
 * 异常分类：
 * 1、请求连接错误、超时错误
 * 2、Http传输错误
 * 3、协议约定错误
 */
public class ERROR {
    /**
     * 未知错误
     */
    public static final int UNKNOWN = -256;
    /**
     * 网络错误
     */
    public static final int NETWORK_ERROR = -128;
    /**
     * HTTP传输错误
     */
    public static final int HTTP_ERROR = -56;
    /**
     * 解析错误
     */
    public static final int PARSE_ERROR = -32;
    /**
     * 检测无网络
     */
    public static final int NO_NET_ERROR = -16;
    /**
     *  json 数据出错
     */
    public static final int JSON_ERROR = -8;
    /**
     *  自定义出错
     */
    public static final int CUSTOM_ERROR = -4;
}
