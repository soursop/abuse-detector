package com.abuse.module;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.function.Predicate;

/**
 * @author soursop
 * @created 2018. 5. 24.
 */
public enum Conjunction {
    AND {
        @Override
        public boolean matchBy(Map<Rulable, Queue<LocalDateTime>> result, Rulable[] rules) {
            for (Rulable rule : rules) {
                if (result.containsKey(rule) == false) {
                    return false;
                }
            }
            return true;
        }
    }, AFTER {
        @Override
        public boolean matchBy(Map<Rulable, Queue<LocalDateTime>> result, Rulable[] rules) {
            for (int i = 0; i < rules.length; i++) {
                Queue<LocalDateTime> times = result.get(rules[i]);
                if (times == null) {
                    return false;
                }
                if (i > 0 && isOrdered(times, result.get(rules[i - 1]))) {
                    return false;
                }
            }
            return true;
        }
    }
    ;

    private static boolean isOrdered(Queue<LocalDateTime> before, Queue<LocalDateTime> after) {
        final LocalDateTime last = before.peek();
        Iterator<LocalDateTime> iterator = after.iterator();
        while (iterator.hasNext() && last.isAfter(iterator.next())) {
            iterator.remove();
        }
        return after.size() > 0;
    }

    public abstract boolean matchBy(Map<Rulable, Queue<LocalDateTime>> result, Rulable[] rules);
}
