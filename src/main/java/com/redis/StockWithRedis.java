package com.redis;

import com.qrcodelogin.entity.Stock;
import com.redis.utils.RedisPool;
import com.redis.utils.RedisPoolUtil;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Transaction;

import java.util.List;

@Slf4j
public class StockWithRedis {

    /**
     * Redis 事务保证库存更新
     * 捕获异常后应该删除缓存
     */
    public static void updateStockWithRedis(Stock stock) {
        JedisCluster jedis = null;
        try {
            jedis = RedisPool.getJedis();
            // todo 实现事务
            // 开始事务
//            Transaction transaction = jedis.multi();
            // 事务操作
            RedisPoolUtil.decr(RedisKeysConstant.STOCK_COUNT + stock.getId());
            RedisPoolUtil.incr(RedisKeysConstant.STOCK_SALE + stock.getId());
            RedisPoolUtil.incr(RedisKeysConstant.STOCK_VERSION + stock.getId());
            // 结束事务
//            List<Object> list = transaction.exec();
        } catch (Exception e) {
            log.error("updateStock 获取 Jedis 实例失败：", e);
        } finally {
            RedisPool.jedisPoolClose(jedis);
        }
    }

}
