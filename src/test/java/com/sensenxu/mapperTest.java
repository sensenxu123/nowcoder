package com.sensenxu;

import com.sensenxu.dao.discussPostMapper;
import com.sensenxu.entity.discussPost;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
@RunWith(SpringRunner.class) //可以加载Springboot测试注解。

@SpringBootTest
@ContextConfiguration(classes = NowcoderCommunityApplication.class) //加载配置类
public class mapperTest {
    @Autowired
    private discussPostMapper mapper;

    @Test
    public void test(){
        List<discussPost> list = mapper.selectDiscussPosts(0, 0, 10);
        System.out.println(list);
    }
    @Test
    public void test1(){
        int rows = mapper.selectDiscussPostRows(0);
        System.out.println(rows);
    }
}
