package com.sensenxu;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class) //可以加载Springboot测试注解。
@SpringBootTest
@ContextConfiguration(classes = NowcoderCommunityApplication.class) //加载配置类

public class kafkaTest {
    @Autowired
    private kafkaProducer kafkaProducer;


    @Test
    public void testKafka()  {
        kafkaProducer.sendMessage("test" , "hello");
        kafkaProducer.sendMessage("test" , "陈莉巍陈莉巍陈莉巍陈莉巍陈莉巍陈莉巍陈莉巍陈莉巍陈莉巍陈莉巍陈莉巍陈莉巍陈莉巍陈莉巍陈莉巍陈莉巍陈莉巍陈莉巍");
        try {
            Thread.sleep(1000*5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
@Component
class kafkaProducer{
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(String topic, String content){
        kafkaTemplate.send(topic, content);
    }

}
@Component
class kafkaConsumer{
    @KafkaListener(topics = {"test"})
    public void handleMessage(ConsumerRecord record){
        System.out.println(record.value());
    }
}
