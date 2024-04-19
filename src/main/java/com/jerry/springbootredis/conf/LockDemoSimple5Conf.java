package com.jerry.springbootredis.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @version 1.0
 * @Author jerryLau
 * @Date 2024/4/19 14:16
 * @注释
 */
@Configuration
public class LockDemoSimple5Conf {


    @Bean
    public ConcurrentHashMap<Thread, String> map() {
        return new ConcurrentHashMap<>();
    }

    /**
     * 使用线程池优化新性能
     *
     * @return
     */
    @Bean
    public ScheduledThreadPoolExecutor scheduledThreadPoolExecutor() {
        return new ScheduledThreadPoolExecutor(10);
    }


}
