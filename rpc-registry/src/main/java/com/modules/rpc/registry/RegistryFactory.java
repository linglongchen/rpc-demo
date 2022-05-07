package com.modules.rpc.registry;

/**
 * @author chenlingl
 * @version 1.0
 * @date 2022/1/4 17:06
 */
public class RegistryFactory {
    //注册服务
    private static volatile RegistryService registryService;

    public static RegistryService getInstance(String registryAddr,RegistryTypeEnum registryTypeEnum) throws Exception {
        if (null != registryAddr) {
            synchronized (RegistryFactory.class) {
                if (null == registryService) {
                    switch (registryTypeEnum) {
                        case ZOOKEEPER:
                            //zookeeper的实现
                            registryService = new ZookeeperRegistryAdapter(registryAddr);
                        case NACOS:
                        case CONSUL:
                        case EUREKA:
                    }
                }
            }
        }
        return registryService;
    }
}
