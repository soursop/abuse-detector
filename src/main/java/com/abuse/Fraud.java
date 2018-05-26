package com.abuse;

import java.util.List;

public class Fraud {
    private final Long userId;
    private final boolean isFraud;
    private final List<String> rule;

    public Fraud(Long userId, boolean isFraud, List<String> rule) {
        this.userId = userId;
        this.isFraud = isFraud;
        this.rule = rule;
    }

    public Long getUserId() {
        return userId;
    }

    public boolean isFraud() {
        return isFraud;
    }

    public List<String> getRule() {
        return rule;
    }
}
