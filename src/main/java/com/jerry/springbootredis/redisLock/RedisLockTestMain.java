//package com.jerry.springbootredis.redisLock;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.ConfigurableApplicationContext;
//
//import java.util.UUID;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.locks.ReentrantLock;
//
//import static java.util.concurrent.TimeUnit.SECONDS;
//
//@SpringBootApplication
//public class RedisLockTestMain {
//
//    @Autowired
//    private RedisLockService redisLockService; // 假设这是你想要注入的服务
//
//    public static void main(String[] args) {
//        ConfigurableApplicationContext context = SpringApplication.run(RedisLockTestMain.class, args);
//        RedisLockTestMain app = context.getBean(RedisLockTestMain.class);
//        app.run(); // 调用自定义的方法
//    }
//
//    static String lockKey = "lock:";
//    static String requestId = UUID.randomUUID().toString(); // 生成一个唯一的请求ID
//    static int expireTime = 5000; // 锁的过期时间，例如10秒
//    private static Integer inventory = 1001; //消耗品数量
//    private static final int NUM = 1000; //线程数
//    private static LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue(); //线程池队列
//    static ReentrantLock reentrantLock = new ReentrantLock(); //lock
//
//    public void run() {
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(inventory, inventory,
//                10L, SECONDS, linkedBlockingQueue);
//        final CountDownLatch countDownLatch = new CountDownLatch(NUM); //同步工具,允许一个或多个线程等待其他线程完成一组操作
//        long start = System.currentTimeMillis();
//        for (int i = 0; i <= NUM; i++) {
//            threadPoolExecutor.execute(new Runnable() {
//                public void run() {
//                    boolean b = redisLockService.tryGetDistributedLock(lockKey, requestId, expireTime);
//                    if (b) {
//                        //枷锁成功
//                        try {
//                            inventory--;
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        } finally {
//                            redisLockService.releaseDistributedLock(lockKey, requestId);
//                        }
//                    } else {
//                        // 获取锁失败，可以选择等待、重试或执行其他逻辑
//                        System.out.println("无法获取数据 请稍后重试...");
//                    }
////                    reentrantLock.unlock();
//                    System.out.println("线程执行:" + Thread.currentThread().getName() + "，当前库存：" + inventory);
//                    countDownLatch.countDown();
//                }
//            });
//        }
//        threadPoolExecutor.shutdown();
//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        long end = System.currentTimeMillis();
//        System.out.println("执行线程数:" + NUM + " 总耗时:" + (end - start) + "ms ， 库存数为:" + inventory);
//    }
//}
//
