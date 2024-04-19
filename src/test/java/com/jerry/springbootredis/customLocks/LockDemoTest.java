package com.jerry.springbootredis.customLocks;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @version 1.0
 * @Author jerryLau
 * @Date 2024/4/16 16:36
 * @注释 测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class LockDemoTest {
    @Autowired
    LockDemoSimple1 lockDemoSimple1;

    @Autowired
    LockDemoSimple2 lockDemoSimple2;

    @Autowired
    LockDemoSimple3 lockDemoSimple3;

    @Autowired
    LockDemoSimple4 lockDemoSimple4;

    @Autowired
    LockDemoSimple5 lockDemoSimple5;


    //测试自定义锁
    @Test
    public void testSimpleLock1() throws Exception {
        for (int i = 0; i < 5; i++) {
            System.out.println("线程:" + i + "开始执行，尝试获取锁，获取结果为：" + lockDemoSimple1.trySimpleLock1());
        }
    }

    @Test
    public void testSimpleLock1Release() throws Exception {
        lockDemoSimple1.releaseSimpleLock1();
    }

    //模仿实际场景，测试自定义锁
    @Test
    public void testSimpleLock2() throws InterruptedException {
        System.out.println("开始");
        Thread[] threads = new Thread[5];
        for (int i = 0; i < 5; i++) {
            threads[i] = new Thread(() -> {
                try {
                    if (lockDemoSimple5.trySimpleLock("Lock:key")) {
                        System.out.println(Thread.currentThread().getName() + " 获取锁成功,开始执行业务逻辑,睡50秒");
                        //模拟业务逻辑
                        Thread.sleep(50000);
                    } else
                        System.out.println(Thread.currentThread().getName() + "获取锁失败,无法执行业务逻辑");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //释放锁
                    System.out.println(Thread.currentThread().getName() + "释放锁");
                    lockDemoSimple5.releaseSimpleLock();
//                    System.out.flush(); // 刷新输出流
                }
            });
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }

    @Test
    public void testSimpleLock3() throws Exception {
        try {
            if (lockDemoSimple5.trySimpleLock("Lock:key", 30)) {
                System.out.println("获取锁成功,开始执行业务逻辑,睡100秒");
                //模拟业务逻辑
                Thread.sleep(100000);
            } else
                System.out.println("获取锁失败,无法执行业务逻辑");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放锁
            System.out.println("释放锁");
            lockDemoSimple5.releaseSimpleLock();
        }
    }

    @Test
    public void testSimpleLock4() throws Exception {

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "开始执行");
            }).start();
        }
    }


}
