package com.kafka.consumer;

import com.kafka.domain.KafkaMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    @KafkaListener(topics = {"yftopic"})
    public void receiveMessage(KafkaMessage kafkaMessage) {   //进行消息接收处理
        System.err.println("【*** 接收消息 ***】 Id=" + kafkaMessage.getId() + "、name = " + kafkaMessage.getName());
        System.out.printf("topic = %s,offset = %d,message = %s \n","yftopic",999,kafkaMessage.getMessage());
    }
}
