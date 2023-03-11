package com.lhx.dispatch.v2.core.adapter;

import com.lhx.dispatch.v2.anno.TopicModelAttribute;
import com.lhx.dispatch.v2.anno.TopicPathVariable;
import com.lhx.dispatch.v2.core.handler.TopicDefaultHandle;
import com.lhx.dispatch.v2.entity.TopicBaseRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * Description
 *
 * @author damon.liu
 * Date 2022-12-26 5:55
 */
@Slf4j
@Component
public class TopicDefaultHandlerAdapter implements TopicHandlerAdapter {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof TopicDefaultHandle);
    }

    @Override
    public Object handle(Object handler) throws Exception {
        TopicDefaultHandle topicHandler = (TopicDefaultHandle) handler;

        // 解析参数
        Object[] org = this.resolverParam(topicHandler);

        // 反射执行方法
        return this.execute(topicHandler.getMethod(), org);
    }


    private Object[] resolverParam(TopicDefaultHandle topicHandler) {
        Method method = topicHandler.getMethod();
        Map<String, String> pathValMap = topicHandler.getPathValMap();
        int parameterCount = method.getParameterCount();
        if (parameterCount == 0) {
            return null;
        }
        TopicBaseRequest request = topicHandler.getRequest();
        Object[] org = new Object[parameterCount];
        int index = 0;


        // 参数数组封装
        Parameter[] parameters = method.getParameters();
        for (Parameter parameter : parameters) {
            Annotation[] annotations = parameter.getAnnotations();
            if (annotations.length > 0) {
                for (Annotation annotation : annotations) {
                    // 获取注解名
                    if (annotation instanceof TopicPathVariable) {
                        TopicPathVariable topicPathVar = (TopicPathVariable) annotation;
                        // 有URI路径赋值
                        org[index] = pathValMap.get(topicPathVar.value());
                    } else if (annotation instanceof TopicModelAttribute) {
                        // 有POJO参数
                        org[index] = request;
                    }
                }
            } else {
                // 暂时只支持单个简单参数绑定
                Class<?> paramClazz = parameter.getType();
                try {
                    Object instance = paramClazz.newInstance();
                    if (instance instanceof String) {
                        org[index] = request.getTopicCode();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ++index;
        }
        return org;
    }

    private Object execute(Method method, Object[] org) {
        Class<?> clazz = method.getDeclaringClass();
        Object instance = applicationContext.getBean(clazz);
        Object result = null;
        try {
            log.info("【开始调用执行器】 {}.{}", clazz.getName(), method.getName());
            result = method.invoke(instance, org);
            log.info("【执行结果】 {} ", result);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            log.error("此处接收被调用方法内部未被捕获的异常");
            Throwable t = e.getTargetException();
            t.printStackTrace();
            log.error("代理异常:{}", t.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
