package com.abuse.module;

import org.junit.Test;

import java.util.Date;

public class SourceTest {

    @Test
    public void testLog() {
        Date date = new Date();
        User user = new User(1l);
//        user.add(Account.class, Account.ACCOUNT.TIME.name(), date.getTime());
//        user.add(Charge.class, Charge.TIME.name(), date.getTime());
        System.out.println(user);
    }
}
