package com.abuse.module;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public interface Rulable {
    boolean matchBy(Map<Rulable, Queue<LocalDateTime>> matched);
    boolean valid(LocalDateTime now, LocalDateTime event);
    long getDuration();
    long duration();
    List<Terminal> terminals();
}
