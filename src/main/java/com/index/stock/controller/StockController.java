package com.index.stock.controller;

import com.aop.login.anno.RedisLimit;
import com.index.stock.server.StockOrderService;
import com.index.stock.server.StockService;
import com.qrcodelogin.Dao.StockMapper;
import com.qrcodelogin.entity.Stock;
import com.redis.limit.RedisOrderLimit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping(value = "/")
public class StockController {

    @Autowired
    private StockOrderService stockOrderService;


    @GetMapping("/buy")
    public String buy(){
        try {
            if (RedisOrderLimit.limit()) {
                stockOrderService.createOrder(1);

            }
        } catch (Exception e) {
            log.error("创建订单失败" + e);
        }
        return "秒杀请求正在处理，排队中";
    }
}
