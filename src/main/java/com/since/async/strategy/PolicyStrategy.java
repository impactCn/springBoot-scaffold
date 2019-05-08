package com.since.async.strategy;

import com.since.async.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;

/**
 * @description: 使用策略模式写线程池策略
 * @author: SY_zheng
 * @create: 2019-05-07
 */
public interface PolicyStrategy {

    /**
     * 线程池策略
     * @param namePrefix
     * @param builder
     * @returna
     */
    ExecutorService policy(String namePrefix, ThreadFactoryBuilder builder);



}