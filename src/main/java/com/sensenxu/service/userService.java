package com.sensenxu.service;

import com.sensenxu.entity.User;
import com.sensenxu.entity.loginTicket;

import java.util.Map;


public interface userService {

    int activation(int userId, String code);
    public User findUserById(int userId);
    public Map<String, Object> register(User user) throws IllegalAccessException;
    Map<String,Object> login(String username, String password, int expiredSeconds);
    void logout(String ticket);
    loginTicket findLoginTicket(String ticket);
    int updateHeader(int userId, String headUrl);
    int updatePassword(int userId, String password);

    User findUserByName(String toName);
}
