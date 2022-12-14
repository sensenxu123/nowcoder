package com.sensenxu.dao;

import com.sensenxu.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface userMapper {

    User selectById(int id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(int id, int status);

    int updateHeader(int id, String headerUrl);

    int updatePassword(int id, String password);

}
