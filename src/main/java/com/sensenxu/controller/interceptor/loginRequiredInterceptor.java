package com.sensenxu.controller.interceptor;

import com.sensenxu.annotation.loginRequired;
import com.sensenxu.util.hostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class  loginRequiredInterceptor implements HandlerInterceptor {
    @Autowired
    private hostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){//只拦截方法
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            loginRequired loginRequired = method.getAnnotation(loginRequired.class);
            if(loginRequired != null && hostHolder.getUser() == null){ //当前没登陆
                response.sendRedirect(request.getContextPath()+"/login");
                return false;
            }
        }


        return true;

    }
}
