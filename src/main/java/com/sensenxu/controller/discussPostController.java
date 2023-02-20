package com.sensenxu.controller;
import com.sensenxu.entity.*;
import com.sensenxu.event.EventProducer;
import com.sensenxu.service.commentService;
import com.sensenxu.service.discussPostService;
import com.sensenxu.service.likeService;
import com.sensenxu.service.userService;
import com.sensenxu.util.communityUtil;
import com.sensenxu.util.hostHolder;
import com.sensenxu.util.redisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

import static com.sensenxu.util.communityConstant.*;

@Controller
@RequestMapping("/discuss")
public class  discussPostController {


    @Autowired
    private discussPostService discussPostService;
    @Autowired
    private userService userService;
    @Autowired
    private hostHolder hostHolder;
    @Autowired
    private likeService likeService;
    @Autowired
    private commentService commentService;

    @Autowired
    private EventProducer eventProducer;

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
        post.setCommentCount(0);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);




        //报错的情况 之后统一处理
        return communityUtil.getJSONString(0,"发布成功");
    }

    @RequestMapping(value = "/detail/{discussPostid}",method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable int discussPostid, Model model, Page page){
        //帖子
        discussPost post = discussPostService.findDiscussPostById(discussPostid);
        model.addAttribute("post",post);
        Integer userId = post.getUserId();
        //作者
        User user = userService.findUserById(userId);
        model.addAttribute("user",user);
        //点赞
        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostid);
        model.addAttribute("likeCount",likeCount);
        int likeStatus = hostHolder.getUser() == null ? 0 :
                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostid);
        model.addAttribute("likeStatus", likeStatus);
        //评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/"+discussPostid);
        page.setRows(post.getCommentCount());
        //拿到所有的评论，仅之后遍历用
        List<Comment> commentList = commentService.findCommentByEntity(ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        //评论：给帖子的评 回复：评论的评论
        //评论VO列表
        List<Map<String, Object>> commentVoList = new ArrayList<>();
        if(commentList != null){
            //遍历每一个一级评论，该评论可能有回复
            for(Comment comment : commentList){
                Map<String,Object> commentVo = new HashMap<>();
                commentVo.put("comment",comment);
                //组装一级评论的所属用户
                commentVo.put("user",userService.findUserById(comment.getUserId()));
                //查询出全部的评论的评论，仅遍历使用
                List<Comment> replyList = commentService.findCommentByEntity(ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);

                likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeCount", likeCount);
                likeStatus = hostHolder.getUser() == null ? 0 :
                        likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId());
                commentVo.put("likeStatus",likeStatus);


                //回复列表
                List<Map<String,Object>> replyVoList = new ArrayList<>();
                if(replyList != null){
                    //遍历所有的回复
                    for(Comment reply:replyList){
                        Map<String,Object> replyVo =new HashMap<>();
                        //回复
                        replyVo.put("reply",reply);
                        replyVo.put("user",userService.findUserById(reply.getUserId()));
                        //回复的目标 是否指向别的用户
                        User target = reply.getTargetId() == 0 ? null: userService.findUserById(reply.getTargetId());
                        replyVo.put("target",target);

                        likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeCount", likeCount);
                        likeStatus = hostHolder.getUser() == null ? 0 :
                                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId());
                        replyVo.put("likeStatus",likeStatus);


                        //整合所有的回复
                        replyVoList.add(replyVo);
                    }
                }
                //一级评论装配所有的回复
                commentVo.put("replys",replyVoList);
                //回复数量
                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, comment.getId());
                 commentVo.put("replyCount",replyCount);
                //整合所有的评论
                commentVoList.add(commentVo);
            }
        }
        model.addAttribute("comments",commentVoList);
        return  "/site/discuss-detail";

    }

    // 置顶
    @RequestMapping(path = "/top", method = RequestMethod.POST)
    @ResponseBody
    public String setTop(int id) {
        discussPostService.updateType(id, 1);

        // 触发发帖事件
        //Event event = new Event()
        //        .setTopic(TOPIC_PUBLISH)
        //        .setUserId(hostHolder.getUser().getId())
        //        .setEntityType(ENTITY_TYPE_POST)
        //        .setEntityId(id);
        //eventProducer.fireEvent(event);

        return communityUtil.getJSONString(0);
    }

    // 加精
    @RequestMapping(path = "/wonderful", method = RequestMethod.POST)
    @ResponseBody
    public String setWonderful(int id) {
        discussPostService.updateStatus(id, 1);

        // 触发发帖事件
        //Event event = new Event()
        //        .setTopic(TOPIC_PUBLISH)
        //        .setUserId(hostHolder.getUser().getId())
        //        .setEntityType(ENTITY_TYPE_POST)
        //        .setEntityId(id);
        //eventProducer.fireEvent(event);

        // 计算帖子分数
        //String redisKey = redisKeyUtil.getPostScoreKey();
        //redisTemplate.opsForSet().add(redisKey, id);

        return communityUtil.getJSONString(0);
    }

    // 删除
    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public String setDelete(int id) {
        discussPostService.updateStatus(id, 2);

        //// 触发删帖事件
        //Event event = new Event()
        //        .setTopic(TOPIC_DELETE)
        //        .setUserId(hostHolder.getUser().getId())
        //        .setEntityType(ENTITY_TYPE_POST)
        //        .setEntityId(id);
        //eventProducer.fireEvent(event);

        return communityUtil.getJSONString(0);
    }


}
