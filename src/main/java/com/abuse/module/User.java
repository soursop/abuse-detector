package com.abuse.module;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class User {
    private final long id;
    private final Map<Class<Enum<?>>, Queue<Map<Enum<?>, Long>>> logs = new ConcurrentHashMap<>();

    public User(long id) {
        this.id = id;
    }

    public void add(Class<Enum<?>> source, Map<Enum<?>, Long> log) {
        Queue<Map<Enum<?>, Long>> queue = logs.get(source);
        if (queue == null) {
            queue = new ConcurrentLinkedQueue<>();
            queue.add(log);
            logs.put(source, queue);
        } else {
            queue.add(log);
        }
    }

    public boolean match(Group group) {
        Clause[] clauses = group.getClauses();
        Map<Clause, Long> count = new HashMap<>();
        for (Clause clause : clauses) {
            Queue<Map<Enum<?>, Long>> queue = logs.get(clause.getClass());
            if (queue == null) {
                continue;
            }
            for (Map<Enum<?>, Long> groups : queue) {
                for (Map.Entry<Enum<?>, Long> entry : groups.entrySet()) {
//                    if (clause.match())
//                    Long cnt = count.get(clause);
//                    if (cnt == null) {
//                        count.put(clause, 1l);
//                    } else {
//                        count.put(clause, cnt + 1l);
//                    }
                }
            }
        }
        return true;
    }

    public static <T extends Enum<T>> T key(Class<T> source, String name) {
        return Enum.valueOf(source, name);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", logs=" + logs +
                '}';
    }
}
