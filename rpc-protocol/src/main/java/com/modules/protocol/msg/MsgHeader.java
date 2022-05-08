package com.modules.protocol.msg;

import lombok.Data;

import java.io.Serializable;

/**
 * 远程数据传输协议
 * @author chenlingl
 */
@Data
public class MsgHeader implements Serializable {
    /*
    +---------------------------------------------------------------+ +---------------------------------------------------------------+
    | 魔数 2byte | 协议版本号 1byte | 序列化算法 1byte | 报文类型 1byte  | 状态 1byte |  消息 ID 8byte  |  数据长度 4byte     |
    +---------------------------------------------------------------+ +---------------------------------------------------------------+
    */

    /**
     * 魔数
     */
    private short magic;
    /**
     * 协议版本号
     */
    private byte version;
    /**
     * 序列化算法
     */
    private byte serialization;
    /**
     * 报文类型
     */
    private byte msgType;
    /**
     * 状态
     */
    private byte status;
    /**
     * 消息id
     */
    private long requestId;
    /**
     * 数据长度
     */
    private int msgLen;
}
