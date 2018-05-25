package com.abuse.module;

import com.abuse.module.types.Send;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.abuse.module.Utils.asArray;
import static com.abuse.module.Utils.parsing;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author soursop
 * @created 2018. 5. 25.
 */
public class RuleTest {

    @Test
    public void testBalance() {
        Map<Enum<?>, Long> log = parsing(Send.FROM_ACCOUNT, 1111l
                , Send.FROM_BALANCE, 20000l
                , Send.TO_ACCOUNT, 2222l
                , Send.TO_ID, 2l
                , Send.AMOUNT, 19500l);

        long duration = TimeUnit.HOURS.toMillis(1);
        Metric[] metrics = asArray(
                new Metric(Send.FROM_BALANCE, duration)
                , new Metric(Send.AMOUNT, -1, duration)
        );
        Sum sum = Sum.of(metrics, Evaluation.LOWER_THAN_AND_EQUALS, 1000l);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime valid = now.minusMinutes(59);
        LocalDateTime invalid = now.minusMinutes(61);

        assertTrue(sum.match(valid, log));
        assertFalse(sum.match(invalid, log));
    }

}
