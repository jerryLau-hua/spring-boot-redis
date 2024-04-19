package com.jerry.springbootredis.redisLock;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @version 1.0
 * @Author jerryLau
 * @Date 2024/4/9 8:52
 * @注释 redis 锁机制测试类
 */
public class RedisLockTestMain2 {
    private static Integer inventory = 1001; //消耗品数量
    private static final int NUM = 1000; //线程数
    private static LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue(); //线程池队列

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(inventory, inventory,
                10L, SECONDS, linkedBlockingQueue);

        CountDownLatch countDownLatch = new CountDownLatch(NUM);//同步工具,允许一个或多个线程等待其他线程完成一组操作

        long start = System.currentTimeMillis();

        Config config = new Config();
        config.useSingleServer().setAddress("8.140.192.79:6379").setPassword("root123qa").setDatabase(0);
        RedissonClient client = Redisson.create(config);
        RLock lock = client.getLock("lock1");

        for (int i = 0; i <= NUM; i++) {
            int finalI = i;
            threadPoolExecutor.execute(new Runnable() {
                public void run() {
                    lock.lock();
                    inventory--;
                    lock.unlock();
                    System.out.println("i=" + finalI + "线程执行:" + Thread.currentThread().getName() + "，当前库存：" + inventory);
                    countDownLatch.countDown();
                }
            });
        }
        threadPoolExecutor.shutdown();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("执行线程数:" + NUM + " 总耗时:" + (end - start) + "ms ， 库存数为:" + inventory);
    }
}

