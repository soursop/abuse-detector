package com.abuse.module;

public enum Evaluation {
    LOWER_THAN_AND_EQUALS {
        @Override
        boolean match(long source, Long target) {
            return source >= target.longValue();
        }
    }
    , GREATER_THAN_AND_EQUALS {
        @Override
        boolean match(long source, Long target) {
            return source <= target.longValue();
        }
    }
    ;
    abstract boolean match(long source, Long target);
}
