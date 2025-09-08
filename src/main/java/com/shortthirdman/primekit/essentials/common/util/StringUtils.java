package com.shortthirdman.primekit.essentials.common.util;

import com.shortthirdman.primekit.essentials.common.GenericConstants;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for string manipulation
 * @author ShortThirdMan
 * @version 1.0.0
 */
public final class StringUtils {

    private StringUtils() {
    }

    /**
     * Convert a snake-case text string to camel-case
     *
     * @apiNote Capitalize first letter of string. Replace the first occurrence of
     *          letter that present after the underscore, to capitalize form of next
     *          letter of underscore
     * @param text the input string to convert
     * @return the camel-case converted text string
     */
    public static String snakeToCamel(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Source text cannot be null or empty");
        }
        text = text.substring(0, 1).toUpperCase() + text.substring(1);
        while (text.contains(GenericConstants.UNDERSCORE.getValue())) {
            text = text.replaceFirst("_[a-z]",
                    String.valueOf(Character.toUpperCase(text.charAt(text.indexOf(GenericConstants.UNDERSCORE.getValue()) + 1))));
        }
        return text;
    }

    /**
     * Convert a camel-case text string to snake-case
     *
     * @apiNote Replace the given regex with replacement string and convert it to
     *          lower case.
     * @param text the input string to convert
     * @return snake-case converted text string
     */
    public static String camelToSnake(String text) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Source text cannot be null or empty");
        }
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        text = text.replaceAll(regex, replacement).toLowerCase();
        return text;
    }

    /**
     * Convert a camel-case text string to snake-case
     *
     * @param text the input string to convert
     * @param upper true if result snake cased string should be upper cased
     * @return snake-case converted text string
     */
    public static String camelToSnake(final String text, final boolean upper) {
        String ret = text.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2").replaceAll("([a-z])([A-Z])", "$1_$2");

        if (upper) {
            return ret.toUpperCase();
        } else {
            return ret.toLowerCase();
        }
    }

    /**
     * Converts a delimited text string into list of string
     *
     * @param delimitedText the input delimited text string
     * @param delimiter the delimiter used to split the text
     * @return the list of strings
     */
    public static List<String> convertToList(String delimitedText, String delimiter) {
        List<String> result = List.of();

        try {
            String[] commaSeparatedArr = delimitedText.split(delimiter);
            result = Arrays.stream(commaSeparatedArr).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error caught while converting input text to list: " + e.getMessage());
        }

        return result;
    }

    /**
     * Trims the input text and returns the trimmed value.
     * @param textValue the source input text to trim
     * @return the trimmed value
     */
    public static String trimText(String textValue) {
        String trimmedText;
        if (textValue == null) {
            trimmedText = null;
        } else {
            trimmedText = textValue.trim();
        }
        return trimmedText;
    }

    /**
     * Given two strings source and target, determine if they are isomorphic.<br>
     * Two strings are isomorphic if the characters in source can be replaced to get target.
     *
     * @param s the source text
     * @param t the target text
     * @return true if two given strings are isomorphic
     */
    public boolean isIsomorphic(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }

        Map<Character, Character> map1 = new HashMap<>();
        Map<Character, Character> map2 = new HashMap<>();

        for (int i = 0; i < s.length(); i++) {
            char c1 = s.charAt(i);
            char c2 = t.charAt(i);

            if (map1.containsKey(c1)) {
                if (c2 != map1.get(c1)) {
                    return false;
                }
            } else {
                if (map2.containsKey(c2)) {
                    return false;
                }

                map1.put(c1, c2);
                map2.put(c2, c1);
            }
        }

        return true;
    }

    /**
     * Helper method to convert a byte[] array (such as a MsgId) to a hex string
     *
     * @param array the input array
     * @return hex string
     */
    public static String arrayToHexString(byte[] array) {
        if (array == null || array.length == 0) {
            return null;
        }

        StringBuilder string = new StringBuilder();

        for (byte b : array) {
            String hexString = Integer.toHexString(0x00FF & b);
            string.append(hexString.length() == 1 ? "0" + hexString : hexString);
        }
        return string.toString();
    }

    /**
     * Convert a byte[] array (such as a MsgId) to a hex string
     *
     * @param array the input array
     * @param offset the offset size
     * @param limit the limit size
     * @return hex string
     */
    public static String arrayToHexString(byte[] array, int offset, int limit) {
        String retVal = null;

        if (array == null || array.length == 0) {
            return retVal;
        }

        StringBuilder hexString = new StringBuilder(array.length);
        int hexVal;
        char hexChar;
        int length = Math.min(limit, array.length);
        for (int i = offset; i < length; i++) {
            hexVal = (array[i] & 0xF0) >> 4;
            hexChar = (char) ((hexVal > 9) ? ('A' + (hexVal - 10)) : ('0' + hexVal));
            hexString.append(hexChar);
            hexVal = array[i] & 0x0F;
            hexChar = (char) ((hexVal > 9) ? ('A' + (hexVal - 10)) : ('0' + hexVal));
            hexString.append(hexChar);
        }
        retVal = hexString.toString();

        return retVal;
    }

    /**
     * Checks each char in a string to see if the string contains
     * any char values greater than those used in ASCII.
     *
     * @param source Input string to search for Non-ASCII chars.
     * @return true, if any char is greater than u007f, false otherwise
     */
    public static boolean containsNonAscii(String source) {
        if (source == null) {
            throw new IllegalArgumentException("");
        }

        source = Normalizer.normalize(source, Normalizer.Form.NFD);

        for (char c : source.toCharArray()) {
            if (c >= '\u007F') {
                return true;
            }
        }

        return false;
    }

    /**
     * Convert ASCII to hex byte array
     * @param src the source byte array
     * @param len the length of source bytes
     * @param padding the padding type length
     * @return the converted hex byte array
     */
    public static byte[] asciiToHex(byte[] src, int len, int padding) {
        byte[] asc;

        if (src == null || len == 0) {
            return new byte[0];
        }

        byte[] bcd = new byte[len];

        if (padding == 0) {
            asc = getLeftPartitionBytes(src, len * 2, (byte) 0x30);
        } else {
            asc = getRightPartitionBytes(src, len * 2, (byte) 0x30);
        }

        for (int i = 0; i < len; i++) {
            bcd[i] = (byte) (convertByteToBCD(asc[i * 2]) << 4 ^ (convertByteToBCD(asc[i * 2 + 1]) & 0x0f));
        }

        return bcd;
    }

    /**
     * Calculates the left partitioned byte array from source
     * @param src the source byte array
     * @param len the length of source bytes
     * @param fill the fill byte character
     * @return the left partitioned byte array from source
     */
    private static byte[] getLeftPartitionBytes(final byte[] src, final int len, final byte fill) {
        byte[] des = new byte[len];

        int lLen = Math.min(src.length, len);
        int rLen = Math.max(src.length, len);

        for (int i = 0; i < len; i++) {
            if (i >= (rLen - lLen))
                des[i] = src[i - rLen + lLen];
            else
                des[i] = fill;
        }

        return des;
    }

    /**
     * Calculates the right partitioned byte array from source
     * @param src the source byte array
     * @param len the length of source bytes
     * @param fill the fill byte character
     * @return the right partitioned byte array from source
     */
    private static byte[] getRightPartitionBytes(final byte[] src, final int len, final byte fill) {
        byte[] result = new byte[len];

        int lLen = Math.min(src.length, len);
        int rLen = Math.max(src.length, len);

        for (int i = 0; i < lLen; i++) {
            if (i < lLen) {
                result[i] = src[i];
            } else {
                result[i] = fill;
            }
        }

        return result;
    }

    /**
     * Convert byte to BCD

     * @param src the source bytes
     * @return the converted BCD byte
     */
    private static byte convertByteToBCD(byte src) {
        byte re = src;

        if (src <= 0x39 && src >= 0x30) {
            re = (byte) (src - 0x30);
        } else if (src <= 0x46 && src >= 0x41) {
            re = (byte) (src - 0x37);
        } else if (src <= 0x66 && src >= 0x61) {
            re = (byte) (src - 0x57);
        }

        return re;
    }

    /**
     * Returns the first non-null and non-blank string from the given values.
     * @param values the input arguments
     * @return the first non-null and non-blank string
     */
    public static String coalesce(String... values) {
        return Arrays.stream(values)
                .filter(s -> s != null && !s.isBlank())
                .findFirst()
                .orElse("");
    }

    /**
     * Joins a list of strings into a comma-separated string.
     * @param items the input list of items
     * @return the comma separated string
     */
    public static String joinWithComma(List<String> items) {
        return items == null ? "" : String.join(", ", items);
    }

    /**
     * Wraps the given value with double quotes.
     * @param value the input value
     * @return the value wrapped with double quotes
     */
    public static String wrapWithQuotes(String value) {
        return "\"" + value + "\"";
    }

    /**
     * Wraps a nullable value into an {@link java.util.Optional}.
     * @param value the input value
     * @param <T> the type of the value
     * @return an {@link java.util.Optional} containing the value if it's not null, otherwise an empty Optional
     */
    public static <T> Optional<T> asOptional(T value) {
        return Optional.ofNullable(value);
    }

    /**
     * Checks if a string is a palindrome.
     * @param input the input string
     * @return true if the string is a palindrome, false otherwise
     */
    public static boolean isPalindrome(String input) {
        if (input == null) return false;
        String clean = input.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        return new StringBuilder(clean).reverse().toString().equals(clean);
    }

    /**
     * Generates a random alphanumeric string of the given length.
     * @param length the length of the string to generate
     * @return the generated random alphanumeric string
     */
    public static String generateRandomAlphanumeric(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        return new Random().ints(length, 0, chars.length())
                .mapToObj(chars::charAt)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    /**
     * Checks whether a string is null or blank (empty or whitespace only).
     * @param input the input string
     * @return true if the string is null or blank, false otherwise
     */
    public static boolean isNullOrBlank(String input) {
        return input == null || input.isBlank();
    }

    /**
     * Converts the first letter to uppercase and leaves the rest unchanged.
     * @param input the input string
     * @return the string with the first letter capitalized
     */
    public static String capitalize(String input) {
        if (isNullOrBlank(input)) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    /**
     * Safely converts an object to a string, returning an empty string if null.
     * @param obj the object to convert
     * @return the string representation of the object, or an empty string if null
     */
    public static String safeToString(Object obj) {
        return Optional.ofNullable(obj).map(Object::toString).orElse("");
    }
}
