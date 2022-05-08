package com.modules.rpc.strategy;

import com.modules.rpc.common.ServiceMeta;
import com.modules.rpc.enums.LoadBalanceEnum;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询策略
 *
 * @author chenlingl
 * @version 1.0
 * @date 2022/1/24 14:47
 */
@Configuration
public class OrderStrategy implements LoadBalanceStrategy {

    /**
     * 使用concurrentHashmap解决并发问题
     */
    private ConcurrentHashMap<String, AtomicInteger> map = new ConcurrentHashMap();

    @Override
    public ServiceInstance<ServiceMeta> executeLoadBalance(Collection<ServiceInstance<ServiceMeta>> serviceInstances) {
        if (serviceInstances.isEmpty()) {
            return null;
        }
        ServiceInstance<ServiceMeta> serviceMetaServiceInstance;
        String key = ((ServiceInstance<ServiceMeta>) serviceInstances.toArray()[0]).getName();
        if (map.get(key) == null) {
            serviceMetaServiceInstance = (ServiceInstance<ServiceMeta>) serviceInstances.toArray()[0];
            map.put(key, new AtomicInteger(1));
        } else {
            AtomicInteger val = map.get(key);
            //轮询达到最大时，回滚
            if (val.intValue() == serviceInstances.size()) {
                val = new AtomicInteger(0);
            }
            serviceMetaServiceInstance = (ServiceInstance<ServiceMeta>) serviceInstances.toArray()[val.intValue()];
            val.incrementAndGet();
            map.put(key, val);
        }
        return serviceMetaServiceInstance;
    }

    @Override
    public String getStrategy() {
        return LoadBalanceEnum.ORDER.getVal();
    }
}
