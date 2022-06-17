package com.index.stock.server;

import org.springframework.stereotype.Component;

/**
 * @author wow
 */
public interface StockOrderService {

    /**
     * 清空订单表
     */
    int delOrderDBBefore();

    /**
     * 限流 + Redis 缓存库存信息 + KafkaTest 异步发送消息
     *
     * @param sid
     */
    void createOrder(int sid) throws Exception;
}
