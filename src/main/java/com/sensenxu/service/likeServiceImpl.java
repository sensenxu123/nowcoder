package com.sensenxu.service;

import com.sensenxu.util.redisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
public class likeServiceImpl implements likeService{

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void like(int userId, int entityType, int entityId , int entityUserId) {
        //String entityLikeKey = redisKeyUtil.getEntityLikeKey(entityType, entityId);
        ////第一次点 就加 再点赞 取消赞
        //Boolean isMebber = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        //if(isMebber){
        //    redisTemplate.opsForSet().remove(entityLikeKey, userId);
        //}else{
        //    redisTemplate.opsForSet().add(entityLikeKey, userId);
        //}
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                String entityLikeKey = redisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = redisKeyUtil.getUserLikeKey(entityUserId);
                Boolean isMebber = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
                //开启事务
                operations.multi();
                if(isMebber){
                    operations.opsForSet().remove(entityLikeKey, userId);
                    operations.opsForValue().decrement(userLikeKey);
                }else{
                    redisTemplate.opsForSet().add(entityLikeKey, userId);
                    operations.opsForValue().increment(userLikeKey);
                }

                return operations.exec();
            }
        });

    }
    //查询某实体点赞的数量
    public long findEntityLikeCount(int entityType, int entityId){
        String entityLikeKey = redisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }
    // 查询某人对某实体的点赞状态 返回int是因为之后可以扩展成-1 为踩的状态
    public int findEntityLikeStatus(int userId, int entityType, int entityId) {
        String entityLikeKey = redisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }
    //查询某个用户获得的赞
    public int findUserLikeCount(int userId){
        String userLikeKey = redisKeyUtil.getUserLikeKey(userId);
        Integer nums = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return  nums == null ? 0 : nums.intValue();

    }
}
