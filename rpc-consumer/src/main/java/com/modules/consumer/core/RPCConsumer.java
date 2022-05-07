package com.modules.consumer.core;

import com.modules.protocol.codec.RpcDecoder;
import com.modules.protocol.codec.RpcEncoder;
import com.modules.protocol.msg.RpcProtocol;
import com.modules.protocol.handler.RpcResponseHandler;
import com.modules.rpc.common.RpcRequest;
import com.modules.rpc.common.RpcServiceHelper;
import com.modules.rpc.common.ServiceMeta;
import com.modules.rpc.registry.RegistryService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chenlingl
 * @version 1.0
 * @date 2022
 * /1/4 22:15
 */
@Slf4j
public class RPCConsumer {
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;

    public RPCConsumer() {
        bootstrap = new Bootstrap();
        eventLoopGroup =new NioEventLoopGroup(4);
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new RpcEncoder())//序列化
                                .addLast(new RpcDecoder())//反序列化
                                .addLast(new RpcResponseHandler());
                    }
                });
    }

    public void sendRequest(RpcProtocol<RpcRequest> rpcProtocol, RegistryService registryService) throws Exception {
        RpcRequest request = rpcProtocol.getBody();
        Object[] params = request.getParams();
        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getServiceVersion());;
        int invokeHashCode = params.length > 0 ? params[0].hashCode() : serviceKey.hashCode();
        ServiceMeta serviceMeta = registryService.discovery(serviceKey,invokeHashCode);
        if (null != serviceMeta) {
            ChannelFuture future = bootstrap.connect(serviceMeta.getServiceAddr(), serviceMeta.getServicePort()).sync();
            future.addListener((ChannelFutureListener) args -> {
                if (future.isSuccess()) {
                    log.info("============connect rpc server {} on port {} success.============", serviceMeta.getServiceAddr(), serviceMeta.getServicePort());
                } else {
                    log.error("===========connect rpc server {} on port {} failed.=============", serviceMeta.getServiceAddr(), serviceMeta.getServicePort());
                    future.cause().printStackTrace();
                    eventLoopGroup.shutdownGracefully();
                }
            });
            future.channel().writeAndFlush(rpcProtocol);
        }
    }
}
