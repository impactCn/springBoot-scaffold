package com.since.aspect;

import com.auth0.jwt.interfaces.Claim;
import com.google.common.base.Stopwatch;
import com.since.enums.MessageEnums;
import com.since.utils.JwtUtils;
import com.since.vo.MessageVO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
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
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        String token = request.getHeader("token");

        String line = (String) request.getSession().getAttribute("line");

        // 进行token验证
        Map<String, Claim> verifyToken = JwtUtils.verifyToken(token);
        if (token != null) {
            if (verifyToken == null) {
                // token有效验证
                return MessageVO.builder()
                        .msgCode(MessageEnums.TOKEN_ERROR)
                        .build();
            }
            // token签发的时间
            Date nowTime = JwtUtils.verifyToken(token).get("iat").asDate();
            // token过期的时间
            Date expireTime = JwtUtils.verifyToken(token).get("exp").asDate();
            if (expireTime.before(nowTime)) {
                // token有效时间验证
                return MessageVO.builder()
                        .msgCode(MessageEnums.TOKEN_TIME_OUT)
                        .build();
            }
            // 进行在线验证
            if (line == null || "off".equals(line)) {
                return MessageVO.builder()
                        .msgCode(MessageEnums.OFF_LINE)
                        .build();
            }

            // 这里继续做检验
        }
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