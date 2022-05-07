package com.modules.consumer.proxy;

import com.modules.consumer.core.RPCConsumer;
import com.modules.protocol.msg.RpcProtocol;
import com.modules.protocol.msg.MsgHeader;
import com.modules.protocol.msg.MsgType;
import com.modules.protocol.constants.ProtocolConstants;
import com.modules.rpc.common.RpcFuture;
import com.modules.rpc.common.RpcRequest;
import com.modules.rpc.common.RpcRequestHolder;
import com.modules.rpc.common.RpcResponse;
import com.modules.rpc.registry.RegistryService;
import com.modules.protocol.serialization.SerializationTypeEnum;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author chenlingl
 * @version 1.0
 * @date 2022/1/4 19:09
 */
public class RPCInvokeProxy implements InvocationHandler {

    private final String serviceVersion;

    private final long timeout;

    private final RegistryService registryService;

    public RPCInvokeProxy(String serviceVersion,long timeout,RegistryService registryService) {
        this.serviceVersion = serviceVersion;
        this.registryService = registryService;
        this.timeout = timeout;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //实现代理逻辑

        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
        MsgHeader header = new MsgHeader();
        long requestId = RpcRequestHolder.REQUEST_ID_GEN.incrementAndGet();
        header.setMagic(ProtocolConstants.MAGIC);
        header.setVersion(ProtocolConstants.VERSION);
        header.setRequestId(requestId);
        header.setSerialization((byte) SerializationTypeEnum.HESSIAN.getType());
        header.setMsgType((byte) MsgType.REQUEST.getType());
        header.setStatus((byte) 0x1);
        protocol.setHeader(header);

        RpcRequest request = new RpcRequest();
        request.setServiceVersion(this.serviceVersion);
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParams(args);
        protocol.setBody(request);

        RPCConsumer rpcConsumer = new RPCConsumer();
        RpcFuture<RpcResponse> future = new RpcFuture<>(new DefaultPromise<>(new DefaultEventLoop()), timeout);
        RpcRequestHolder.REQUEST_MAP.put(requestId, future);
        rpcConsumer.sendRequest(protocol, this.registryService);
        // TODO hold request by ThreadLocal
        return future.getPromise().get(future.getTimeout(), TimeUnit.MILLISECONDS).getData();
    }
}
