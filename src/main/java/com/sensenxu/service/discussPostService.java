package com.sensenxu.service;

import com.sensenxu.entity.discussPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface discussPostService {
    List<discussPost> findDiscussPosts(int userId, int offset, int limit);
    int selectDiscussPostRows(int userId);
    int addDiscussPost(discussPost discussPost);
    discussPost findDiscussPostById(int id);
    int updateCommentCount(int postId, int commentCount);
    int updateType(int id, int type);
    int updateStatus(int id, int status);
    int updateScore(int id, double score);
}
