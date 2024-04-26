package com.jerry.springbootredis.customLocks;

import com.jerry.springbootredis.customLocks.lockInterFace.LockInterface;
import com.jerry.springbootredis.customLocks.lockInterFace.LockObtainInterface;
import org.springframework.data.redis.core.StringRedisTemplate;


/**
 * @version 1.0
 * @Author jerryLau
 * @Date 2024/4/16 16:29
 * @注释 最简易的分布式锁实现 获取锁
 */
public class LockObtain implements LockObtainInterface {
    //redis Template
    private StringRedisTemplate redisTemplate;

    //prefix
    private String prefix;


    public LockObtain(StringRedisTemplate redisTemplate, String prefix) {
        this.prefix = prefix;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public LockInterface obtainLock(String key) {
        return new LockDemoSimple6(redisTemplate, prefix + ":" + key);
    }
}

