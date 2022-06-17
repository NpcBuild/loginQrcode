package com.redis.utils;

import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class RedisPool {

    private static JedisCluster  pool;

    private static Integer maxTotal = 300;

    private static Integer maxIdle = 100;

    private static Integer maxWait = 10000;

    private static Boolean testOnBorrow = true;

////    @Value("${spring.redis.host}")
//    private static String redisIP = "localhost";
////    @Value("${spring.redis.port}")
//    private static Integer redisPort = 6380;

    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setBlockWhenExhausted(true);
        config.setMaxWaitMillis(maxWait);

        Set<HostAndPort> shareInfos = new LinkedHashSet<HostAndPort>();
        shareInfos.add(new HostAndPort("localhost", 6380));
        shareInfos.add(new HostAndPort("localhost", 6381));
        shareInfos.add(new HostAndPort("localhost", 6382));
        shareInfos.add(new HostAndPort("localhost", 6383));
        shareInfos.add(new HostAndPort("localhost", 6384));
        shareInfos.add(new HostAndPort("localhost", 6385));

        pool = new JedisCluster(shareInfos, config);
    }

    // 类加载到 jvm 时直接初始化连接池
    static {
        initPool();
    }

    public static JedisCluster getJedis() {
        return pool;
    }

    public static void jedisPoolClose(JedisCluster jedis) {
        if (jedis != null) {
            try {
                jedis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
