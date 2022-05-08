package com.modules.rpc.enums;

/**
 * @author chenlingl
 * @version 1.0
 * @date 2022/1/25 10:05
 */
public enum LoadBalanceEnum {
    ORDER("ORDER"),
    RANDOM("RANDOM");

    private String val;

    LoadBalanceEnum(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
