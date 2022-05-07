package com.modules.protocol.handler;

import com.modules.protocol.msg.MsgHeader;
import com.modules.protocol.msg.MsgStatus;
import com.modules.protocol.msg.MsgType;
import com.modules.protocol.msg.RpcProtocol;
import com.modules.rpc.common.RpcRequest;
import com.modules.rpc.common.RpcResponse;
import com.modules.rpc.common.RpcServiceHelper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.reflect.FastClass;

import java.util.Map;

/**
 * @author chenlingl
 * @version 1.0
 * @date 2022/1/5 16:23
 */
@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {
    private final Map<String,Object> rpcService;

    public RpcRequestHandler(Map<String,Object> map) {
        this.rpcService = map;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcRequest> rpcRequestRpcProtocol) {
        RpcRequestProcessor.submitRequest(() -> {
            RpcProtocol<RpcResponse> resProtocol = new RpcProtocol<>();
            RpcResponse response = new RpcResponse();
            MsgHeader header = rpcRequestRpcProtocol.getHeader();
            header.setMsgType((byte) MsgType.RESPONSE.getType());
            try {
                Object result = handle(rpcRequestRpcProtocol.getBody());
                response.setData(result);
                header.setStatus((byte) MsgStatus.SUCCESS.getCode());
                resProtocol.setHeader(header);
                resProtocol.setBody(response);
            } catch (Throwable throwable) {
                header.setStatus((byte) MsgStatus.FAIL.getCode());
                response.setMessage(throwable.toString());
                log.error("==============process request {} error====================", header.getRequestId(), throwable);
            }
            channelHandlerContext.writeAndFlush(resProtocol);
            log.info("==============process request： {}====================", resProtocol);
        });
    }

    private Object handle(RpcRequest request) throws Throwable {
        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getServiceVersion());
        Object serviceBean = rpcService.get(serviceKey);

        if (serviceBean == null) {
            throw new RuntimeException(String.format("service not exist: %s:%s", request.getClassName(), request.getMethodName()));
        }

        Class<?> serviceClass = serviceBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        Object[] parameters = request.getParams();

        FastClass fastClass = FastClass.create(serviceClass);
        int methodIndex = fastClass.getIndex(methodName, parameterTypes);
        return fastClass.invoke(methodIndex, serviceBean, parameters);
    }
}
