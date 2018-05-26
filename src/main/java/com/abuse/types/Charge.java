package com.abuse.types;

import java.util.Map;

public enum Charge implements Type {
    ACCOUNT
    , AMOUNT
    , BANK_ACCOUNT
    ;
    public static Map<Enum<?>, Long> parsing(Long ... values) {
        return Type.parsing(values(), values);
    }
}
