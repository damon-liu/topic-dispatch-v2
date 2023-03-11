package com.lhx.dispatch.v2.core.interceptor;

/**
 * Description
 *
 * @author damon.liu
 * Date 2022-12-26 3:15
 */

public interface TopicHandlerInterceptor {

    /**
     * 前置处理
     *
     * @param handler 处理器
     * @return
     * @throws Exception
     */
    default void preHandle(Object handler) throws Exception {
    }

    /**
     * 后置处理
     *
     * @param handler 处理器
     * @throws Exception
     */
    default void postHandle(Object handler) throws Exception {
    }

    /**
     * 处理完成
     *
     * @param handler 处理器
     * @throws Exception
     */
    default void afterCompletion(Object handler, Exception ex) throws Exception {
    }
}
