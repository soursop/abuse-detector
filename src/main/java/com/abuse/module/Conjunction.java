package com.abuse.module;

import java.util.Date;
import java.util.Map;

/**
 * @author soursop
 * @created 2018. 5. 24.
 */
public enum Conjunction {
    AND {
        @Override
        public boolean matchBy(Map<Rule, Long> result, Rule[] rules) {
            long now = new Date().getTime();
            for (Rule rule : rules) {
                Long time = result.get(rule);
                if (time == null) {
                    return false;
                }
                if (!rule.valid(now, time)) {
                    result.remove(rule);
                    return false;
                }
            }
            return true;
        }
    }, AFTER {
        @Override
        public boolean matchBy(Map<Rule, Long> result, Rule[] rules) {
            long now = new Date().getTime();
            for (int i = 0; i < rules.length; i++) {
                Long time = result.get(rules[i]);
                if (time == null) {
                    return false;
                }
                if (!rules[i].valid(now, time)) {
                    result.remove(rules[i]);
                    return false;
                }
                if (i > 0 && time.longValue() < result.get(rules[i - 1]).longValue()) {
                    return false;
                }
            }
            return true;
        }
    }
    ;

    public abstract boolean matchBy(Map<Rule, Long> result, Rule[] rules);
}
