package com.since.config.schedule;

import com.google.common.base.Stopwatch;
import com.since.aspect.SuccessAndFailureAspect;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description: 开启多线程定时器配置类
 * @author: SY_zheng
 * @create: 2019-05-05
 */
@Configuration
@EnableScheduling
@EnableAsync
@Slf4j
public class MultiThreadScheduleTask {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 时间间隔为30秒
     */
    private static final long API_LOG_INTERVAL = 30 * 1000L;


    /**
     * 每天凌晨0清除调用API次数
     */
    @Async
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearApiCount() {
        // 避免内存溢出
        SuccessAndFailureAspect.API_COUNT.clear();
    }

    /**
     * 每天凌晨1清除错误缓存
     */
    @Async
    @Scheduled(cron = "0 0 1 * * ?")
    public void clearErrorRedis() {

        log.info("开始执行清除错误缓存......");
        // 计算清除时间
        Stopwatch startTime = Stopwatch.createStarted();
        // 正则匹配login的API接口产生的全部键
        String keys = "login::userLogin_*";
        // 匹配数字
        String pattern = "[^\\d]+([\\d]+)[^\\d]+.*";
        // 计算错误缓存
        AtomicInteger count = new AtomicInteger(0);
        Set<String> keyList = redisTemplate.keys(keys);
        keyList.forEach(i -> {
            String key = i;
            String value = redisTemplate.opsForValue().get(key);
            Pattern p = Pattern.compile(pattern);
            Matcher matcher = p.matcher(value);
            if (matcher.find()) {
                if (!"000000".equals(matcher.group(1))) {
                    count.getAndIncrement();
                    redisTemplate.delete(key);
                }
            }
        });
        long endTime = startTime.stop().elapsed(TimeUnit.MILLISECONDS);
        log.info("缓存错误缓存结束，共清除了{}个，时间为：{}毫秒", count.get(), endTime);


        // 下面提供管道写法
        // 用redisTemplate模板操作直接省去了很多数据封装的问题，但是性能上稍微差一点
//        redisTemplate.executePipelined(new SessionCallback<Object>() {
//            @Override
//            public <K, V> Object execute(RedisOperations<K, V> operations) throws DataAccessException {
//
//                List list = new ArrayList(1){};
//
//                Set<String> keysList = redisTemplate.keys(keys);
//
//                keysList.forEach(i->{
//                    int count = 0;
//                    String key = i;
//                    System.err.println(i);
//
//                    String value = redisTemplate.opsForValue().get(key);
//                    // 匹配数字
//                    String pattern = "[^\\d]+([\\d]+)[^\\d]+.*";
//                    Pattern p = Pattern.compile(pattern);
//                    Matcher matcher = p.matcher(value);
//
//                    System.err.println(matcher.group(1));
//
//
//                    if (matcher.find()) {
//                        if (!matcher.group(1).equals("000000")) {
//                            redisTemplate.delete(key);
//                            System.err.println(count);
//                        }
//                    }
//                });

    }
}