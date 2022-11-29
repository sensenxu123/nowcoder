package com.sensenxu.controller;

import com.sensenxu.entity.Page;
import com.sensenxu.entity.User;
import com.sensenxu.entity.discussPost;
import com.sensenxu.service.discussPostService;
import com.sensenxu.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private discussPostService discussPostService;
    @Autowired
    private userService userService;
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){
        //方法调用前，model和page都会自动实例化，并且将page注入到model中
        page.setRows(discussPostService.selectDiscussPostRows(0));
        page.setPath("/index");

        List<discussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String , Object>> discussPosts = new ArrayList<>();
        if(list != null){
            for(discussPost post : list){
                Map<String , Object> map = new HashMap<>();
                map.put("post",post);
                int userId = post.getUserId();
                User user = userService.findUserById(userId);
                map.put("user", user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);

        return "/index";
    }

}
