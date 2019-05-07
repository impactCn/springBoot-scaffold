package com.since.aspect;

import com.google.common.collect.Lists;
import com.since.bo.MailBO;
import com.since.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: api调用失败发送邮件，调用成功记录API次数
 * @author: SY_zheng
 * @create: 2019-05-05
 */
@Aspect
@Slf4j
@Component
@EnableAsync
public class SuccessAndFailureAspect {

    /**
     * 由于存在并发，选择线程安全的ConcurrentHashMap。使用全局变量记录请求的api调用情况
     */
    public static Map<String, Integer> API_COUNT = new ConcurrentHashMap<>();


    @Autowired
    private MailUtils mailUtils;

    @Pointcut("execution(* com.since.controller.*.*(..))")
    public void pointCut(){}


    /**
     * 接口错误的时候，发送邮件给管理员
     * @param joinPoint
     * @param throwable
     */
    @Async
    @AfterThrowing(pointcut = "pointCut()", throwing = "throwable")
    public void errorMailSend(JoinPoint joinPoint, Throwable throwable){

        // 请使用StringBuilder进行字符串拼接，禁止使用+拼接，拼接标题

        StringBuilder subject = new StringBuilder();
        subject.append("类名：");
        String clazz = joinPoint.getTarget().getClass().getName();
        subject.append(clazz);
        subject.append("\t");
        subject.append("接口名：");
        subject.append(joinPoint.getSignature().getName());
        subject.append("异常");

        // 拼接内容
        StringBuilder text = new StringBuilder();
        text.append("参数：");
        text.append("\n");
        List<Object> args = Lists.newArrayList(joinPoint.getArgs());
        // 获取参数名
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        List<String> parameterNames = Lists.newArrayList(methodSignature.getParameterNames());

        // 构建K-V
        Map<String ,Object> map = new HashMap<>(args.size());
        for (int i = 0; i < args.size(); i++) {
            map.put(parameterNames.get(i), args.get(i));
        }
        text.append(map);
        text.append("\n");
        text.append("错误信息：");
        text.append("\n");
        text.append(throwable.getMessage());
        text.append("\n");

        // 获取本地详细错误信息
        StackTraceElement[] trace = throwable.getStackTrace();
        for (StackTraceElement s : trace) {
            text.append("\tat");
            text.append(s);
            text.append("\n");
        }


        MailBO mailBO = new MailBO.Builder()
                .from("xxx@qq.com")
                // 可添加多个发送邮件对象
                .to(new String[]{"xxx@qq.com"})
                .subject(subject.toString())
                .context(text.toString())
                .build();
        mailUtils.simpleMail(mailBO);
    }

    /**
     * 接口调用成功，记录接口调用次数
     * @param joinPoint
     */
    @Async
    @AfterReturning(pointcut = "pointCut()")
    public void loggingApi(JoinPoint joinPoint) throws IOException {
        // 获取接口名
        String api = joinPoint.getSignature().getName();

        // 计算调用情况
        if (API_COUNT.containsKey(api)) {
            int count = API_COUNT.get(api);
            count = count + 1;
            API_COUNT.put(api, count);
        } else {
            API_COUNT.put(api, 1);
        }

    }
}