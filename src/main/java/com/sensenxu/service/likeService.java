package com.sensenxu.service;

public interface likeService {
    //点赞
    void like(int userId, int entityType, int entityId,int entityUserId);
    //根据类型和该类型的id，查找post/comment/reply的数量
    long findEntityLikeCount(int entityType, int entityId);
    //当前用户是否点赞了
    int findEntityLikeStatus(int userId, int entityType, int entityId);
    int findUserLikeCount(int userId);
}
