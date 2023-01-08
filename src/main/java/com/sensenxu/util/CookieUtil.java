package com.sensenxu.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {
    public static String getValue(HttpServletRequest request, String name){
        if(request == null || name == null){
            throw new IllegalArgumentException("参数为空");
        }
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie u : cookies){
                if(u.getName().equals(name))return u.getValue();
            }
        }
        return null;
    }
}
