package com.abuse.rule;

import com.abuse.types.Type;

import java.util.Map;

/**
 * @author soursop
 * @created 2018. 5. 24.
 */
public class Rule extends AbstractTerminal {
    private final Enum<?> key;
    private final Evaluation evaluation;
    private final long source;

    public Rule(Enum<?> key, Evaluation evaluation, long source, long duration) {
        this(key, evaluation, source, duration, 1, false);
    }

    public Rule(Enum<?> key, Evaluation evaluation, long source, long duration, int frequency) {
        this(key, evaluation, source, duration, frequency, false);
    }

    private Rule(Enum<?> key, Evaluation evaluation, long source, long duration, int frequency, boolean isLazy) {
        super(duration, frequency, isLazy);
        this.key = key;
        this.evaluation = evaluation;
        this.source = source;
    }

    @Override
    public boolean match(Map<Enum<? extends Type>, Long> result) {
        Long value = result.get(key);
        return value != null && evaluation.match(source, value);
    }

    @Override
    public Rule toLazy() {
        return new Rule(key, evaluation, source, duration(), frequency(), true);
    }

    @Override
    public String toString() {
        return "Rule(" + key + ":" +
            evaluation + ":" + source + "|" + frequency()  + "|" + duration() +
                ')';
    }
}
