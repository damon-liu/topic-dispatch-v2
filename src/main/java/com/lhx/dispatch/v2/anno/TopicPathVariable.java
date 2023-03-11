package com.lhx.dispatch.v2.anno;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 类似 @PathVariable 用于解析请求地址上参数
 * @author damon
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TopicPathVariable {

    @AliasFor("name")
    String value() default "";

    @AliasFor("value")
    String name() default "";

}
