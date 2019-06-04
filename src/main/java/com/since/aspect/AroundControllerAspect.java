package com.since.aspect;

import com.google.common.base.Stopwatch;
import com.since.enums.MessageEnums;
import com.since.vo.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @description: 在调用api的之前，作拦截处理，同时记录代码运行时间
 * @author: SY_zheng
 * @create: 2019-05-05
 */
@Aspect
@Component
@Slf4j
public class AroundControllerAspect {

    @Pointcut("execution(* com.since.controller.*.*(..))")
    public void pointCut(){}

    /**
     * 参数验证，记录接口调用时间
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("pointCut()")
    public Object apiAround(ProceedingJoinPoint joinPoint) throws Throwable {

        // 计算接口时间
        Stopwatch startTime = Stopwatch.createStarted();
        // 获取request


        MessageVO messageVO = null;
        try {
            messageVO = (MessageVO)joinPoint.proceed(joinPoint.getArgs());
            long endTime = startTime.stop().elapsed(TimeUnit.MILLISECONDS);
            log.info(" 方法名：{} 调用时间：{}毫秒", joinPoint.getSignature().getName(), endTime);
        } catch (Exception e) {
            return handleException(joinPoint, e);
        }

        return messageVO;

    }

    /**
     * 捕获全局异常
     * @param e
     * @return
     */
    private MessageVO handleException(ProceedingJoinPoint joinPoint, Throwable e) {

        MessageVO messageVO = null;
        if (e instanceof Exception) {
            messageVO = MessageVO.builder().
                    msgCode(MessageEnums.API_ERROR)
                    .build();
        }
        log.error(" 方法名：{} 接口错误信息：{}", joinPoint.getSignature().getName(), e.getMessage());
        return messageVO;
    }
}