package com.abuse.module;

import com.abuse.module.types.Account;
import com.abuse.module.types.Receive;
import com.abuse.module.types.Send;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.abuse.module.Utils.asArray;
import static com.abuse.module.Utils.parsing;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author soursop
 * @created 2018. 5. 25.
 */
public class RuleTest {
    private long ONE_HOUR = TimeUnit.HOURS.toMillis(1);
    private long SEVEN_DAYS = TimeUnit.DAYS.toMillis(7);

    private Map<Enum<?>, Long> SEND = parsing(Send.FROM_ACCOUNT, 1111l
            , Send.FROM_BALANCE, 20000l
            , Send.TO_ACCOUNT, 2222l
            , Send.TO_ID, 2l
            , Send.AMOUNT, 19500l);
    private Map<Enum<?>, Long> ACCOUNT = parsing(
            Account.ACCOUNT, 1111l
    );
    private Map<Enum<?>, Long> RECEIVE = parsing(
            Receive.TO_ACCOUNT, 1111l
            , Receive.BEFORE_BALANCE, 20000l
            , Receive.FROM_ACCOUNT, 2222l
            , Receive.TO_ID, 1l
            , Receive.AMOUNT, 100000l);


    private Rulable OVER_PRICE = new Rule(Receive.AMOUNT, Evaluation.GREATER_THAN_AND_EQUALS, 100000l, SEVEN_DAYS, 5);
    private Rulable CREATE_DETECT = new Rule(Account.ACCOUNT, Evaluation.GREATER_THAN_AND_EQUALS, 0l, SEVEN_DAYS, 1);
    private Metric[] BALANCE = asArray(
            new Metric(Send.FROM_BALANCE, ONE_HOUR)
            , new Metric(Send.AMOUNT, -1, ONE_HOUR)
    );

    @Test
    public void testBalance() {
        Sum sum = Sum.of(BALANCE, Evaluation.LOWER_THAN_AND_EQUALS, 1000l);
        assertTrue(sum.match(SEND));
    }

    @Test
    public void testFrequency() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime valid = now.minus(SEVEN_DAYS, ChronoUnit.MILLIS);
        LocalDateTime invalid = now.minus(SEVEN_DAYS + 1, ChronoUnit.MILLIS);

        Rules rules = Rules.of("rule2", OVER_PRICE);
        Aggregator aggregator = Aggregator.of(rules);
        for (int i = 0; i < 4; i++) {
            aggregator.aggregate(now, valid, RECEIVE);
        }
        // before frquency
        aggregator.aggregate(now, invalid, RECEIVE);
        assertThat(aggregator.match(now), containsInAnyOrder());

        // after frequency
        aggregator.aggregate(now, valid, RECEIVE);
        assertThat(aggregator.match(now), containsInAnyOrder("rule2"));
    }

    @Test
    public void testConjunction() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime valid = now.minus(SEVEN_DAYS, ChronoUnit.MILLIS);
        LocalDateTime invalid = now.minus(SEVEN_DAYS + 1, ChronoUnit.MILLIS);

        Rules rules = Rules.of("rule2", CREATE_DETECT, OVER_PRICE);
        Aggregator aggregator = Aggregator.of(rules);
        aggregator.aggregate(now, invalid, ACCOUNT);
        for (int i = 0; i < 5; i++) {
            aggregator.aggregate(now, valid, RECEIVE);
        }
        assertThat(aggregator.match(now), containsInAnyOrder());

        aggregator.aggregate(now, valid, ACCOUNT);
        assertThat(aggregator.match(now), containsInAnyOrder("rule2"));
    }

    @Test
    public void testAfter() {

    }


}
