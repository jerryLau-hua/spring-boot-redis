package com.jerry.springbootredis.conf;

import com.jerry.springbootredis.customLocks.LockObtain;
import com.jerry.springbootredis.customLocks.lockInterFace.LockObtainInterface;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @version 1.0
 * @Author jerryLau
 * @Date 2024/4/25 13:35
 * @注释 适用于案例6的自定义配置类
 */
@Configuration
public class LockDemoSimple6Conf {
    @Bean
    public LockObtainInterface lockRegistry(StringRedisTemplate redisTemplate) {
        return new LockObtain(redisTemplate, "lock");
    }

}
