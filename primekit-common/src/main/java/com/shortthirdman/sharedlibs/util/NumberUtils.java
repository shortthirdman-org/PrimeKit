package com.shortthirdman.sharedlibs.util;

import com.shortthirdman.sharedlibs.common.Constants;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Class provides common functions on number formats.
 * @author shortthirdman
 * @since 1.0.0
 */
public class NumberUtils {

    /**
     * @param amount
     * @param precision
     * @param pattern
     * @param locale
     * @return
     */
    public static String formatCurrency(double amount, int precision, String pattern, Locale locale) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        DecimalFormat df = (DecimalFormat) nf;
        df.setMinimumFractionDigits(precision);
        df.setMaximumFractionDigits(precision);
        df.setDecimalSeparatorAlwaysShown(true);
        df.applyPattern(pattern);
        return df.format(amount);
    }

    /**
     * @param amount
     * @param precision
     * @param pattern
     * @param locale
     * @return
     */
    public static String formatNumber(double amount, int precision, String pattern, Locale locale) {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        DecimalFormat df = (DecimalFormat) nf;
        df.setMinimumFractionDigits(precision);
        df.setMaximumFractionDigits(precision);
        df.setDecimalSeparatorAlwaysShown(true);
        df.applyPattern(pattern);
        return df.format(amount);
    }

    /**
     * @param amount
     * @param precision
     * @param locale
     * @return
     */
    public static String formatCurrency(double amount, int precision, Locale locale) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        nf.setMinimumFractionDigits(precision);
        nf.setMaximumFractionDigits(precision);
        return nf.format(amount);
    }

    /**
     * @param amount
     * @param precision
     * @param locale
     * @return
     */
    public static String formatNumber(double amount, int precision, Locale locale) {
        NumberFormat nf = NumberFormat.getNumberInstance(locale);
        nf.setMinimumFractionDigits(precision);
        nf.setMaximumFractionDigits(precision);
        return nf.format(amount);
    }

    /**
     * @param number
     * @return
     */
    public static String changeToDecimalFormat(Object number, String pattern) {
        BigDecimal bdNumber = new BigDecimal(number.toString());
        bdNumber = bdNumber.stripTrailingZeros();           //Returns a BigDecimal with any trailing zero's removed
        pattern = (pattern == null) ? "###,##0.0###########" : pattern;        //To apply formatting when the number of digits in input equals the pattern
        DecimalFormat newFormat = new DecimalFormat(pattern, new DecimalFormatSymbols(Locale.US));
        return newFormat.format(bdNumber);
    }

    /**
     * @param number
     * @return
     */
    public static double removeCommasFromNumber(Object number) {
        try {
            StringBuffer inputNo = new StringBuffer(number.toString());
            if (inputNo.length() > 0) {
                while (inputNo.indexOf(Constants.COMMA.toString()) != -1) {
                    inputNo.deleteCharAt(inputNo.indexOf(Constants.COMMA.toString()));
                }
            } else {
                return 0.0;
            }
            return Double.parseDouble(inputNo.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * @param bigDecimalString
     * @param precision
     * @return
     */
    public static String changeToRequiredDecimals(String bigDecimalString, int precision) {
        String newFormattedString = null;
        String afterDecimal = null;
        if (bigDecimalString == null || bigDecimalString.length() == 0) {
            return "0.0";
        }
        if (bigDecimalString.contains(Constants.DOT_PERIOD.toString())) {
            afterDecimal = bigDecimalString.substring(bigDecimalString
                    .indexOf(Constants.DOT_PERIOD.toString()) + 1);
            int length = Math.abs((afterDecimal.length() - precision));
            if (afterDecimal.length() < precision) {
                newFormattedString = bigDecimalString;
                for (int i = 0; i < length; i++) {
                    newFormattedString = newFormattedString + Constants.ZERO;
                }
            } else if (afterDecimal.length() > precision) {
                newFormattedString = bigDecimalString.substring(0,
                        bigDecimalString.length() - length);
                if (precision == 0) {
                    newFormattedString = newFormattedString.substring(0,
                            newFormattedString.indexOf(Constants.DOT_PERIOD.toString()));
                } else {
                    newFormattedString = bigDecimalString;
                }

            } else {
                if (precision > 0) {
                    newFormattedString = bigDecimalString + Constants.DOT_PERIOD;
                } else {
                    newFormattedString = bigDecimalString;
                }
                for (int i = 0; i < precision; i++) {
                    newFormattedString = newFormattedString + Constants.ZERO;
                }
            }
        }
        return newFormattedString;
    }
}
