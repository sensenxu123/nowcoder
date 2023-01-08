package com.sensenxu;

import com.sensenxu.util.sensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class) //可以加载Springboot测试注解。
@SpringBootTest
public class sensitiveTest {


    @Autowired
    private sensitiveFilter sensitiveFilter;
    @Test
    public void testSensitiveFilter(){
        String text="这里不可以赌博，也不可以@嫖@娼@，但是能唱歌";
        String filter = sensitiveFilter.filter(text);
        System.out.println(filter);
    }
}
