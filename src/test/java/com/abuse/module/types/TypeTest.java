package com.abuse.module.types;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author soursop
 * @created 2018. 5. 25.
 */
public class TypeTest {

    @Test
    public void testAccount() {
        Map<Enum<?>, Long> log = Account.parsing(1111l);
        assertEquals(log.get(Account.ACCOUNT).longValue(), 1111l);
    }

    @Test
    public void testCharge() {
        long account = 1111l;
        long amount = 10000l;
        long bankAccount = 2222l;
        Map<Enum<?>, Long> log = Charge.parsing(account, amount, bankAccount);

        assertEquals(log.get(Charge.ACCOUNT).longValue(), account);
        assertEquals(log.get(Charge.AMOUNT).longValue(), amount);
        assertEquals(log.get(Charge.BANK_ACCOUNT).longValue(), bankAccount);
    }

    @Test
    public void testReceive() {
        long toAccount = 1111l;
        long beforeBalance = 10000l;
        long fromAccount = 2222l;
        long toId = 2l;
        long amount = 20000l;
        Map<Enum<?>, Long> log = Receive.parsing(toAccount, beforeBalance, fromAccount, toId, amount);

        assertEquals(log.get(Receive.TO_ACCOUNT).longValue(), toAccount);
        assertEquals(log.get(Receive.BEFORE_BALANCE).longValue(), beforeBalance);
        assertEquals(log.get(Receive.FROM_ACCOUNT).longValue(), fromAccount);
        assertEquals(log.get(Receive.TO_ID).longValue(), toId);
        assertEquals(log.get(Receive.AMOUNT).longValue(), amount);
    }

    @Test
    public void testSend() {
        long fromAccount = 2222l;
        long fromBalance = 10000l;
        long toAccount = 1111l;
        long toId = 2222l;
        long amount = 2222l;
        Map<Enum<?>, Long> log = Send.parsing(fromAccount, fromBalance, toAccount, toId, amount);

        assertEquals(log.get(Send.FROM_ACCOUNT).longValue(), fromAccount);
        assertEquals(log.get(Send.FROM_BALANCE).longValue(), fromBalance);
        assertEquals(log.get(Send.TO_ACCOUNT).longValue(), toAccount);
        assertEquals(log.get(Send.TO_ID).longValue(), toId);
        assertEquals(log.get(Send.AMOUNT).longValue(), amount);
    }


}
