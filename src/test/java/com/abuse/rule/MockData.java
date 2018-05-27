package com.abuse.rule;

import com.abuse.types.*;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.abuse.rule.Utils.asArray;
import static com.abuse.rule.Utils.parsing;

public class MockData {
    public static long ONE_HOUR = TimeUnit.HOURS.toMillis(1);
    public static long TWO_HOUR = TimeUnit.HOURS.toMillis(2);
    public static long SEVEN_DAYS = TimeUnit.DAYS.toMillis(7);

    public static Map<Enum<? extends Type>, Long> SEND_500 = parsing(Send.FROM_ACCOUNT, 1111l
            , Send.FROM_BALANCE, 200_000l
            , Send.TO_ACCOUNT, 2222l
            , Send.TO_ID, 2l
            , Send.AMOUNT, 199_500l);

    public static Map<Enum<? extends Type>, Long> ACCOUNT_1111 = parsing(
            Create.ACCOUNT, 1111l
    );

    public static Metric[] BALANCE = asArray(
            Metric.plus(Send.FROM_BALANCE)
            , Metric.minus(Send.AMOUNT)
    );

    public static Map<Enum<? extends Type>, Long> CHARGE_200_000 = parsing(
            Charge.ACCOUNT, 1111l
            , Charge.AMOUNT, 200_000l
            , Charge.BANK_ACCOUNT, 1010l
    );

    public static Map<Enum<? extends Type>, Long> RECEIVE_100_000 = parsing(
            Receive.TO_ACCOUNT, 1111l
            , Receive.BEFORE_BALANCE, 20000l
            , Receive.FROM_ACCOUNT, 2222l
            , Receive.FROM_ID, 1l
            , Receive.AMOUNT, 100_000l);

    public static Map<Enum<? extends Type>, Long> RECEIVE_50_000 = parsing(
            Receive.TO_ACCOUNT, 1111l
            , Receive.BEFORE_BALANCE, 20000l
            , Receive.FROM_ACCOUNT, 2222l
            , Receive.FROM_ID, 1l
            , Receive.AMOUNT, 50_000l);
}
