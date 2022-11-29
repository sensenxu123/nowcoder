package com.sensenxu.service;

import com.sensenxu.dao.discussPostMapper;
import com.sensenxu.entity.discussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class discussPostServiceImpl implements discussPostService{
    @Autowired
    private discussPostMapper discussPostMapper;
    @Override
    public List<discussPost> findDiscussPosts(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    @Override
    public int selectDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }
}
