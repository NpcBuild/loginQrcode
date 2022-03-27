package com.qrcodelogin.Dao;

import com.qrcodelogin.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    User findByAccount(@Param("account") String account);
}