package com.kafka.producer;

import com.kafka.domain.KafkaMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ExecutionException;

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<Object,Object> kafkaTemplate;

    /**
     * 同步发送
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public SendResult<Object, Object> sendMsgSync(KafkaMessage kafkaMessageParam) throws ExecutionException, InterruptedException {
        System.out.println("发送开始·······");
        // 模拟发送的消息
        Integer id = new Random().nextInt(100);
        String name = kafkaMessageParam.getName();
        String message = kafkaMessageParam.getMessage();
        KafkaMessage kafkaMessage = new KafkaMessage(id,name,message);
        // 同步等待
        return  kafkaTemplate.send("yftopic", kafkaMessage).get();
    }
}
