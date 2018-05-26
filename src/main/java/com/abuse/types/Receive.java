package com.abuse.types;

import java.util.Map;

public enum Receive implements Type {
    TO_ACCOUNT
    , BEFORE_BALANCE
    , FROM_ACCOUNT
    , FROM_ID
    , AMOUNT
    ;
    public static Map<Enum<?>, Long> parsing(Long ... values) {
        return Type.parsing(values(), values);
    }
}
