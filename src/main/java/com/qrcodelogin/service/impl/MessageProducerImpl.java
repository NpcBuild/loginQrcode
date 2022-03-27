package com.qrcodelogin.service.impl;

import com.qrcodelogin.service.IMessageProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class MessageProducerImpl implements IMessageProducer {
    @Resource
    private KafkaTemplate<String,String> kafkaTemplate;   //kafka消息模块

    @Override
    public void send(String msg) {
        System.out.println("发送开始·······");
        this.kafkaTemplate.sendDefault("yftopic",msg);
        System.out.println("发送结束·······");
    }
}
