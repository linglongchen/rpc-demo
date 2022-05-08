package com.modules.rpc.strategy;

import com.modules.rpc.common.ServiceMeta;
import com.modules.rpc.enums.LoadBalanceEnum;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Random;
/**
 * 随机策略
 * @author chenlingl
 * @version 1.0
 * @date 2022/1/24 14:46
 */
@Configuration
public class RandomStrategy implements LoadBalanceStrategy {

    @Override
    public ServiceInstance<ServiceMeta> executeLoadBalance(Collection<ServiceInstance<ServiceMeta>> serviceInstances) {
        Random random = new Random();
        int randomSum = random.nextInt(serviceInstances.size());
        return (ServiceInstance<ServiceMeta>) serviceInstances.toArray()[randomSum];
    }

    @Override
    public String getStrategy() {
        return LoadBalanceEnum.RANDOM.getVal();
    }
}
