package com.modules.rpc.core;

import com.modules.protocol.codec.RpcDecoder;
import com.modules.protocol.codec.RpcEncoder;
import com.modules.protocol.handler.RpcRequestHandler;
import com.modules.rpc.annotation.RPCService;
import com.modules.rpc.common.RpcServiceHelper;
import com.modules.rpc.common.ServiceMeta;
import com.modules.rpc.registry.RegistryService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 服务提供者暴露服务的核心逻辑在该类中实现
 * @author chenlingl
 * @version 1.0
 * @date 2021/12/30 15:30
 */
@Slf4j
public class RPCProviderBean implements InitializingBean, BeanPostProcessor {
    /**
     * 服务提供者url
     */
    private String serviceAddress;
    /**
     * 服务端口
     */
    private final int servicePort;
    /**
     * 注册中心地址
     */
    private final RegistryService serviceRegistry;

    /**
     * 服务暴露容器
     */
    private final Map<String, Object> rpcServiceMap = new HashMap<>();

    public RPCProviderBean(int servicePort, RegistryService registryService) {
        this.servicePort = servicePort;
        this.serviceRegistry = registryService;
    }

    @Override
    public void afterPropertiesSet() {
        //使用线程池在netty服务端监听端口做流程处理
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1,2,1, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1),new ThreadPoolExecutor.AbortPolicy());
        executor.submit(new ProviderStartTask());
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //Bean初始化之后开始捕捉需要暴露的服务接口
        RPCService rpcService = bean.getClass().getAnnotation(RPCService.class);
        if (rpcService != null) {
            String interfaceName = rpcService.serviceInterface().getName();
            String serviceVersion = rpcService.serviceVersion();
            try {
                ServiceMeta serviceMeta = new ServiceMeta();
                serviceMeta.setServiceAddress(this.serviceAddress);
                serviceMeta.setServicePort(this.servicePort);
                serviceMeta.setServiceName(interfaceName);
                serviceMeta.setServiceVersion(serviceVersion);
                //注册到注册中心
                serviceRegistry.register(serviceMeta);
                rpcServiceMap.put(RpcServiceHelper.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getServiceVersion()),bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bean;
    }


    /**
     * 服务提供者需要启动服务，即对外可以暴露自己
     * 通过netty的方式实现
     */
    protected void runServer() throws UnknownHostException, InterruptedException {
        this.serviceAddress = InetAddress.getLocalHost().getHostAddress();
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new RpcEncoder())//序列化加密
                                    .addLast(new RpcDecoder())//序列化解密
                                    .addLast(new RpcRequestHandler(rpcServiceMap));//处理类
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            ChannelFuture channelFuture = bootstrap.bind(this.serviceAddress,this.servicePort).sync();
            log.info("=========server address {} started on port {}=================", this.serviceAddress, this.servicePort);
            channelFuture.channel().closeFuture().sync();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    /**
     * 内部类线程
     */
    public class ProviderStartTask implements Runnable {
        //开始一个线程执行处理逻辑
        @SneakyThrows
        @Override
        public void run() {
            runServer();
        }
    }
}
