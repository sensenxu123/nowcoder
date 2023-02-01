package com.sensenxu.service;

import com.sensenxu.dao.commentMapper;
import com.sensenxu.entity.Comment;
import com.sensenxu.util.communityConstant;
import com.sensenxu.util.sensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
@Service
public class commentServiceImpl implements commentService , communityConstant {

    @Autowired
    private commentMapper commentMapper;
    @Autowired
    private sensitiveFilter sensitiveFilter;
    @Autowired
    private discussPostService discussPostService;
    @Override
    public List<Comment> findCommentByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentByEntity(entityType,entityId,offset,limit);
    }

    @Override
    public int findCommentCount(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType,entityId);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        if(comment == null)throw new IllegalArgumentException("参数不能为空");
        //转译特殊字符
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        //过滤敏感词
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int rows = commentMapper.insertComment(comment);
        //更新评论数量
        if(comment.getEntityType() == ENTITY_TYPE_POST) {//只更新评论 not回复 entityId指评论所对应的帖子id
            int count = commentMapper.selectCountByEntity(comment.getEntityType(),comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(),count);
        }
        return rows;

    }

    @Override
    public Comment findCommentById(int id) {
        return commentMapper.selectCommentById(id);
    }
}
