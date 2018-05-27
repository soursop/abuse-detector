package com.abuse.rule;

import com.abuse.types.Type;

import java.util.Arrays;
import java.util.Map;

public class Sum extends AbstractTerminal {
    private final Reducible[] rules;
    private final Evaluation evaluation;
    private final long source;

    private Sum(Reducible[] rules, Evaluation evaluation, long source, long duration, int frequency, boolean isLazy) {
        super(duration, frequency, isLazy);
        this.rules = rules;
        this.evaluation = evaluation;
        this.source = source;
    }

    public static Sum of(Reducible[] rules, Evaluation evaluation, long source, long duration) {
        return of(rules, evaluation, source, duration, 1);
    }

    public static Sum of(Reducible[] rules, Evaluation evaluation, long source, long duration, int frequency) {
        return new Sum(rules, evaluation, source, duration, frequency, false);
    }

    @Override
    public boolean match(Map<Enum<? extends Type>, Long> result) {
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
    public Sum toLazy() {
        return new Sum(rules, evaluation, source, duration(), frequency(), true);
    }

    @Override
    public String toString() {
        return "Sum(" + Arrays.toString(rules) +
                evaluation + ":" + source + "|" + frequency()  + "|" + duration() +
                + ')';
    }
}
