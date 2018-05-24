package com.abuse.module;

import java.util.HashMap;
import java.util.Map;

public class Sum implements Frequencible {
    private final Rule[] rules;
    private final Evaluation evaluation;
    private final long duration;
    private final long source;
    private final int frequency;

    public Sum(Rule[] rules, Evaluation evaluation, long source) {
        this.rules = rules;
        this.evaluation = evaluation;
        long duration = 0;
        int frequency = 0;
        for (Frequencible rule : rules) {
            duration = Math.max(duration, rule.getDuration());
            frequency = Math.max(frequency, rule.getFrequency());
        }
        this.duration = duration;
        this.frequency = frequency;
        this.source = source;
    }

    @Override
    public boolean match(Long event, Map<Enum<?>, Long> result) {
        Map<Rule, Long> frequencies = new HashMap<>();
        Map<Frequencible, Long> matched = new HashMap<>();
        long sum = 0;
        for (Rule rule : rules) {
            if (rule.match(event, result)) {
                Long cnt = frequencies.get(rule);
                long minus = cnt == null? rule.getFrequency() - 1l : cnt - 1;
                sum += rule.aggregate(result);
                if (minus == 0) {
                    matched.put(rule, event);
                } else {
                    frequencies.put(rule, minus);
                }
            }
        }
        return evaluation.match(source, sum);
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
