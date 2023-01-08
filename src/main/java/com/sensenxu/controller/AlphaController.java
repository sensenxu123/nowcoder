package com.sensenxu.controller;

import com.sensenxu.util.communityUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpResponse;

@Controller
@RequestMapping("/alpha")
public class AlphaController {


    @RequestMapping(value = "/cookie/set",method = RequestMethod.GET)
    @ResponseBody
    public String setCookie(HttpServletResponse response){
        Cookie cookie = new Cookie("name", "sensenxu");
        cookie.setPath("/community/alpha");
        response.addCookie(cookie);
        return "set cookie";

    }
    @RequestMapping(value = "/ajax",method = RequestMethod.POST)
    @ResponseBody
    public String testAjax(String name, int age){
        System.out.println(name);
        System.out.println(age);
        return communityUtil.getJSONString(0,"操作成功");
    }
}
