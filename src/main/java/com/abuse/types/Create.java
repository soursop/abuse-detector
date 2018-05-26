package com.abuse.types;


import java.util.Map;

/**
 * @author soursop
 * @created 2018. 5. 24.
 */
public enum Create implements Type {
    ACCOUNT
    ;

    public static Map<Enum<?>, Long> parsing(Long ... values) {
        return Type.parsing(values(), values);
    }
}
