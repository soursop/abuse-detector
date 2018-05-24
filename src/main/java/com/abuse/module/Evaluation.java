package com.abuse.module;

public enum Evaluation {
    EQUALS {
        @Override
        boolean match(Long source, Long target) {
            return source.longValue() == target.longValue();
        }
    }
    , LOWER_THAN_AND_EQUALS {
        @Override
        boolean match(Long source, Long target) {
            return source.longValue() >= target.longValue();
        }
    }
    , GREATER_THAN_AND_EQUALS {
        @Override
        boolean match(Long source, Long target) {
            return source.longValue() <= target.longValue();
        }
    }
    ;
    abstract boolean match(Long source, Long target);
}
