package com.abuse.rule;

/**
 * @author soursop
 * @created 2018. 5. 25.
 */
public class Assert {

    public static void assertSize(int s1, int s2) {
        if (s1 != s2) {
            throw new IllegalArgumentException(String.format("invalid size %d != %d", s1, s2));
        }
    }
    public static int assertPairs(int v) {
        if (v < 2 || v % 2 != 0) {
            throw new IllegalArgumentException(String.format("invalid pairs %d", v));
        }
        return v / 2;
    }

    public static int assertEqualsExceptZero(int v1, int v2) {
        if (v1 != v2 && v1 != 0 && v2 != 0) {
            throw new IllegalArgumentException(String.format("invalid value %d != %d", v1, v2));
        }
        return Math.max(v1, v2);
    }

    public static long assertEqualsExceptZero(long v1, long v2) {
        if (v1 != v2 && v1 != 0 && v2 != 0) {
            throw new IllegalArgumentException(String.format("invalid value %d != %d", v1, v2));
        }
        return Math.max(v1, v2);
    }

}
