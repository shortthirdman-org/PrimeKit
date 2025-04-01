// Copyright (c) ShortThirdMan 2025.
package com.shortthirdman.primekit.essentials.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentUtilsTest {

    @Test
    void test_validCardNumbers() {
        assertTrue(PaymentUtils.isValidCardNumber("4532015112830366"), "Valid credit card number");
        assertTrue(PaymentUtils.isValidCardNumber("6011514435546211"), "Valid credit card number");
        assertTrue(PaymentUtils.isValidCardNumber("378282246310005"), "Valid American Express number.");
    }

    @Test
    void test_invalidCardNumbers() {
        assertFalse(PaymentUtils.isValidCardNumber("123456789012345"), "Invalid card number");
        assertFalse(PaymentUtils.isValidCardNumber("0000000000000000"), "Invalid card number");
        assertFalse(PaymentUtils.isValidCardNumber("4532015112830367"), "Slightly altered from a valid number to become invalid.");
        assertFalse(PaymentUtils.isValidCardNumber("6011514435546210"), "Slightly altered from a valid number to become invalid.");
    }
}