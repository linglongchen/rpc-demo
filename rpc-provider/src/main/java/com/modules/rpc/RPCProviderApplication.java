package com.modules.rpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * 服务提供者需要提供的能力：
 *  1.服务提供者启动服务，并暴露服务端口；
 *
 *  2.启动时扫描需要对外发布的服务，并将服务元数据信息发布到注册中心；
 *
 *  3.接收 RPC 请求，解码后得到请求消息；
 *
 *  4.提交请求至自定义线程池进行处理，并将处理结果写回客户端。
 *
 * @author chenlingl
 * @version 1.0
 * @date 2021/12/30 15:25
 */
@EnableConfigurationProperties
@SpringBootApplication
public class RPCProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(RPCProviderApplication.class,args);
    }
}
