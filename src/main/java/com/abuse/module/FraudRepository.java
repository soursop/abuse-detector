package com.abuse.module;

import com.abuse.rule.Rulable;
import com.abuse.types.Type;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public interface FraudRepository {
    void add(Long id, Long account, LocalDateTime event, Map<Enum<? extends Type>, Long> log);
    List<String> findById(Long id);
    Map<Rulable, Queue<LocalDateTime>> status(Long id, Long account);
}
