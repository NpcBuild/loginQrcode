package com.index.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class SchedulerConfig {

    @Bean(name = "downLoadThreadPool")
    public Executor bankExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //设置核心线程数为 3
        executor.setCorePoolSize(3);
        //最大线程数为10
        executor.setMaxPoolSize(10);
        //任务队列的大小
        executor.setQueueCapacity(3);
        //线程前缀名
        executor.setThreadNamePrefix("downLoad-");
        //线程存活时间
        executor.setKeepAliveSeconds(30);
        //初始化
        executor.initialize();
        return executor;
    }
}
