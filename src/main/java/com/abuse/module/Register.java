package com.abuse.module;

import com.abuse.rule.Aggregator;
import com.abuse.rule.Rules;
import com.abuse.types.Type;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Register {
    private final Rules[] rules;
    private final Map<Long, Aggregator> accounts = new HashMap<>();

    public Register(Rules[] rules) {
        this.rules = rules;
    }

    public synchronized void add(Long account, LocalDateTime now, LocalDateTime event, Map<Enum<? extends Type>, Long> log) {
        accounts.getOrDefault(account, Aggregator.of(rules)).aggregate(now, event, log);
    }
}
