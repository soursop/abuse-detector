package com.abuse.event;

import org.joda.time.DateTime;

/**
 * @author soursop
 * @created 2018. 5. 24.
 */
public class Window {
    private final long from;
    private final Enum<?> key;

    public Window(Enum<?> key, long from) {
        this.from = from;
        this.key = key;
    }

}
