package com.abuse.module;

import com.abuse.rule.Rulable;
import com.abuse.types.Type;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public interface FraudRepository {
    void add(Long id, Long account, LocalDateTime event, Map<Enum<? extends Type>, Long> log);
    Set<String> findById(Long id);
    Map<Rulable, Queue<LocalDateTime>> status(Long id, Long account);
}
