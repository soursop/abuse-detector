package com.abuse.module;


import com.abuse.module.types.Type;

import java.util.Map;

/**
 * @author soursop
 * @created 2018. 5. 25.
 */
public interface Reducible {
    boolean match(Map<Enum<? extends Type>, Long> result);
    long aggregate(Map<Enum<? extends Type>, Long> result);
}
