package com.sensenxu;

import com.sensenxu.dao.discussPostMapper;
import com.sensenxu.dao.loginTicketMapper;
import com.sensenxu.entity.discussPost;

import com.sensenxu.entity.loginTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
@RunWith(SpringRunner.class) //可以加载Springboot测试注解。

@SpringBootTest
@ContextConfiguration(classes = NowcoderCommunityApplication.class) //加载配置类
public class mapperTest {
    @Autowired
    private discussPostMapper mapper;
    @Autowired
    private loginTicketMapper loginTicketMapper;

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
    @Test
    public void testLoginTicketMapper(){
        loginTicket ticket = new loginTicket();
        ticket.setTicket("abc");
        ticket.setUserId(11);
        ticket.setStatus(0);
        ticket.setExpired(new Date(System.currentTimeMillis() + 1000*60*10));
        loginTicketMapper.insertLoginTicket(ticket);

    }
    @Test
    public void testLoginTicketSelect(){

        loginTicket ticket = loginTicketMapper.selectByTicket("abc");
        System.out.println(ticket);
        loginTicketMapper.updateStatus("abc",12);


    }


}
