package com.modules.rpc.strategy;

import com.google.common.collect.Maps;
import com.modules.rpc.common.ServiceMeta;
import com.modules.rpc.enums.LoadBalanceEnum;
import com.modules.rpc.properties.LoadBalanceProperties;
import com.modules.rpc.util.SpringUtil;
import org.apache.curator.x.discovery.ServiceInstance;
import java.util.Collection;
import java.util.Map;

/**
 * @author chenlingl
 * @version 1.0
 * @date 2022/1/24 14:54
 */
public class StrategyFactory {

    private static final Map<String,LoadBalanceStrategy> map = Maps.newHashMap();


    public ServiceInstance<ServiceMeta> createStrategy(Collection<ServiceInstance<ServiceMeta>> serviceInstances) {
        LoadBalanceProperties loadBalanceProperties = SpringUtil.getBean(LoadBalanceProperties.class);
        LoadBalanceStrategy loadBalanceStrategy = getStrategy(loadBalanceProperties.getLoadBalance());
        return loadBalanceStrategy.executeLoadBalance(serviceInstances);
    }

    public LoadBalanceStrategy getStrategy(String strategy){
        if (!map.containsKey(strategy)) {
            if (strategy.equals(LoadBalanceEnum.ORDER.name())) {
                map.put(strategy,new OrderStrategy());
            } else if (strategy.equals(LoadBalanceEnum.RANDOM.getVal())) {
                map.put(strategy,new RandomStrategy());
            }
        }
        return map.get(strategy);
    }
}
