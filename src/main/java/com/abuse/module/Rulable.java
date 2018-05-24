package com.abuse.module;


import java.util.Map;

public interface Rulable {
    boolean match(Long event, Map<Enum<?>, Long> result);
}
