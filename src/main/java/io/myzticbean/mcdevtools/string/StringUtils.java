package io.myzticbean.mcdevtools.string;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

    /**
     * Capitalizes first letters of all words
     * @param str
     * @return
     */
    public static String capitalizeAllFirstLetters(String str) {
        char[] array = str.toCharArray();

        // Uppercase first letter.
        array[0] = Character.toUpperCase(array[0]);

        // Uppercase all letters that follow a whitespace character.
        for (int i = 1; i < array.length; i++) {
            if (Character.isWhitespace(array[i - 1])) {
                array[i] = Character.toUpperCase(array[i]);
            }
        }

        return new String(array);
    }

    /**
     * Capitalizes first letter of first word only
     * @param str
     * @return
     */
    public static String capitalizeOnlyFirstLetter(String str) {
        return (str.substring(0, 1).toUpperCase() + str.substring(1));
    }
}
