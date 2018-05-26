package com.abuse.module;

import com.abuse.rule.Rules;

public interface RulesRepository {
    Rules[] findAll();
}
