package com.qrcodelogin.login;

import com.QrcodeloginApplication;
import com.qrcodelogin.service.IMessageProducer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest(classes = QrcodeloginApplication.class)  //定义要测试的SpringBoot类
@RunWith(SpringJUnit4ClassRunner.class)   //使用JUnit进行测试
@WebAppConfiguration   //进行Web应用配置
public class test {
    @Autowired
    private RedisTemplate<String, String> redisTemplate; //引入RedisTemplate
    @Autowired
    private IMessageProducer iMessageProducer;
    @Test
    public void testSet(){
        //操作String类型
        this.redisTemplate.opsForValue().set("mm","yf");
        System.out.println(this.redisTemplate.opsForValue().get("mm"));

        //操作List类型
        this.redisTemplate.opsForList().leftPush("list","vvv");
        this.redisTemplate.opsForList().leftPop("list");

        //操作Set类型
        this.redisTemplate.opsForSet().add("set","v_v_v");
        this.redisTemplate.opsForSet().pop("set");

        //操作Hash类型
        redisTemplate.opsForHash().put("hash","test","hello");
        System.out.println("Hash类型的数据操作:"+redisTemplate.opsForHash().get("hash","test"));

        //操作ZSet类型
        redisTemplate.opsForZSet().add("zset","z_v",1);
        redisTemplate.opsForZSet().add("zset","z_v_v",2);
        System.out.println("ZSet类型的数据操作："+redisTemplate.opsForZSet().range("zset",0,2));
    }

    @Test
    public void testKafka(){
        iMessageProducer.send("123456");
    }
}
