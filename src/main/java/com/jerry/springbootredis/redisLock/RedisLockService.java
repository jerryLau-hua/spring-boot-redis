//package com.jerry.springbootredis.redisLock;
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import redis.clients.jedis.Jedis;
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.params.SetParams;
//
//import java.util.Collections;
//
//@Service
//public class RedisLockService {
//
//    @Autowired
//    private JedisPool jedisPool;
//
//    private static final Long RELEASE_SUCCESS = 1L;
//
//    /**
//     * 尝试获取分布式锁
//     * @param lockKey 锁键
//     * @param requestId 请求标识
//     * @param expireTime 锁的过期时间（毫秒）
//     * @return 是否获取成功
//     */
//    public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {
//        try (Jedis jedis = jedisPool.getResource()) {
//            String result = jedis.set(lockKey, requestId, SetParams.setParams().nx().px(expireTime));
//            return "OK".equals(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 释放分布式锁
//     * @param lockKey 锁键
//     * @param requestId 请求标识
//     * @return 是否释放成功
//     */
//    public boolean releaseDistributedLock(String lockKey, String requestId) {
//        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
//                "return redis.call('del', KEYS[1]) " +
//                "else " +
//                "return 0 " +
//                "end";
//
//        try (Jedis jedis = jedisPool.getResource()) {
//
//            Long result = (Long) jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
//            return RELEASE_SUCCESS.equals(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//}
