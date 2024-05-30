package com.shortthirdman.sharedlibs.util;

import org.junit.Assert;
import org.junit.Test;

public class PasswordUtilsTest {

    @Test
    public void testSecurePassword() {
        String myPassword = "myAdminPass#123";
        String salt = PasswordUtils.getSalt(30);

        // Protect user's password. The generated value can be stored in DB.
        String mySecurePassword = PasswordUtils.generateSecurePassword(myPassword, salt);

        // Print out protected password
        System.out.println("My secure password = " + mySecurePassword);
        System.out.println("Salt value = " + salt);

        Assert.assertNotNull(mySecurePassword);
    }

    @Test
    public void matchGeneratedPassword() {
        // User provided password to validate
        String providedPassword = "myPassword123";

        // Encrypted and Base64 encoded password read from database
        String securePassword = "HhaNvzTsVYwS/x/zbYXlLOE3ETMXQgllqrDaJY9PD/U=";

        // Salt value stored in database
        String salt = "EqdmPh53c9x33EygXpTpcoJvc4VXLK";

        boolean passwordMatch = PasswordUtils.verifyUserPassword(providedPassword, securePassword, salt);

        if (passwordMatch) {
            System.out.println("Provided user password " + providedPassword + " is correct.");
        } else {
            System.out.println("Provided password is incorrect");
        }
        Assert.assertTrue(passwordMatch);
    }
}
