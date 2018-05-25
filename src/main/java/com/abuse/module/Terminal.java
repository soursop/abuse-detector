package com.abuse.module;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author soursop
 * @created 2018. 5. 25.
 */
public interface Terminal extends Rulable {
    boolean match(Map<Enum<?>, Long> result);
    int getFrequency();
}
