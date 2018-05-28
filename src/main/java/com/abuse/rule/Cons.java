package com.abuse.rule;

import java.time.LocalDateTime;
import java.util.*;

public class Cons implements Rules {
    private final String name;
    private final Rulable[] rules;
    private final Conjunction<Rulable> conjunction;

    public Cons(String name, Rulable[] rules, Conjunction<Rulable> conjunction) {
        this.name = name;
        this.rules = rules;
        this.conjunction = conjunction;
    }

    public static Cons of(String name, Rulable ... rules) {
        return of(name, rules, Conjunction.AND);
    }

    public static Cons of(String name, Rulable[] rules, Conjunction<Rulable> conjunction) {
        return new Cons(name, conjunction.convert(rules), conjunction);
    }

    @Override
    public List<Terminal> terminals() {
        return terminals(rules);
    }

    @Override
    public boolean matchBy(Map<Terminal, LinkedList<LocalDateTime>> matched) {
        return conjunction.matchBy(matched, rules);
    }

    @Override
    public String name() {
        return name;
    }

    public static List<Terminal> terminals(Rulable[] rules) {
        List<Terminal> terminals = new ArrayList<>();
        for (Rulable rule : rules) {
            terminals.addAll(rule.terminals());
        }
        return terminals;
    }
}
