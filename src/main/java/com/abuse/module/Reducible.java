package com.abuse.module;


import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author soursop
 * @created 2018. 5. 25.
 */
public interface Reducible extends Rulable {
    boolean match(LocalDateTime now, LocalDateTime event, Map<Enum<?>, Long> result);
    long aggregate(Map<Enum<?>, Long> result);
}
