package fr.paris.lutece.plugins.identitystore.util;

import java.io.File;
import java.util.Comparator;

public class FileNameAlphanumericComparator {
    private static final String DIGIT_AND_DECIMAL_REGEX = "[^\\d.]";

    private FileNameAlphanumericComparator() {
    }

    public static Comparator<File> createStringComparator() {
        return Comparator.comparingDouble(FileNameAlphanumericComparator::parseStringToNumber);
    }

    private static double parseStringToNumber(File input){

        final String digitsOnly = input.getName().replaceAll(DIGIT_AND_DECIMAL_REGEX, "");

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
