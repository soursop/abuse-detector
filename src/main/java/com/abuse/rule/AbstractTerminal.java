package com.abuse.rule;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

abstract class AbstractTerminal implements Terminal {
    private final long duration;
    private final int frequency;
    private final Handler handler;
    private enum Handler {
        NONE {
            @Override
            boolean valid(long duration, LocalDateTime bigger, LocalDateTime smaller) {
                return true;
            }
        }
        , PROC {
            @Override
            boolean valid(long duration, LocalDateTime bigger, LocalDateTime smaller) {
                long diff = smaller.until(bigger, ChronoUnit.MILLIS);
                return diff > -1 && diff <= duration;
            }
        }
        ;
        abstract boolean valid(long duration, LocalDateTime bigger, LocalDateTime smaller);
    }

    protected AbstractTerminal(long duration, int frequency, boolean isLazy) {
        this.duration = duration;
        this.frequency = frequency;
        this.handler = isLazy? Handler.NONE : Handler.PROC;
    }

    @Override
    public int frequency() {
        return frequency;
    }

    @Override
    public boolean matchBy(Map<Terminal, LinkedList<LocalDateTime>> matched) {
        return matched.containsKey(this);
    }

    @Override
    public List<Terminal> terminals() {
        return Arrays.asList(this);
    }

    @Override
    public boolean valid(LocalDateTime now, LocalDateTime event) {
        return handler.valid(duration, now, event);
    }

    @Override
    public long duration() {
        return duration;
    }
}
