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
    public int updateStockByOptimistic(Stock stock) {

        return stockMapper.updateByOptimistic(stock);
    }
}
