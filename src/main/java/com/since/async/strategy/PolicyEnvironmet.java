package com.since.async.strategy;

import com.since.async.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;

/**
 * @description: 环境角色，对外提供策略
 * @author: SY_zheng
 * @create: 2019-05-08
 */
public class PolicyEnvironmet {

    /**
     * 持有对策略类的引用
     */
    private PolicyStrategy policyStrategy;

    public PolicyEnvironmet(PolicyStrategy policyStrategy) {
        this.policyStrategy = policyStrategy;
    }

    public ExecutorService policy(String namePrefix, ThreadFactoryBuilder builder) {
        return policyStrategy.policy(namePrefix, builder);
    }
}