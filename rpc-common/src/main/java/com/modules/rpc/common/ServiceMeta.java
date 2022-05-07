package com.modules.rpc.common;

import lombok.Data;

/**
 * @author chenlingl
 * @version 1.0
 * @date 2021/12/30 16:00
 */
@Data
public class ServiceMeta {
    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务版本
     */
    private String serviceVersion;

    /**
     * 服务url
     */
    private String serviceAddr;

    /**
     * 服务port
     */
    private int servicePort;

    private int serverPort;
}
