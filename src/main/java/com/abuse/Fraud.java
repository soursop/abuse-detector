package com.abuse;

public class Fraud {
    private final Long userId;
    private final boolean isFraud;
    private final String rule;

    public Fraud(Long userId, boolean isFraud, String rule) {
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

    public String getRule() {
        return rule;
    }
}
