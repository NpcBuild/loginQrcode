package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling   //开启定时任务
public class QrcodeloginApplication {

    static {   //系统环境属性
        System.setProperty("java.security.auth.login.config","D:/AppData/work/loginQrcode/src/main/java/com/qrcodelogin/kafka_client_jaas.conf");
    }
    public static void main(String[] args) throws Exception{
        SpringApplication.run(QrcodeloginApplication.class, args);
    }

}
