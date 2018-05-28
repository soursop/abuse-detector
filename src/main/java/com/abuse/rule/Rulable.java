package com.abuse.rule;


import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface Rulable {
    boolean matchBy(Map<Terminal, LinkedList<LocalDateTime>> matched);
    List<Terminal> terminals();
}
