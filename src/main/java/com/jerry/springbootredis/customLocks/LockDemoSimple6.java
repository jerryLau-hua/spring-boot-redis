package com.jerry.springbootredis.customLocks;

import com.jerry.springbootredis.customLocks.lockInterFace.LockInterface;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/***
 * 🔒操作的实现类
 */
public class LockDemoSimple6 implements LockInterface {
    private StringRedisTemplate redisTemplate;
    private String lockKey;
    private String lockKeyValue;
    private long DEFAULT_RELEASE_TIME = 30;
    private static final DefaultRedisScript<Long> LOCK_SCRIPT;
    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;
    private ScheduledExecutorService scheduledExecutorService  = new ScheduledThreadPoolExecutor(1);

    static {
        // 加载释放锁的脚本
        LOCK_SCRIPT = new DefaultRedisScript<>();
        LOCK_SCRIPT.setScriptSource(new ResourceScriptSource(new
                ClassPathResource("lock.lua")));
        LOCK_SCRIPT.setResultType(Long.class);
        // 加载释放锁的脚本
        UNLOCK_SCRIPT = new DefaultRedisScript<>();
        UNLOCK_SCRIPT.setScriptSource(new ResourceScriptSource(new
                ClassPathResource("unlock.lua")));
        UNLOCK_SCRIPT.setResultType(Long.class);
    }

    public LockDemoSimple6(StringRedisTemplate redisTemplate, String lockKey) {
        this.redisTemplate = redisTemplate;
        this.lockKey = lockKey;
        this.lockKeyValue = UUID.randomUUID().toString();
    }



    @Override
    public boolean tryLock() {
        // 执行脚本
        Long result = redisTemplate.execute(LOCK_SCRIPT, Collections.singletonList(lockKey),
                lockKeyValue, String.valueOf(DEFAULT_RELEASE_TIME));
        // 判断结果
        return result != null && result.intValue() == 1;
    }

    @Override
    public boolean tryLock(long time) {
        Instant endTime = Instant.now().plusMillis(time);

        while(Instant.now().getEpochSecond() < endTime.getEpochSecond()) {
            Long result = redisTemplate.execute(LOCK_SCRIPT, Collections.singletonList(lockKey),
                    lockKeyValue, String.valueOf(DEFAULT_RELEASE_TIME));

            if (result != null && result.intValue() == 1) {
                renewKey(Thread.currentThread());

                return true;
            }
        }

        return false;
    }

    @Override
    public void lock() {

        while (true) {
            Long result = redisTemplate.execute(LOCK_SCRIPT, Collections.singletonList(lockKey),
                    lockKeyValue, String.valueOf(DEFAULT_RELEASE_TIME));

            if (result != null && result.intValue() == 1) {
                renewKey(Thread.currentThread());

                break;
            }
        }
    }

    @Override
    public int unlock() {
        // 执行脚本
        Long execute = redisTemplate.execute(
                UNLOCK_SCRIPT,
                Collections.singletonList(lockKey),
                lockKeyValue, String.valueOf(DEFAULT_RELEASE_TIME));
        System.out.println("execute:"+execute);

        return execute.intValue();
    }

    /**
     * 定时续费
     * @param thread
     */
    public void renewKey(Thread thread) {
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            if (thread.isAlive() && redisTemplate.hasKey(lockKey)) {
                redisTemplate.expire(lockKey, DEFAULT_RELEASE_TIME, TimeUnit.SECONDS);
            } else {
                throw new RuntimeException("终止定时任务");
            }
        }, 10, 10, TimeUnit.SECONDS);
    }
}