//package com.jerry.springbootredis.conf;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.JedisPoolConfig;
//
//@Configuration
//public class JedisConfig {
//
//    @Value("${spring.redis.host}")
//    private String host;
//
//    @Value("${spring.redis.port}")
//    private int port;
//    @Value("${spring.redis.password}")
//    private String password;
//
//    @Value("${spring.redis.jedis.pool.max-active}")
//    private int maxActive;
//
//
//    @Value("${spring.redis.jedis.pool.max-idle}")
//    private int maxIdle;
//
//    @Value("${spring.redis.jedis.pool.min-idle}")
//    private int minIdle;
//
//    @Value("${spring.redis.jedis.pool.max-wait}")
//    private int maxWait;
//
//    @Bean
//    public JedisPool jedisPool() {
//        return new JedisPool(new JedisPoolConfig(), host, port, maxWait, password);
//    }
//}