package com.shortthirdman.primekit.essentials.common.util;

import java.util.Optional;

/**
 * Utility class for web-related operations.
 * @author ShortThirdMan
 * @version 1.0
 */
public final class WebUtils {

    private WebUtils() {}

    /**
     * Validates a URL string using regex.
     * @param url the URL string to validate
     * @return true if the URL is valid, false otherwise
     */
    public static boolean isValidURL(String url) {
        return Optional.ofNullable(url)
                .map(u -> u.matches("^(http|https)://[\\w.-]+(?:\\.[\\w.-]+)+.*$"))
                .orElse(false);
    }

    /**
     * Checks whether the input is a valid email address.
     * @param email the email address to check
     * @return true if the email address is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        return Optional.ofNullable(email)
                .map(e -> e.matches("^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$"))
                .orElse(false);
    }
}
