package com.bolezni.utils;

import java.security.SecureRandom;

public final class VerificationCodeGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int MIN_VALUE = 100000;
    private static final int MAX_VALUE = 900000;

    private VerificationCodeGenerator() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String generate() {
        int code = MIN_VALUE + RANDOM.nextInt(MAX_VALUE);
        return String.valueOf(code);
    }

    public static String generate(int length) {
        if (length < 4 || length > 10) {
            throw new IllegalArgumentException("Code length must be between 4 and 10");
        }

        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - min;
        int code = min + RANDOM.nextInt(max);

        return String.valueOf(code);
    }
}
