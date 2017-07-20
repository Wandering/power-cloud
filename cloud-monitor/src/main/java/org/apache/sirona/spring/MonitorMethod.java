package org.apache.sirona.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * TODO 一句话描述该类用途
 * <p/>
 * 创建时间: 14/10/28 下午5:57<br/>
 *
 * @author qyang
 * @since v0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MonitorMethod {
    String value() default "333";

    String test() default "";
}
