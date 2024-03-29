package com.sensenxu.controller.advice;



import com.sensenxu.util.communityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice(annotations = Controller.class)//只扫描带有Controller注解的bean
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);
    @ExceptionHandler({Exception.class})
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {

        logger.error("服务器发生异常"+e.getMessage());
        //把栈里所有的异常记录
        for(StackTraceElement element : e.getStackTrace()){
            logger.error(element.toString() );
        }
        //判断是普通请求还是异步请求
        String requestHeader = request.getHeader("x-requested-with");
        if(requestHeader.equals("XMLHttpRequest")){
            //异步请求返回json
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(communityUtil.getJSONString(1,"服务器异常"));
        }else{
           response.sendRedirect(request.getContextPath()+"/error");
        }



    }


}
