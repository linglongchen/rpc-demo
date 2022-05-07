package com.modules.rpc.registry;


import com.modules.rpc.common.ServiceMeta;

import java.io.IOException;

/**
 * 注册服务对外接口
 * @author chenlingl
 * @version 1.0
 * @date 2021/12/30 15:38
 */
public interface RegistryService {
    /**
     * 注册
     * @param serviceMeta
     * @throws Exception
     */
    void register(ServiceMeta serviceMeta) throws Exception;

    /**
     * 销毁
     * @param serviceMeta
     * @throws Exception
     */
    void unRegister(ServiceMeta serviceMeta) throws Exception;

    /**
     * 服务发现
     * @param serviceName
     * @param invokerHashCode
     * @return
     * @throws Exception
     */
    ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception;

    /**
     * 销毁
     * @throws IOException
     */
    void destroy() throws IOException;
}
