package com.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    @KafkaListener(topics = {"yftopic"})
    public void receiveMessage(ConsumerRecord<String,String> record) {   //进行消息接收处理
        System.err.println("【*** 接收消息 ***】 key=" + record.key() + "、value = " + record.value());
        System.out.printf("topic = %s,offset = %d,value = %s \n",record.topic(),record.offset(),record.value());
    }
}
