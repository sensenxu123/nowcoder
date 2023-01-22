package com.sensenxu.service;

import java.util.List;
import java.util.Map;

public interface followService {

    void follow(int userId, int entityType, int entityId);
    void unfollow(int userId, int entityType, int entityId);
    //查询当前用户关注的数量
    long findFolloweeCount(int userId, int entityType);
    long findFollowerCount(int entityType, int entityId);
    boolean hasFollowed(int userId, int entityType, int entityId);
    //查询某个用户要关注的人
    List<Map<String, Object>> findFollowees(int userId, int offset, int limit);
    //查询某个用户的粉丝
    List<Map<String, Object>> findFollowers(int userId, int offset, int limit);
}
