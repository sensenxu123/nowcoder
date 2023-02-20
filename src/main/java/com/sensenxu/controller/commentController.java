package com.sensenxu.controller;

import com.sensenxu.entity.Comment;
import com.sensenxu.entity.Event;
import com.sensenxu.entity.discussPost;
import com.sensenxu.event.EventProducer;
import com.sensenxu.service.commentService;
import com.sensenxu.service.discussPostService;
import com.sensenxu.util.communityConstant;
import com.sensenxu.util.hostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@Controller
@RequestMapping("/comment")
public class commentController implements communityConstant {

    @Autowired
    private commentService commentService;
    @Autowired
    private hostHolder hostHolder;
    @Autowired
    private EventProducer eventProducer;
    @Autowired
    private discussPostService discussPostService;

    @RequestMapping(value = "/add/{discussPostId}",method = RequestMethod.POST)
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment){
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        if(comment.getTargetId() == null)comment.setTargetId(0);
        commentService.addComment(comment);

        //触发评论事件
        Event event = new Event().setTopic(TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityType(comment.getEntityType())
                .setEntityId(comment.getEntityId())
                .setData("postId", discussPostId);
        if(comment.getEntityType() == ENTITY_TYPE_POST){
            discussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId()); //存储帖子的作者
        } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());//获得评论的作者
        }

        //触发发帖事件






        return "redirect:/discuss/detail/"+discussPostId;
    }

}
