package com.sensenxu.service;

import com.sensenxu.entity.Comment;

import java.util.List;

public interface commentService {
    List<Comment> findCommentByEntity(int entityType, int entityId, int offset, int limit);
    int findCommentCount(int entityType, int entityId);
    //添加评论
    int addComment(Comment comment);
    Comment findCommentById(int id);
}
