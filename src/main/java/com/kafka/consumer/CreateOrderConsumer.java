package com.kafka.consumer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.index.stock.server.StockOrderService;
import com.kafka.domain.KafkaMessage;
import com.qrcodelogin.entity.Stock;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CreateOrderConsumer {

    private Gson gson = new GsonBuilder().create();

    @Autowired
    StockOrderService stockOrderService;

    @KafkaListener(topics = {"createOrderTopic"})
    public void receiveMessage(ConsumerRecord<String, String> record) throws Exception{   //进行消息接收处理
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        // Object -> String
        String message = (String) kafkaMessage.get();
        // 反序列化
        Stock stock = gson.fromJson((String) message, Stock.class);
        stockOrderService.consumerTopicToCreateOrderWithKafka(stock);
        System.err.println("【*** 接收消息 ***】 Id=" + stock.getId() + "、name = " + stock.getName());
        System.out.printf("topic = %s,offset = %d,message = %s \n","createOrderTopic",888,stock.getName());
    }
}
