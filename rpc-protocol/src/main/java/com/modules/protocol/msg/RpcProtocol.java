package com.modules.protocol.msg;

import lombok.Data;

import java.io.Serializable;

/**
 * 远程数据传输协议
 * @author chenlingl
 */
@Data
public class RpcProtocol<T> implements Serializable {
    private MsgHeader header;
    private T body;
}
