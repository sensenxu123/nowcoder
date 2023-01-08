package com.sensenxu.controller;

import com.google.code.kaptcha.Producer;
import com.sensenxu.entity.User;
import com.sensenxu.service.userService;
import com.sensenxu.util.communityConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Controller
public class loginController implements communityConstant {
    @Autowired
    private userService userService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String getRegisterPage(){

        return "/site/register";
    }
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage(){

        return "/site/login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) throws IllegalAccessException {
        Map<String, Object> map = userService.register(user);
        if(map == null || map.isEmpty()){
            //map没有写报错信息，注册成功
            model.addAttribute("msg","注册成功，我们已经向您的邮箱发送了激活邮件，请尽快激活");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }else{

            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
        }

    }

    @RequestMapping(value = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId")int userId,@PathVariable("code")String code){

        int result = userService.activation(userId, code);
        if(result == ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功，账号已经可以使用了");
            model.addAttribute("target","/login");
        }else if(result == ACTIVATION_REPEAT){
            model.addAttribute("msg","该账号已激活");
            model.addAttribute("target","/index");
        }else if(result == ACTIVATION_FAILURE){
            model.addAttribute("msg","激活失败");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }
    @Autowired
    private Producer kaptchaProducer;

    private static final Logger logger = LoggerFactory.getLogger(loginController.class);
    @RequestMapping(value = "/kaptcha",method = RequestMethod.GET)
    public void getKaptchar(HttpServletResponse response, HttpSession session){
        //生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);
        //验证码存入session-服务器，以便检验
        session.setAttribute("kaptcha",text);
        //将图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image,"png",outputStream);
        } catch (IOException e) {
            logger.error("响应验证码失败"+e.getMessage());
        }


    }
    @Value("${server.servlet.context-path}")
    private String contextPath;


    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(String username, String password, String code, boolean remMe, Model model,HttpSession session, HttpServletResponse response){
        String  kaptcha = (String) session.getAttribute("kaptcha");
        //检查验证码
        if(StringUtils.isBlank(kaptcha) ||StringUtils.isBlank(code)||!kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg","验证码不正确");
            return "/site/login";
        }
        //检查账号密码
        int expiredSeconds = remMe ? REMME_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if(map.containsKey("ticket")){ //如果检查成功 map中会有ticket信息，失败的坏会有xxxMsg信息
            Cookie cookie = new Cookie("ticket",map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }else{ //错误时
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            return "/site/login";

        }

    }
    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/login"; //重定向默认get请求

    }
}
