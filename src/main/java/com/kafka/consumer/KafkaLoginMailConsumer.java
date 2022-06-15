package com.kafka.consumer;

import com.kafka.domain.KafkaMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @author wow
 */
@Component
public class KafkaLoginMailConsumer {

    @Autowired
    private JavaMailSender javaMailSender;

    @KafkaListener(topics = {"loginMailTopic"})
    public void receiveMessage(KafkaMessage kafkaMessage) {   //进行消息接收处理
        /**
         * 登陆成功发送邮件提醒
         */
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("1623285565@qq.com");
        msg.setBcc();
        msg.setTo("zhazhafei@aliyun.com");
        msg.setSubject("登录");
        msg.setText("扫码登录成功提醒");
        try {
            javaMailSender.send(msg);
            System.out.printf("邮件发送成功！");
        } catch (MailException ex) {
            System.err.println(ex.getMessage());
        }
        System.err.println("【*** 接收登录邮件消息 ***】 Id=" + kafkaMessage.getId() + "、name = " + kafkaMessage.getName());
        System.out.printf("topic = %s,offset = %d,message = %s \n","loginMailTopic",000,kafkaMessage.getMessage());
    }
}
