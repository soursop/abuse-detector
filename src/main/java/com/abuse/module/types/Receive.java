package com.abuse.module.types;

import java.util.Map;

public enum Receive implements Type {
    TO_ACCOUNT
    , BEFORE_BALANCE
    , FROM_ACCOUNT
    , TO_ID
    , AMOUNT
    ;
    public static Map<Enum<?>, Long> parsing(Long ... values) {
        return Type.parsing(values(), values);
    }
}
