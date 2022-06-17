package com.index.stock.server.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.index.stock.server.StockOrderService;
import com.qrcodelogin.Dao.StockOrderMapper;
import com.qrcodelogin.entity.Stock;
import com.redis.RedisKeysConstant;
import com.redis.utils.RedisPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class StockOrderServiceImpl implements StockOrderService {

    @Autowired
    private KafkaTemplate<Object,Object> kafkaTemplate;
    @Autowired
    private StockOrderMapper stockOrderMapper;

    @Value("${spring.kafka.template.createOrder-Topic}")
    private String kafkaTopic;

    private Gson gson = new GsonBuilder().create();

    @Override
    public int delOrderDBBefore() {
        return stockOrderMapper.delOrderDBBefore();
    }

    @Override
    public void createOrder(int sid) throws Exception {
        // 校验库存
        Stock stock = checkStockWithRedis(sid);
        // 下单请求发送至 kafka，需要序列化 stock
        kafkaTemplate.send(kafkaTopic, gson.toJson(stock));
        log.info("秒杀下单消息发送至 Kafka 成功");
    }

    /**
     * Redis 校验库存
     *
     * @param sid
     */
    private Stock checkStockWithRedis(int sid) throws Exception {
        Integer count = Integer.parseInt(RedisPoolUtil.get(RedisKeysConstant.STOCK_COUNT + sid));
        Integer sale = Integer.parseInt(RedisPoolUtil.get(RedisKeysConstant.STOCK_SALE + sid));
        Integer version = Integer.parseInt(RedisPoolUtil.get(RedisKeysConstant.STOCK_VERSION + sid));
        if (count < 1) {
            log.info("库存不足");
            throw new RuntimeException("库存不足 Redis currentCount: " + sale);
        }
        Stock stock = new Stock();
        stock.setId(sid);
        stock.setCount(count);
        stock.setSale(sale);
        stock.setVersion(version);
        // 此处应该是热更新，但是在数据库中只有一个商品，所以直接赋值
        stock.setName("手机");

        return stock;
    }
}
