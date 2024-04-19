package com.jerry.springbootredis.customLocks;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @Author jerryLau
 * @Date 2024/4/16 16:29
 * @注释 最简易的分布式锁实现 版本5 继续分装简化逻辑
 */
@Component
public class LockDemoSimple5 {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private String value;

    private ThreadLocal<String> keyMap = new ThreadLocal<String>();

    @Autowired
    private ScheduledExecutorService scheduledExecutorService;

    /***
     * 尝试加锁
     * @param key
     * @return
     */
    public boolean trySimpleLock(String key) {
        keyMap.set(key);
        // 尝试获取锁
        String uuid = UUID.randomUUID().toString();
        System.out.println(Thread.currentThread().getName() + "获取锁   " + key + "   " + uuid + "方法被调用");
        // 原子性操作setNX
        if (redisTemplate.opsForValue().setIfAbsent(key, uuid)) {
            // 加锁
            this.value = uuid;
            renewKey(Thread.currentThread(), key);
            return true;
        }
        return false;
    }

    /***
     * 给定时间内尝试加锁
     * @param key
     * @param timeout 超时时间
     * @return
     */
    public boolean trySimpleLock(String key, int timeout) {
        keyMap.set(key);
        // 尝试获取锁
        String uuid = UUID.randomUUID().toString();

        //计算结束时间
        Instant endTime = Instant.now().plusSeconds(timeout);

        //时间比较
        while (Instant.now().getEpochSecond() < endTime.getEpochSecond()) {
            // 原子性操作setNX
            if (redisTemplate.opsForValue().setIfAbsent(key, uuid)) {
                // 加锁
                this.value = uuid;
                renewKey(Thread.currentThread(), key);
                return true;
            }
        }
        return false;
    }

    /***
     * 尝试加锁
     * @param key
     * @param timeout
     * @return
     */
    public void Lock(String key, int timeout) {
        keyMap.set(key);
        // 尝试获取锁
        String uuid = UUID.randomUUID().toString();
        while (true) {
            if (redisTemplate.opsForValue().setIfAbsent(key, uuid)) {
                // 加锁
                this.value = uuid;
                renewKey(Thread.currentThread(), key);
                break;
            }
        }
    }

    /****
     * 释放锁
     */
    public void releaseSimpleLock() {
        System.out.println(Thread.currentThread().getName() + "释放锁方法被调用");
        String key = keyMap.get();
        System.out.println(Thread.currentThread().getName() + "释放锁   " +  " VALUE保存的：  " + this.value);
        System.out.println(Thread.currentThread().getName() + "释放锁   " +  "value从redis获取的：   " + redisTemplate.opsForValue().get(key));

        if (value != null && value.equals(redisTemplate.opsForValue().get(key))) {
            System.out.println( Thread.currentThread().getName() + "释放自己的锁");
            redisTemplate.delete(key);
            keyMap.remove();
        } else {
            System.out.println("不是我自己的锁，我不释放");
        }
    }


    /**
     * 定时续费
     *
     * @param thread
     * @param key
     */
    public void renewKey(Thread thread, String key) {
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

