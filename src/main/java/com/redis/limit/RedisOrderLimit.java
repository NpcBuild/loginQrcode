package com.redis.limit;

import com.redis.lock.service.LockService;
import com.redis.utils.RedisPool;
import com.redis.utils.ScriptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.Collections;

@Slf4j
public class RedisOrderLimit {
    private static final int FAIL_CODE = 0;

    private static Integer limit = 50;

    @Autowired
    private static LockService lockService;

    /**
     * Redis 限流
     */
    public static Boolean limit() {
        JedisCluster jedis = null;
        Object result = null;
        Long start = System.currentTimeMillis();
        try {
            // 获取 jedis 实例
            jedis = RedisPool.getJedis();
            // 解析 Lua 文件
            String script = ScriptUtil.getScript("limit.lua");
            // 请求限流
            String key = String.valueOf(System.currentTimeMillis() / 1000);
            // 计数限流
            result = jedis.eval(script, Collections.singletonList(key), Collections.singletonList(String.valueOf(limit)));
            if (FAIL_CODE != (Long) result) {
                log.info("成功获取令牌");
                return true;
            } else {
                log.info("请求太多被拦截");
            }
        } catch (Exception e) {
            log.error("limit 获取 Jedis 实例失败：", e);
        } finally {
            RedisPool.jedisPoolClose(jedis);
        }
        return false;
    }
}
