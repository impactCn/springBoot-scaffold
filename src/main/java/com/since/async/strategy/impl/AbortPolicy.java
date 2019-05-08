package com.since.async.strategy.impl;

import com.since.async.ThreadFactoryBuilder;
import com.since.async.strategy.PolicyStrategy;

import java.util.concurrent.*;

/**
 * @description: AbortPolicy策略
 * @author: SY_zheng
 * @create: 2019-05-07
 */
public class AbortPolicy implements PolicyStrategy {

    @Override
    public ExecutorService policy(String namePrefix, ThreadFactoryBuilder builder) {
        ThreadFactory threadFactory = builder.setNamePrefix(namePrefix).build();
        ExecutorService singleThreadPool = null;
        singleThreadPool = new ThreadPoolExecutor(
                    1,
                    1,
                    0L,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingDeque<Runnable>(1024),
                    threadFactory,
                    new ThreadPoolExecutor.AbortPolicy()
            );
        return singleThreadPool;
    }
}