package com.modules.rpc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chenlingl
 * @version 1.0
 * @date 2022/1/24 18:21
 */
@Data
@ConfigurationProperties(prefix = "lb")
public class LoadBalanceProperties {
    private String loadBalance;
}
