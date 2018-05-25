package com.abuse.module.types;

import com.abuse.module.Assert;

import java.util.HashMap;
import java.util.Map;

public interface Type {

    static <T extends Enum> Map<Enum<?>, Long> parsing(T[] keys, Long ... values) {
        Assert.assertSize(keys.length, values.length);

        Map<Enum<?>, Long> map = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

}
