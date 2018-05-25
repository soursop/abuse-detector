package com.abuse.module;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @author soursop
 * @created 2018. 5. 24.
 */
public class Rule extends AbstractRulable implements Terminal {
    private final Enum<?> key;
    private final Evaluation evaluation;
    private final long source;
    private final int frequency;

    public Rule(Enum<?> key, Evaluation evaluation, long source, long duration) {
        this(key, evaluation, source, duration, 1);
    }

    public Rule(Enum<?> key, Evaluation evaluation, long source, long duration, int frequency) {
        super(duration);
        this.key = key;
        this.evaluation = evaluation;
        this.source = source;
        this.frequency = frequency;
    }

    @Override
    public boolean match(Map<Enum<?>, Long> result) {
        Long value = result.get(key);
        return value != null && evaluation.match(source, value);
    }

    @Override
    public int getFrequency() {
        return frequency;
    }

    @Override
    public boolean matchBy(Map<Rulable, Queue<LocalDateTime>> matched) {
        return matched.containsKey(this);
    }

    @Override
    public List<Terminal> terminals() {
        return Arrays.asList(this);
    }

}
