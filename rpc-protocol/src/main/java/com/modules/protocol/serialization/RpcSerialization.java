package com.modules.protocol.serialization;

import java.io.IOException;

public interface RpcSerialization {
    <T> byte[] serialize(T obj) throws Exception;

    <T> T deserialize(byte[] data, Class<T> clz) throws Exception;
}