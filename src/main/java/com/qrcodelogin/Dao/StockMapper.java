package com.qrcodelogin.Dao;

import com.qrcodelogin.entity.Stock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StockMapper {
    Stock getStockById (@Param("sid") int sid);
    int updateStockById (@Param("stock") Stock stock);
    /**
     * 乐观锁 version
     */
    @Update("UPDATE t_stock SET count = count - 1, sale = sale + 1, version = version + 1 WHERE " +
            "id = #{id, jdbcType = INTEGER} AND version = #{version, jdbcType = INTEGER}")
    int updateByOptimistic(Stock stock);
}
