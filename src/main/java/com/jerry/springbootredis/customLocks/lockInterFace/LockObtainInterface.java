package com.jerry.springbootredis.customLocks.lockInterFace;


/**
 * @version 1.0
 * @Author jerryLau
 * @Date 2024/4/25 13:40
 * @注释 创建锁
 */
public interface LockObtainInterface {

    /***
     * 创建🔒
     * @return
     */
    public LockInterface obtainLock(String key);
}
