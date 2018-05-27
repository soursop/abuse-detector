package com.abuse.rule;

import com.abuse.types.Type;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.Queue;

/**
 * @author soursop
 * @created 2018. 5. 25.
 */
public interface Terminal extends Rulable {
    boolean match(Map<Enum<? extends Type>, Long> result);
    boolean matchBy(long duration, Rulable before, Map<Terminal, Queue<LocalDateTime>> matched);
    boolean valid(LocalDateTime now, LocalDateTime event);
    long duration();
    int frequency();
    Terminal toLazy();
    Comparator<LocalDateTime> comparator();
}
