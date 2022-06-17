package com.redis.lock.service;

public interface RedisLockService {
    public void unLock(String key);
    public <T> T lockExecute(String key, LockService.LockExecute<T> lockExecute);
}
