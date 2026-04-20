package com.petshop.util;

public final class PayloadUtil {

    private PayloadUtil() {}

    public static String asString(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }

    public static int asInt(Object value, int defaultValue) {
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(String.valueOf(value).trim());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public static double asDouble(Object value, double defaultValue) {
        if (value == null) return defaultValue;
        try {
            return Double.parseDouble(String.valueOf(value).trim());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public static boolean asBoolean(Object value, boolean defaultValue) {
        if (value == null) return defaultValue;
        return Boolean.parseBoolean(String.valueOf(value).trim());
    }
}