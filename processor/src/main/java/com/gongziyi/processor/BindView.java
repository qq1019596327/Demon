package com.gongziyi.processor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 2019/9/12
 *
 * @author: gongziyi
 * Description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface BindView {
    int value() default -1;
}
