package com.index.stock.server.impl;

import com.index.stock.server.StockService;
import com.qrcodelogin.Dao.StockMapper;
import com.qrcodelogin.Dao.StockOrderMapper;
import com.qrcodelogin.entity.Stock;
import com.qrcodelogin.entity.StockOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockService stockService;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private StockOrderMapper stockOrderMapper;


    @Override
    public Stock getStockById(int sid) {
        return stockMapper.getStockById(sid);
    }
    @Override
    public int updateStockById(Stock stock) {
        return stockMapper.updateStockById(stock);
    }

    @Override
    public int createWrongOrder(int sid) throws Exception {
        /**
         * 未处理逻辑
         */
//        // 数据库校验库存
//        Stock stock = checkStock(sid);
//        // 扣库存(无锁)
//        saleStock(stock);
//        // 生成订单
//        stock.setName(stock.getName());
//        int res = createOrder(stock);
//        return res;

        // 校验库存
        Stock stock = checkStock(sid);
        // 乐观锁更新
        saleStockOptimstic(stock);
        // 创建订单
        int id = createOrder(stock);

        return id;
    }
    /**
     * 乐观锁扣库存
     */
    private void saleStockOptimstic(Stock stock) throws Exception {
        int count = updateStockByOptimistic(stock);
        if (count == 0) {
            throw new RuntimeException("并发更新库存失败");
        }
    }

    private Stock checkStock(int sid) throws Exception {
        Stock stock = stockService.getStockById(sid);
        if (stock.getCount() < 1) {
            throw new RuntimeException("库存不足");
        }
        return stock;
    }
    private int saleStock(Stock stock) {
        stock.setSale(stock.getSale() + 1);
        stock.setCount(stock.getCount() - 1);
        log.info("卖出去了！");
        return stockService.updateStockById(stock);
    }
    private int createOrder(Stock stock) throws Exception {
        StockOrder order = new StockOrder();
        order.setSid(stock.getId());
        order.setName(stock.getName());
        order.setCreate_time(new Date());
        int res = stockOrderMapper.insertSelective(order);
        if (res == 0) {
            throw new RuntimeException("创建订单失败");
        }
        return res;
    }

    @Override
    public int updateStockByOptimistic(Stock stock) {

        return stockMapper.updateByOptimistic(stock);
    }
}
