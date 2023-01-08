package com.sensenxu.controller;

import com.sensenxu.entity.User;
import com.sensenxu.entity.discussPost;
import com.sensenxu.service.discussPostService;
import com.sensenxu.util.communityUtil;
import com.sensenxu.util.hostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class discussPostController {


    @Autowired
    private discussPostService disucssPostService;
    @Autowired
    private hostHolder hostHolder;

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content){
        User user = hostHolder.getUser();
        if(user == null ){
            return communityUtil.getJSONString(403,"你还没有登陆");
        }
        discussPost post = new discussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setType(0);
        post.setStatus(0);
        post.setCreateTime(new Date());
        disucssPostService.addDiscussPost(post);
        //报错的情况 之后统一处理
        return communityUtil.getJSONString(0,"发布成功");
    }
}
