// Copyright (c) ShortThirdMan 2025.
package com.shortthirdman.primekit.essentials.common.util;

/**
 * Utility methods for payments
 */
public final class PaymentUtils {

    private PaymentUtils() {}

    /**
     * Validates a credit card number using Luhn's algorithm.
     *
     * @param cardNumber The credit/debit card number as a string.
     * @return true if the number is valid, false otherwise.
     */
    public static boolean isValidCardNumber(String cardNumber) {
        // Remove any non-digit characters
        String sanitizedNumber = cardNumber.replaceAll("\\D", "");

        int length = sanitizedNumber.length();
        if (length == 0) {
            return false;
        }

        int sum = 0;
        boolean shouldDouble = false;

        // Iterate over the digits of the number, starting from the end
        for (int i = length - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(sanitizedNumber.charAt(i));

            if (shouldDouble) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            shouldDouble = !shouldDouble;
        }

        return (sum % 10 == 0);
    }
}
