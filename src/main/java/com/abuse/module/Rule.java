package com.abuse.module;

/**
 * @author soursop
 * @created 2018. 5. 24.
 */
public class Rule {
    private final Enum<?> key;
    private final Evaluation evaluation;
    private final long duration;
    private final int frequency;

    public Rule(Enum<?> key, Evaluation evaluation, long duration, int frequency) {
        this.key = key;
        this.evaluation = evaluation;
        this.duration = duration;
        this.frequency = frequency;
    }

    public boolean valid(long now, Long event) {
        return now - event.longValue() <= duration;
    }

}
