package com.lhx.dispatch.v2.core.security;

import com.lhx.dispatch.v2.anno.TopicController;
import com.lhx.dispatch.v2.core.handler.TopicDefaultHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description
 *
 * @author damon.liu
 * Date 2022-12-26 8:54
 */
@Slf4j
@Component
public class SecurityContext {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 安全权限集合
     */
    public static Map<String, List<String>> securityMap = new HashMap<>();


    /**
     * 单例对象
     * volatile禁止指令重排序优化
     */
    private static volatile SecurityContext securityContext;

    /**
     * 双重加锁的方式实现
     *
     * @return
     */
    public static SecurityContext getInstance() {
        if (securityContext == null) {
            synchronized (SecurityContext.class) {
                if (securityContext == null) {
                    securityContext = new SecurityContext();
                }
            }
        }
        return securityContext;
    }

    /**
     * 初始安全上下文
     */
    @PostConstruct
    public void init() {
        Map<String, Object> topicClazzBeans = applicationContext.getBeansWithAnnotation(TopicController.class);
        topicClazzBeans.forEach((name, bean) -> {
            // 获取被@Topic修饰controller类
            Class<?> beanClass = bean.getClass();
            // controller类中所有的方法
            Method[] methods = beanClass.getMethods();
            for (Method method : methods) {
                if (beanClass.isAnnotationPresent(Security.class) && method.isAnnotationPresent(Security.class)) {
                    // 类和方法上同时有@Security注解
                    Security securityAnnotation = beanClass.getAnnotation(Security.class);
                    String[] values = securityAnnotation.value();
                    List<String> valueList = Arrays.asList(values);
                    Security methodSecurityAnnotation = method.getAnnotation(Security.class);
                    String[] methodValues = methodSecurityAnnotation.value();
                    List<String> methodValueList = Arrays.asList(methodValues);
                    // 取交集
                    valueList.retainAll(methodValueList);
                    securityMap.put(method.getName(), valueList);
                } else if (beanClass.isAnnotationPresent(Security.class)) {
                    // 仅类上有@Security注解
                    Security securityAnnotation = beanClass.getAnnotation(Security.class);
                    String[] values = securityAnnotation.value();
                    List<String> valueList = Arrays.asList(values);
                    securityMap.put(method.getName(), valueList);
                } else if (method.isAnnotationPresent(Security.class)) {
                    // 仅方法上有@Security注解
                    Security methodSecurityAnnotation = method.getAnnotation(Security.class);
                    String[] methodValues = methodSecurityAnnotation.value();
                    List<String> methodValueList = Arrays.asList(methodValues);
                    securityMap.put(method.getName(), methodValueList);
                }
            }
        });
    }

    /**
     * 验证是否有权限
     *
     * @param handler 处理器
     * @return
     */
    public boolean verify(TopicDefaultHandle handler, String authValue) {
        return verify(handler.getMethod().getName(), authValue);
    }

    /**
     * 验证是否有权限
     *
     * @param handlerName 处理器名称
     * @param authValue   值
     * @return
     */
    public boolean verify(String handlerName, String authValue) {
        List<String> authList = securityMap.get(handlerName);
        // 无权限控制直接放行
        if (authList == null || authList.size() == 0) {
            return true;
        }
        log.info("security截器拦截器 preHandle...");
        if (authList.contains(authValue)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 私有化构造函数
     */
    // private SecurityContext() {
    //     try {
    //         initSecurityContext();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         throw new RuntimeException(e);
    //     }
    // }
}
