package com.jerry.springbootredis.customLocks;

import com.jerry.springbootredis.conf.LockDemoSimple6Conf;
import com.jerry.springbootredis.customLocks.lockInterFace.LockInterface;
import com.jerry.springbootredis.customLocks.lockInterFace.LockObtainInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    @Autowired
    LockDemoSimple6Conf conf;
    @Autowired
    StringRedisTemplate template;


    //测试自定义锁v2
    @Test
    public void testSimpleLock1A() throws Exception {
        try {
            if (lockDemoSimple4.trySimpleLock1()) {
                System.out.println("程序A：执行业务逻辑,睡50秒钟");
                Thread.sleep(50000);
            } else {
                System.out.println("程序A：获取锁失败");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("程序A：释放锁");
            lockDemoSimple4.releaseSimpleLock1();
        }

    }

    @Test
    public void testSimpleLockB() throws Exception {
        try {
            if (lockDemoSimple4.trySimpleLock1()) {
                System.out.println("程序B：执行业务逻辑,睡100秒钟");
                Thread.sleep(100000);
            } else {
                System.out.println("程序B：获取锁失败");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("程序B：释放锁");
            lockDemoSimple4.releaseSimpleLock1();
        }

    }

    @Test
    public void testSimpleLock1Release() throws Exception {
        lockDemoSimple1.releaseSimpleLock1();
    }

    //模仿实际场景，测试自定义锁
    @Test
    public void testSimpleLock2() throws InterruptedException {
        System.out.println("程序A：开始");
        LockObtainInterface lockObtainInterface = conf.lockRegistry(template);
        LockInterface lockDemoSimple6 = lockObtainInterface.obtainLock("111");

        try {
            boolean b = lockDemoSimple6.tryLock(5000);
            if (b) {
                System.out.println("程序A： 获取锁成功,开始执行业务逻辑,睡50秒");
                //模拟业务逻辑
                Thread.sleep(50000);
            } else
                System.out.println("程序A：获取锁失败,无法执行业务逻辑");
        } catch (
                Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("执行结束");
            //释放锁
            System.out.println("程序A：释放锁");
            int unlock = lockDemoSimple6.unlock();
            switch (unlock) {
                case 1:
                    System.out.println("释放自己的锁成功");
                    break;
                case 0:
                    System.out.println("释放自己的锁失败：不是自己的锁，我不释放");
                    break;
                default:
                    break;
            }
        }

    }

    @Test
    public void testSimpleLock3() throws Exception {
        LockObtainInterface lockObtainInterface = conf.lockRegistry(template);
        LockInterface lockDemoSimple6 = lockObtainInterface.obtainLock("111");
        try {

            System.out.println("程序B：开始获取锁");
            boolean b = lockDemoSimple6.tryLock();
            if (b) {
                System.out.println("程序B：获取锁成功,开始执行业务逻辑,睡30秒");
                //模拟业务逻辑
                Thread.sleep(30000);
            } else {
                System.out.println("程序B获取锁失败，无法执行业务");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放锁
            System.out.println("程序B：释放锁");
            int unlock = lockDemoSimple6.unlock();
            switch (unlock) {
                case 1:
                    System.out.println("释放自己的锁成功");
                    break;
                case 0:
                    System.out.println("释放自己的锁失败：不是自己的锁，我不释放");
                    break;
                default:
                    break;
            }
        }
    }

    @Test
    public void testSimpleLock4() throws Exception {


    }


}
