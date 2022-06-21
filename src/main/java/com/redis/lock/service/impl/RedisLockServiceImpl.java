package com.redis.lock.service.impl;

import com.redis.lock.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.UUID;

@Service
@Slf4j
public class RedisLockServiceImpl implements LockService {

    private static final String LOCK_SUCCESS = "OK";

    @Autowired
    protected RedisTemplate<String, String> redisTemplate;

    @Override
    public void unLock(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public synchronized boolean isLock(String key, int seconds) {
        /**
         * 存储数据到缓存中，并制定过期时间和当Key存在时是否覆盖。
         * nxxx的值只能取NX或者XX，如果取NX，则只有当key不存在是才进行set，如果取XX，则只有当key已经存在时才进行set
         * expx的值只能取EX或者PX，代表数据过期时间的单位，EX代表秒，PX代表毫秒。
         * time 过期时间，单位是expx所代表的单位。
         */
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            // 将value设置为当前时间戳+随机数
            String lockValue = System.currentTimeMillis() + UUID.randomUUID().toString();

            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
//                Jedis conn = (Jedis) connection.getNativeConnection();
//                String result = conn.set(key, lockValue, "NX", "EX", seconds);
//                return result != null && result.equalsIgnoreCase(LOCK_SUCCESS);
                Object nativeConnection = connection.getNativeConnection();
                String result = null;
                // 集群
                if (nativeConnection instanceof JedisCluster) {
                    result = ((JedisCluster) nativeConnection).set(key, lockValue, "NX", "EX", seconds);
                }
                // 单机
                if (nativeConnection instanceof Jedis) {
                    result = ((Jedis) nativeConnection).set(key, lockValue, "NX", "EX", seconds);
                }
                return result != null && result.equalsIgnoreCase(LOCK_SUCCESS);
            }
        });
    }

    @Override
    public <T> T lockExecute(String key, LockService.LockExecute<T> lockExecute) {
        boolean isLock = isLock(key, 15);
        final int SLEEP_TIME = 200;
        final int RETRY_NUM = 20;
        int i;
        for (i = 0; i < RETRY_NUM; i++) {
            if (isLock) {
                break;
            }
            try {
                log.debug("wait redis lock key > {}", key);
                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                log.warn("wait redis error {}", e.getMessage());
            }
            isLock = isLock(key, 150);
        }
        if (!isLock) {
            log.warn("wait lock time out key > {}", key);
            return lockExecute.waitTimeOut();
        }
        try {
            if (i > 0) {
                log.debug("wait lock retry count {}", i);
            }
            return lockExecute.execute();
        } finally {
            unLock(key);
        }
    }
}
