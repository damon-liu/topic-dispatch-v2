package com.lhx.dispatch.v2.core.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Description
 *
 * @author damon.liu
 * Date 2022-12-26 8:46
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Security {

    /**
     * 值
     * @return
     */
    String[]  value() default {};

}
