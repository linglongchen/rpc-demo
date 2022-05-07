package com.modules.consumer.config;

import com.modules.rpc.properties.LoadBalanceAutoConfiguration;
import lombok.Data;
import org.springframework.context.annotation.Import;

/**
 * @author chenlingl
 * @version 1.0
 * @date 2022/1/24 17:53
 */
@Data
@Import({LoadBalanceAutoConfiguration.class})
public class AutoConfig {
}
