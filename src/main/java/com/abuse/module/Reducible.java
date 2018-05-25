package com.abuse.module;


import java.util.Map;

/**
 * @author soursop
 * @created 2018. 5. 25.
 */
public interface Reducible extends Rulable {
    boolean match(Map<Enum<?>, Long> result);
    long aggregate(Map<Enum<?>, Long> result);
}
