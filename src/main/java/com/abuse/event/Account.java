package com.abuse.event;

/**
 * @author soursop
 * @created 2018. 5. 24.
 */
public class Account implements Accountable {
    private final long time;
    private final long id;
    private final int account;

    public Account(long time, long id, int account) {
        this.time = time;
        this.id = id;
        this.account = account;
    }

    @Override
    public long time() {
        return 0;
    }

    @Override
    public long invoker() {
        return 0;
    }

    @Override
    public int account() {
        return 0;
    }

}
