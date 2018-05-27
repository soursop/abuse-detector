package com.abuse.rule;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Queue;

public interface Conjunction<T extends Rulable> {

    boolean matchBy(Map<Terminal, Queue<LocalDateTime>> result, T[] rules);
    T[] convert(T[] input);

    Conjunction<Rulable> AND = new Conjunction<Rulable>() {
        @Override
        public boolean matchBy(Map<Terminal, Queue<LocalDateTime>> result, Rulable[] rules) {
            for (Rulable rule : rules) {
                if (rule.matchBy(result) == false) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public Rulable[] convert(Rulable[] input) {
            return input;
        }
    }
    ;
    Conjunction<Terminal> AFTER = new Conjunction<Terminal>() {
        @Override
        public boolean matchBy(Map<Terminal, Queue<LocalDateTime>> result, Terminal[] rules) {
            if (rules.length < 1 || !rules[0].matchBy(result)) {
                return false;
            }
            for (int i = 1; i < rules.length; i++) {
                if (!rules[i].matchBy(rules[i].duration(), rules[i - 1], result)) {
                    return false;
                }
                if (i > 1 && !rules[i].matchBy(rules[0].duration(), rules[0], result)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public Terminal[] convert(Terminal[] rules) {
            Terminal[] convert = new Terminal[rules.length];
            for (int i = 0; i < rules.length; i++) {
                convert[i] = rules[i].toLazy();
            }
            return convert;
        }
    }
    ;
}
