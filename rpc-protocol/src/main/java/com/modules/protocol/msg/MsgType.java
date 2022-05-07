package com.modules.protocol.msg;

import lombok.Getter;

/**
 * 消息类型
 * @author chenlingl
 */

public enum MsgType {
    /**
     * 请求
     */
    REQUEST(1),
    /**
     * 响应
     */
    RESPONSE(2),
    /**
     * 心跳
     */
    HEARTBEAT(3);

    @Getter
    private final int type;

    MsgType(int type) {
        this.type = type;
    }

    public static MsgType findByType(int type) {
        for (MsgType msgType : MsgType.values()) {
            if (msgType.getType() == type) {
                return msgType;
            }
        }
        return null;
    }
}
