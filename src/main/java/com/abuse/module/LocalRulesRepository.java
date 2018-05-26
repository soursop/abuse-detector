package com.abuse.module;

import com.abuse.rule.*;
import com.abuse.types.Charge;
import com.abuse.types.Create;
import com.abuse.types.Receive;
import com.abuse.types.Send;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.abuse.rule.Utils.asArray;

@Component
public class LocalRulesRepository implements RulesRepository {
    private Rules[] rules;

    @PostConstruct
    public void initialize() {
        List<Rules> list = new ArrayList<>();

        long ONE_HOUR = TimeUnit.HOURS.toMillis(1);
        long TWO_HOUR = TimeUnit.HOURS.toMillis(2);
        long SEVEN_DAYS = TimeUnit.DAYS.toMillis(7);

        Terminal CREATE_DETECT_1H = new Rule(Create.ACCOUNT, Evaluation.GREATER_THAN_AND_EQUALS, 0l, ONE_HOUR, 1);
        Terminal HIGH_PRICE = new Rule(Charge.AMOUNT, Evaluation.EQUALS, 200000l, ONE_HOUR, 1);
        Terminal LOWER_PRICE = Sum.of(asArray(
                Metric.plus(Send.FROM_BALANCE)
                , Metric.minus(Send.AMOUNT)
        ), Evaluation.LOWER_THAN_AND_EQUALS, 1000l, 0);
        list.add(Seq.of("RuleA", CREATE_DETECT_1H, HIGH_PRICE, LOWER_PRICE));

        Terminal OVER_PRICE_10 = new Rule(Receive.AMOUNT, Evaluation.GREATER_THAN_AND_EQUALS, 100000l, SEVEN_DAYS, 5);
        Terminal CREATE_DETECT_7D = new Rule(Create.ACCOUNT, Evaluation.GREATER_THAN_AND_EQUALS, 0l, SEVEN_DAYS, 1);
        list.add(Seq.of("RuleB", CREATE_DETECT_7D, OVER_PRICE_10));

        Terminal OVER_PRICE5 = new Rule(Receive.AMOUNT, Evaluation.GREATER_THAN_AND_EQUALS, 50000l, TWO_HOUR, 3);
        list.add(Cons.of("RuleC", OVER_PRICE5));

        rules = list.toArray(new Rules[list.size()]);
    }

    @Override
    public Rules[] findAll() {
        return rules;
    }
}
