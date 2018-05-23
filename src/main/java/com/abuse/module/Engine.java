package com.abuse.module;

import java.util.Map;

public interface Engine {
    void reload();
    boolean valid(Enum<?> key, Long value);
}
