package com.since.config.mvc;

import com.since.config.mvc.interceptor.ParamsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @description: 解决跨域
 * @author: SY_zheng
 * @create: 2019-05-09
 */
@Configuration
public class MVCConfig implements WebMvcConfigurer {

    private static final String[] ORIGINS = new String[] { "GET", "POST", "PUT", "DELETE" };

    @Resource
    private ParamsInterceptor paramsInterceptor;

    /**
     * 配置跨域
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*").allowCredentials(true).allowedMethods(ORIGINS)
                .maxAge(3600);
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(paramsInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/api/login/login")
                .excludePathPatterns("/api/login/logout");
    }
}