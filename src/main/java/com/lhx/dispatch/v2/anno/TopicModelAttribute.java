package com.lhx.dispatch.v2.anno;

import java.lang.annotation.*;

/**
 * 类似 @RequestMapping 用法
 * @author damon
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TopicModelAttribute {
}
