package com.modules.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;

/**
 * 使用该注解的接口会暴露到服务注册中心
 * @author chenlingl
 * @version 1.0
 * @date 2021/12/30 15:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface RPCService {
    /**
     * 接口名称
     * @return
     */
    Class<?> serviceInterface() default Object.class;

    /**
     * 接口版本
     * @return
     */
    String serviceVersion() default "1.0";
}
