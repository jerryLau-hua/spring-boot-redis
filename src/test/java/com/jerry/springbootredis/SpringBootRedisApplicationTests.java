//package com.jerry.springbootredis;
//
//import com.jerry.springbootredis.redisLock.RedisLockService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.UUID;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.locks.ReentrantLock;
//
//import static java.util.concurrent.TimeUnit.SECONDS;
//
//@SpringBootTest
//class SpringBootRedisApplicationTests {
//    @Autowired
//    private RedisLockService redisLockService;
//    String lockKey = "lock";
//    String requestId = UUID.randomUUID().toString(); // 生成一个唯一的请求ID
//    int expireTime = 300; // 锁的过期时间，例如10秒
//    private Integer inventory = 1001; //消耗品数量
//    private final int NUM = 1000; //线程数
//    private LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue(); //线程池队列
//    static ReentrantLock reentrantLock = new ReentrantLock(); //lock
//
//    @Test
//    void contextLoads() {
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(inventory, inventory,
//                10L, SECONDS, linkedBlockingQueue);
//        final CountDownLatch countDownLatch = new CountDownLatch(NUM); //同步工具,允许一个或多个线程等待其他线程完成一组操作
//        long start = System.currentTimeMillis();
//        for (int i = 0; i <= NUM; i++) {
//            int finalI = i;
//            threadPoolExecutor.execute(new Runnable() {
//                public void run() {
//                    boolean b = redisLockService.tryGetDistributedLock(lockKey, requestId, expireTime);
//                    if (b) {
//                        //枷锁成功
//                        try {
////                            System.out.println("i=" + finalI + "线程获取锁，执行开始:" + Thread.currentThread().getName() + "，当前库存：" + inventory);
//                            inventory--;
//                            System.out.println(inventory);
////                            System.out.println("i=" + finalI + "线程获取锁，执行结束:" + Thread.currentThread().getName() + "，当前库存：" + inventory);
//                            boolean b1 = redisLockService.releaseDistributedLock(lockKey, requestId);
////                            System.out.println("i=" + finalI + "线程释放锁，执行结束:"+b1 );
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        } finally {
//                            redisLockService.releaseDistributedLock(lockKey, requestId);
////                            System.out.println("i=" + finalI + "线程超时释放锁:" + Thread.currentThread().getName() + "，当前库存：" + inventory);
//                        }
//                    } else {
//                        // 获取锁失败，可以选择等待、重试或执行其他逻辑
////                        System.out.println("i=" + finalI + "无法获取，稍后重试...");
//                    }
//
////                    System.out.println("线程执行:" + Thread.currentThread().getName() + "，当前库存：" + inventory);
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
//
//}
