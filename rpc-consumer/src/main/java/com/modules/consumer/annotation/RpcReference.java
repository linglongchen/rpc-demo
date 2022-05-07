package com.modules.consumer.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenlingl
 * @version 1.0
 * @date 2022/1/4 18:59
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Autowired
public @interface RpcReference {
    String serviceVersion() default "1.0";

    String registryType() default "ZOOKEEPER";

    String registryAddr() default "";

    long timeout() default 5000;
}
