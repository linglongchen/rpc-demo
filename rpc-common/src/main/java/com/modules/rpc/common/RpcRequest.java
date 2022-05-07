package com.modules.rpc.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcRequest implements Serializable {
    private String serviceVersion;
    private String className;
    private String methodName;
    private Object[] params;
    private Class<?>[] parameterTypes;
}
