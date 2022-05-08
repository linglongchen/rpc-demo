package com.modules.rpc.core;

import com.modules.rpc.registry.RegistryFactory;
import com.modules.rpc.registry.RegistryTypeEnum;
import com.modules.rpc.common.RpcProperties;
import com.modules.rpc.registry.RegistryService;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * RPCProvider自动配置
 * @author chenlingl
 * @version 1.0
 * @date 2021/12/30 15:30
 */
@Configuration
@EnableConfigurationProperties(RpcProperties.class)
public class RPCProviderAutoConfiguration {


    @Resource
    private RpcProperties rpcProperties;


    @Bean
    public RPCProviderBean init() throws Exception {
        //获取注册类型，是consul、nacos、erueka、zookeeper等注册中心
        RegistryTypeEnum type = RegistryTypeEnum.valueOf(rpcProperties.getRegistryType());
        RegistryService serviceRegistry = RegistryFactory.getInstance(rpcProperties.getRegistryAddr(), type);
        return new RPCProviderBean(rpcProperties.getServicePort(), serviceRegistry);
    }
}
