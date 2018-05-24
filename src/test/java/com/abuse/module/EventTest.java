package com.abuse.module;

import com.abuse.module.types.Account;
import org.junit.Test;

import java.util.Date;
import java.util.PriorityQueue;

public class EventTest {

    @Test
    public void testLog() {
        Date date = new Date();
        Event event = new Event(date.getTime(), Account.class, 1111l);
        Event event2 = new Event(date.getTime() + 1, Account.class, 22222l);
        Event event3 = new Event(date.getTime() + 1, Account.class, 3333l);
        PriorityQueue<Event> queue = new PriorityQueue<>();
        queue.add(event);
        queue.add(event2);
        queue.add(event3);
        System.out.println(queue);
    }
}
