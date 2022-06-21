package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling   //开启定时任务
public class QrcodeloginApplication /*extends SpringBootServletInitializer*/ {

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(QrcodeloginApplication.class);
//    }

    static {   //系统环境属性
        System.setProperty("java.security.auth.login.config","./qrcodelogin/kafka_client_jaas.conf");
    }
    public static void main(String[] args) throws Exception{
//        new SpringApplicationBuilder(QrcodeloginApplication.class).web(true).run(args);
        SpringApplication.run(QrcodeloginApplication.class, args);
    }

}
