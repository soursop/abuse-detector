package com.abuse.module;

public class Clause {
    private final Enum<?> key;
    private final long source;
    private final Evaluation evaluation;
    private final int frequency;

    public Clause(Enum<?> key, long source, Evaluation evaluation, int frequency) {
        this.key = key;
        this.source = source;
        this.evaluation = evaluation;
        this.frequency = frequency;
    }

    public Enum<?> getKey() {
        return key;
    }

    public int getFrequency() {
        return frequency;
    }

    public boolean match(Long target) {
        return evaluation.match(source, target);
    }
}
