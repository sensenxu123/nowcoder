package com.sensenxu.dao;

import com.sensenxu.entity.discussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface discussPostMapper{
    //我发布的帖子 里面可以用这个
    List<discussPost> selectDiscussPosts(int userId, int offset, int limit);
    //Param 用于给参数取别名，如果只有一个参数，并且在<if>里使用，则必须加别名
    int selectDiscussPostRows(@Param("userId") int userId);




}
