package com.abuse.module;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @author soursop
 * @created 2018. 5. 24.
 */
public class Metric extends AbstractRulable implements Reducible {
    private final Enum<?> key;
    private final long sign;

    public Metric(Enum<?> key, long duration) {
        this(key, 1, duration);
    }

    public Metric(Enum<?> key, long sign, long duration) {
        super(duration);
        this.key = key;
        this.sign = sign;
    }

    @Override
    public boolean match(LocalDateTime now, LocalDateTime event, Map<Enum<?>, Long> result) {
        Long value = result.get(key);
        return value != null && valid(now, event);
    }

    @Override
    public long aggregate(Map<Enum<?>, Long> result) {
        return sign * result.get(key);
    }

    @Override
    public boolean matchBy(Map<Rulable, Queue<LocalDateTime>> matched) {
        return matched.containsKey(this);
    }

    @Override
    public List<Terminal> terminals() {
        return Collections.emptyList();
    }
}
