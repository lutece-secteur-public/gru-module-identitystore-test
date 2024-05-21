package fr.paris.lutece.plugins.identitystore.modules.test.util;

import java.util.Comparator;

public class StringAlphanumericComparator {
    private static final String DIGIT_AND_DECIMAL_REGEX = "[^\\d.]";

    private StringAlphanumericComparator() {
    }

    public static Comparator<String> createStringComparator() {
        return Comparator.comparingDouble(StringAlphanumericComparator::parseStringToNumber);
    }

    private static double parseStringToNumber(String input){

        final String digitsOnly = input.replaceAll(DIGIT_AND_DECIMAL_REGEX, "");

        if(digitsOnly.isEmpty()) {
            return 0;
        } else {
            try {
                return Double.parseDouble(digitsOnly);
            } catch (NumberFormatException nfe){
                return 0;
            }
        }
    }
}
