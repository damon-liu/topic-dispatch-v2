package com.lhx.dispatch.v2.core;

import com.lhx.dispatch.v2.anno.TopicController;
import com.lhx.dispatch.v2.anno.TopicPathVariable;
import com.lhx.dispatch.v2.anno.TopicRequestMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Component
public class TopicHandlerMapping {

    @Autowired
    private ApplicationContext applicationContext;

    public static Map<String, Method> handlerMapping = new HashMap<>();

    @PostConstruct
    public void init() {
        Map<String, Object> topicClazzBeans = applicationContext.getBeansWithAnnotation(TopicController.class);
        topicClazzBeans.forEach((name, bean) -> {
            // 获取被@Topic修饰controller类
            Class<?> clazz = bean.getClass();
            TopicRequestMapping baseTopic = clazz.getAnnotation(TopicRequestMapping.class);
            // controller类中所有的方法
            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                // 判断注解为RequestMapping
                if (method.isAnnotationPresent(TopicRequestMapping.class)) {
                    // 校验
                    checkMethodParam(method);
                    // 获取方法上的路径
                    TopicRequestMapping methodRm = method.getAnnotation(TopicRequestMapping.class);
                    String methodPath = methodRm.value();
                    handlerMapping.put(baseTopic.value() + methodPath, method);
                }
            }
        });
        log.info("topicHandlerMapping: {}", handlerMapping);
    }

    // @PostConstruct
    // public void init() {
    //     Map<String, Object> topicClazzBeans = applicationContext.getBeansWithAnnotation(TopicController.class);
    //     topicClazzBeans.forEach((name, bean) -> {
    //         // 获取被@Topic修饰controller类
    //         Class<?> clazz = bean.getClass();
    //         TopicController baseTopic = clazz.getAnnotation(TopicController.class);
    //         // controller类中所有的方法
    //         Method[] methods = clazz.getMethods();
    //         for (Method method : methods) {
    //             // 判断注解为RequestMapping
    //             if (method.isAnnotationPresent(TopicController.class)) {
    //                 // 校验
    //                 checkMethodParam(method);
    //                 // 获取方法上的路径
    //                 TopicController methodRm = method.getAnnotation(TopicController.class);
    //                 String methodPath = methodRm.value();
    //                 handlerMapping.put(baseTopic.value() + methodPath, method);
    //             }
    //         }
    //     });
    //     log.info("topicHandlerMapping: {}", handlerMapping);
    // }

    private void checkMethodParam(Method method) {
        Class<?>[] types = method.getParameterTypes();

        //获取方法参数注解
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        int index = 0;
        for (Annotation[] annotations : parameterAnnotations) {
            for (Annotation annotation : annotations) {
                //获取注解名
                if (annotation instanceof TopicPathVariable) {
                    index++;
                }
            }
        }
        if (types.length > index + 1) {
            log.error("使用@topic注解的方法，除了@TopicPathVar标示的参数，其他参数只能为 <= 1 个 -- {}", method);
            throw new IllegalArgumentException("使用@topic注解的方法，除了@TopicPathVar标示的参数，其他参数只能为 <= 1 个");
        }
    }
}
