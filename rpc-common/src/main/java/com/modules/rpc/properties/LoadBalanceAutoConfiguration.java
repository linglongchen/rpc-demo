package com.modules.rpc.properties;

import com.modules.rpc.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;


/**
 * @author chenlingl
 * @version 1.0
 * @date 2022/1/24 18:20
 */
@Configuration
@EnableConfigurationProperties(LoadBalanceProperties.class)
@Slf4j
public class LoadBalanceAutoConfiguration {

    @Resource
    private LoadBalanceProperties loadBalanceProperties;


    @Bean
    public void init() {
        log.info("==============负载均衡策略：{}===============",loadBalanceProperties.getLoadBalance());
    }

    @Bean
    public SpringUtil initUtil() {
        return new SpringUtil();
    }
}
