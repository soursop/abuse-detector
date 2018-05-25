package com.abuse.module;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @author soursop
 * @created 2018. 5. 25.
 */
abstract class AbstractRulable implements Rulable {
    private final long duration;

    protected AbstractRulable(long duration) {
        this.duration = duration;
    }

    @Override
    public boolean valid(LocalDateTime now, LocalDateTime event) {
        return event.until(now, ChronoUnit.MILLIS) <= duration;
    }

    @Override
    public long duration() {
        return duration;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    public static long duration(Rulable[] rules) {
        long duration = 0;
        for (Rulable rule : rules) {
            duration = Assert.assertEqualsExceptZero(duration, rule.duration());
        }
        return duration;
    }

    public static List<Terminal> terminals(Rulable[] rules) {
        List<Terminal> terminals = new ArrayList<>();
        for (Rulable rule : rules) {
            terminals.addAll(rule.terminals());
        }
        return terminals;
    }
}
