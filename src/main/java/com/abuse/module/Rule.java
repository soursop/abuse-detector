package com.abuse.module;

import java.util.Date;
import java.util.Map;

/**
 * @author soursop
 * @created 2018. 5. 24.
 */
public class Rule implements Frequencible {
    private final Enum<?> key;
    private final Evaluation evaluation;
    private final long source;
    private final long sign;
    private final long duration;
    private final int frequency;

    public Rule(Enum<?> key, Evaluation evaluation, long source, long duration) {
        this(key, evaluation, 1, source, duration, 1);
    }

    public Rule(Enum<?> key, Evaluation evaluation, long sign, long source, long duration, int frequency) {
        this.key = key;
        this.evaluation = evaluation;
        this.sign = sign;
        this.source = source;
        this.duration = duration;
        this.frequency = frequency;
    }

    @Override
    public boolean match(Long event, Map<Enum<?>, Long> result) {
        Long value = result.get(key);
        return value != null && valid(new Date().getTime(), event) && evaluation.match(source, value);
    }

    public long aggregate(Map<Enum<?>, Long> result) {
        return sign * result.get(key);
    }

    @Override
    public boolean valid(Long now, Long event) {
        return now - event.longValue() <= duration;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public int getFrequency() {
        return frequency;
    }
}
