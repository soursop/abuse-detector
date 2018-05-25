package com.abuse.module;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class Sum extends AbstractRulable implements Terminal {
    private final Reducible[] rules;
    private final Evaluation evaluation;
    private final long source;
    private final int frequency;

    private Sum(Reducible[] rules, Evaluation evaluation, long source, long duration, int frequency) {
        super(duration);
        this.rules = rules;
        this.evaluation = evaluation;
        this.source = source;
        this.frequency = frequency;
    }

    public static Sum of(Reducible[] rules, Evaluation evaluation, long source) {
        return of(rules, evaluation, source, 1);
    }

    public static Sum of(Reducible[] rules, Evaluation evaluation, long source, int frequency) {
        return new Sum(rules, evaluation, source, duration(rules), frequency);
    }

    @Override
    public boolean match(Map<Enum<?>, Long> result) {
        long sum = 0;
        for (Reducible rule : rules) {
            if (rule.match(result)) {
                sum += rule.aggregate(result);
            } else {
                return false;
            }
        }
        return evaluation.match(source, sum);
    }

    @Override
    public boolean matchBy(Map<Rulable, Queue<LocalDateTime>> matched) {
        return matched.containsKey(this);
    }

    @Override
    public List<Terminal> terminals() {
        return Arrays.asList(this);
    }

    @Override
    public int getFrequency() {
        return frequency;
    }
}
