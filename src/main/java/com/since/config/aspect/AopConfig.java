package com.since.config.aspect;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @description: 开启aop类
 * @author: SY_zheng
 * @create: 2019-05-05
 */
@Configuration
@ComponentScan("com.since.aspect")
@EnableAspectJAutoProxy
public class AopConfig {
}