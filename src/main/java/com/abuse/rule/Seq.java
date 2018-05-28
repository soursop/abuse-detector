package com.abuse.rule;

import java.time.LocalDateTime;
import java.util.*;

public class Seq implements Rules {
    private final String name;
    private final Terminal[] rules;
    private final Conjunction<Terminal> conjunction;

    public Seq(String name, Terminal[] rules, Conjunction conjunction) {
        this.name = name;
        this.rules = rules;
        this.conjunction = conjunction;
    }

    public static Seq of(String name, Terminal ... rules) {
        return of(name, rules, Conjunction.AFTER);
    }

    public static Seq of(String name, Terminal[] rules, Conjunction<Terminal> conjunction) {
        return new Seq(name, conjunction.convert(rules), conjunction);
    }

    @Override
    public List<Terminal> terminals() {
        return Arrays.asList(rules);
    }

    @Override
    public boolean matchBy(Map<Terminal, LinkedList<LocalDateTime>> matched) {
        return conjunction.matchBy(matched, rules);
    }

    @Override
    public String name() {
        return name;
    }
}
