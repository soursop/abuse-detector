package com.abuse.module;

import com.abuse.rule.*;
import com.abuse.types.*;

import java.time.LocalDateTime;
import java.util.*;

public class Register {
    private final Rules[] rules;
    private final Map<Long, Aggregator> accounts = new HashMap<>();

    public Register(Rules[] rules) {
        this.rules = rules;
    }

    public Aggregator findByAccount(Long account) {
        return accounts.get(account);
    }

    public synchronized void add(Long account, LocalDateTime now, LocalDateTime event, Map<Enum<? extends Type>, Long> log) {
        Aggregator aggregator = accounts.get(account);
        if (aggregator == null) {
            aggregator = Aggregator.of(rules);
            accounts.put(account, aggregator);
        }
        aggregator.aggregate(now, event, log);
    }

    public synchronized Set<String> findAny(final LocalDateTime now) {
        Set<String> result = new HashSet<>();
        for (Aggregator aggregator : accounts.values()) {
            result.addAll(aggregator.match(now));
        }
        return result;
    }

}
