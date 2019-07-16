package com.since.annotation;

import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description:
 * @author: SY_zheng
 * @create: 2019-07-12
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Document
public @interface ObjectFilter {

    // 过滤的属性
    String[] filterParams() default {};

    // 检查对象
    Class object();

}