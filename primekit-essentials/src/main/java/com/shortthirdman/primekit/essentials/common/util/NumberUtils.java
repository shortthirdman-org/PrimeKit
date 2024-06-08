package com.shortthirdman.primekit.essentials.common.util;

import com.shortthirdman.primekit.essentials.common.GenericConstants;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public final class NumberUtils {

    private NumberUtils() {
    }

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
     * @param number the input number object
     * @param pattern the pattern format
     * @return the String value of converted number
     */
    public static String changeToDecimalFormat(Object number, String pattern) {
        BigDecimal bdNumber = new BigDecimal(number.toString());
        bdNumber = bdNumber.stripTrailingZeros();           //Returns a BigDecimal with any trailing zero's removed
        pattern = (pattern == null) ? "###,##0.0###########" : pattern;        //To apply formatting when the number of digits in input equals the pattern
        DecimalFormat newFormat = new DecimalFormat(pattern, new DecimalFormatSymbols(Locale.US));
        return newFormat.format(bdNumber);
    }

    /**
     * @param number the input number object
     * @return the double value of converted number
     */
    public static double removeCommasFromNumber(Object number) {
        try {
            StringBuilder inputNo = new StringBuilder(number.toString());
            if (!inputNo.isEmpty()) {
                while (inputNo.indexOf(GenericConstants.COMMA.getValue()) != -1) {
                    inputNo.deleteCharAt(inputNo.indexOf(GenericConstants.COMMA.getValue()));
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
     * @param bigDecimalValue the input big decimal value in string
     * @param precision the precision value
     * @return the string formatted value
     */
    public static String changeToRequiredDecimals(String bigDecimalValue, int precision) {
        if (bigDecimalValue == null || bigDecimalValue.isEmpty()) {
            return "0.0";
        }

        StringBuilder newFormattedString = new StringBuilder();
        String afterDecimal = null;
        if (bigDecimalValue.contains(GenericConstants.DOT_PERIOD.getValue())) {
            afterDecimal = bigDecimalValue.substring(bigDecimalValue
                    .indexOf(GenericConstants.DOT_PERIOD.getValue()) + 1);
            int length = Math.abs((afterDecimal.length() - precision));
            if (afterDecimal.length() < precision) {
                newFormattedString = new StringBuilder(bigDecimalValue);
                for (int i = 0; i < length; i++) {
                    newFormattedString.append(GenericConstants.ZERO.getValue());
                }
            } else if (afterDecimal.length() > precision) {
                newFormattedString = new StringBuilder(bigDecimalValue.substring(0,
                        bigDecimalValue.length() - length));
                if (precision == 0) {
                    newFormattedString = new StringBuilder(newFormattedString.substring(0,
                            newFormattedString.toString().indexOf(GenericConstants.DOT_PERIOD.getValue())));
                } else {
                    newFormattedString = new StringBuilder(bigDecimalValue);
                }

            } else {
                if (precision > 0) {
                    newFormattedString = new StringBuilder(bigDecimalValue + GenericConstants.DOT_PERIOD.getValue());
                } else {
                    newFormattedString = new StringBuilder(bigDecimalValue);
                }
                for (int i = 0; i < precision; i++) {
                    newFormattedString = new StringBuilder(newFormattedString + GenericConstants.ZERO.getValue());
                }
            }
        }

        return newFormattedString.toString();
    }
}
