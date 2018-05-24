package com.abuse.module;

import java.util.*;

public class Rules implements Rulable {
    private final Frequencible[] rules;
    private final Conjunction conjunction;

    public Rules(Frequencible[] rules, Conjunction conjunction) {
        this.rules = rules;
        this.conjunction = conjunction;
    }

    @Override
    public boolean match(Long event, Map<Enum<?>, Long> result) {
        Map<Frequencible, Long> frequencies = new HashMap<>();
        Map<Frequencible, Long> matched = new HashMap<>();
        for (Frequencible rule : rules) {
            if (rule.match(event, result)) {
                Long cnt = frequencies.get(rule);
                long minus = cnt == null? rule.getFrequency() - 1l : cnt - 1;
                if (minus == 0) {
                    matched.put(rule, event);
                } else {
                    frequencies.put(rule, minus);
                }
            }
        }
        return conjunction.matchBy(matched, rules);
    }
}
