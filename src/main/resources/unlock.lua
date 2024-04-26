local key = KEYS[1]
local threadId = ARGV[1]
local releaseTime = tonumber(ARGV[2])

-- 检查锁的持有者身份
if (redis.call('HEXISTS', key, threadId) == 0) then
    -- 释放失败，因为调用者不是锁的持有者
    return 0
end

-- 减少锁的持有者计数
local count = redis.call('HINCRBY', key, threadId, -1)

-- 如果计数大于0，重新设置过期时间
if (count > 0) then
    redis.call('EXPIRE', key, releaseTime)
    -- 释放成功，但锁仍然被其他持有者持有
    return 1
else
    -- 删除整个哈希键，因为没有任何持有者了
    redis.call('DEL', key)
    -- 释放成功，且锁已经完全释放
    return 1
end