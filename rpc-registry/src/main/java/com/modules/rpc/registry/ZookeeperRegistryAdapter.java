package com.modules.rpc.registry;

import com.modules.rpc.common.RpcServiceHelper;
import com.modules.rpc.common.ServiceMeta;
import com.modules.rpc.strategy.StrategyFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.io.IOException;
import java.util.Collection;

/**
 * @author chenlingl
 * @version 1.0
 * @date 2022/1/4 17:08
 */
public class ZookeeperRegistryAdapter implements RegistryService{
    public static final int BASE_SLEEP_TIME_MS = 1000;

    public static final int MAX_RETRES = 3;

    public static final String ZK_BASE_PATH = "/rpc-demo";

    /**
     * 服务发现
     */
    private final ServiceDiscovery<ServiceMeta> serviceDiscovery;


    public ZookeeperRegistryAdapter(String registryAddr) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddr,new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS,MAX_RETRES));
        client.start();
        JsonInstanceSerializer<ServiceMeta> serializer = new JsonInstanceSerializer(ServiceMeta.class);
        this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMeta.class)
                .client(client)
                .serializer(serializer)
                .basePath(ZK_BASE_PATH)
                .build();
        this.serviceDiscovery.start();
    }
    @Override
    public void register(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceInstance = ServiceInstance
                .<ServiceMeta>builder()
                .name(RpcServiceHelper.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getServiceVersion()))
                .address(serviceMeta.getServiceAddress())
                .port(serviceMeta.getServicePort())
                .payload(serviceMeta)
                .build();
        serviceDiscovery.registerService(serviceInstance);
    }

    @Override
    public void unRegister(ServiceMeta serviceMeta) throws Exception {

    }

    @Override
    public ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception {
        Collection<ServiceInstance<ServiceMeta>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
        //增加负载均衡策略
        StrategyFactory loadBalance = new StrategyFactory();
        ServiceInstance<ServiceMeta> instance = loadBalance.createStrategy(serviceInstances);
//        ServiceInstance<ServiceMeta> instance = (ServiceInstance<ServiceMeta>) serviceInstances.toArray()[0];
        if (null != instance) {
            return instance.getPayload();
        }
        return null;
    }

    @Override
    public void destroy() throws IOException {

    }
}
