package com.sensenxu.config;

import com.sensenxu.controller.AlphaInterceptor;
import com.sensenxu.controller.interceptor.loginRequiredInterceptor;
import com.sensenxu.controller.interceptor.loginTicketInterceptor;
import com.sensenxu.controller.interceptor.messageInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private AlphaInterceptor alphaInterceptor;
    @Autowired
    private loginTicketInterceptor loginTicketInterceptor;
    @Autowired
    private loginRequiredInterceptor loginRequiredInterceptor;
    @Autowired
    private messageInterceptor messageInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(alphaInterceptor)//拦截一切请求
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg")
        .addPathPatterns("/register","/login");

        registry.addInterceptor(loginTicketInterceptor)//拦截一切请求
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");
                //.addPathPatterns("/register","/login"); 拦截全部的  去除这一行

        registry.addInterceptor(loginRequiredInterceptor)//拦截一切请求
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");
        //.addPathPatterns("/register","/login"); 拦截全部的  去除这一行

        registry.addInterceptor(messageInterceptor)//拦截一切请求
                .excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpg","/**/*.jpeg");
        //.addPathPatterns("/register","/login"); 拦截全部的  去除这一行
    }
}
