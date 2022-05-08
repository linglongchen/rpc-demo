package com.modules;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author chenlingl
 * @version 1.0
 * @date 2022/1/4 22:30
 */
@SpringBootApplication
@EnableConfigurationProperties
public class RPCConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(RPCConsumerApplication.class,args);
    }
}
