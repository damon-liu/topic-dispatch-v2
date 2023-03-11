package com.lhx.dispatch.v2.anno;

import com.lhx.dispatch.v2.dictionary.TopicMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 订阅的topic 类似RequestMapping
 * 目前 非路径参数 只能存在一个
 * @author User
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TopicRequestMapping {

    /**
     * topic具体地址
     * @return
     */
    String value() default "";
}
