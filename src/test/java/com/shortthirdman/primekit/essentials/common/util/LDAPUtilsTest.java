package com.shortthirdman.primekit.essentials.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LDAPUtilsTest {

    @Test
    void escapeDN() {
        assertEquals("No special characters to escape", "Helloé", LDAPUtils.escapeDN("Helloé"));
        assertEquals("leading #", "\\# Helloé", LDAPUtils.escapeDN("# Helloé"));
        assertEquals("leading space", "\\ Helloé", LDAPUtils.escapeDN(" Helloé"));
        assertEquals("trailing space", "Helloé\\ ", LDAPUtils.escapeDN("Helloé "));
        assertEquals("only 3 spaces", "\\  \\ ", LDAPUtils.escapeDN("   "));
        assertEquals("Christmas Tree DN", "\\ Hello\\\\ \\+ \\, \\\"World\\\" \\;\\ ", LDAPUtils.escapeDN(" Hello\\ + , \"World\" ; "));
    }

    @Test
    void escapeSearchFilter() {
        assertEquals("No special characters to escape", "Hi This is a test #çà", LDAPUtils.escapeSearchFilter("Hi This is a test #çà"));
        assertEquals("LDAP Christams Tree", "Hi \\28This\\29 = is \\2a a \\5c test # ç à ô", LDAPUtils.escapeSearchFilter("Hi (This) = is * a \\ test # ç à ô"));
    }
}