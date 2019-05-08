package com.since.async.redis.impl;

import com.since.async.ThreadFactoryBuilder;
import com.since.async.redis.RedisAsync;
import com.since.async.strategy.PolicyEnvironmet;
import com.since.async.strategy.impl.AbortPolicy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @description:
 * @author: SY_zheng
 * @create: 2019-05-07
 */
@Slf4j
@Component
public class RedisAsyncImpl implements RedisAsync {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    private ThreadFactoryBuilder builder = new ThreadFactoryBuilder();

    @Override
    public void updateRedisAsync(String key, String value, String namePrefix) {
        PolicyEnvironmet environmet = new PolicyEnvironmet(new AbortPolicy());
        ExecutorService singleThreadPool = environmet.policy(namePrefix, this.builder);


        Future<Boolean> future = singleThreadPool.submit(() -> {

            // 标识是否成功
            boolean flag;
            // 先检查是或存在缓存
            if (redisTemplate.opsForValue().get(key) == null) {
                // 如果没有
                redisTemplate.opsForValue().set(key, value);
                flag = true;
            } else {
                // 存在有，则先删除原先缓存，在添加
                redisTemplate.delete(key);
                redisTemplate.opsForValue().set(key, value);
                flag = true;
            }

            return flag;
        });

        try {
            if (future.get()) {
                log.info("线程池：{} ------异步更新缓存成功", namePrefix);
            }
        } catch (InterruptedException e) {
            log.error("异步更新缓存出现问题，线程池：{}被中断", namePrefix);
        } catch (ExecutionException e) {
            log.error("异步更新缓存出现问题，线程池：{}任务异常", namePrefix);
        } finally {
            singleThreadPool.shutdown();
        }

    }

    @Override
    public void deleteRedisAsync(String key, String namePrefix) {
        PolicyEnvironmet environmet = new PolicyEnvironmet(new AbortPolicy());
        ExecutorService singleThreadPool = environmet.policy(namePrefix, this.builder);

        Future<Boolean> future = singleThreadPool.submit(() -> {
            // 先检查时候是否存在缓存
            if (redisTemplate.opsForValue().get(key) == null) {
                // 不存在
                return false;
            } else {
                // 存在
                redisTemplate.delete(key);
                return true;
            }

        });

        try {
            if (future.get()) {
                log.info("线程池：{} ------异步删除缓存成功", namePrefix);
            } else {
                log.info("线程池：{} ------缓存不存在，不需要删除", namePrefix);
            }
        } catch (InterruptedException e) {
            log.error("异步删除缓存出现问题，缓存线程池：{}被中断", namePrefix);
        } catch (ExecutionException e) {
            log.error("异步删除缓存出现问题，线程池：{}任务异常", namePrefix);
        } finally {
            singleThreadPool.shutdown();
        }
    }

    @Override
    public void addRedisAsync(String key, String value, String namePrefix) {
        PolicyEnvironmet environmet = new PolicyEnvironmet(new AbortPolicy());
        ExecutorService singleThreadPool = environmet.policy(namePrefix, this.builder);

        Future<Boolean> future = singleThreadPool.submit(() -> {
            // 检查是否存在
            if (redisTemplate.opsForValue().get(key) == null) {
                // 不存在
                redisTemplate.opsForValue().set(key, value);
                return true;
            } else {
                // 存在
                return false;
            }
        });

        try {
            if (future.get()) {
                log.info("线程池：{} ------异步添加缓存成功", namePrefix);
            } else {
                log.info("线程池：{} ------缓存已存在", namePrefix);
            }
        } catch (InterruptedException e) {
            log.error("异步添加缓存出现问题，缓存线程池：{}被中断", namePrefix);
        } catch (ExecutionException e) {
            log.error("异步添加缓存出现问题，线程池：{}任务异常", namePrefix);
        } finally {
            singleThreadPool.shutdown();
        }
    }
}