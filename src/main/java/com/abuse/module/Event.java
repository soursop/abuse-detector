package com.abuse.module;

import java.util.HashMap;
import java.util.Map;

/**
 * @author soursop
 * @created 2018. 5. 24.
 */
public class Event implements Comparable<Event> {
    private final long time;
    private final Class<? extends Enum<?>> source;
    private final Map<Enum<?>, Long> logs = new HashMap<>();

    public Event(long time, Class<? extends Enum<?>> source, long ... values) {
        this.time = time;
        this.source = source;
        Enum<?>[] keys = source.getEnumConstants();
        if (values.length != keys.length) {
            throw new IllegalArgumentException(String.format("value length %d != key length %d", values.length, keys.length));
        }
        for (int i = 0; i < keys.length; i++) {
            logs.put(keys[i], values[i]);
        }
    }

    public Map<Enum<?>, Long> getLogs() {
        return logs;
    }

    @Override
    public String toString() {
        return source.getSimpleName() + "{" +
                "time=" + time +
                ", " + logs +
                '}';
    }

    @Override
    public int compareTo(Event o) {
        return Long.compare(time, o.time);
    }
}
