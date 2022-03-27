package com.qrcodelogin.service.impl;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {    //Kafka消息的消费程序类
    @KafkaListener(topics = {"yftopic"})
    public void receiveMessage(ConsumerRecord<String,String> record) {   //进行消息接收处理
        System.err.println("【*** 接收消息 ***】 key=" + record.key() + "、value = " + record.value());
        System.out.printf("topic = %s,offset = %d,value = %s \n",record.topic(),record.offset(),record.value());
    }
}
