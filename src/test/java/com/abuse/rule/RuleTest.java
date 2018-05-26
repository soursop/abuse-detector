package com.abuse.rule;

import com.abuse.types.*;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.abuse.rule.Utils.asArray;
import static com.abuse.rule.Utils.parsing;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author soursop
 * @created 2018. 5. 25.
 */
public class RuleTest {
    private long ONE_HOUR = TimeUnit.HOURS.toMillis(1);
    private long TWO_HOUR = TimeUnit.HOURS.toMillis(2);
    private long SEVEN_DAYS = TimeUnit.DAYS.toMillis(7);

    private Map<Enum<? extends Type>, Long> SEND = parsing(Send.FROM_ACCOUNT, 1111l
            , Send.FROM_BALANCE, 20000l
            , Send.TO_ACCOUNT, 2222l
            , Send.TO_ID, 2l
            , Send.AMOUNT, 19500l);
    private Map<Enum<? extends Type>, Long> ACCOUNT = parsing(
            Create.ACCOUNT, 1111l
    );
    private Metric[] BALANCE = asArray(
            Metric.plus(Send.FROM_BALANCE)
            , Metric.minus(Send.AMOUNT)
    );

    /**
     * 잔액이 1000원 이하가 되는 경우
     */
    @Test
    public void testBalance() {
        Sum sum = Sum.of(BALANCE, Evaluation.LOWER_THAN_AND_EQUALS, 1000l, ONE_HOUR);
        assertTrue(sum.match(SEND));
    }

    /**
     * 계좌 개설을 후 1시간 이내, 20만원 충전 후 잔액이 1000원 이하가 되는 경우
     */
    @Test
    public void testAfterWithin() {
        Map<Enum<? extends Type>, Long> CHARGE = parsing(
                Charge.ACCOUNT, 1111l
                , Charge.AMOUNT, 200000l
                , Charge.BANK_ACCOUNT, 1010l
        );

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime event = now.minus(ONE_HOUR * 2, ChronoUnit.MILLIS);
        LocalDateTime valid = now.minus(ONE_HOUR, ChronoUnit.MILLIS);
        LocalDateTime over = now.minus(ONE_HOUR - 1, ChronoUnit.MILLIS);
        LocalDateTime before = now.minus(ONE_HOUR + 2, ChronoUnit.MILLIS);

        Terminal CREATE_DETECT = new Rule(Create.ACCOUNT, Evaluation.GREATER_THAN_AND_EQUALS, 0l, ONE_HOUR, 1);
        Terminal HIGH_PRICE = new Rule(Charge.AMOUNT, Evaluation.EQUALS, 200000l, ONE_HOUR, 1);
        Terminal LOWER_PRICE = Sum.of(BALANCE, Evaluation.LOWER_THAN_AND_EQUALS, 1000l, 0);

        Seq rules = Seq.of("RuleA", CREATE_DETECT, HIGH_PRICE, LOWER_PRICE);
        Aggregator aggregator = Aggregator.of(rules);
        aggregator.aggregate(now, event, ACCOUNT);
        aggregator.aggregate(now, valid, CHARGE);

        // 1시간 이후 잔고 1000원 이하
        aggregator.aggregate(now, over, SEND);
        assertThat(aggregator.match(now), empty());

        // 20만원 충전 이전에 잔고 1000원 이하
        aggregator.aggregate(now, before, SEND);
        assertThat(aggregator.match(now), empty());

        // 1시간 이내 20만원 충전 이후 잔고 1000원 이하
        aggregator.aggregate(now, valid, SEND);
        assertThat(aggregator.match(now), containsInAnyOrder("RuleA"));

    }

    /**
     * 계좌 개설을 후, 7일 이내, 받기로 10만원 이상 금액을 5회 이상 하는 경우
     */
    @Test
    public void testAfter() {
        Map<Enum<? extends Type>, Long> RECEIVE = parsing(
                Receive.TO_ACCOUNT, 1111l
                , Receive.BEFORE_BALANCE, 20000l
                , Receive.FROM_ACCOUNT, 2222l
                , Receive.FROM_ID, 1l
                , Receive.AMOUNT, 100000l);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime event = now.minus(SEVEN_DAYS * 2, ChronoUnit.MILLIS);
        LocalDateTime valid = now.minus(SEVEN_DAYS, ChronoUnit.MILLIS);
        LocalDateTime invalid = now.minus(SEVEN_DAYS - 1, ChronoUnit.MILLIS);

        Terminal OVER_PRICE = new Rule(Receive.AMOUNT, Evaluation.GREATER_THAN_AND_EQUALS, 100000l, SEVEN_DAYS, 5);
        Terminal CREATE_DETECT = new Rule(Create.ACCOUNT, Evaluation.GREATER_THAN_AND_EQUALS, 0l, SEVEN_DAYS, 1);

        Seq rules = Seq.of("RuleB", CREATE_DETECT, OVER_PRICE);
        Aggregator aggregator = Aggregator.of(rules);
        aggregator.aggregate(now, event, ACCOUNT);

        // 7일 이내 10만원 이상 4회
        for (int i = 0; i < 4; i++) {
            aggregator.aggregate(now, valid, RECEIVE);
        }

        // 7일 초과후 10만원 이상 1회
        aggregator.aggregate(now, invalid, RECEIVE);
        assertThat(aggregator.match(now), empty());

        // 7일 이내 4회 + 1회 = 10만원 이상 5회
        aggregator.aggregate(now, valid, RECEIVE);
        assertThat(aggregator.match(now), containsInAnyOrder("RuleB"));
    }

    /**
     * 2시간 이내 5만원 이상금액 3회 이상인 경우
     */
    @Test
    public void testFrequency() {
        Map<Enum<? extends Type>, Long> RECEIVE = parsing(
                Receive.TO_ACCOUNT, 1111l
                , Receive.BEFORE_BALANCE, 20000l
                , Receive.FROM_ACCOUNT, 2222l
                , Receive.FROM_ID, 1l
                , Receive.AMOUNT, 50000l);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime valid = now.minus(TWO_HOUR, ChronoUnit.MILLIS);
        LocalDateTime invalid = now.minus(TWO_HOUR + 1, ChronoUnit.MILLIS);

        Terminal OVER_PRICE = new Rule(Receive.AMOUNT, Evaluation.GREATER_THAN_AND_EQUALS, 50000l, TWO_HOUR, 3);
        Cons rules = Cons.of("RuleC", OVER_PRICE);
        Aggregator aggregator = Aggregator.of(rules);
        for (int i = 0; i < 2; i++) {
            aggregator.aggregate(now, valid, RECEIVE);
        }
        // 2시간 초과 후, 5만원
        aggregator.aggregate(now, invalid, RECEIVE);
        assertThat(aggregator.match(now), empty());

        // 2시간 이내 2회 + 1회 5만원
        aggregator.aggregate(now, valid, RECEIVE);
        assertThat(aggregator.match(now), containsInAnyOrder("RuleC"));
    }

}
