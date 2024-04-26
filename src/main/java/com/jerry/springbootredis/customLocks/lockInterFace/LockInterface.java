package com.jerry.springbootredis.customLocks.lockInterFace;

/**
 * @version 1.0
 * @Author jerryLau
 * @Date 2024/4/25 13:31
 * @注释 自定义锁接口 加锁等操作
 */
public interface LockInterface {


    /**
     * 尝试获取🔒资源，如果获取不到，就阻塞
     */
    public void lock();

    /**
     * 尝试获取🔒资源
     *
     * @return 获取到返回true, 如获取不到返回false
     */
    public boolean tryLock();

    /**
     * 尝试在指定时间内获取🔒资源
     *
     * @param time
     * @return获取指定时间内没有获取到返回true,如获取不到返回false
     */
    public boolean tryLock(long time);

    /**
     * 释放🔒
     */
    public int unlock();

}
