package com.sensenxu.util;

public interface communityConstant {
    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;
    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;
    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;
    /**
     * 默认登陆凭证的超时时间
     */
    int DEFAULT_EXPIRED_SECONDS = 3600*12;
    /**
     * 点击记住我的 默认登陆凭证的超时时间
     */
    int REMME_EXPIRED_SECONDS = 3600*24*100;
}
