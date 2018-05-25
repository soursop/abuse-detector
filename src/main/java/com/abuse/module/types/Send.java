package com.abuse.module.types;

import java.util.Map;

public enum Send implements Type {
    FROM_ACCOUNT
    , FROM_BALANCE
    , TO_ACCOUNT
    , TO_ID
    , AMOUNT
    ;
    public static Map<Enum<?>, Long> parsing(Long ... values) {
        return Type.parsing(values(), values);
    }
}
