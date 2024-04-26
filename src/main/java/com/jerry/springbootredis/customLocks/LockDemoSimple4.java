package com.jerry.springbootredis.customLocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @Author jerryLau
 * @Date 2024/4/16 16:29
 * @注释 最简易的分布式锁实现 版本4 定时器续费
 */
@Component
public class LockDemoSimple4 {
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
            renewKey(Thread.currentThread(), lockKey);
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


    /**
     * 定时续费
     * @param thread 线程
     * @param key
     */
    public void renewKey(Thread thread, String key) {
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (thread.isAlive() && redisTemplate.hasKey(key)) {
                    System.out.println("线程还在,给key续30秒");
                    redisTemplate.expire(key, 30, TimeUnit.SECONDS);
                } else {
                    System.out.println("线程已经不存在,终止定时任务");
                    throw new RuntimeException("终止定时任务");
                }
            }
        }, 10, 10, TimeUnit.SECONDS);
    }
}

