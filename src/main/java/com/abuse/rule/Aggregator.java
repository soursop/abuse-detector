package com.abuse.rule;

import com.abuse.types.Type;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author soursop
 * @created 2018. 5. 25.
 */
public class Aggregator {
    private final Rules[] rules;
    private final Terminal[] terminals;
    private final Map<Rulable, Queue<LocalDateTime>> frequencies = new HashMap<>();

    private Aggregator(Rules[] rules, Terminal[] terminals) {
        this.rules = rules;
        this.terminals = terminals;
    }

    public synchronized void aggregate(LocalDateTime now, LocalDateTime event, Map<Enum<? extends Type>, Long> result) {
        for (Terminal rule : terminals) {
            if (rule.match(result)) {
                Queue<LocalDateTime> queue = frequencies.get(rule);
                if (queue == null) {
                    queue = new PriorityQueue<>();
                } else {
                    queue = refresh(now, rule, queue);
                }
                queue.add(event);
                frequencies.put(rule, queue);
            }
        }
    }

    public synchronized List<String> match(LocalDateTime now) {
        List<String> list = new ArrayList<>();
        Map<Terminal, Queue<LocalDateTime>> matched = new HashMap<>();

        for (Terminal terminal : terminals) {
            Queue<LocalDateTime> queue = refresh(now, terminal, frequencies.get(terminal));
            if (queue == null || terminal.getFrequency() > queue.size()) {
                continue;
            }
            Queue<LocalDateTime> agg = new PriorityQueue<>(terminal.comparator());
            agg.addAll(queue);
            matched.put(terminal, agg);
        }
        for (Rules rule : rules) {
            if (rule.matchBy(matched)) {
                list.add(rule.name());
            }
        }
        return list;
    }

    private Queue<LocalDateTime> refresh(LocalDateTime now, Terminal rule, Queue<LocalDateTime> queue) {
        if (queue == null) {
            return null;
        }
        Iterator<LocalDateTime> iterator = queue.iterator();
        while (iterator.hasNext() && !rule.valid(now, iterator.next())) {
            iterator.remove();
        }
        return queue;
    }

    public Map<Rulable, Queue<LocalDateTime>> getFrequencies() {
        return frequencies;
    }

    public static Aggregator of(Rules ... rules) {
        List<Terminal> terminals = Cons.terminals(rules);
        return new Aggregator(rules, terminals.toArray(new Terminal[terminals.size()]));
    }

}
