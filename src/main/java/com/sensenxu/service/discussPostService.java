package com.sensenxu.service;

import com.sensenxu.entity.discussPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface discussPostService {
    List<discussPost> findDiscussPosts(int userId, int offset, int limit);
    int selectDiscussPostRows(int userId);
    int addDiscussPost(discussPost discussPost);
}
