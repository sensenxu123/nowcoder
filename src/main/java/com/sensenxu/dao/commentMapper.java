package com.sensenxu.dao;

import com.sensenxu.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface commentMapper {
    List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit);
    int selectCountByEntity(int entityType, int entityId);
    //添加评论
    int insertComment(Comment comment);

    Comment selectCommentById(int id);
}
