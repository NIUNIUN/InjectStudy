package com.qinglianyun.annolib;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tang_xqing on 2020/8/26.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface TraceLog {
    int code() default 100;

    String msg() default "success";
}
