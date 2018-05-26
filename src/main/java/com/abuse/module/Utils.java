package com.abuse.module;

import com.abuse.module.types.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * @author soursop
 * @created 2018. 5. 25.
 */
public class Utils {

    public static Map<Enum<? extends Type>, Long> parsing(Object ... values) {
        int size = Assert.assertPairs(values.length);
        Map<Enum<? extends Type>, Long> log = new HashMap<>();
        for (int i = 0; i < size; i++) {
            int key = 2 * i;
            int value = key + 1;
            log.put((Enum<? extends Type>) values[key], (Long) values[value]);
        }
        return log;
    }

    public static <T> T[] asArray(T ... values) {
        return values;
    }

}
