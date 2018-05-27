package com.abuse.rule;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


public class UntilNow {
    private final LocalDateTime from;
    private final LocalDateTime before;
    private final LocalDateTime within;

    public UntilNow(LocalDateTime from, long timeout) {
        this.from = from;
        within = from.minus(timeout, ChronoUnit.MILLIS);
        before = from.minus(timeout + 1, ChronoUnit.MILLIS);
    }

    public LocalDateTime from() {
        return from;
    }

    public LocalDateTime before() {
        return before;
    }

    public LocalDateTime within() {
        return within;
    }
}
