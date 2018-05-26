package com.abuse.module;

import com.abuse.rule.*;
import com.abuse.types.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public synchronized List<String> findAny(final LocalDateTime now) {
        List<String> result = new ArrayList<>();
        for (Aggregator aggregator : accounts.values()) {
            result.addAll(aggregator.match(now));
        }
        return result;
    }

}