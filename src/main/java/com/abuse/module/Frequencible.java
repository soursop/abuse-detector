package com.abuse.module;

public interface Frequencible extends Rulable {
    boolean valid(Long now, Long event);
    long getDuration();
    int getFrequency();
}
