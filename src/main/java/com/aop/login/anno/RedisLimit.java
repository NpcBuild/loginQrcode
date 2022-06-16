package com.aop.login.anno;

import java.lang.annotation.*;

/**
 * @author wow
 */
@Documented
//可以放到方法上
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLimit {
    /**
     * 标识参数名，必须是请求参数中的一个
     */
    String identifier();

    /**
     * 在多长的时间内监控，如希望在 60s 内尝试
     * 次数限制为 5 次，那么 watch=60; unit:s
     */
    long watch();

    /**
     * 锁定时长， unit:s
     */
    long lock();

    /**
     * 错误的尝试次数
     */
    int times();
}
