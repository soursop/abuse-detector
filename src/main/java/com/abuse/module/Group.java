package com.abuse.module;

import java.util.HashMap;
import java.util.Map;

public class Group {
    private final Clause[] clauses;

    public Group(Clause[] clauses) {
        this.clauses = clauses;
    }

    public Clause[] getClauses() {
        return clauses;
    }

    public boolean match(Map<Clause, Long> count) {
        for (Clause clause : clauses) {
            Long cnt = count.get(clause);
            if (cnt == null || cnt.longValue() < clause.getFrequency()) {
                return false;
            }
        }
        return true;
    }
}
