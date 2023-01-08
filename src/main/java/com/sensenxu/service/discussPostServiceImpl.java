package com.sensenxu.service;

import com.sensenxu.dao.discussPostMapper;
import com.sensenxu.entity.discussPost;
import com.sensenxu.util.sensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
@Service
public class discussPostServiceImpl implements discussPostService{
    @Autowired
    private discussPostMapper discussPostMapper;
    @Autowired
    private sensitiveFilter sensitiveFilter;
    @Override
    public List<discussPost> findDiscussPosts(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    @Override
    public int selectDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    @Override
    public int addDiscussPost(discussPost discussPost) {
        if(discussPost == null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //转译html标记
        discussPost.setTitle(HtmlUtils.htmlEscape(discussPost.getTitle()));
        discussPost.setContent(HtmlUtils.htmlEscape(discussPost.getContent()));
        //过滤敏感词
        discussPost.setTitle(sensitiveFilter.filter(discussPost.getTitle()));
        discussPost.setContent(sensitiveFilter.filter(discussPost.getContent()));
        return discussPostMapper.insertDiscussPost(discussPost);
    }
}
