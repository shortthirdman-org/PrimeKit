package com.shortthirdman.sharedlibs.util;

import com.shortthirdman.sharedlibs.common.Constants;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for manipulation with String class
 *
 * @version 1.0
 * @author shortthirdman-org
 */
public class StringUtils {

    private StringUtils() {}

    /**
     * Convert a snake-case text string to camel-case
     *
     * @apiNote Capitalize first letter of string. Replace the first occurrence of
     *          letter that present after the underscore, to capitalize form of next
     *          letter of underscore
     * @param text
     * @return the camel-case converted text string
     */
    public static String snakeToCamel(String text) {
        text = text.substring(0, 1).toUpperCase() + text.substring(1);
        while (text.contains(Constants.UNDERSCORE.toString())) {
            text = text.replaceFirst("_[a-z]",
                    String.valueOf(Character.toUpperCase(text.charAt(text.indexOf(Constants.UNDERSCORE.toString()) + 1))));
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
     * @return
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
     * @param delimitedText
     * @param delimiter
     * @return
     */
    public static List<String> convertToList(String delimitedText, String delimiter) {
        List<String> result = new ArrayList<>();
        try {
            String[] commaSeparatedArr = delimitedText.split(delimiter);
            result = Arrays.stream(commaSeparatedArr).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
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
     * @param array
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
     * @param array
     * @param offset
     * @param limit
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
        source = Normalizer.normalize(source, Normalizer.Form.NFD);
        for (char c : source.toCharArray()) {
            if (c >= '\u007F') {
                return true;
            }
        }
        return false;
    }

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
     * @param src the source byte array
     * @param len the length of source bytes
     * @param fill the fill byte character
     * @return the right partitioned byte array from source
     */
    private static byte[] getRightPartitionBytes(final byte[] src, final int len, final byte fill) {
        byte[] des = new byte[len];

        int lLen = Math.min(src.length, len);
        int rLen = Math.max(src.length, len);

        for (int i = 0; i < lLen; i++) {
            if (i < lLen) {
                des[i] = src[i];
            } else {
                des[i] = fill;
            }
        }

        return des;
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
}
