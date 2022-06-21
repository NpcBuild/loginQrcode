package com.index.stock.server;

import com.qrcodelogin.entity.Stock;

public interface StockService {
    public Stock getStockById(int sid);


    /**
     * 乐观锁更新库存，解决超卖问题
     */
    int updateStockByOptimistic(Stock stock);
}
