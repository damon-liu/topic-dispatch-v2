package com.lhx.dispatch.v2.core.adapter;

/**
 * Description
 *
 * @author damon.liu
 * Date 2022-12-26 4:30
 */
public interface TopicHandlerAdapter {

    boolean supports(Object handler);


    Object handle(Object handler) throws Exception;
}
