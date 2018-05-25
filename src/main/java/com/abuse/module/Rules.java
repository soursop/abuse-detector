package com.abuse.module;

import java.time.LocalDateTime;
import java.util.*;

public class Rules extends AbstractRulable {
    private final String name;
    private final Rulable[] rules;
    private final Conjunction conjunction;

    public Rules(String name, Rulable[] rules, Conjunction conjunction, long duration) {
        super(duration);
        this.name = name;
        this.rules = rules;
        this.conjunction = conjunction;
    }

    public static Rules of(String name, Rulable ... rules) {
        return of(name, rules, Conjunction.AND);
    }

    public static Rules of(String name, Rulable[] rules, Conjunction conjunction) {
        return new Rules(name, rules, conjunction, duration(rules));
    }

    @Override
    public long duration() {
        return duration(rules);
    }

    @Override
    public List<Terminal> terminals() {
        return terminals(rules);
    }

    @Override
    public boolean matchBy(Map<Rulable, Queue<LocalDateTime>> matched) {
        return conjunction.matchBy(matched, rules);
    }

    public String name() {
        return name;
    }
}
