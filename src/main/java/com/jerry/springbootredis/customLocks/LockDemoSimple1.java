package com.jerry.springbootredis.customLocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @version 1.0
 * @Author jerryLau
 * @Date 2024/4/16 16:29
 * @注释 最简易的分布式锁实现 版本1
 */
@Component
public class LockDemoSimple1 {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /***
     * 尝试加锁
     */
    public boolean trySimpleLock1() {
        String lockKey = "lock:consumer";

        // 尝试获取锁
        String value = redisTemplate.opsForValue().get(lockKey);
        if (value == null) {
            // 加锁
            redisTemplate.opsForValue().set(lockKey, "1");
            return true;
        }
        return false;
    }

    /****
     * 释放锁
     */
    public void releaseSimpleLock1() {
        String lockKey = "lock:consumer";
        redisTemplate.delete(lockKey);
    }


}
