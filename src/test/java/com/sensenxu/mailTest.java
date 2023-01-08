package com.sensenxu;

import com.sensenxu.util.mailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringRunner.class) //可以加载Springboot测试注解。
@SpringBootTest
@ContextConfiguration(classes = NowcoderCommunityApplication.class) //加载配置类
public class mailTest {

    @Autowired
    private mailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void test(){
        mailClient.sendMail("975694749@qq.com","TEST","welcome");
    }
    @Test
    public void testHtmlEmail(){
        Context context = new Context();
        context.setVariable("username","sensenxu");
        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);
        mailClient.sendMail("975694749@qq.com","HTML",content);

    }

}
