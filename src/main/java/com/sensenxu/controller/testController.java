package com.sensenxu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/test")
public class testController {
    @ResponseBody
    @RequestMapping("sayhello")
    public String sayHello(){
        return "hello sensenxu";
    }
}
