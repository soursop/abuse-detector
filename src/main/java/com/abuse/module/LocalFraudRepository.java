package com.abuse.module;

import com.abuse.types.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        register.getOrDefault(id, new Register(repository.findAll()))
                .add(account, LocalDateTime.now(), event, log);
    }

    @Override
    public synchronized List<String> findById(Long id) {
        Register register = this.register.get(id);
        if (register == null) {
            return Collections.emptyList();
        }
        return register.findAny(LocalDateTime.now());
    }
}
