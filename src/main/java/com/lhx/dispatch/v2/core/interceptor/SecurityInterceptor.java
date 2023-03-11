package com.lhx.dispatch.v2.core.interceptor;


import com.lhx.dispatch.v2.core.handler.TopicDefaultHandle;
import com.lhx.dispatch.v2.core.security.SecurityContext;
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
@Order(1)
@Slf4j
public class SecurityInterceptor implements TopicHandlerInterceptor {

    @Override
    public void preHandle(Object handler) throws Exception {
        SecurityContext securityContext = SecurityContext.getInstance();
        boolean result = securityContext.verify((TopicDefaultHandle) handler, "damon");
        if (!result) {
            throw new Exception("403  对不起,无权限访问!");
        }
    }

    // @Override
    // public void postHandle(Object handler) throws Exception {
    //     log.info("security截器拦截器 postHandle...");
    // }

    // @Override
    // public void afterCompletion(Object handler, Exception ex) throws Exception {
    //     log.info("security截器拦截器 afterCompletion处...");
    // }
}
