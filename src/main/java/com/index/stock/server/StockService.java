package com.index.stock.server;

import com.qrcodelogin.entity.Stock;

public interface StockService {
    public int createWrongOrder(int sid) throws Exception;
    public Stock getStockById(int sid);
    public int updateStockById(Stock stock);
}
