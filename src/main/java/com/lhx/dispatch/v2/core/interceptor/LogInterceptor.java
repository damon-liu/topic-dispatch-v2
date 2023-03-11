package com.lhx.dispatch.v2.core.interceptor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Description
 *
 * @author damon.liu
 * Date 2022-12-26 3:18
 */
@Component
@Slf4j
@Order(2)
public class LogInterceptor implements TopicHandlerInterceptor {


    @Override
    public void preHandle(Object handler) throws Exception {
        log.info("log拦截器 preHandl...");
    }

    @Override
    public void postHandle(Object handler) throws Exception {
        log.info("log拦截器 postHandle...");
    }

    @Override
    public void afterCompletion(Object handler, Exception ex) throws Exception {
        log.info("log拦截器 afterCompletion...");
    }
}
