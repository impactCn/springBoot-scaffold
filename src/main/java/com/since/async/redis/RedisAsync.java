package com.since.async.redis;

/**
 * @description: 异步处理缓存的更新和删除
 * @author: SY_zheng
 * @create: 2019-05-07
 */
public interface RedisAsync {

    /**
     * 异步更新redis缓存
     * @param key redis的key
     * @param value redis的value
     * @param namePrefix 线程名
     */
    void updateRedisAsync(String key, String value, String namePrefix);

    /**
     * 异步删除redis缓存
     * @param key redis的key
     * @param namePrefix 线程名
     */
    void deleteRedisAsync(String key, String namePrefix);

    /**
     * 异步添加redis缓存
     * @param key redis的key
     * @param value redis的value
     * @param namePrefix 线程名
     */
    void addRedisAsync(String key, String value, String namePrefix);
}
