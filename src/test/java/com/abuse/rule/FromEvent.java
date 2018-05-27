package com.abuse.rule;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;



public class FromEvent {
    private final LocalDateTime from;
    private final LocalDateTime after;
    private final LocalDateTime within;

    public FromEvent(LocalDateTime from, long timeout) {
        this.from = from;
        within = from.plus(timeout, ChronoUnit.MILLIS);
        after = from.plus(timeout + 1, ChronoUnit.MILLIS);
    }

    public LocalDateTime from() {
        return from;
    }

    public LocalDateTime after() {
        return after;
    }

    public LocalDateTime within() {
        return within;
    }

    public LocalDateTime within(long ms) {
        return ms < 0? within.minus(Math.abs(ms), ChronoUnit.MILLIS) : within.plus(ms, ChronoUnit.MILLIS);
    }
}
