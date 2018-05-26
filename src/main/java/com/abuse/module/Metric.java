package com.abuse.module;

import com.abuse.module.types.Type;

import java.util.Map;

/**
 * @author soursop
 * @created 2018. 5. 24.
 */
public class Metric implements Reducible {
    private final Enum<? extends Type> key;
    private final long sign;

    private Metric(Enum<? extends Type> key, long sign) {
        this.key = key;
        this.sign = sign;
    }

    public static Metric plus(Enum<? extends Type> key) {
        return new Metric(key, 1);
    }

    public static Metric minus(Enum<? extends Type> key) {
        return new Metric(key, -1);
    }

    @Override
    public boolean match(Map<Enum<? extends Type>, Long> result) {
        return result.containsKey(key);
    }

    @Override
    public long aggregate(Map<Enum<? extends Type>, Long> result) {
        return sign * result.get(key);
    }
}
