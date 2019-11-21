package ru.ange.mhb.utils;

public class StrikeThrough {

    private static final char STRIKE_THROUGH_CHARACTER = '\u0336';

    public static String getStrikeThroughText(String text) {
        String result = new String();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            result += String.valueOf(c) + STRIKE_THROUGH_CHARACTER;
        }
        return result;
    }
}
