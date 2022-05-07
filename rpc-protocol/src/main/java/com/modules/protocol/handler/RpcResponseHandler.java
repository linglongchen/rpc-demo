package com.modules.protocol.handler;

import com.modules.protocol.msg.RpcProtocol;
import com.modules.rpc.common.RpcFuture;
import com.modules.rpc.common.RpcRequestHolder;
import com.modules.rpc.common.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chenlingl
 * @version 1.0
 * @date 2022/1/5 16:19
 */
@Slf4j
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> rpcResponseRpcProtocol) {
        long requestId = rpcResponseRpcProtocol.getHeader().getRequestId();
        RpcFuture<RpcResponse> rpcResponseRpcFuture = RpcRequestHolder.REQUEST_MAP.remove(requestId);
        rpcResponseRpcFuture.getPromise().setSuccess(rpcResponseRpcProtocol.getBody());
        log.info("==========consumer发起请求=============");
    }
}
