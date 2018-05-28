package com.abuse.rule;

import com.abuse.types.*;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.abuse.rule.MockData.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author soursop
 * @created 2018. 5. 25.
 */
public class RuleTest {
    /**
     * 잔액이 1000원 이하가 되는 경우
     */
    @Test
    public void testBalance() {
        Sum sum = Sum.of(BALANCE, Evaluation.LOWER_THAN_AND_EQUALS, 1000l, ONE_HOUR);
        assertTrue(sum.match(SEND_500));
    }

    /**
     * 계좌 개설을 후 1시간 이내, 20만원 충전 후 잔액이 1000원 이하가 되는 경우
     */
    @Test
    public void testAfterWithin() {
        LocalDateTime now = LocalDateTime.now();
        FromEvent time = new FromEvent(now.minus(ONE_HOUR * 2, ChronoUnit.MILLIS), ONE_HOUR);

        Terminal CREATE_DETECT = new Rule(Create.ACCOUNT, Evaluation.GREATER_THAN_AND_EQUALS, 0l, ONE_HOUR, 1);
        Terminal HIGH_PRICE = new Rule(Charge.AMOUNT, Evaluation.EQUALS, 200_000l, ONE_HOUR, 1);
        Terminal LOWER_PRICE = Sum.of(BALANCE, Evaluation.LOWER_THAN_AND_EQUALS, 1000l, Long.MAX_VALUE);

        Seq rules = Seq.of("RuleA", CREATE_DETECT, HIGH_PRICE, LOWER_PRICE);
        Aggregator aggregator = Aggregator.of(rules);
        aggregator.aggregate(now, time.from(), ACCOUNT_1111);
        aggregator.aggregate(now, time.within(), CHARGE_200_000);

        // 1시간 이후 잔고 1000원 이하
        aggregator.aggregate(now, time.after(), SEND_500);
        assertThat(aggregator.match(now), empty());

        // 20만원 충전 이전에 잔고 1000원 이하
        aggregator.aggregate(now, time.within(-1l), SEND_500);
        assertThat(aggregator.match(now), empty());

        // 1시간 이내 20만원 충전 이후 잔고 1000원 이하
        aggregator.aggregate(now, time.within(), SEND_500);
        assertThat(aggregator.match(now), containsInAnyOrder("RuleA"));

    }

    /**
     * 계좌 개설을 후, 7일 이내, 받기로 10만원 이상 금액을 5회 이상 하는 경우
     */
    @Test
    public void testAfter() {
        LocalDateTime now = LocalDateTime.now();
        FromEvent time = new FromEvent(now.minus(SEVEN_DAYS * 2, ChronoUnit.MILLIS), SEVEN_DAYS);

        Terminal OVER_PRICE = new Rule(Receive.AMOUNT, Evaluation.GREATER_THAN_AND_EQUALS, 100_000l, SEVEN_DAYS, 5);
        Terminal CREATE_DETECT = new Rule(Create.ACCOUNT, Evaluation.GREATER_THAN_AND_EQUALS, 0l, SEVEN_DAYS, 1);

        Seq rules = Seq.of("RuleB", CREATE_DETECT, OVER_PRICE);
        Aggregator aggregator = Aggregator.of(rules);
        aggregator.aggregate(now, time.from(), ACCOUNT_1111);

        // 7일 이내 10만원 이상 4회
        for (int i = 0; i < 4; i++) {
            aggregator.aggregate(now, time.within(), RECEIVE_100_000);
        }

        // 7일 초과후 10만원 이상 1회
        aggregator.aggregate(now, time.after(), RECEIVE_100_000);
        assertThat(aggregator.match(now), empty());

        // 7일 이내 4회 + 1회 = 10만원 이상 5회
        aggregator.aggregate(now, time.within(), RECEIVE_100_000);
        assertThat(aggregator.match(now), containsInAnyOrder("RuleB"));
    }

    /**
     * 2시간 이내 5만원 이상금액 3회 이상인 경우
     */
    @Test
    public void testFrequency() {
        UntilNow time = new UntilNow(LocalDateTime.now(), TWO_HOUR);

        Terminal OVER_PRICE = new Rule(Receive.AMOUNT, Evaluation.GREATER_THAN_AND_EQUALS, 50_000l, TWO_HOUR, 3);
        Cons rules = Cons.of("RuleC", OVER_PRICE);
        Aggregator aggregator = Aggregator.of(rules);
        for (int i = 0; i < 2; i++) {
            aggregator.aggregate(time.from(), time.within(), RECEIVE_50_000);
        }
        // 2시간 초과 후, 5만원
        aggregator.aggregate(time.from(), time.before(), RECEIVE_50_000);
        assertThat(aggregator.match(time.from()), empty());

        // 2시간 이내 2회 + 1회 5만원
        aggregator.aggregate(time.from(), time.within(), RECEIVE_50_000);
        assertThat(aggregator.match(time.from()), containsInAnyOrder("RuleC"));
    }

}
