package com.modules.rpc.strategy;

import com.modules.rpc.common.ServiceMeta;
import org.apache.curator.x.discovery.ServiceInstance;

import java.util.Collection;

/**
 * @author chenlingl
 * @version 1.0
 * @date 2022/1/24 14:45
 */
public interface LoadBalanceStrategy {
    /**
     * 策略
     * @param serviceInstances
     * @return
     */
    ServiceInstance<ServiceMeta> executeLoadBalance(Collection<ServiceInstance<ServiceMeta>> serviceInstances);

    /**
     * 获取策略
     * @return
     */
    String getStrategy();
}
