package com.github.ylyan2015.springbootdemo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * <p>提供常用的Redis操作封装，包括字符串、哈希、列表、集合等数据类型的读写操作</p>
 *
 * <h3>序列化说明:</h3>
 * <ul>
 *   <li>Key序列化: StringRedisSerializer (UTF-8编码)</li>
 *   <li>Value序列化: GenericJackson2JsonRedisSerializer (JSON格式)</li>
 *   <li>Hash Key/Value序列化: StringRedisSerializer + GenericJackson2JsonRedisSerializer</li>
 * </ul>
 *
 * <h3>注意事项:</h3>
 * <ul>
 *   <li>存储的对象必须实现Serializable接口或为基本数据类型</li>
 *   <li>过期时间单位为秒</li>
 *   <li>所有异常都会记录日志并返回安全值，不会抛出异常</li>
 * </ul>
 *
 * @see com.github.ylyan2015.springbootdemo.config.RedisConfig
 */
@Slf4j
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("设置过期时间失败, key: {}", key, e);
            return false;
        }
    }

    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("判断key是否存在失败, key: {}", key, e);
            return false;
        }
    }

    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("设置缓存失败, key: {}", key, e);
            return false;
        }
    }

    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("设置缓存并指定过期时间失败, key: {}", key, e);
            return false;
        }
    }
}
