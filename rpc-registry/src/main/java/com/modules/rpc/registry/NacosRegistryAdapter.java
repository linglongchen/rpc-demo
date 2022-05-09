package com.modules.rpc.registry;

import com.modules.rpc.common.ServiceMeta;

import java.io.IOException;

public class NacosRegistryAdapter implements RegistryService{
    @Override
    public void register(ServiceMeta serviceMeta) throws Exception {

    }

    @Override
    public void unRegister(ServiceMeta serviceMeta) throws Exception {

    }

    @Override
    public ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception {
        return null;
    }

    @Override
    public void destroy() throws IOException {

    }
}
