package com.modules.rpc.autoConfig;

import com.modules.rpc.core.RPCProviderAutoConfiguration;
import lombok.Data;
import org.springframework.context.annotation.Import;

/**
 * @author chenlingl
 * @version 1.0
 * @date 2022/1/24 13:55
 */
@Data
@Import(RPCProviderAutoConfiguration.class)
public class RpcAutoConfig {
}
