package com.lhx.dispatch.v2.core;


import com.lhx.dispatch.v2.core.adapter.TopicHandlerAdapter;
import com.lhx.dispatch.v2.core.handler.TopicDefaultHandle;
import com.lhx.dispatch.v2.core.handler.TopicHandlerExecutionChain;
import com.lhx.dispatch.v2.core.interceptor.TopicHandlerInterceptor;
import com.lhx.dispatch.v2.entity.TopicBaseRequest;
import com.lhx.dispatch.v2.entity.TopicResult;
import com.lhx.dispatch.v2.util.PathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class TopicDispatch {

    @Resource
    private List<TopicHandlerAdapter> handlerAdapters;

    @Resource
    private List<TopicHandlerInterceptor> handlerInterceptorList;


    public Object doDispatchTopic(TopicBaseRequest eq) throws Exception {
        TopicHandlerExecutionChain mappedHandler = null;
        boolean isException = false;
        Object obj;

        try {
            // 1.处理器执行链
            mappedHandler = this.getHandler(eq);
            Object handler = mappedHandler.getHandler();

            // 2.处理器适配器
            TopicHandlerAdapter adapter = this.getHandlerAdapter(handler);

            // 3.执行前置处理（拦截器前置处理）
            mappedHandler.applyPreHandle();

            // 4.处理器适配器处理请求
            obj = adapter.handle(handler);

            // 5.执行后置处理（拦截器后置处理）
            mappedHandler.applyPostHandle();
        } catch (Exception e) {
            isException = true;
            log.info(e.getMessage());
            return new TopicResult<>(-1, "fail", e.getMessage());
        } finally {
            // 执行整个请求完毕后置处理
            if (!isException && mappedHandler != null) {
                mappedHandler.triggerAfterCompletion(null);
            }
        }

        // 返回结果集
        return new TopicResult<>(0, "success", obj);
    }

    protected TopicHandlerAdapter getHandlerAdapter(Object handler) throws Exception {
        if (this.handlerAdapters != null) {
            for (TopicHandlerAdapter adapter : this.handlerAdapters) {
                if (adapter.supports(handler)) {
                    return adapter;
                }
            }
        }
        throw new Exception(handler + ": 未找到对应的适配器！");
    }

    private TopicHandlerExecutionChain getHandler(TopicBaseRequest request) throws Exception {
        // 去除尾 /
        String topicPath = request.getTopicUri();
        topicPath = topicPath.startsWith("/") ? topicPath : "/" + topicPath;
        topicPath = topicPath.endsWith("/") ? topicPath.substring(0, topicPath.length() - 2) : topicPath;

        // 通过uri匹配实现类
        Method method = TopicHandlerMapping.handlerMapping.get(topicPath);
        Map<String, String> pathValMap = null;
        if (method == null) {
            // 地址匹配法实施
            for (String pattern : TopicHandlerMapping.handlerMapping.keySet()) {
                if (PathUtil.isPathMatch(pattern, topicPath)) {
                    method = TopicHandlerMapping.handlerMapping.get(pattern);
                    log.info("匹配执行器 {} -> {}", topicPath, pattern);
                    pathValMap = PathUtil.pathKeyVal(pattern, topicPath);
                    break;
                }
            }
        }

        // 封装执行器
        if (method == null) {
            throw new Exception("404  未找到: " + topicPath + "对应的执行器！");
        }
        TopicDefaultHandle defaultHandle = new TopicDefaultHandle(pathValMap, method, request);

        // 封装执行器链
        return getHandlerExecutionChain(defaultHandle);
    }

    protected TopicHandlerExecutionChain getHandlerExecutionChain(TopicDefaultHandle handler) {
        TopicHandlerExecutionChain handlerChain = new TopicHandlerExecutionChain(handler);
        handlerChain.setInterceptorList(handlerInterceptorList);
        handlerChain.setInterceptors(handlerChain.getInterceptors());
        return handlerChain;
    }
}
