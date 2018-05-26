package com.abuse.module;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

abstract class AbstractTerminal implements Terminal {
    private final long duration;
    private final int frequency;
    private final Handler pre;
    private final Handler post;
    private final Comparator<LocalDateTime> comparator;
    private static Comparator<LocalDateTime> ASC = new Comparator<LocalDateTime>() {
        @Override
        public int compare(LocalDateTime mine, LocalDateTime other) {
            int cmp = mine.toLocalDate().compareTo(other.toLocalDate());
            if (cmp == 0) {
                cmp = mine.toLocalTime().compareTo(other.toLocalTime());
                if (cmp == 0) {
                    cmp = mine.getChronology().compareTo(other.getChronology());
                }
            }
            return cmp;
        }
    }
    ;
    private static Comparator<LocalDateTime> DESC = new Comparator<LocalDateTime>() {
        @Override
        public int compare(LocalDateTime mine, LocalDateTime other) {
            return ASC.compare(other, mine);
        }
    }
    ;

    private enum Handler {
        NONE {
            @Override
            boolean valid(long duration, LocalDateTime now, LocalDateTime event) {
                return true;
            }
        }
        , PROC {
            @Override
            boolean valid(long duration, LocalDateTime now, LocalDateTime event) {
                long diff = event.until(now, ChronoUnit.MILLIS);
                return diff > -1 && diff <= duration;
            }
        }
        ;
        abstract boolean valid(long duration, LocalDateTime now, LocalDateTime event);
    }

    protected AbstractTerminal(long duration, int frequency, boolean isLazy) {
        this.duration = duration;
        this.frequency = frequency;
        this.pre = isLazy? Handler.NONE : Handler.PROC;
        this.post = isLazy? Handler.PROC : Handler.NONE;
        this.comparator = isLazy? DESC : ASC;
    }

    @Override
    public int getFrequency() {
        return frequency;
    }

    @Override
    public boolean matchBy(Map<Terminal, Queue<LocalDateTime>> matched) {
        return matched.containsKey(this);
    }

    @Override
    public List<Terminal> terminals() {
        return Arrays.asList(this);
    }

    @Override
    public boolean valid(LocalDateTime now, LocalDateTime event) {
        return pre.valid(duration, now, event);
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public Comparator<LocalDateTime> comparator() {
        return comparator;
    }

    @Override
    public boolean matchBy(long duration, Rulable before, Map<Terminal, Queue<LocalDateTime>> matched) {
        Queue<LocalDateTime> from = matched.get(before);
        if (from == null) {
            return false;
        }
        Queue<LocalDateTime> to = matched.get(this);
        if (to == null) {
            return false;
        }
        return isWithin(duration, from, to);
    }

    private boolean isWithin(long duration, Queue<LocalDateTime> before, Queue<LocalDateTime> after) {
        final LocalDateTime last = before.peek();
        Iterator<LocalDateTime> iterator = after.iterator();
        while (iterator.hasNext() && !post.valid(duration, iterator.next(), last)) {
            iterator.remove();
        }
        return after.size() >= getFrequency();
    }
}
