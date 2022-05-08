package com.modules.rpc.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "rpc")
public class RpcProperties {

    private Integer servicePort;

    private String registryAddr;

    private String registryType;

    private int serverPort;

}
