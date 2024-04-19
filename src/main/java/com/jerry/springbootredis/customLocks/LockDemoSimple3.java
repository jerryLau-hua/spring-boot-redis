package com.jerry.springbootredis.customLocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @version 1.0
 * @Author jerryLau
 * @Date 2024/4/16 16:29
 * @注释 最简易的分布式锁实现 版本3 保证原子性
 */
@Component
public class LockDemoSimple3 {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    private String value;
    private String lockKey = "lock:consumer";

    /***
     * 尝试加锁
     */
    public boolean trySimpleLock1() {
        // 尝试获取锁
        String uuid = UUID.randomUUID().toString();
        // 原子性操作setNX
        if (redisTemplate.opsForValue().setIfAbsent(lockKey, uuid)) {
            // 加锁
            this.value = uuid;
            return true;
        }
        return false;
    }

    /****
     * 释放锁
     */
    public void releaseSimpleLock1() {

        if (value != null && value.equals(redisTemplate.opsForValue().get(lockKey))) {
            System.out.println("释放自己的锁");
            redisTemplate.delete(lockKey);
        } else {
            System.out.println("不是我自己的锁，我不释放");
        }
    }


}
