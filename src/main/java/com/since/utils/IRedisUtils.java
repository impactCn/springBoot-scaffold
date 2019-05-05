package com.since.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description:  提供手动操作redis的工具类
 * @author: SY_zheng
 * @create: 2019-05-05
 */
public interface IRedisUtils {

    /**
     * 指定缓存时间
     * @param key 键
     * @param time 时间（秒）
     * @return
     */
    boolean expire(String key, long time);

    /**
     * 根据key 获取过期时间
     * @param key 键
     * @return 时间（秒） 返回0代表永久有效
     */
    long getExpire(String key);

    /**
     * 判断key是否存在
     * @param key 键
     * @return true存在 false不存在
     */
    boolean hasKey(String key) ;

    /**
     * 删除缓存
     * @param key 键
     */
    void del(String... key);

    // ==============String====================

    /**
     * String缓存获取
     * @param key 键
     * @return 值
     */
    Object get(String key);

    /**
     * String缓存放入
     * @param key  键
     * @param value 值
     * @return true成功 false失败
     */
    boolean set(String key, Object value);

    /**
     * String缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间（秒） time要大于0 如果time小于等于0 将设置无限期
     * @return true 成功 false失败
     */
    boolean set(String key, Object value, long time);

    /**
     * 递增
     * @param key 键
     * @param delta 要增加几（小于0）
     * @return
     */
    long incr(String key, long delta) ;

    /**
     * 递减
     * @param key 键
     * @param delta 要减少几（小于0）
     * @return
     */
    long decr(String key, long delta);

    // ==============Map====================

    /**
     * HashGet
     * @param key  键
     * @param item
     * @return 对应的多个值
     */
    Object hget(String key, String item);

    /**
     * 获取hashkey对应的所有键值
     * @param key 键
     * @return 对应的多个值
     */
    Map<Object, Object> hmget(String key);

    /**
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false失败
     */
    boolean hmset(String key, Map<String , Object> map) ;

    /**
     * HashSet 并设置时间
     * @param key 键
     * @param map
     * @param time
     * @return true 成功 false 失败
     */
    boolean hmset(String key, Map<String, Object> map, long time) ;

    /**
     * 向一张hash表放入数据，如果不存在将创建
     * @param key 键
     * @param item
     * @param value
     * @return
     */
    boolean hset(String key, String item, Object value);

    /**
     * 向一张hash表放入数据，如果不存在将创建
     * @param key 键
     * @param item
     * @param value
     * @param time 时间（秒） 注意：如果已存在的hash表有时间，这里将会替换原有的时间
     * @return
     */
    boolean hset(String key, String item, Object value, long time) ;

    /**
     * 删除hash表中的值
     * @param key 键
     * @param item
     */
    void hdel(String key, Object... item);

    /**
     * 判断hash表中是否有该项的值
     * @param key 键
     * @param item
     * @return
     */
    boolean hHasKey(String key, String item);

    /**
     * hash递增，如果不存在，就会创建一个 并把新增的值返回
     * @param key 键
     * @param item
     * @param by 要增加几（大于0）
     * @return
     */
    double hincr(String key, String item, double by) ;

    /**
     * hash递减
     * @param key 键
     * @param item
     * @param by
     * @return
     */
    double hdecr(String key, String item, double by);

    // ==============Set====================

    /**
     * key获取set中的所有值
     * @param key 键
     * @return
     */
    Set<Object> sGet(String key);

    /**
     * 根据value从一个set中查询，是否存在
     * @param key 键
     * @param value
     * @return
     */
    boolean sHasKey(String key, Object value);

    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values
     * @return 成功个数
     */
    long sSet(String key, Object... values);

    /**
     * 将set数据放入缓存 并且设置时间
     * @param key 键
     * @param time
     * @param values
     * @return 成功个数
     */
    long sSetAndTime(String key, long time, Object... values);

    /**
     * 获取set缓存的长度
     * @param key 键
     * @return
     */
    long sGetSetSize(String key) ;

    /**
     * 移除set缓存中的key与values
     * @param key 键
     * @param values 可以多个
     * @return 移除个数
     */
    long setRemove(String key, Object... values) ;

    // ==============List====================

    /**
     * 获取list的缓存的内容
     * @param key 键
     * @param start 开始
     * @param end 结束 0 到 -1 代表所有值
     * @return
     */
    List<Object> lGet(String key, long start, long end);

    /**
     * 获取list缓存的长度
     * @param key 键
     * @return
     */
    long lGetListSize(String key) ;

    /**
     * 通过索引 获取list中的值
     * @param key 键
     * @param index 索引 index >= 0 时， 0代表，1第二元素，依此类推；index<0时， -1 表尾 -2 代表倒数第二
     * @return
     */
    Object lGetIndex(String key, long index) ;

    /**
     * 将list放入缓存
     * @param key 键
     * @param value
     * @return
     */
    boolean lSet(String key, Object value) ;

    /**
     * 将list放入缓存，并且设置时间
     * @param key 键
     * @param value
     * @param time
     * @return
     */
    boolean lSet(String key, Object value, long time);

    /**
     * 根据索引修改list中的某条数据
     * @param key 键
     * @param index
     * @param value
     * @return
     */
    boolean lUpdateIndex(String key, long index, Object value);

    /**
     * 移除N个值为value
     * @param key 键
     * @param count 移除数量
     * @param value
     * @return 移除数量
     */
    long lReomve(String key, long count, Object value) ;
}
