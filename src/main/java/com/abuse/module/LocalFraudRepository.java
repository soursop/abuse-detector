package com.abuse.module;

import com.abuse.rule.Aggregator;
import com.abuse.rule.Rulable;
import com.abuse.types.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class LocalFraudRepository implements FraudRepository {
    private final Map<Long, Register> register = new HashMap<>();
    private final RulesRepository repository;

    @Autowired
    public LocalFraudRepository(RulesRepository repository) {
        this.repository = repository;
    }

    @Override
    public synchronized void add(Long id, Long account, LocalDateTime event, Map<Enum<? extends Type>, Long> log) {
        Register register = this.register.get(id);
        if (register == null) {
            register = new Register(repository.findAll());
            this.register.put(id, register);
        }
        register.add(account, LocalDateTime.now(), event, log);
    }

    @Override
    public synchronized Set<String> findById(Long id) {
        Register register = this.register.get(id);
        if (register == null) {
            return Collections.emptySet();
        }
        return register.findAny(LocalDateTime.now());
    }

    @Override
    public Map<Rulable, Queue<LocalDateTime>> status(Long id, Long account) {
        Register register = this.register.get(id);
        if (register == null) {
            return Collections.emptyMap();
        }
        Aggregator aggregator = register.findByAccount(account);
        if (aggregator == null) {
            return Collections.emptyMap();
        }
        return aggregator.getFrequencies();
    }
}
