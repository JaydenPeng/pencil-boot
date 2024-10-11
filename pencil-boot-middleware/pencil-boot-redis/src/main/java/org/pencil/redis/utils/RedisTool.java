package org.pencil.redis.utils;

import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author pencil
 * @date 24/10/11 20:33
 */
public class RedisTool {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public StringRedisTemplate getTemplate() {
        return stringRedisTemplate;
    }

    public boolean lock(String key, long expire) {
        return stringRedisTemplate.opsForValue().setIfAbsent(key, "lock", expire, TimeUnit.SECONDS);
    }

    public void unlock(String key) {
        stringRedisTemplate.delete(key);
    }

    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void set(String key, String value, long expire) {
        stringRedisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }


    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void del(String key) {
        stringRedisTemplate.delete(key);
    }

    public Long inc(String key) {
        return stringRedisTemplate.opsForValue().increment(key, 1);
    }

    public Long dec(String key) {
        return stringRedisTemplate.opsForValue().decrement(key, 1);
    }

    public void expire(String key, Duration duration) {
        stringRedisTemplate.expire(key, duration);
    }


    public Boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    public Long getExpire(String key) {
        return stringRedisTemplate.getExpire(key);
    }

    public String getSet(String key, String value) {
        return stringRedisTemplate.opsForValue().getAndSet(key, value);
    }

    public List<String> multiGet(List<String> keys) {
        return stringRedisTemplate.opsForValue().multiGet(keys);
    }

    public void publishMessage(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }

    public void publishStream(String channel, String group, String message) {
        stringRedisTemplate.opsForStream().add(channel, Map.of(group, message));
    }

    public void createGroup(String channel, String group) {
        stringRedisTemplate.opsForStream().createGroup(channel, group);
    }
}
