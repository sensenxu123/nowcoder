package com.sensenxu.controller;

import com.sensenxu.entity.Comment;
import com.sensenxu.service.commentService;
import com.sensenxu.util.hostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class commentController {

    @Autowired
    private commentService commentService;
    @Autowired
    private hostHolder hostHolder;

    @RequestMapping(value = "/add/{discussPostId}",method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment){
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        if(comment.getTargetId() == null)comment.setTargetId(0);
        commentService.addComment(comment);

        return "redirect:/discuss/detail/"+discussPostId;
    }

}
