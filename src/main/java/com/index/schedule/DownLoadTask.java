package com.index.schedule;

import com.kafka.domain.KafkaMessage;
import com.kafka.producer.KafkaProducer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Component
@EnableAsync   //开启对异步任务的支持,就可以使用多线程了
public class DownLoadTask {
    private static final Log logger = LogFactory.getLog(DownLoadTask.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    /**
     * 异步执行方法。从指定线程池获取新线程来执行方法
     */
    @Async("downLoadThreadPool")
    @Scheduled(cron = "0 0 0 * * ?")
    public void schedule(){
        logger.info("A开始下载···");
        logger.info("A下载完成，解析中···");
        logger.info("A解析完成");
    }

    @Async("downLoadThreadPool")
    @Scheduled(cron = "0 0 0 * * ?")
    public void schedule2(){
        logger.info("B开始下载···");
        logger.info("B下载完成，解析中···");
        logger.info("B解析完成");
    }


    @Async("downLoadThreadPool")
    @Scheduled(cron = "0 0 23 * * ?")
    public void schedule3(){
        logger.info("开始MQ生产···");
        KafkaMessage kafkaMessage = new KafkaMessage();
        kafkaMessage.setMessage("异步处理、流量削峰、");
        try {
            kafkaProducer.sendMsgSync(kafkaMessage);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("MQ生产完成");
    }

}
