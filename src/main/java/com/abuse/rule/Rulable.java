package com.abuse.rule;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public interface Rulable {
    boolean matchBy(Map<Terminal, Queue<LocalDateTime>> matched);
    List<Terminal> terminals();
}
