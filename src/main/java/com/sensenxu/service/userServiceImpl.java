package com.sensenxu.service;

import com.sensenxu.dao.userMapper;
import com.sensenxu.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userServiceImpl implements userService{
    @Autowired
    private userMapper userMapper;
    @Override
    public User findUserById(int userId) {
        return  userMapper.selectById(userId);
    }
}
