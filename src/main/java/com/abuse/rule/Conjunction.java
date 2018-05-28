package com.abuse.rule;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.Map;

public interface Conjunction<T extends Rulable> {

    boolean matchBy(Map<Terminal, LinkedList<LocalDateTime>> result, T[] rules);
    T[] convert(T[] input);

    Conjunction<Rulable> AND = new Conjunction<Rulable>() {
        @Override
        public boolean matchBy(Map<Terminal, LinkedList<LocalDateTime>> result, Rulable[] rules) {
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
        public boolean matchBy(Map<Terminal, LinkedList<LocalDateTime>> result, Terminal[] rules) {
            LinkedList<LocalDateTime>[] queues = new LinkedList[rules.length];
            for(int i = 0; i < rules.length; i++) {
                LinkedList<LocalDateTime> queue = result.get(rules[i]);
                if (queue == null) {
                    return false;
                }
                queues[i] = queue;
            }
            return search(rules, queues);
        }

        @Override
        public Terminal[] convert(Terminal[] rules) {
            Terminal[] convert = new Terminal[rules.length];
            for (int i = 0; i < rules.length; i++) {
                convert[i] = rules[i].toLazy();
            }
            return convert;
        }

        private boolean search(Terminal[] rules, LinkedList<LocalDateTime>... queues) {
            int idx = 0;
            while (!queues[0].isEmpty()) {
                if (idx + 1 >= queues.length) {
                    if (validate(0, rules, new LinkedList<>(), queues)) {
                        return true;
                    }
                    queues[0].remove();
                    idx = 0;
                } else {
                    LocalDateTime head = queues[idx].peek();
                    while (!queues[idx + 1].isEmpty() && head.isAfter(queues[idx + 1].peek())) {
                        queues[idx + 1].remove();
                    }
                    idx = idx + 1;
                }
            }
            return false;
        }

        private boolean validate(int idx, Terminal[] rules, LinkedList<LocalDateTime> result, LinkedList<LocalDateTime>... queues) {
            if (idx == queues.length) {
//                System.out.println(result);
                return true;
            }
            LinkedList<LocalDateTime> next = new LinkedList<>();
            for (LocalDateTime date : queues[idx]) {
                if (result.size() > 0
                        && (invalid(rules[idx].duration(), date, result.getLast())
                        || invalid(rules[0].duration(), date, result.peek()))) {
                    return false;
                }
                next.add(date);
                if (next.size() >= rules[idx].frequency()) {
                    LinkedList<LocalDateTime> queue = new LinkedList<>(result);
                    queue.add(next.getLast());
                    if (validate(idx + 1, rules, queue, queues)) {
                        return true;
                    } else {
                        next.removeFirst();
                    }
                }
            }
            return false;
        }

        private boolean invalid(long duration, LocalDateTime bigger, LocalDateTime smaller) {
            long diff = smaller.until(bigger, ChronoUnit.MILLIS);
            return diff < 0 || diff > duration;
        }
    }
    ;
}
